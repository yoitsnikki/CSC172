/*
 * Niharika Agrawal
 * CSC 172
 * Calculator using Stacks, ArrayList, List, and Queues from scratch
 * October 10, 2024
 */

 // import programs
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.text.DecimalFormat;

public class URCalculator {

    private static DecimalFormat df = new DecimalFormat("0.00");

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Error: Please provide input and output file paths.");
            return;
        }

        String inputFile = args[0];
        String outputFile = args[1];

        try {
            Path inputPath = Paths.get(inputFile);
            URList<String> lines = new URArrayList<>(Files.readAllLines(inputPath)); // read all lines from the input file
            URList<String> results = new URArrayList<>();

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                //System.out.println("Processing line: " + line); // debugging: log the current line being processed
                try {
                    URQueue<String> tokens = tokenize(line); // tokenize the infix expression using URQueue
                    //System.out.println("Tokens: " + tokens.toString()); // debugging: log the tokens after tokenization

                    URQueue<String> postfixQueue = infixToPostfix(tokens); // convert infix to postfix using URQueue
                    //System.out.println("Postfix: " + postfixQueue.toString()); // debugging: log the postfix expression

                    double result = evaluatePostfix(postfixQueue); // evaluate the postfix expression using URStack
                    //System.out.println("Result: " + result); // debugging: log the result

                    results.add(df.format(result)); // format result to two decimal places
                } catch (Exception e) {
                    //System.out.println("Error processing line: " + line); // debugging: log the error line
                    e.printStackTrace(); // print the full exception stack trace for detailed debugging
                    results.add("Error: " + e.getMessage());
                }
            }

            // write all results to the output file
            Files.write(Paths.get(outputFile), results);

        } catch (IOException e) {
            System.out.println("Error reading/writing file: " + e.getMessage());
        }
    }

    // split the input expression into tokens, using URQueue
    private static URQueue<String> tokenize(String expression) {
        URQueue<String> tokens = new URQueue<>();
        StringBuilder token = new StringBuilder();
    
        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);
    
            // ignore whitespace
            if (Character.isWhitespace(ch)) {
                continue;
            }
    
            if (Character.isDigit(ch) || ch == '.') {
                token.append(ch); // build numeric tokens
            } 
            
            // check for multi-character operators
            else {
                if (token.length() > 0) {
                    tokens.enqueue(token.toString()); // add the completed number
                    token.setLength(0); // clear the token builder
                }

                // add operator or parentheses as separate token
                if (ch == '(' || ch == ')' || isOperator(ch)) {
                    tokens.enqueue(String.valueOf(ch));
                } 
                
                // skip the next two characters
                else if (i + 2 < expression.length() && expression.substring(i, i + 3).equals("sin")) {
                    tokens.enqueue("sin");
                    i += 2;
                } 
                
                // skip the next two characters
                else if (i + 2 < expression.length() && expression.substring(i, i + 3).equals("cos")) {
                    tokens.enqueue("cos");
                    i += 2;
                }

                // skip the next two characters
                else if (i + 2 < expression.length() && expression.substring(i, i + 3).equals("tan")) {
                    tokens.enqueue("tan");
                    i += 2;
                }
            }
        }
    
        // add last token if exists
        if (token.length() > 0) {
            tokens.enqueue(token.toString());
        }
    
        return tokens;
    }
    

    // infix to postfix conversion
    private static URQueue<String> infixToPostfix(URQueue<String> tokens) {
        URQueue<String> outputQueue = new URQueue<>();
        URStack<String> operatorStack = new URStack<>();

        while (!tokens.isEmpty()) {
            String token = tokens.dequeue();

            // add numbers directly to output
            if (isNumeric(token)) {
                outputQueue.enqueue(token);
            } 
            
            // push left parentheses onto stack
            else if (token.equals("(")) {
                operatorStack.push(token);
            } 
            
            // pop from the stack to the output until left parenthesis is found
            else if (token.equals(")")) {
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
                    outputQueue.enqueue(operatorStack.pop());
                }
                operatorStack.pop(); // pop and discard the left parenthesis
            } 
            
            // push the current operator onto the stack
            else if (isOperator(token.charAt(0))) {
                while (!operatorStack.isEmpty() && precedence(operatorStack.peek()) >= precedence(token)) {
                    outputQueue.enqueue(operatorStack.pop());
                }
                operatorStack.push(token);
            }
        }

        // pop the remaining operators from the stack
        while (!operatorStack.isEmpty()) {
            outputQueue.enqueue(operatorStack.pop());
        }

        return outputQueue;
    }

    // evaluate postfix expression
    private static double evaluatePostfix(URQueue<String> postfixQueue) {
        URStack<Double> stack = new URStack<>();
    
        while (!postfixQueue.isEmpty()) {
            String token = postfixQueue.dequeue();
            //System.out.println("Evaluating token: " + token); // debugging output
    
            // push numbers onto the stack
            if (isNumeric(token)) {
                stack.push(Double.parseDouble(token));
                //System.out.println("Pushing number onto stack: " + token); // debugging output
            } 
            
            // process logical NOT (!)
            else if (isOperator(token.charAt(0))) {
                if (token.equals("!")) {
                    if (stack.size() < 1) {
                        throw new IllegalArgumentException("Insufficient operands for unary operator");
                    }
                    double a = stack.pop();
                    stack.push(a == 0 ? 1.0 : 0.0);
                    //System.out.println("Applying NOT: " + a + " => " + (a == 0 ? 1.0 : 0.0)); // Debugging output
                } 
                
                // handle binary operators
                else {
                    if (stack.size() < 2) {
                        throw new IllegalArgumentException("Insufficient operands for operator");
                    }
    
                    double b = stack.pop(); // pop the second operand
                    double a = stack.pop(); // pop the first operand
    
                    switch (token) {
                        case "+":
                            stack.push(a + b);
                            break;
                        case "-":
                            stack.push(a - b);
                            break;
                        case "*":
                            stack.push(a * b);
                            break;
                        case "/":
                            if (b == 0) throw new ArithmeticException("Division by zero");
                            stack.push(a / b);
                            break;
                        case "%":
                            stack.push(a % b);
                            break;
                        case "^":
                            stack.push(Math.pow(a, b));
                            break;
                        case "=":
                            stack.push(a == b ? 1.0 : 0.0);
                            break;
                        case "<":
                            stack.push(a < b ? 1.0 : 0.0);
                            break;
                        case ">":
                            stack.push(a > b ? 1.0 : 0.0);
                            break;
                        case "&":
                            stack.push((a != 0 && b != 0) ? 1.0 : 0.0);
                            break;
                        case "|":
                            stack.push((a != 0 || b != 0) ? 1.0 : 0.0);
                            break;
                        case "sin":
                            stack.push(Math.sin(Math.toRadians(b)));
                            break;
                        case "cos":
                            stack.push(Math.cos(Math.toRadians(b)));
                            break;
                        case "tan":
                            stack.push(Math.tan(Math.toRadians(b)));
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown operator: " + token);
                    }
                }
            } else {
                throw new IllegalArgumentException("Invalid token: " + token);
            }
        }
    
        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid postfix expression.");
        }
    
        return stack.pop();
    }

    // check if something is numeric
    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // check if something is an operator
    private static boolean isOperator(char ch) {
        return "+-*/^=<>|&!%".indexOf(ch) != -1;
    }

    // order of operations
    private static int precedence(String operator) {
        switch (operator) {
            case "=":
            case "<":
            case ">":
            case "&":
            case "|":
                return 1;
            case "+":
            case "-":
                return 2;
            case "*":
            case "/":
            case "%":
                return 3;
            case "^":
                return 4;
            case "!":
                return 5;
            default:
                return 0;
        }
    }
}