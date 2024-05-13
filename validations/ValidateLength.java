package validations;

public class ValidateLength implements Validation {
    private int equation_length;

    public ValidateLength(int length) {
        equation_length = length;
    }

    @Override
    public String validate(String input) {

        if (input == null) {
            System.out.println("Empty input!");
            return "Empty input!";
        }

        if (input.length() < equation_length) {
            return "Your input are too less.";
        } else if (input.length() > equation_length) {
            return "Your input are too long.";
        }
        return null;
    }
}
