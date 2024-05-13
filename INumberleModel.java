import java.io.FileNotFoundException;
import java.util.List;
import java.util.Observer;

public interface INumberleModel {

    void initialize() throws FileNotFoundException;

    boolean processInput(String input);

    boolean isGameOver();

    boolean isGameWon();

    int getMaxGuess();

    int getEquationLength();

    String getTargetEquation();

    String getCurrentGuess();

    int getRemainingAttempts();

    void startNewGame() throws FileNotFoundException;

    public void setObserver(Observer observer);
}
