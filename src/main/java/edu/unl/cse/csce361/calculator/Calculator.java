package edu.unl.cse.csce361.calculator;

import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.Arrays;

public class Calculator {
    public static final int MAXIMUM_STACK_DEPTH = 100;
    public static final int MAXIMUM_TOKEN_LENGTH = 20;
    public static final char NUMBER = '0';
    public static final char DONE = 'q';
    public static final char OPERAND_TOO_LONG = '9';

    private static PushbackInputStream stdin = new PushbackInputStream(System.in);

    private int stackPointer;
    private double[] stack;

    public Calculator() {
        stackPointer = 0;
        stack = new double[MAXIMUM_STACK_DEPTH];
    }

    public static void main(String[] args) throws IOException {
        new Calculator().run();
    }

    void run() throws IOException {
        int type;
        char[] token = new char[MAXIMUM_TOKEN_LENGTH];
        String message;

        type = getToken(token, MAXIMUM_TOKEN_LENGTH);
        while (type != DONE) {
            message = calculate(type, token);
            if (message != null) {
                System.out.println(message);
            }
            type = getToken(token, MAXIMUM_TOKEN_LENGTH);
        }
    }

    String calculate(int type, char[] token) {
        double number1;
        double number2;
        String message;
        switch (type) {
            case NUMBER:
                number1 = Double.parseDouble(new String(token));
                push(number1);
                message = null;
                break;
            case '+':
                number1 = pop();
                number2 = pop();
                push(number1 + number2);
                message = null;
                break;
            case '*':
                number1 = pop();
                number2 = pop();
                push(number1 * number2);
                message = null;
                break;
            case '-':
                number1 = pop();
                number2 = pop();
                push(number2 - number1);
                message = null;
                break;
            case '/':
                number1 = pop();
                if (number1 != 0.0) {
                    number2 = pop();
                    push(number2 / number1);
                    message = null;
                } else {
                    message = "zero divisor popped";
                }
                break;
            case '=':
                number1 = pop();
                message = "\t" + number1;
                push(number1);
                break;
            case 'c':
                clear();
                message = null;
                break;
            case OPERAND_TOO_LONG:
                message = new String(Arrays.copyOfRange(token, 0, MAXIMUM_TOKEN_LENGTH - 1)) + "... is too long";
                break;
            default:
                message = "unknown command " + (char)type;
                break;
        }
        return message;
    }

    private void push(double number) {
        if (stackPointer < MAXIMUM_STACK_DEPTH) {
            stack[stackPointer++] = number;
        } else {
            System.out.println("error: stack full");
            clear();
        }
    }

    private double pop() {
        double number;

        if (stackPointer > 0) {
            number = stack[--stackPointer];
        } else {
            System.out.println("error: stack empty");
            clear();
            number = 0;
        }
        return number;
    }

    private void clear() {
        stackPointer = 0;
    }

    @SuppressWarnings("SameParameterValue")
    private int getToken(char[] token, int limit) throws IOException {
        int index, c, tokenType;

        c = stdin.read();
        while (c == ' ' || c == '\t' || c == '\n') {
            c = stdin.read();
        }

        if (c != '.' && (c < '0' || c > '9')) {
            tokenType = c;
        } else {
            token[0] = (char)c;

            c = stdin.read();
            index = 1;
            while (c >= '0' && c <= '9') {
                if (index < limit) {
                    token[index] = (char)c;
                }
                c = stdin.read();
                index++;
            }
            if (c == '.') {
                if (index < limit) {
                    token[index] = (char)c;
                }
                index++;
                c = stdin.read();
                while (c >= '0' && c <= '9') {
                    if (index < limit) {
                        token[index] = (char)c;
                    }
                    index++;
                    c = stdin.read();
                }
            }

            if (index < limit) {
                stdin.unread(c);
                while(index < limit) {          // to mimic the C-like behavior, we need to pump NULs into the string
                    token[index] = '\0';        // otherwise, Double.parseDouble(new String(token)) will crash if
                    index++;                    // if there are non-NUL characters after the first NUL
                }
                tokenType = NUMBER;
            } else {
                while (c != '\n' && c != DONE) {
                    c = stdin.read();
                }
                token[limit - 1] = '\0';      // leave this here, intentionally, as a vestigial of C strings
                tokenType = OPERAND_TOO_LONG;
            }
        }
        return tokenType;
    }
}
