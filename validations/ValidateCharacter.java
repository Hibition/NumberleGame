package validations;

public class ValidateCharacter implements Validation {

    @Override
    public String validate(String input) {
        if (!input.matches("[0-9+\\-*/=]+")) {
            return "Your input only can be number or operator.";
        }
        return null;
    }
}
