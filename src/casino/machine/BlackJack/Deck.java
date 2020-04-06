package casino.machine.BlackJack;

import java.util.Random;

public class Deck {
    String CARD_NAMES = "234567890JQKA";
    int[] CARD_VALUES = {2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11};
    String CARD_COLORS = "SHDC";
    private Card[] deck = new Card[52];

    public Deck() {
        for (int i = 0; i < 52; i++) {
            String name = String.valueOf(CARD_NAMES.charAt(i%13));
            deck[i] = new Card(name, CARD_VALUES[i%13], CARD_COLORS.charAt((int) Math.floor(i/13)));
        }
    }

    public void shuffleDeck() {
        Random rand = new Random();
        for (int i = 52; i > 1; i--) {
            Card temp = deck[i - 1];
            int newPosition = rand.nextInt(i);
            deck[i - 1] = deck[newPosition];
            deck[newPosition] = temp;
        }
    }

    public String printDeck() {
        String output = "";
        for (int i = 0; i < 52; i++) {
            output = output + " " + deck[i].toString();
        }
        return output;
    }

    public Card[] splitDeck() {
        deck[0] = new Card("2", 2, 'H');
        deck[1] = new Card("2", 2, 'D');
        deck[4] = new Card("2", 2, 'C');
        deck[5] = new Card("2", 2, 'S');
        return deck;

    }

    public Card[] getDeck() {
        return deck;
    }

}
