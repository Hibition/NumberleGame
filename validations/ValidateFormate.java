package validations;

public class ValidateFormate implements Validation {
    private boolean preCharacter; // if preChracter is 1 means it's number otherwise is operator.
    private int equation_length;

    public ValidateFormate(int length) {
        equation_length = length;
    }

    private boolean charDistinguish(String c) {
        assert c.length() == 1;
        if (c.matches("[0-9]")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String validate(String input) {
        if (!input.contains("=")) return "Lack of '='";

        preCharacter = charDistinguish(String.valueOf(input.charAt(0)));
        if (!preCharacter) {
            return "The First character must be a number!";
        }
        for (int i = 1; i < equation_length; i++) {
            boolean currentCharacter = charDistinguish(String.valueOf(input.charAt(i)));
            if (!preCharacter && !currentCharacter) {
                return "The place after Operator can not be Operator!";
            }
            preCharacter = currentCharacter;
        }
        if (!preCharacter) {
            return "The place after Operator can not be Operator!";
        }
        return null;
    }
}
