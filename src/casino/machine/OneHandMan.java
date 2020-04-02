package casino.machine;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import java.util.List;

public class OneHandMan {
    // constructor
    public static int play( int playerTokens) {
        Scanner in = new Scanner(System.in);
        Random rand = new Random();
        int correctness = 0;
        String[] letters = {"A", "B", "C"};
        List<String> bet = new ArrayList<String>();
        List<String> gameResult = new ArrayList<String>();
        System.out.print("Make your bet");
        for (int i = 0; i < 3; i++) {
            bet.add(in.next());
            gameResult.add(letters[rand.nextInt(3)]);

            System.out.println(gameResult.get(i));
            if(bet.get(i).equals(gameResult.get(i))) {
                correctness ++;
            }
        }
        if (correctness == 0) {
            System.out.print("You lost");
            return 0;
        }
        if (correctness == 1) {
            System.out.print("1 correct");
            return playerTokens;
        }
        if (correctness == 2) {
            System.out.print("2 correct");
            return  2*playerTokens;
        }
        if (correctness == 3) {
            System.out.print("3 correct");
            return  4*playerTokens;
        }
        return 0;
    }
}
