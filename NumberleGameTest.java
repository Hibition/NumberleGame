import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class NumberleGameTest {
    private NumberleModel model;

    @Before
    public void setUp() throws Exception {
        model = new NumberleModel();
    }


    @Test
    public void testInitialize() throws Exception{
        /**
         * @pre. The model should not be null.
         * @post. the target equation should not be null after game initialized
         * @post. remaining attempts should be set to MAX_ATTEMPTS,
         * @post. the game should not be won or over at initialization.
         */

        assertNotNull("The model should exist", model);

        model.startNewGame();

        assertNotNull("Target equation should not be null after initialization", model.getTargetEquation());
        assertEquals("Initial remaining attempts should be MAX_ATTEMPTS", NumberleModel.MAX_ATTEMPTS, model.getRemainingAttempts());
        assertFalse("Game should not be won at start", model.isGameWon());
        assertFalse("Game should not be over at start", model.isGameOver());

    }


    @Test
    public void testGameProcess() throws FileNotFoundException {
        /**
         * @pre The game should not be won or over after start.
         * @post The game should be over after MAX_ATTEMPTS wrong attempts.
         * @post. The current guess of the model should be equal to the input.
         * @post. The game should not be won after the first wrong try, and the game should not be over after the first wrong try.
         * @post. The initial remaining attempts should be MAX_ATTEMPTS, and it should decrease by 1 after each wrong attempt until it reaches 0.
         * @post. The game should not be won after running out of chances, but the game should be over after MAX_ATTEMPTS wrong attempts.
         * @post. After inputting the correct equation, the game should be won and over.
         * @post. The color should show the information of situation about the input.
         * @post. The color should be all green after input the right equation.
         */

        model.startNewGame();

        assertFalse("Game should not be won at start", model.isGameWon());
        assertFalse("Game should not be over at start", model.isGameOver());

        // test game loss process
        String wrongEquation = "1+2*3=7";   // because the randomly choose flag is false, we can make sure that '1+2*3=7' is wrong equation
        model.processInput(wrongEquation);
        System.out.println(model.getCurrentGuess());
        assertEquals("the current guess of model should equal to the input", wrongEquation, model.getCurrentGuess());
        assertFalse("Game should not be won after first wrong try", model.isGameWon());
        assertFalse("Game should not be over after first wrong try", model.isGameOver());
        int[] rightIndeed = {2, 0, 1, 0, 1, 0, 2};
        assertArrayEquals("The color of guess situation should indeed the status of guess", model.getGussSiduation(), rightIndeed);
        assertEquals("Initial remaining attempts should reduce 1 after input", NumberleModel.MAX_ATTEMPTS - 1, model.getRemainingAttempts());
        for (int i = 0; i < NumberleModel.MAX_ATTEMPTS - 1; i++) {
            model.processInput(wrongEquation);
        }
        assertFalse("Game should not be won after run out the chances", model.isGameWon());
        assertEquals("Initial remaining attempts should be 0", 0, model.getRemainingAttempts());
        assertTrue("Game should be over after MAX_ATTEMPTS wrong attempts", model.isGameOver());

        // test game won process
        model.startNewGame();
        String targetEquation = model.getTargetEquation();
        System.out.println(targetEquation);
        assertFalse("Game should not be won at start", model.isGameWon());
        assertFalse("Game should not be over at start", model.isGameOver());

        model.processInput(targetEquation);
        int[] targetIndeed = {0,0,0,0,0,0,0};
        assertTrue("Game should be won after input correct equation", model.isGameWon());
        assertTrue("Game should won after input correct equation", model.isGameOver());
        assertArrayEquals("The color of guess situation should all 0 (means green)", model.getGussSiduation(), targetIndeed);

    }

    @Test
    public void testInputValid() throws FileNotFoundException {
        /**
         * @pre The NumberleModel instance is initialized and ready for testing.
         * @post The method tests various scenarios including valid input, input with incorrect length, input with invalid characters,
         *       input with invalid equation format, and input with incorrect math. The assertions verify that the controller processes
         *       the input correctly, updates the remaining attempts as expected, and generates appropriate error messages when necessary.
         */
        model.startNewGame();

        // test the input which length not equal to 7
        String lessLength = "1=1";
        String moreLength = "2222=2222";
        assertFalse("Processing less length input should return false", model.processInput(lessLength));
        assertNotNull("Error message should not be null after processing less length input", model.getErrorMessage());
        assertFalse("Processing long length input should return false", model.processInput(moreLength));
        assertNotNull("Error message should not be null after processing long length input", model.getErrorMessage());
        assertEquals("Initial remaining attempts should not change with invalid length", NumberleModel.MAX_ATTEMPTS, model.getRemainingAttempts());

        // test the character validate
        String notNumber = "aaa%%%b";
        assertFalse("Processing other symbol (not number or '+-*/) input should return false", model.processInput(moreLength));
        assertNotNull("Error message should not be null after processing other symbol (not number or '+-*/) input", model.getErrorMessage());
        assertEquals("Initial remaining attempts should not change with other symbol (not number or '+-*/) input", NumberleModel.MAX_ATTEMPTS, model.getRemainingAttempts());

        // test the equation Formate
        String invalidInput1 = "1234+56";
        String invalidInput2 = "1234567";
        String invalidInput3 = "+1234=5";
        String invalidInput4 = "123+=67";
        // For invalidInput1
        assertFalse("Processing an equation without '=' should return false", model.processInput(invalidInput1));
        assertNotNull("Error message should not be null after processing an equation without '='", model.getErrorMessage());
        // For invalidInput2
        assertFalse("Processing an equation without an operator should return false", model.processInput(invalidInput2));
        assertNotNull("Error message should not be null after processing an equation without an operator", model.getErrorMessage());
        // For invalidInput3
        assertFalse("Processing an equation with no number before operator should return false", model.processInput(invalidInput3));
        assertNotNull("Error message should not be null after processing an equation with no number before operator", model.getErrorMessage());
        // For invalidInput4
        assertFalse("Processing an equation with operator after operator should return false", model.processInput(invalidInput4));
        assertNotNull("Error message should not be null after processing an equation with operator after operator", model.getErrorMessage());
        assertEquals("Initial remaining attempts should not change with an equation with invalid format", NumberleModel.MAX_ATTEMPTS, model.getRemainingAttempts());

        // test the math check
        String invalidInput = "1+1+2=3";
        assertFalse("Processing invalid input should return false", model.processInput(invalidInput));
        assertNotNull("Error message should not be null after processing invalid input", model.getErrorMessage());
        assertEquals("Initial remaining attempts should not change with an equation with wrong equation in math area.", NumberleModel.MAX_ATTEMPTS, model.getRemainingAttempts());

        // test the valid sample.
        String input = "1+2*3=7"; // Assuming valid input for the sake of the test
        int oldAttempts = model.getRemainingAttempts();
        assertTrue("Controller should successfully process correct input", model.processInput(input));
        assertFalse("Controller should report game not won on randomly input", model.isGameWon());
        assertEquals("Initial remaining attempts should reduce 1 after valid input", NumberleModel.MAX_ATTEMPTS - 1, model.getRemainingAttempts());

    }
}

