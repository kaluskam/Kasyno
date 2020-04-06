package casino.machine;

import casino.machine.BlackJack.BlackJack2;

public class Machine {
    private int tokens = 1000;
    private boolean ON = false;
    // constructor
    public  Machine() {
    }

    public Object runGame(String gameName, int bet, int leftTokens) {
        if (gameName.equals("BlackJack")) {
            BlackJack2 game = new BlackJack2(bet, leftTokens);
            return game;
        }
        return null;
    }


    public void switchON() {
        this.ON = true;
    }

    public void turnOff() {
        this.ON = false;
    }

    public void addTokens(int howMany) {
        this.tokens += howMany;
    }

    public boolean subtractTokens(int howMany) {
        if (this.tokens < howMany) {
            return false;
        }
        this.tokens -= howMany;
        return true;
    }

    // getters

    public boolean getON() {
        return ON;
    }

    public int getTokens() {
        return tokens;
    }

}
