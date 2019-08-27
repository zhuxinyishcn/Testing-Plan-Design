package edu.unl.cse.csce361.calculator;

import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.Arrays;

public class Calculator {
    private static final int MAXIMUM_STACK_DEPTH = 100;
    private static final int MAXIMUM_OPERATOR_LENGTH = 20;
    private static final char NUMBER = '0';
    private static final char DONE = 'q';
    private static final char OPERAND_TOO_LONG = '9';

    private static PushbackInputStream stdin = new PushbackInputStream(System.in);

    private int stackPointer;
    private double[] stack;

    private Calculator() {
        stackPointer = 0;
        stack = new double[MAXIMUM_STACK_DEPTH];
    }

    public static void main(String[] args) throws IOException {
        new Calculator().run();
    }

    private void run() throws IOException {
        int type;
        char[] s = new char[MAXIMUM_OPERATOR_LENGTH];
        double number1, number2;

        type = token(s, MAXIMUM_OPERATOR_LENGTH);
        while (type != DONE) {
            switch (type) {
                case NUMBER:
                    number1 = Double.parseDouble(new String(s));
                    push(number1);
                    break;
                case '+':
                    number1 = pop();
                    number2 = pop();
                    push(number1 + number2);
                    break;
                case '*':
                    number1 = pop();
                    number2 = pop();
                    push(number1 * number2);
                    break;
                case '-':
                    number1 = pop();
                    number2 = pop();
                    push(number2 - number1);
                    break;
                case '/':
                    number1 = pop();
                    if (number1 != 0.0) {
                        number2 = pop();
                        push(number2 / number1);
                    } else {
                        System.out.println("zero divisor popped");
                    }
                    break;
                case '=':
                    number1 = pop();
                    System.out.println("\t" + number1);
                    push(number1);
                    break;
                case 'c':
                    clear();
                    break;
                case OPERAND_TOO_LONG:
                    System.out.println(new String(Arrays.copyOfRange(s, 0, MAXIMUM_OPERATOR_LENGTH - 1)) + "... is too long");
                    break;
                default:
                    System.out.println("unknown command " + (char)type);
                    break;

            }

            type = token(s, MAXIMUM_OPERATOR_LENGTH);
        }
    }

    private void push(double operand) {
        if (stackPointer < MAXIMUM_STACK_DEPTH) {
            stack[stackPointer++] = operand;
        } else {
            System.out.println("error: stack full");
            clear();
        }
    }

    private double pop() {
        double operand;

        if (stackPointer > 0) {
            operand = stack[--stackPointer];
        } else {
            System.out.println("error: stack empty");
            clear();
            operand = 0;
        }
        return operand;
    }

    private void clear() {
        stackPointer = 0;
    }

    @SuppressWarnings("SameParameterValue")
    private int token(char[] string, int limit) throws IOException {
        int index, c, returnValue;

        c = stdin.read();
        while (c == ' ' || c == '\t' || c == '\n') {
            c = stdin.read();
        }

        if (c != '.' && (c < '0' || c > '9')) {
            returnValue = c;
        } else {
            string[0] = (char)c;

            c = stdin.read();
            index = 1;
            while (c >= '0' && c <= '9') {
                if (index < limit) {
                    string[index] = (char)c;
                }
                c = stdin.read();
                index++;
            }

            if (c == '.') {
                if (index < limit) {
                    string[index] = (char)c;
                }
                index++;
                c = stdin.read();
                while (c >= '0' && c <= '9') {
                    if (index < limit) {
                        string[index] = (char)c;
                    }
                    index++;
                    c = stdin.read();
                }
            }

            if (index < limit) {
                stdin.unread(c);
                while(index < limit) {          // to mimic the C-like behavior, we need to pump NULs into the string
                    string[index] = '\0';       // otherwise, Double.parseDouble(new String(operand)) will crash if
                    index++;                    // if there are non-NUL characters after the first NUL
                }
                returnValue = NUMBER;
            } else {
                while (c != '\n' && c != DONE) {
                    c = stdin.read();
                }
                string[limit - 1] = '\0';       // leave this here, intentionally, as a vestigial of C strings
                returnValue = OPERAND_TOO_LONG;
            }
        }

        return returnValue;
    }
}
