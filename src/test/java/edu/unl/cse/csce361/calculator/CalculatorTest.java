package edu.unl.cse.csce361.calculator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class CalculatorTest {
    private InputStream in;
    private PrintStream out;
    private PrintStream err;

    private Calculator calculator;
    private ByteArrayOutputStream fakeOut;

    private void setFakeIn(String input) {
        if (input == null) {
            input = "";
        }
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }


    @Before
    public void setUp() {
        calculator = new Calculator();      // ensures each test case has a fresh Calculator object to work with
        in = System.in;
        out = System.out;
        err = System.err;
        fakeOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(fakeOut));
    }

    @After
    public void tearDown() {
        System.setIn(in);
        System.setOut(out);
        System.setErr(err);
    }

/*
    @Test
    public void fakeInputOutputDemonstration() {
        setFakeIn("this is a test.");
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        System.out.println(line);
        assertEquals("this is a test.\n", fakeOut.toString());  // note the linefeed!
    }
*/

    @Test
    public void testPopEmptyStack() {
        int type = '=';
        char[] token = "=".toCharArray();
        String message = "\t0.0";
        String output = "error: stack empty\n";
        assertEquals(message, calculator.calculate(type, token));
        assertEquals(output, fakeOut.toString());
    }
}