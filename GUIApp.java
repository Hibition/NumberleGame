import javax.swing.*;
import java.io.FileNotFoundException;


public class GUIApp {
    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        try {
                            createAndShowGUI();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    public static void createAndShowGUI() throws FileNotFoundException {
        INumberleModel model = new NumberleModel();
        System.out.println(model.getRemainingAttempts());
        NumberleController controller = new NumberleController(model);
        controller.startNewGame();
        NumberleView view = new NumberleView(controller, model.getMaxGuess(), model.getEquationLength());

    }
}
