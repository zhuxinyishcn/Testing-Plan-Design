package edu.unl.cse.csce361.calculator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;

import static org.junit.Assert.*;

public class CalculatorTest {
	private InputStream in;
	private PrintStream out;
	private PrintStream err;
	private String lineSeparator;
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
		if (System.getProperty("os.name").contains("Windows")) {
			lineSeparator = "\r\n";
		} else {
			lineSeparator = "\n";
		}
		calculator = new Calculator(); // ensures each test case has a fresh Calculator object to work with
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

	@Test
	public void fakeInputOutputDemonstration() {
		setFakeIn("this is a test.");
		Scanner scanner = new Scanner(System.in);
		String line = scanner.nextLine();
		System.out.println(line);
		scanner.close();
		assertEquals("this is a test." + lineSeparator, fakeOut.toString()); // note the linefeed!
	}

	/*
	 * 1.1 Test if type is the declared constant NUMBER then token will be treated
	 * as the textual representation of an integer
	 */
	@Test
	public void testTypeInteger() {
		char[] token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		token[0] = '4';
		String message = null;
		assertEquals(message, calculator.calculate(Calculator.NUMBER, token));
	}

	/*
	 * 1.2 Test if type is the declared constant NUMBER then token will be treated
	 * as the textual representation of an double
	 */
	@Test
	public void testTypeDouble() {
		char[] token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		token[0] = '1';
		token[1] = '2';
		token[2] = '.';
		token[3] = '2';
		token[4] = '3';
		String message = null;
		assertEquals(message, calculator.calculate(Calculator.NUMBER, token));
	}

	/*
	 * 1.3 Test if type is not a number then it will result a error message
	 */
	@Test(expected = NumberFormatException.class)
	public void testNotNumber() {
		char[] token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		String notNumber = "not a number";
		for (int i = 0; i < notNumber.length(); i++) {
			token[i] = notNumber.charAt(i);
		}
		String message = null;
		assertEquals(message, calculator.calculate(Calculator.NUMBER, token));
	}

	/*
	 * 2. Test numbers exceeding this size results in an error message.
	 */
	@Test
	public void testTooLongNumbers() {
		char[] token = new char[Calculator.MAXIMUM_TOKEN_LENGTH + 1];
		// intentionally create a 20+ long number
		for (int i = 0; i < Calculator.MAXIMUM_TOKEN_LENGTH + 1; i++) {
			token[i] = token[0] = Integer.toString(i).charAt(0);
		}
		String message = new String(Arrays.copyOfRange(token, 0, 20 - 1)) + "... is too long";
		assertEquals(message, calculator.calculate(Calculator.OPERAND_TOO_LONG, token));
	}

	/*
	 * 3.1 Test if type is the character = then the function will return a tab
	 * character followed by a textual representation of the value at the top of the
	 * stack; the stack will remain unchanged.
	 */
	@Test
	public void testEqualForInteger() {
		char[] token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		token[0] = '2';
		calculator.calculate(Calculator.NUMBER, token);
		int type = '=';
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		String message = "\t2.0".toString();
		assertEquals(message, calculator.calculate(type, token));
	}

	/*
	 * 3.1 Test if type is the character = then the function will return a tab
	 * character followed by a textual representation of the value at the top of the
	 * stack; the stack will remain unchanged.
	 */
	@Test
	public void testEqualForDouble() {
		char[] token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		token[0] = '2';
		token[1] = '1';
		token[2] = '.';
		token[3] = '3';
		token[4] = '5';
		calculator.calculate(Calculator.NUMBER, token);
		int type = '=';
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		String message = "\t21.35".toString();
		assertEquals(message, calculator.calculate(type, token));
	}

	/*
	 * 4.0 Test If the value is 0.0 ,and the other is integer and type is \ then the
	 * function will return “zero divisor popped”.
	 */
	@Test
	public void testIntegerDivideZero() {
		char[] token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		token[0] = '2';
		calculator.calculate(Calculator.NUMBER, token);
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		token[0] = '0';
		calculator.calculate(Calculator.NUMBER, token);
		int type = '/';
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		String message = "zero divisor popped";
		assertEquals(message, calculator.calculate(type, token));
	}

	/*
	 * 4.1 Test If the value is 0.0,and the other is double and type is \ then the
	 * function will return “zero divisor popped”.
	 */
	@Test
	public void testDoubleDivideZero() {
		char[] token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		token[0] = '9';
		token[1] = '2';
		token[2] = '3';
		token[3] = '.';
		token[4] = '7';
		token[5] = '6';
		calculator.calculate(Calculator.NUMBER, token);
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		token[0] = '0';
		calculator.calculate(Calculator.NUMBER, token);
		int type = '/';
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		String message = "zero divisor popped";
		assertEquals(message, calculator.calculate(type, token));
	}

