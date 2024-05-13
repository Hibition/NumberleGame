// NumberleModel.java
import validations.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class NumberleModel extends Observable implements INumberleModel {
    private static final int maxGuess = 6;
    private static final int equationLength = 7;

    private String targetEquation;
    private String currentGuess;
    private int remainingAttempts;
    private boolean gameWon;
    private Validation[] validators = {new ValidateLength(equationLength), new ValidateCharacter(),
            new ValidateFormate(equationLength), new ValidateMathmatic()};

    private String errorMessage = null;
    private int[] guessSituation = new int[equationLength]; // the state of each symbols of currentGuess.
    private Map<Character, Integer> symbolStatus = new HashMap<>(); // the map to store the state of ten number and four arithmetic sign

    private boolean showEquationFlag = false;
    private boolean validEquationFlag = true;
    private boolean randomEquationFlag = false;


    private boolean invariant(){
        /**
         * The remaining attempts must not be negative and less or equal to max guess.
         */
        return maxGuess >= remainingAttempts && remainingAttempts >= 0;
    }

    public int getMaxGuess(){
        /**
         * @pre. none
         * @post. the number of max guess should not be negative.
         */
        assert maxGuess >=0 : "The max chances of guess should more than 0";
        return maxGuess;
    }

    public int getEquationLength(){
        /**
         * @pre. none
         * @post. the number of equation length should more than three because need at lease one '=' and two number.
         */
        assert equationLength >= 3 : "The equation length must more than 3";
        return equationLength;
    }

    public int[] getGussSiduation(){
        /**
         * @pre. guess situation must exist
         * @post.  the length should be 7.
         */
        assert guessSituation != null : "the guessSituation must not be null";
        assert guessSituation.length == equationLength :  "the length of guess situation must be 7";
        return guessSituation;
    }

    public Map<Character, Integer> getSymbolStatus(){
        /**
         * @pre. symbol status must exist.
         * @post. none
         */
        assert symbolStatus != null : "the symbolState can't be null.";
        return symbolStatus;
    }

    @Override
    public String getTargetEquation() {
        /**
         * @pre. the targetEquation must exist
         * @post. none.
         */
        assert targetEquation != null : "the target equation must noe be null";
        return targetEquation;
    }

    @Override
    public String getCurrentGuess() {
        /**
         * @pre. the current guess must exist.
         * @post. the length of current guess must fit equation length.
         */
        assert currentGuess != null : "the current guess must not be null.";
        assert currentGuess.length() == equationLength : "the length of current Guess must equal to equation length";
        return currentGuess;
    }

    @Override
    public int getRemainingAttempts() {
        /**
         * @pre. none
         * @post. invariant must maintain true.
         */
        assert invariant():"invariant must be maintain true";
        return remainingAttempts;
    }

    @Override
    public void startNewGame() throws FileNotFoundException {
        /**
         * @pre. none
         * @post. invariant must maintain true.
         */
        initialize();
        assert invariant():"invariant must be maintain true";
    }

    public void setObserver(Observer observer){
        /**
         * @pre. invariant should be ture at beginning.
         * @pre. observer must exist.
         * @post. invariant must maintain true.
         */
        assert invariant():"invariant must be true initially";
        assert observer != null : "observer must not be null";
        this.addObserver(observer);
        assert invariant():"invariant must be maintain true";
    }

    public void clearErrorMessage(){
        /**
         * @pre. none.
         * @post. the errorMessage must be null;
         */
        this.errorMessage = null;
        assert errorMessage == null : "the error message must be null after clear";
    }

    public String getErrorMessage(){
        /**
         * @pre. none
         * @posst. none
         */
        return errorMessage;
    }

    @Override
    public boolean isGameOver() {
        /**
         * @pre. invariant should be ture at beginning.
         * @post. none
         */
        assert invariant():"invariant must be true initially";
        return remainingAttempts <= 0 || gameWon;
    }

    @Override
    public boolean isGameWon() {
        /**
         * @pre. none
         * @post. none.
         */
        return gameWon;
    }


    @Override
    public void initialize() throws FileNotFoundException {
        /**
         * @pre. none
         * @post. targetEquation must have value.
         * @post. currentGuess must not be null after initialization.
         * @post. invariant must be true after initialize.
         */

        // read the file and randomly choose one equation.
        try {
            InputStream inputStream = new FileInputStream("equations.txt");
            Scanner file = new Scanner(inputStream, "utf-8");
            int lineNum = 1;
            if(randomEquationFlag) {    // if randomEquationFlag is true, random choose one equation from the file.
                Random rand = new Random();
                lineNum = rand.nextInt(107) + 1;
            }

            for(int i=0; i<lineNum; i++) {
                targetEquation = file.nextLine();
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        assert targetEquation != null : "targetEquation must exit after read the file";

        if(showEquationFlag) System.out.println(targetEquation); // if the show equation flag is true, print out the target equation for testing

        currentGuess = "       ";
        remainingAttempts = maxGuess;
        assert invariant():"invariant must be true";
        assert currentGuess != null : "currentGuess must not be null after initialization";

        gameWon = false;
        guessSituation[0] = -1;
        setChanged();
        notifyObservers();
    }

    @Override
    public boolean processInput(String input) {
        /**
         * @pre. invariant should be true at the beginning.
         * @pre. input must exist.
         * @post. remainingAttempts should stay the same if the input is not valid while the validEquationFlag is true.
         * @post. remainingAttempts should reduce if invalid input while validEquationFlag is false.
         * @post. remainingAttempts should reduce after finish one try.
         * @post. invariant should maintain true at the end.
         */

        assert invariant():"invariant must be true initially";
        assert input != null : "the input must not be null";
        input = input.replaceAll("\\s", "");
        currentGuess = input;

        int oldAttempts = remainingAttempts;
        for(int i=0; i<validators.length; i++){
            String val = validators[i].validate(input);
            if(val != null){
                if(validEquationFlag) {
                    errorMessage = val;
                    System.out.println("\u001B[34m" + val + "\u001B[0m");
                    setChanged();
                    notifyObservers();
                    assert remainingAttempts == oldAttempts : "The remaining attempts should not reduce will input invalid equation.";
                    return false;
                }else{
                    remainingAttempts--;
                    setChanged();
                    notifyObservers();
                    assert remainingAttempts == oldAttempts - 1 : "The remaining attempts should reduce one while invalid input if validEquationFlag be set false.";
                    return true;
                }

            }
        }

        for(int i=0; i<input.length(); i++){
            if(input.charAt(i) == targetEquation.charAt(i)){
                guessSituation[i] = 0;
                symbolStatus.put(input.charAt(i), 0);
                System.out.println("\u001B[32m" + input.charAt(i) + ": is in the Right place" + "\u001B[0m");
            }else if(targetEquation.contains(String.valueOf(input.charAt(i)))){
                guessSituation[i] = 1;
                if(symbolStatus.get(input.charAt(i)) == null || symbolStatus.get(input.charAt(i)) != 0) symbolStatus.put(input.charAt(i), 1);
                System.out.println("\u001B[33m" + input.charAt(i) + ": is in the Wrong place" + "\u001B[0m");
            }else{
                guessSituation[i] = 2;
                symbolStatus.put(input.charAt(i), 2);
                System.out.println(input.charAt(i) + ": not exist.");
            }
        }

        if(input.equals(targetEquation)){
            this.gameWon = true;
        }

        remainingAttempts--;
        setChanged();
        notifyObservers();
        assert invariant():"invariant must be maintained";
        assert remainingAttempts == oldAttempts - 1 : "The remaining attempts should reduce one after one try";
        return true;
    }

}
