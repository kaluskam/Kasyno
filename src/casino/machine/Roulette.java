package casino.machine;

import java.util.Random;
import java.util.Scanner;

public class Roulette {
    // 0 - black, 1 - red, -1 - 0

    public static int play(int playerTokens) {
        int[] colors = {-1,1,0,1,0,1,0,1,0,1,0,0,1,0,1,0,1,0,1,1,0,1,0,1,0,1,0,1,0,0,1,0,1,0,1,0,1};
        System.out.println("Type color or field number");
        Scanner in = new Scanner(System.in);
        String bet = in.next();
        Random rand = new Random();
        int result = rand.nextInt(37);
        System.out.println(result);
        if (bet.equals("red")) {
            if (colors[result] == 1) return 2*playerTokens;
            return 0;
        }
        if (bet.equals("black")) {
            if (colors[result] == 0) return 2*playerTokens;
            return 0;
        }
        int bet1 = Integer.parseInt(bet);
        if (bet1 == result) return 9*playerTokens;
        return 0;

    }
}
