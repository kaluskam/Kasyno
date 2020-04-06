package casino;

public class Player {
    private String nickName;
    private int money;
    private int tokens;

    // constructor
    public Player(String name, int money, int tokens) {
        this.money = money;
        this.tokens = tokens;
        this.nickName = name;
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

    public boolean buyTokens(int price) {
        if (price > this.money) {
            return false;
        }
        this.money -= price;
        this.tokens += price/5;
        return true;
    }

    //getters

    public String getNickName() {
        return  nickName;
    }

    public int getMoney() {
        return money;
    }

    public int getTokens() {
        return tokens;
    }

    public String toString() {
        String strMoney = String.valueOf(money).toString();
        String strTokens = String.valueOf(tokens).toString();
        return nickName + " " + strMoney + " " + strTokens;
    }

    public void addMoney(int Money) {
        money += Money;
    }
}
