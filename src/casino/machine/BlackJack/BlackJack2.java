package casino.machine.BlackJack;

public class BlackJack2 {
    private Deck deck = new Deck();
    private BJPlayer2 dealer;
    private BJPlayer2 player;
    private boolean gameOver = false;

    private int currentCard = 4;
    // information about player
    private int whichHandTurn = 1;
    private Hand currentHand;

    public BlackJack2(int bet, int leftToken) {
        // prepare deck
        deck.shuffleDeck();
        deck.getDeck()[3] = new Card("4", 4, 'H');
        deck.getDeck()[4] = new Card("4", 4, 'S');
        deck.getDeck()[2] = new Card("4", 4, 'C');


        // make players
        dealer = new BJPlayer2(0, 0);
        player = new BJPlayer2(bet, leftToken);

        // deal first 4 cards
        // to dealer
        Hand dealerHand = new Hand(0);
        deck.getDeck()[0].changeVisibility();
        dealerHand.addCard(deck.getDeck()[0]);
        dealerHand.addCard(deck.getDeck()[1]);
        dealer.addHand(dealerHand);
        // to player
        Hand firstHand = new Hand(bet);
        firstHand.addCard(deck.getDeck()[2]);
        firstHand.addCard(deck.getDeck()[3]);
        player.addHand(firstHand);
        currentHand = player.getHands().get(0);

        // check if black jack
        if (firstHand.getPoints() == 21) {
            // draw
            if (dealerHand.getPoints() == 21) {
                player.modifyPrize(bet);
                player.getHands().get(0).setPrize(0);
            }
            // win
            else player.modifyPrize((int) (bet * 2.5));
            player.getHands().get(0).setPrize((int) (bet * 1.5));
        }

    }

    private int curHand = 0;
    private boolean isMoveCorrect = false;
    public void takeAction(String action) {        

        // hit
        if (action.equals("H")) {
            currentHand.addCard(deck.getDeck()[currentCard]);
            isMoveCorrect = true;
            if (currentHand.getPoints() >= 21) {
                player.changeActiveHands(-1);
                currentHand.cannotPlay();
            }
            currentCard++;
        }

        // stay
        if (action.equals("S")) {
            currentHand.cannotPlay();
            isMoveCorrect = true;
            player.changeActiveHands(-1);
        }

        // double
        if (action.equals("D") && currentHand.getHand().size() == 2 && currentHand.getBet() * 2 <= player.getLeftTokens()) {
            player.subLeftTokens(currentHand.getBet());
            currentHand.setDoubled();
            deck.getDeck()[currentCard].rotate();
            currentHand.addCard(deck.getDeck()[currentCard]);
            currentHand.cannotPlay();
            currentCard++;
            isMoveCorrect = true;
            player.changeActiveHands(-1);
        }

        // split
        if (action.equals("P") && currentHand.getHand().size() == 2 && currentHand.getBet() <= player.getLeftTokens()
                && currentHand.getHand().get(0).getName().equals(currentHand.getHand().get(1).getName())){
            player.subLeftTokens(currentHand.getBet());

            Hand prevHand = new Hand(currentHand.getBet());
            prevHand.addCard(currentHand.getHand().get(0));
            Hand newHand = new Hand(currentHand.getBet());
            newHand.addCard(currentHand.getHand().get(1));
            prevHand.setTurn(false);

            player.getHands().set(curHand, prevHand);

            // slide hands
            if (player.getHands().size() == curHand + 1) {
                player.addHand(newHand);
            }
            else if(player.getHands().size() == 2 || (player.getHands().size() == 3 && curHand == 1)) {
                //add last hand
                player.addHand(player.getHands().get(player.getHands().size() - 1));
                // add new hand
                player.getHands().set(curHand + 1,newHand);
            }
            else {
                //add last hand
                player.addHand(player.getHands().get(2));
                // move second hand
                Hand temp = player.getHands().get(1);
                player.getHands().set(2, temp);
                player.getHands().set(1, newHand);
            }
            isMoveCorrect = true;
            curHand++;
            player.changeActiveHands(1);
        }
        
        // check if move was correct
        if (isMoveCorrect)
        {player.checkTurn(curHand);
            curHand = player.getWhoseTurn();
            isMoveCorrect = false;
            if (curHand == -1) {
                gameOver = true;
                return;
            }
            currentHand = player.getHands().get(curHand);
        }
        // wait as long as move is correct
        else {currentHand = player.getHands().get(curHand);}        
    }

    public void dealerTurn() {
        int dealerIter = 2;
        while (dealer.getHands().get(0).getPoints() < 17) {
            dealer.getHands().get(0).addCard(deck.getDeck()[currentCard]);
            currentCard++;
        }
    }
    
    public int Prize() {
        for (int i = 0; i < player.getHands().size(); i++) {
            // draw
            if (player.getHands().get(i).getPoints() == dealer.getHands().get(0).getPoints() && player.getHands().get(i).getPoints() <= 21) {
                player.getHands().get(i).changeResult(0);
                player.getHands().get(i).setPrize(0);
            }
            // win
            else if (player.getHands().get(i).getPoints() > dealer.getHands().get(0).getPoints() && player.getHands().get(i).getPoints() <= 21) {
                player.getHands().get(i).changeResult(1);
                player.getHands().get(i).setPrize(player.getHands().get(i).getBet());
            }
            else if (player.getHands().get(i).getPoints() <= 21 && dealer.getHands().get(0).getPoints() > 21) {
                player.getHands().get(i).changeResult(1);
                player.getHands().get(i).setPrize(player.getHands().get(i).getBet());
            }
            else {
                player.getHands().get(i).changeResult(-1);
                player.getHands().get(i).setPrize(-player.getHands().get(i).getBet());
            }

            if (i == 0) {
                if (player.getHands().get(0).getResult() >= 0) player.getHands().get(0).setPrize(player.getBet());
            }

            player.modifyPrize(player.getHands().get(i).getPrize());
            System.out.println(player.getPrize());
        }
        return player.getPrize();
    }
    
    // getters
    public boolean isGameOver() {
        if (player.getActiveHands() > 0) {
            gameOver = false;
        }
        else gameOver = true;
        return gameOver;
    }

    public BJPlayer2 getPlayer() {
        return player;
    }

    public BJPlayer2 getDealer() {
        return dealer;
    }
}

