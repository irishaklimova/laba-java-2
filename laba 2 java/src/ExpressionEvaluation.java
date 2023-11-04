import java.util.*;

public class ExpressionEvaluation {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

// Ask user to enter the expression
        System.out.print("Enter an expression: ");
        String expression = scanner.nextLine();

// Remove whitespace from the expression
        expression = expression.replaceAll("\\s", "");

        try {
// Evaluate the expression
            double result = evaluateExpression(expression);

// Print the result
            System.out.println("Result: " + result);
        } catch (Exception e) {
// Print error message if there is an error
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static double evaluateExpression(String expression) {
// Check if expression is empty
        if (expression.isEmpty()) {
            throw new IllegalArgumentException("Expression is empty");
        }

// Create a list to store the tokens of the expression
        List<String> tokens = new ArrayList<>();

// Variables to store the current token and the last character
        String currentToken = "";
        char lastChar = ' ';

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

// Check if character is a digit or a decimal point
            if (Character.isDigit(c) || c == '.') {
// Check if current token is empty or last character is digit or decimal point
                if (currentToken.isEmpty() || Character.isDigit(lastChar) || lastChar == '.') {
// Append the character to the current token
                    currentToken += c;
                } else {
// Add the current token to the list
                    tokens.add(currentToken);

// Start a new token with the current character
                    currentToken = String.valueOf(c);
                }
            } else if (isOperator(c) || c == '(' || c == ')') {
// Check if current token is not empty
                if (!currentToken.isEmpty()) {
// Add the current token to the list
                    tokens.add(currentToken);

// Reset the current token
                    currentToken = "";
                }

// Add the operator or parenthesis to the list as a separate token
                tokens.add(String.valueOf(c));
            } else {
                throw new IllegalArgumentException("Invalid character: " + c);
            }

// Update the last character
            lastChar = c;
        }

// Check if there is still a token left
        if (!currentToken.isEmpty()) {
// Add the current token to the list
            tokens.add(currentToken);
        }

// Create a stack to store the operands and operators
        Stack<Double> operands = new Stack<>();
        Stack<String> operators = new Stack<>();

        for (String token : tokens) {
// Check if token is a number
            if (isNumber(token)) {
// Push the number onto the stack of operands
                operands.push(Double.parseDouble(token));
            } else if (isOperator(token.charAt(0))) {
// Check if the operator stack is empty or the top operator has lower precedence
                while (!operators.isEmpty() && hasHigherPrecedence(operators.peek().charAt(0), token.charAt(0))) {
// Apply the top operator to the top two operands
                    applyOperator(operands, operators);
                }

// Push the current operator onto the stack of operators
                operators.push(token);
            } else if (token.equals("(")) {
// Push the left parenthesis onto the stack of operators
                operators.push(token);
            } else if (token.equals(")")) {
// Apply operators until left parenthesis is encountered
                while (!operators.isEmpty() && !operators.peek().equals("(")) {
                    applyOperator(operands, operators);
                }

// Remove the left parenthesis from the stack of operators
                operators.pop();
            } else {
                throw new IllegalArgumentException("Invalid token: " + token);
            }
        }

// Apply remaining operators
        while (!operators.isEmpty()) {
            applyOperator(operands, operators);
        }

// Check if there is only one operand left
        if (operands.size() != 1) {
            throw new IllegalArgumentException("Invalid expression");
        }

// Return the result
        return operands.pop();
    }

    public static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    public static boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public
    static boolean hasHigherPrecedence(char op1, char op2) {
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return true;
        } else {
            return false;
        }
    }

    public static void applyOperator(Stack<Double> operands, Stack<String> operators) {
        double operand2 = operands.pop();
        double operand1 = operands.pop();
        char operator = operators.pop().charAt(0);

        double result = 0;

        switch (operator) {
            case '+':
                result = operand1 + operand2;
                break;
            case '-':
                result = operand1 - operand2;
                break;
            case '*':
                result = operand1 * operand2;
                break;
            case '/':
                if (operand2 == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                result = operand1 / operand2;
                break;
        }

        operands.push(result);
    }
}