package casino.machine.BlackJack;

import java.util.ArrayList;
import java.util.List;

public class BJPlayer2 {
    private List<Hand> hands = new ArrayList<>();
    private int bet;
    private int leftTokens;
    private int prize = 0;
    private int whoseTurn = 1;
    private int activeHands = 1;

    public BJPlayer2(int bet, int leftTokens) {
        this.bet = bet;
        this.leftTokens = leftTokens;
    }

    public void addHand(Hand hand) {
        hands.add(hand);
    }

    public void modifyPrize(int tokens) {
        prize += tokens;
    }

    public void checkTurn(int currentHand) {
        int i = (currentHand + 1) % hands.size();
        hands.get(currentHand).setTurn(false);
        whoseTurn = -1;
        for (int j = 0; j < hands.size(); j++) {
            if (hands.get(i).getCanPlay()) {
                hands.get(i).setTurn(true);
                whoseTurn = i;
                break;
            }
            hands.get(i).setTurn(false);
            i = (i + 1) % hands.size();
        }
    }

    public void subLeftTokens(int howMany) {
        leftTokens -= howMany;
    }

    public void changeActiveHands(int number) {
        activeHands += number;
    }

    // getters
    public List<Hand> getHands() {
        return hands;
    }

    public int getPrize() {
        return prize;
    }

    public int getWhoseTurn() {
        return whoseTurn;
    }

    public int getLeftTokens() {
        return leftTokens;
    }

    public int getBet() {
        return bet;
    }

    public int getActiveHands() {
        return activeHands;
    }
}
