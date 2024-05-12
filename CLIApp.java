import java.awt.*;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

public class CLIApp {

    private static String getKeyList(Map<Character, Integer> symbolStatus, int value) {
        String res = "";
        for (Map.Entry<Character, Integer> entry : symbolStatus.entrySet()) {
            if (entry.getValue().equals(value)) {
                res = res + entry.getKey() + ", ";
            }
        }
        assert res != null : "result can't be null";
        return res;
    }

    public static void main(String[] args) throws FileNotFoundException {

        Scanner user_input = new Scanner(System.in);

        NumberleModel model = new NumberleModel();

        System.out.println(model.getRemainingAttempts());

        model.startNewGame();

        while (!model.isGameOver()) {
            System.out.println("enter the game");
            String str = "";
            do {
                str = user_input.nextLine();
            } while (!model.processInput(str));

            Map<Character, Integer> symbolStatus = model.getSymbolStatus();
            System.out.print("The symbol in right place: ");
            System.out.println("\u001B[32m" + getKeyList(symbolStatus, 0) + "\u001B[0m");
            System.out.print("The symbol in wrong place: ");
            System.out.println("\u001B[33m" + getKeyList(symbolStatus, 1) + "\u001B[0m");
            System.out.print("The symbol not in equation: ");
            System.out.println(getKeyList(symbolStatus, 2));

            if (model.isGameOver()) {
                if (model.isGameWon()) {
                    System.out.println("You are win!");
                } else {
                    System.out.println("You are lose!");
                }
                break;
            }
            System.out.println("You have remain: " + model.getRemainingAttempts() + " chances.");
        }
    }


}
