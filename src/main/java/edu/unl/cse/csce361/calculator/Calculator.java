package edu.unl.cse.csce361.calculator;

import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.Arrays;

public class Calculator {
    private static final int MAXVAL = 100;
    private static final int MAXOP = 20;
    private static final char NUMBER = '0';
    private static final char DONE = 'q';
    private static final char TOOBIG = '9';

    private static PushbackInputStream stdin = new PushbackInputStream(System.in);

    private static int sp = 0;
    private static double[] val = new double[MAXVAL];

    public static void main(String[] args) throws IOException {
        new Calculator();                     // to get EclEmma to see 100% statement coverage in Summer'19 whitebox testing
        int type;
        char[] s = new char[MAXOP];
        double num1, num2;

        type = getop(s, MAXOP);
        while (type != DONE) {
            switch (type) {

                case NUMBER:
                    num1 = Double.parseDouble(new String(s));
                    push(num1);
                    break;
                case '+':
                    num1 = pop();
                    num2 = pop();
                    push(num1 + num2);
                    break;

                case '*':
                    num1 = pop();
                    num2 = pop();
                    push(num1 * num2);
                    break;

                case '-':
                    num1 = pop();
                    num2 = pop();
                    push(num2 - num1);
                    break;

                case '/':
                    num1 = pop();
                    if (num1 != 0.0) {
                        num2 = pop();
                        push(num2 / num1);
                    } else {
                        System.out.println("zero divisor popped");
                    }
                    break;

                case '=':
                    num1 = pop();
                    System.out.println("\t" + num1);
                    push(num1);
                    break;

                case 'c':
                    clear();
                    break;

                case TOOBIG:
//                    printf("%.20s ... is too long\n",s);
                    System.out.println(new String(Arrays.copyOfRange(s, 0, MAXOP - 1)) + "... is too long");
                    break;

                default:
                    System.out.println("unknown command " + (char) type);
                    break;

            }

            type = getop(s, MAXOP);
        }
    }

    private static void push(double f) {
        if (sp < MAXVAL) {
            val[sp++] = f;
        } else {
            System.out.println("error: stack full");
            clear();
        }
    }

    private static double pop() {
        double retval;

        if (sp > 0) {
            retval = val[--sp];
        } else {
            System.out.println("error: stack empty");
            clear();
            retval = 0;
        }
        return (retval);
    }

    private static void clear() {
        sp = 0;
    }

    @SuppressWarnings("SameParameterValue")
    private static int getop(char[] s, int lim) throws IOException {
        int i, c, retval;

        c = stdin.read();
        while (c == ' ' || c == '\t' || c == '\n') {
            c = stdin.read();
        }

        if (c != '.' && (c < '0' || c > '9')) {
            retval = c;
        } else {
            s[0] = (char) c;

            c = stdin.read();
            i = 1;
            while (c >= '0' && c <= '9') {
                if (i < lim) {
                    s[i] = (char) c;
                }
                c = stdin.read();
                i++;
            }

            if (c == '.') {
                if (i < lim) {
                    s[i] = (char) c;
                }
                i++;
                c = stdin.read();
                while (c >= '0' && c <= '9') {
                    if (i < lim) {
                        s[i] = (char) c;
                    }
                    i++;
                    c = stdin.read();
                }
            }

            if (i < lim) {
                stdin.unread(c);
                s[i] = '\0';
                retval = NUMBER;
            } else {
                while (c != '\n' && c != DONE) {
                    c = stdin.read();
                }
                s[lim - 1] = '\0';            // leave this here, intentionally, as a vestigial of C strings
                retval = TOOBIG;
            }
        }

        return retval;
    }
}
