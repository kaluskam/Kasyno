package sample;

import casino.Casino;
import casino.Player;
import casino.machine.BlackJack.BJplayer;
import casino.machine.BlackJack.BlackJack;
import casino.machine.BlackJack.Card;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class Main extends Application {
    private Casino casino = new Casino();
    private Stage window;
    private Player currentPlayer;
    private BlackJack gameBJ;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setMinHeight(800);
        window.setMinWidth(800);

        window.setScene(new Scene(new StackPane()));

        // load font
        Font.loadFont(Main.class.getResourceAsStream("graphics/Windlass.ttf"),40);

        // main casino
        loginDialog();

        window.setTitle("Casino");
        window.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    private void mainCasino() {
        VBox layout = new VBox(10);
        layout.getStylesheets().add(getClass().getResource("graphics/casino.css").toExternalForm());

        //Label
        Label CASINO = new Label("CASINO");
        Label empty = new Label("");
        layout.getChildren().addAll(CASINO, empty);

        Button topUpAccount = new Button("Top Up Account");
        Button buyTokens = new Button("Buy tokens");
        Button blackJack = new Button("BlackJack");
        layout.getChildren().addAll(blackJack, buyTokens, topUpAccount);

        blackJack.setOnAction(e -> {
            int bet = askforBet();
            if (bet == 0) {
                mainCasino();
            }
            else {
                gameBJ = (BlackJack) casino.startGame(currentPlayer, "BlackJack", bet);
                BorderPane layoutBP = new BorderPane();
                Scene scene2 = new Scene(layoutBP);
                layoutBP.setStyle("-fx-background-color: radial-gradient(center 50% 50%, radius 100%, #4c7a15, #2f4f09);");
                BlackJackPanel(layoutBP, scene2);
                if (gameBJ.isGameover()) {
                    int prize = gameBJ.getPrize();
                    if (casino.getTotalTokens() < prize) {
                        currentPlayer.addTokens(casino.getTotalTokens());
                        casino.subtractTokens(casino.getTotalTokens());
                    }
                    else {
                        currentPlayer.addTokens(prize);
                        casino.subtractTokens(prize);
                    }
                }
            }
        });

        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 800, 600);
        window.setScene(scene);
        window.show();
    }

    private int askforBet() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("BlackJack");
        dialog.setHeaderText("Please type your bet.");
        dialog.setResizable(true);



        Label lblBet = new Label("Bet: ");
        TextField txtBet = new TextField();
        Label message = new Label("");


        DialogPane dialogPane = dialog.getDialogPane();

        GridPane grid = new GridPane();
        dialogPane.getStylesheets().add(getClass().getResource("graphics/dialogs.css").toExternalForm());
        grid.add(lblBet, 1, 1);
        grid.add(txtBet, 2, 1);
        grid.add(message, 1, 2);
        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeSubmit = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeSubmit);

        dialog.setResultConverter((Callback<ButtonType, String>) btn -> {
            if (btn == buttonTypeSubmit) {
                return new String(txtBet.getText());
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        int bet = 0;
        String strBet = "";
        if (result.isPresent()) {
            strBet = result.get();
            bet = Integer.parseInt(strBet);
            if (currentPlayer.getTokens() < bet) {
                message.setText("You have to buy more tokens.");
                return 0;
            }
        }

        return bet;

    }

    private void BlackJackPanel(BorderPane layout, Scene scene) {
        double width = window.getScene().getWidth();
        double height = window.getScene().getHeight();


        Button hit = new Button("H");
        Button stay = new Button("S");
        Button doubleD = new Button("D");
        Button split = new Button("P");

        Label message = new Label();


        // actions for buttons
        // hit
        final int[] prize = {0};
        hit.setOnAction(e -> {
            gameBJ.takeAction("H");
            //check whether game is still running
            if (gameBJ.isGameover()) {
                gameBJ.dealerTurn();
                dealerCards(layout, scene);
            }
            else {
                BlackJackPanel(layout, scene);
            }

        });

        // stay
        stay.setOnAction(e -> {
            gameBJ.takeAction("S");
            //check whether game is still running
            //check whether game is still running
            if (gameBJ.isGameover()) {
                gameBJ.dealerTurn();
                dealerCards(layout, scene);
            }
            else {
                BlackJackPanel(layout, scene);
            }
        });

        // double
        doubleD.setOnAction(e -> {
            gameBJ.takeAction("D");
            //check whether game is still running
            //check whether game is still running
            if (gameBJ.isGameover()) {
                gameBJ.dealerTurn();
                dealerCards(layout, scene);
            }
            else {
                BlackJackPanel(layout, scene);
            }
        });

        //split
        split.setOnAction(e -> {
            gameBJ.takeAction("P");
            //check whether game is still running
            //check whether game is still running
            if (gameBJ.isGameover()) {
                gameBJ.dealerTurn();
                dealerCards(layout, scene);
            }
            else {
                BlackJackPanel(layout, scene);
            }
        });

        HBox buttonLayout = new HBox(10);
        buttonLayout.getStylesheets().add(getClass().getResource("graphics/BJPanel.css").toExternalForm());
        buttonLayout.getChildren().addAll(hit, stay, doubleD, split);

        Canvas canvas = loadCards();
        canvas.setId("cards");
        layout.setCenter(canvas);
        buttonLayout.setAlignment(Pos.CENTER);
        BorderPane.setMargin(buttonLayout, new Insets(0,0,50,0));
        layout.setBottom(buttonLayout);

        scene.setRoot(layout);
        window.setScene(scene);
        window.show();
    }

    private Canvas loadCards() {
        //Coordinates for player Interface
        double width = window.getScene().getWidth();
        double height = window.getScene().getHeight() - 100;
        double cardHeight = height / 4;
        double cardWidth = cardHeight * 0.65;
        double space = cardWidth / 3;


        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        List<Card> dealerHand = gameBJ.getDealer().getHand();
        Image[] handImage = new Image[gameBJ.getDealer().getHand().size()];
        for (int i = 0; i < gameBJ.getDealer().getHand().size(); i++) {
            // get image
            String path = dealerHand.get(i).getPathName();
            handImage[i] = new Image(path);
            gc.drawImage(handImage[i], (width - handImage.length * (cardWidth + space) + space) / 2 + i * space, 10, cardWidth, cardHeight);
            gc.restore();
        }

        List<BJplayer> playerHands = gameBJ.getPlayerHands();
        List<Image[]> playerImage = new ArrayList<>();
        for (BJplayer player: playerHands) {
            Image[] hand = new Image[player.getHand().size()];
            playerImage.add(hand);
            for (int i = 0; i < player.getHand().size(); i++) {
                String path = player.getHand().get(i).getPathName();
                hand[i] = new Image(path);
                gc.drawImage(hand[i], (width - handImage.length * (cardWidth + space) + space) / 2 + i * space, height - 200 - i * space, cardWidth, cardHeight );
                gc.restore();
            }

        }
        return canvas;

    }

    private void dealerCards(BorderPane layout, Scene scene) {
        double width = window.getScene().getWidth();
        double height = window.getScene().getHeight();
        // show dealer's fisrt card
        gameBJ.getDealer().getHand().get(0).changeVisibility();

        Button newGame = new Button("New Game");
        Button backToMenu = new Button("Back to Menu");
        String message = "";

        gameBJ.countPrize();
        final int[] prize = {gameBJ.getPrize()};

        if (prize[0] != 0) {
            message = "You have won" + String.valueOf(prize[0]);
            if (casino.getTotalTokens() < prize[0]) {
                currentPlayer.addTokens(casino.getTotalTokens());
                casino.subtractTokens(casino.getTotalTokens());
            }
            else {
                currentPlayer.addTokens(prize[0]);
                casino.subtractTokens(prize[0]);
            }
        }

        newGame.setOnAction(e -> {
            int bet = askforBet();
            if (bet == 0) {
                mainCasino();
            }
            else {
                gameBJ = (BlackJack) casino.startGame(currentPlayer, "BlackJack", bet);
                BorderPane layoutBP = new BorderPane();
                Scene scene2 = new Scene(layoutBP);
                layoutBP.setStyle("-fx-background-color: radial-gradient(center 50% 50%, radius 100%, #4c7a15, #2f4f09);");
                BlackJackPanel(layoutBP, scene2);
                if (gameBJ.isGameover()) {
                    prize[0] = gameBJ.getPrize();
                    if (casino.getTotalTokens() < prize[0]) {
                        currentPlayer.addTokens(casino.getTotalTokens());
                        casino.subtractTokens(casino.getTotalTokens());
                    }
                    else {
                        currentPlayer.addTokens(prize[0]);
                        casino.subtractTokens(prize[0]);
                    }
                }
            }
        });

        backToMenu.setOnAction(e -> {
            mainCasino();
        });


        HBox buttonLayout = new HBox(10);
        buttonLayout.getStylesheets().add(getClass().getResource("graphics/BJPanel.css").toExternalForm());
        buttonLayout.getChildren().addAll(newGame, backToMenu);

        Canvas canvas = loadCards();
        //GraphicsContext gc = canvas.getGraphicsContext2D();
        //gc.strokeText(message, height - 400, width - 300, 60);
        canvas.setId("cards");
        layout.setCenter(canvas);
        buttonLayout.setAlignment(Pos.CENTER);
        BorderPane.setMargin(buttonLayout, new Insets(0,0,50,0));
        layout.setBottom(buttonLayout);

        scene.setRoot(layout);
        window.setScene(scene);
        window.show();
    }



    private void loginDialog() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10, 50, 50, 50));

        //Add Hbox
        HBox hBox = new HBox(10);
        hBox.setPadding(new Insets(20, 20, 20, 30));

        //Add GridPane
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        //implement Nodes for GridPane
        Label username = new Label("Username");
        TextField txtUsername = new TextField();
        Button btnLogin = new Button("Login");
        Label message = new Label();
        Button btnRegister = new Button("Sing Up");

        //add Nodes to GridPane layout
        gridPane.add(username, 0, 0);
        gridPane.add(txtUsername, 1, 0);
        gridPane.add(btnLogin, 0, 1);
        gridPane.add(message, 1, 2);
        gridPane.add(btnRegister, 1, 1);

        //Action for btnLogin
        final String[] checkUser = new String[1];
        btnLogin.setOnAction(actionEvent -> {
            checkUser[0] = txtUsername.getText().toString();
            String user = checkUser[0];
            Player player = new Player(user, 0, 0);
            if (!casino.checkIfInBase(player)) {
                System.out.println("You need to register.");
            }
            else {
                currentPlayer = casino.getPlayer(user);
                mainCasino();
            }

        });

        btnRegister.setOnAction(actionEvent -> {
            registerDialog();
        });

        //Add HBox and GridPane layout to BorderPane
        borderPane.setTop(hBox);
        borderPane.setCenter(gridPane);

        //Add BorderPane to scene
        Scene scene = new Scene(borderPane);
        window.setScene(scene);
        window.show();
    }

    private void registerDialog() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10, 50, 50, 50));

        //Add Hbox
        HBox hBox = new HBox(10);
        hBox.setPadding(new Insets(20, 20, 20, 30));

        //Add GridPane
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        //implement Nodes for GridPane
        Label username = new Label("Username");
        TextField txtUsername = new TextField();
        Label lblMoney = new Label("Your money");
        TextField txtUserMoney = new TextField();
        Button btnRegister = new Button("Sing Up");
        Label message = new Label();

        //add Nodes to GridPane layout
        gridPane.add(username, 0, 0);
        gridPane.add(txtUsername, 1, 0);
        gridPane.add(lblMoney, 0, 1);
        gridPane.add(txtUserMoney, 1, 1);
        gridPane.add(btnRegister, 2, 1);

        //Action for btnLogin
        final String[] checkUser = new String[2];
        btnRegister.setOnAction(actionEvent -> {
            checkUser[0] = txtUsername.getText().toString();
            String user = checkUser[0];
            checkUser[1] = txtUserMoney.getText().toString();
            String strMoney = checkUser[1];
            int money = Integer.parseInt(strMoney);
            Player player = new Player(user, money, 0);
            if (casino.checkIfInBase(player)) {
                message.setText("This name has been already taken.");
                gridPane.add(message, 2,2);
            }
            else {
                try {
                    casino.addPlayer(player);
                    currentPlayer = casino.getPlayer(user);
                    mainCasino();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //Add HBox and GridPane layout to BorderPane
        borderPane.setTop(hBox);
        borderPane.setCenter(gridPane);

        //Add BorderPane to scene
        Scene scene = new Scene(borderPane);
        window.setScene(scene);
        window.show();
    }
}
