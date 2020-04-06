package casino.machine.BlackJack;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private int bet;
    private List<Card> hand = new ArrayList<>();
    private int points = 0;
    // how many active aces are on hand
    private int aces = 0;
    private int prize = 0;

    private boolean Doubled = false;
    private boolean Turn = true;
    private boolean canPlay = true;
    // 0 - draw, -1 - lose, 1 - win
    private int result = -1;

    public Hand(int bet) {
        this.bet = bet;
    }

    public void addCard(Card card) {
        hand.add(card);
        if (card.getName().equals("A")){
            aces ++;
        }
        if (points + card.getValue() > 21 && aces > 0) {
            points += (card.getValue() - 10);
            aces--;
        }
        else points += card.getValue();
    }

    public void setDoubled() {
        bet *= 2;
        Doubled = true;
    }

    public void setTurn(boolean value) {
        Turn = value;
    }

    public void cannotPlay() {
        canPlay = false;
    }

    public void changeResult(int result) {
        this.result = result;
    }

    public void setPrize(int amount) {
        prize += amount;
    }

    //getters
    public int getBet() {
        return bet;
    }
    public List<Card> getHand() {
        return hand;
    }

    public int getPoints() {
        return points;
    }

    public boolean isTurn() {
        return Turn;
    }

    public boolean getCanPlay() {
        return canPlay;
    }

    public int getResult() {
        return result;
    }

    public int getPrize() {
        return prize;
    }


}
