package casino.machine.BlackJack;

public class Card {
    private String name;
    private int value;
    private char color;
    private String pathName;
    private boolean visible = true;
    private boolean rotated = false;

    public Card(String name, int value, char color ) {
        this.name = name;
        this.value = value;
        this.color = color;
    }

    public void changeVisibility() {
        if (visible) visible = false;
        else visible = true;
    }

    public void rotate() {
        rotated = true;
    }

    public String toString() {
        if (name.equals("0")) {
            return "10" + String.valueOf(color);
        }
        return name + String.valueOf(color);
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public char getColor() {
        return color;
    }

    public String getPathName() {
        String cardName;
        if (name.equals("0")) {
            cardName = "10" + String.valueOf(color);
        }
        else cardName = name + String.valueOf(color);

        if (visible) {
            pathName = "sample/graphics/cards/" + cardName + ".png";
            return pathName;
        }
        else {
            pathName = "sample/graphics/cards/red_back.png";
            }
        return pathName;
    }

    public boolean getVisible() {
        return visible;
    }

    public boolean getRotated() {
        return rotated;
    }
}
