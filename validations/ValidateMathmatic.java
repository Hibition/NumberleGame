package validations;

import java.util.Stack;

public class ValidateMathmatic implements Validation {


    @Override
    public String validate(String expression) {
        String[] expressions = expression.split("=");
        int result1 = doCuculation(expressions[0]);
        int result2 = doCuculation(expressions[1]);

        if (result1 != result2) {
            return "It's not a equation!";
        }

        return null;
    }


    private static Stack<Integer> reverseStack(Stack<Integer> stack) {
        if (stack.isEmpty()) {
            return stack;
        }
        Stack<Integer> tempStack = new Stack<>();
        while (!stack.isEmpty()) {
            tempStack.push(stack.pop());
        }
        return tempStack;
    }

    private static Stack<Character> reverseStackString(Stack<Character> stack) {
        if (stack.isEmpty()) {
            return stack;
        }
        Stack<Character> tempStack = new Stack<>();
        while (!stack.isEmpty()) {
            tempStack.push(stack.pop());
        }
        return tempStack;
    }


    private static int doCuculation(String expression) {
        Stack<Integer> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (Character.isDigit(c)) {
                StringBuilder numStr = new StringBuilder();
                numStr.append(c);
                while (i + 1 < expression.length() && Character.isDigit(expression.charAt(i + 1))) {
                    numStr.append(expression.charAt(i + 1));
                    i++;
                }
                numbers.push(Integer.parseInt(numStr.toString()));
            } else if (c == '+' || c == '-') {
                while (!operators.isEmpty() && (operators.peek() == '*' || operators.peek() == '/')) {
                    char op = operators.pop();
                    int num2 = numbers.pop();
                    int num1 = numbers.pop();
                    numbers.push(applyOperation(num1, num2, op));
                }
                operators.push(c);
            } else if (c == '*' || c == '/') {
                operators.push(c);
            }
        }

        if (!operators.isEmpty() && (operators.peek() == '*' || operators.peek() == '/')) {
            char op = operators.pop();
            int num2 = numbers.pop();
            int num1 = numbers.pop();
            numbers.push(applyOperation(num1, num2, op));
        }

        numbers = reverseStack(numbers);
        operators = reverseStackString(operators);

        while (!operators.isEmpty()) {
            char op = operators.pop();
            int num1 = numbers.pop();
            int num2 = numbers.pop();
            numbers.push(applyOperation(num1, num2, op));
        }

        int result = numbers.pop();
//        System.out.println(result);

        return result;
    }

    private static int applyOperation(int num1, int num2, char op) {
        switch (op) {
            case '+':
                return num1 + num2;
            case '-':
                return num1 - num2;
            case '*':
                return num1 * num2;
            case '/':
                return num1 / num2;
            default:
                return 0;
        }
    }
}
