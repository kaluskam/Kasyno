package casino.machine.BlackJack;

import java.util.ArrayList;
import java.util.List;

public class BJplayer {
    private List<Card> hand = new ArrayList<Card>();
    private int points = 0;
    private int aces = 0;
    private boolean canPlay = true;
    private int bet = 0;
    private int winner = -1;
    private int prize = 0;

    public BJplayer(int bet) {
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

    public void cannotPlay() {
        canPlay = false;
    }

    public void Winner() {
        winner = 1;
    }

    public void Draw() {
        winner = 0;
    }

    public void Loser() {
        winner = -1;
    }

    public void assignPrize(int Prize) {
        prize = Prize;
    }

    // getters

    public boolean getCanPlay() {
        return canPlay;
    }

    public List<Card> getHand() {
        return hand;
    }

    public int getPoints() {
        return points;
    }

    public void doubleBet() {
        this.bet += bet;
    }

    public int getBet() {
        return bet;
    }

    public int getWinner() {
        return winner;
    }

    public int getPrize() {
        return prize;
    }
}