	/*
	 * 4.2 Test If the value is normal two integer and type is \ then the function
	 * will return the value of two number divide.
	 */
	@Test
	public void testIntegerDivideNonZero() {
		char[] token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		token[0] = '8';
		calculator.calculate(Calculator.NUMBER, token);
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		token[0] = '2';
		calculator.calculate(Calculator.NUMBER, token);
		int type = '/';
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		calculator.calculate(type, token);
		type = '=';
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		String message = "\t" + Double.toString(8 / 2);
		assertEquals(message, calculator.calculate(type, token));
	}

	/*
	 * 4.3 Test If the value is normal two integer and type is + then the function
	 * will return the value of two number sum.
	 */
	@Test
	public void testIntegerPlus() {
		char[] token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		token[0] = '8';
		calculator.calculate(Calculator.NUMBER, token);
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		token[0] = '2';
		calculator.calculate(Calculator.NUMBER, token);
		int type = '+';
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		calculator.calculate(type, token);
		type = '=';
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		String message = "\t" + Double.toString(8 + 2);
		assertEquals(message, calculator.calculate(type, token));
	}

	/*
	 * 4.4 Test If the value is normal two integer and type is - then the function
	 * will return the value of two number subtraction.
	 */
	@Test
	public void testIntegerSubtraction() {
		char[] token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		token[0] = '8';
		calculator.calculate(Calculator.NUMBER, token);
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		token[0] = '2';
		calculator.calculate(Calculator.NUMBER, token);
		int type = '-';
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		calculator.calculate(type, token);
		type = '=';
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		String message = "\t" + Double.toString(8 - 2);
		assertEquals(message, calculator.calculate(type, token));
	}

	/*
	 * 4.5 Test If the value is normal two integer and type is * then the function
	 * will return the value of two number multiplication.
	 */
	@Test
	public void testIntegerMultiplication() {
		char[] token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		token[0] = '8';
		calculator.calculate(Calculator.NUMBER, token);
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		token[0] = '2';
		calculator.calculate(Calculator.NUMBER, token);
		int type = '*';
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		calculator.calculate(type, token);
		type = '=';
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		String message = "\t" + Double.toString(8 * 2);
		assertEquals(message, calculator.calculate(type, token));
	}

	/*
	 * 4.6 Test If the value is normal two Double and type is + then the function
	 * will return the value of two number addition.
	 */
	@Test
	public void testDoublePlus() {
		char[] token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		token[0] = '8';
		token[1] = '2';
		token[2] = '.';
		token[3] = '4';
		token[4] = '5';
		calculator.calculate(Calculator.NUMBER, token);
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		token[0] = '2';
		token[1] = '2';
		token[2] = '.';
		token[3] = '0';
		token[4] = '3';
		calculator.calculate(Calculator.NUMBER, token);
		int type = '+';
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		calculator.calculate(type, token);
		type = '=';
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		String message = "\t" + Double.toString(82.45 + 22.03);
		assertEquals(message, calculator.calculate(type, token));
	}

	/*
	 * 4.7 Test If the value is normal two Double and type is - then the function
	 * will return the value of two number Subtraction.
	 */
	@Test
	public void testDoubleSubtraction() {
		char[] token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		token[0] = '8';
		token[1] = '2';
		token[2] = '.';
		token[3] = '4';
		token[4] = '5';
		calculator.calculate(Calculator.NUMBER, token);
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		token[0] = '2';
		token[1] = '2';
		token[2] = '.';
		token[3] = '0';
		token[4] = '3';
		calculator.calculate(Calculator.NUMBER, token);
		int type = '-';
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		calculator.calculate(type, token);
		type = '=';
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		String message = "\t" + Double.toString(82.45 - 22.03);
		assertEquals(message, calculator.calculate(type, token));
	}

	/*
	 * 4.8 Test If the value is normal two Double and type is * then the function
	 * will return the value of two number multiplication.
	 */
	@Test
	public void testDoubleMultiplication() {
		char[] token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		token[0] = '8';
		token[1] = '2';
		token[2] = '.';
		token[3] = '4';
		token[4] = '5';
		calculator.calculate(Calculator.NUMBER, token);
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		token[0] = '2';
		token[1] = '2';
		token[2] = '.';
		token[3] = '0';
		token[4] = '3';
		calculator.calculate(Calculator.NUMBER, token);
		int type = '*';
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		calculator.calculate(type, token);
		type = '=';
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		String message = "\t" + Double.toString(82.45 * 22.03);
		assertEquals(message, calculator.calculate(type, token));
	}

