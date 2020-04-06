package casino;

import casino.machine.BlackJack.BlackJack2;

import java.io.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Casino {
    private int totalMoney = 100000;
    private int totalTokens = 5000;
    private List<Player> playerBase = new ArrayList<Player>();
    // constructor
    public Casino() {
    //prepare base with players
        try {
            File myObj = new File("src/casino/PlayersBase");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] specData = data.split(" ");
                playerBase.add(new Player(specData[0], Integer.parseInt(specData[1]), Integer.parseInt(specData[2])));
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void addPlayer(Player player) throws IOException {

        if (!checkIfInBase(player)) {
            this.playerBase.add(player);
            try
            {
                String filename = "src/casino/PlayersBase";
                FileWriter fw = new FileWriter(filename,true); //the true will append the new data
                fw.write("\n" + player.toString());//appends the string to the file
                fw.close();
            }
            catch(IOException ioe)
            {
                System.err.println("IOException: " + ioe.getMessage());
            }
        }
    }

    public void refreshPlayer(Player player) throws IOException {
        try {
            File inputFile = new File("src/casino/PlayersBase");
            File tempFile = new File("tempFile.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] splittedLine = currentLine.split(" ");
                if (splittedLine[0].equals(player.getNickName())) {
                    writer.write(player.toString() + "\n");
                    continue;
                }
                writer.write(currentLine + "\n");
            }
            writer.close();
            reader.close();
            inputFile.delete();
            tempFile.renameTo(inputFile);
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public boolean checkIfInBase(Player player) {
        if (playerBase.size() == 0) return false;
        for (int i = 0; i < this.playerBase.size(); i++) {
            if (this.playerBase.get(i).getNickName().equals(player.getNickName())) {
                return true;
            }
        }
        return false;
    }

    public Object startGame(Player player, String gameName, int bet){
        if (player.subtractTokens(bet)) {
            totalTokens += bet;
            int leftTokens = player.getTokens();
            if (gameName.equals("BlackJack")) {
                BlackJack2 game = new BlackJack2(bet, leftTokens);
                return game;
            }
        }
        return null;
    }

    public void addTokens(int howMany) {
        this.totalTokens += howMany;
    }

    public boolean subtractTokens(int howMany) {
        if (this.totalTokens < howMany) {
            return false;
        }
        this.totalTokens -= howMany;
        return true;
    }

    // getters
    public List<Player> getPlayerBase() {
        return playerBase;
    }

    public Player getPlayer(String nickname) {
        for (int i = 0; i < playerBase.size(); i++) {
            if(playerBase.get(i).getNickName().equals(nickname)) return playerBase.get(i);
        }
        return null;
    }

    public int getTotalTokens() {
        return totalTokens;
    }

}
