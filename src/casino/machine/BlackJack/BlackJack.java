package casino.machine.BlackJack;


import casino.Casino;
import casino.Player;
import casino.machine.Machine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class BlackJack {
    private  Deck deck = new Deck();
    public  boolean gameover = false;
    private  BJplayer player;
    private  List<BJplayer> playerHands = new ArrayList<>();
    private  BJplayer dealer;
    private  int tokensAtTheBegging;
    private  int prize = 0;
    private  int currentCard = 4;
    private  int leftTokens;

    public BlackJack(int playerTokens, int leftTokens) {
        this.player = new BJplayer(playerTokens);
        this.dealer = new BJplayer(0);
        this.tokensAtTheBegging = tokensAtTheBegging;
        this.leftTokens = leftTokens;
        // prepare deck
        deck.shuffleDeck();
        this.deck = deck;

        // deal cards
        player.addCard(deck.getDeck()[0]);
        player.addCard(deck.getDeck()[1]);
        playerHands.add(player);

        dealer.addCard(deck.getDeck()[2]);
        dealer.getHand().get(0).changeVisibility();
        dealer.addCard(deck.getDeck()[3]);

        //check if Black Jack
        if (player.getPoints() == 21) {
            gameover = true;
            if (dealer.getPoints() == 21) {
                prize = tokensAtTheBegging;
                player.Draw();
            }
            else {
                prize = (int) (2.5 * tokensAtTheBegging);
                player.Winner();
            }
        }
        
        else prize = tokensAtTheBegging;
    }
    
    
    private  int iterHands = 0;
    private  int howManyHands = playerHands.size();
    private  int canPlay = 1;
    
    public  void takeAction(String action) {
        //hit
        if (action.equals("H")) {
            playerHands.get(iterHands).addCard(deck.getDeck()[currentCard]);
            currentCard++;
            System.out.println(playerHands.get(iterHands).getPoints());
            if (playerHands.get(iterHands).getPoints() >= 21) {
                playerHands.get(iterHands).cannotPlay();
                canPlay -= 1;
            }
        }
        // stand
        else if (action.equals("S")) {
            playerHands.get(iterHands).cannotPlay();
            canPlay -= 1;
        }
        // double
        else if (playerHands.get(iterHands).getHand().size() == 2 && action.equals("D")) {
            if (leftTokens >= 2 * playerHands.get(iterHands).getBet()) {
                playerHands.get(iterHands).addCard(deck.getDeck()[currentCard]);
                prize += playerHands.get(iterHands).getBet();
                leftTokens -= playerHands.get(iterHands).getBet();
                playerHands.get(iterHands).doubleBet();
                playerHands.get(iterHands).cannotPlay();
                canPlay -= 1;
                currentCard++;
            }
            else System.out.println("You don't have enough tokens.");
        }
        // split
        else if (playerHands.get(iterHands).getHand().size() == 2 && action.equals("P") && playerHands.get(iterHands).getHand().get(0).getName().equals(playerHands.get(iterHands).getHand().get(1).getName())) {
            if (leftTokens >= 2 * playerHands.get(iterHands).getBet()) {
                prize += playerHands.get(iterHands).getBet();
                leftTokens -= playerHands.get(iterHands).getBet();

                howManyHands += 1;
                canPlay += 1;
                BJplayer newPlayer = new BJplayer(playerHands.get(iterHands).getBet());
                newPlayer.addCard(playerHands.get(iterHands).getHand().get(0));
                playerHands.set(iterHands, newPlayer);
                playerHands.add(newPlayer);
            }
            else System.out.println("You don't have enough tokens.");
        }
        iterHands ++;
        if (canPlay == 0) {
            gameover = true;
        }
        else iterHands = iterHands % canPlay;

    }

    public void dealerTurn() {
        int dealerIter = 2;
        boolean move = true;
        while (move) {
            if (dealer.getPoints() < 17) {
                dealer.addCard(deck.getDeck()[currentCard]);
                System.out.println("Next dealer card is: " + dealer.getHand().get(dealerIter).toString());
            }
            else move = false;
        }
    }

    public  void countPrize() {
        for (int i = 0; i < howManyHands; i++) {
            if (playerHands.get(i).getPoints() > 21) {
                prize = 0;
                playerHands.get(i).Loser();
            }
            else if (playerHands.get(i).getPoints() < dealer.getPoints()) {
                prize = 0;
                playerHands.get(i).Loser();
                playerHands.get(i).assignPrize(-playerHands.get(i).getBet());
            }
            else if (playerHands.get(i).getPoints() == dealer.getPoints()) {
                prize += playerHands.get(i).getBet();
                playerHands.get(i).Draw();
                playerHands.get(i).assignPrize(0);
            }
            else if (playerHands.get(i).getPoints() > dealer.getPoints()) {
                prize += 2 * playerHands.get(i).getBet();
                playerHands.get(i).Winner();
                playerHands.get(i).assignPrize(playerHands.get(i).getBet());
            }
        }
    }



    public  int getPrize() {
        return prize;
    }

    public  boolean isGameover() {
        return gameover;
    }


    // getters
    public  BJplayer getPlayer() {
        return player;
    }

    public  List<BJplayer> getPlayerHands() {
        return playerHands;
    }

    public  BJplayer getDealer() {
        return dealer;
    }


}