	/*
	 * 4.9 Test If the value is normal two integer and type is \ then the function
	 * will return the value of two number divide.
	 */
	@Test
	public void testDoubleDivideNonZero() {
		char[] token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		token[0] = '8';
		token[1] = '2';
		token[2] = '.';
		token[3] = '4';
		token[4] = '5';
		calculator.calculate(Calculator.NUMBER, token);
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		token[0] = '2';
		token[1] = '2';
		token[2] = '.';
		token[3] = '0';
		token[4] = '3';
		calculator.calculate(Calculator.NUMBER, token);
		int type = '/';
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		calculator.calculate(type, token);
		type = '=';
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		String message = "\t" + Double.toString(82.45 / 22.03);
		assertEquals(message, calculator.calculate(type, token));
	}

	/*
	 * 5. Test If type is the character c then the function will pop the top value
	 * off the stack and return null.
	 */
	@Test
	public void testClear() {
		char[] token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		token[0] = '8';
		calculator.calculate(Calculator.NUMBER, token);
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		token[0] = '2';
		calculator.calculate(Calculator.NUMBER, token);
		int type = 'c';
		token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		String message = null;
		assertEquals(message, calculator.calculate(type, token));
	}

	/*
	 * 6. Test If type is any other value then the function will return “unknown
	 * command” followed by type
	 */
	@Test
	public void testUnknownCommand() {
		int type = 'x';
		char[] token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		String message = "unknown command x";
		assertEquals(message, calculator.calculate(type, token));
	}

	/*
	 * 7. Test stack is empty then the program prints an error message.
	 */
	@Test
	public void testPopEmptyStack() {
		int type = '=';
		char[] token = "=".toCharArray();
		String message = "\t0.0";
		String output = "error: stack empty" + lineSeparator;
		assertEquals(message, calculator.calculate(type, token));
		assertEquals(output, fakeOut.toString());
	}

	/*
	 * 8.1 Test If the function attempts to push a integer value onto a full stack
	 * then the function will output “error: stack full” and return null.
	 */
	@Test
	public void testTooFewIntegerValues() {
		// intentionally make the stack full of numbers
		for (int i = 1; i < 101; i++) {
			char[] token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
			token[0] = Integer.toString(i).charAt(0);
			calculator.calculate(Calculator.NUMBER, token);
		}
		// try to put another number into the stack
		char[] token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		token[0] = '1';
		String message = null;
		String output = "error: stack full" + lineSeparator;
		String programOutput = calculator.calculate(Calculator.NUMBER, token);
		assertEquals(output, fakeOut.toString());
		assertEquals(message, programOutput);
	}

	/*
	 * 8.2 Test If the function attempts to push a Double value onto a full stack
	 * then the function will output “error: stack full” and return null.
	 */
	@Test
	public void testTooFewDoubleValues() {
		// intentionally make the stack full of numbers
		for (int i = 1; i < 101; i++) {
			char[] token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
			// this part is I intentionally make a double number
			token[0] = Integer.toString(i).charAt(0);
			token[1] = '.';
			token[2] = Integer.toString(i).charAt(0);
			calculator.calculate(Calculator.NUMBER, token);
		}
		// try to put another number into the stack
		char[] token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		token[0] = '1';
		token[1] = '.';
		token[2] = '9';
		String message = null;
		String output = "error: stack full" + lineSeparator;
		String programOutput = calculator.calculate(Calculator.NUMBER, token);
		assertEquals(output, fakeOut.toString());
		assertEquals(message, programOutput);
	}

	/*
	 * 8.3 Test If the function attempts to push a Double value onto a full stack
	 * then the function will output “error: stack full” and return null.
	 */
	@Test(expected = NumberFormatException.class)
	public void testTooFewCharValues() {
		// intentionally make the stack full of numbers
		for (int i = 1; i < 101; i++) {
			char[] token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
			// this part is I intentionally make a random single char
			token[0] = (char) (65 + i);
			calculator.calculate(Calculator.NUMBER, token);
		}
		// try to put another number into the stack
		char[] token = new char[Calculator.MAXIMUM_TOKEN_LENGTH];
		token[0] = 'x';
		String message = null;
		String output = "error: stack full" + lineSeparator;
		String programOutput = calculator.calculate(Calculator.NUMBER, token);
		assertEquals(output, fakeOut.toString());
		assertEquals(message, programOutput);
	}
}