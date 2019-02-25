package stringparser;

import logicgates.ANDGate;
import logicgates.Connection;
import logicgates.FinalOutput;
import logicgates.LogicGate;
import logicgates.NOTGate;
import logicgates.NamedInput;
import logicgates.ORGate;

/**
 * @author Nicholas Contreras
 */

public class StringParser {

	public static LogicGate parseString(String expression) {
		FinalOutput fo = new FinalOutput();
		Connection c = new Connection();
		c.setConnectedInput(fo);
		fo.setInput(0, parseStringRecursive(c, expression));
		return fo;
	}

	private static Connection parseStringRecursive(Connection toParent, String expression) {

		LogicGate newGate = null;

		if (isLeaf(expression)) {
			newGate = new NamedInput(expression.trim());
		} else {
			String[] splitStatement = splitStatement(expression);

			switch (splitStatement[1]) {
			case "AND":
				newGate = new ANDGate();
				break;
			case "OR":
				newGate = new ORGate();
				break;
			case "NOT":
				newGate = new NOTGate();
				break;
			}

			if (newGate.getInputCount() == 2) {
				Connection left = new Connection();
				left.setConnectedInput(newGate);
				left.setInputID(1);
				newGate.setInput(1, parseStringRecursive(left, splitStatement[0]));
			}

			Connection right = new Connection();
			right.setConnectedInput(newGate);
			right.setInputID(0);
			newGate.setInput(0, parseStringRecursive(right, splitStatement[2]));

		}
		newGate.setOutput(toParent);
		toParent.setConnectedOutput(newGate);

		return toParent;
	}

	private static boolean isLeaf(String statement) {
		return !statement.contains("(");
	}

	// (a | (b & c))

	private static String[] splitStatement(String statement) {

		System.out.println("split: " + statement);

		statement = statement.trim();

		if (statement.charAt(0) != '(') {
			throw new RuntimeException("Expected to consume a open-paren first");
		}

		String[] splitString = new String[3];

		int curIndex = 1;
		int parenLayer = 1;

		while (true) {
			char curChar = statement.charAt(curIndex);

			System.out.println("checking: " + curChar);

			if (curChar == ' ') {
			} else if (curChar == '(') {
				parenLayer++;
			} else if (curChar == ')') {
				parenLayer--;
			} else if (Character.isLetter(curChar)) {
			} else {
				if (parenLayer == 1) {

					if (curChar == '&') {
						splitString[1] = "AND";
					} else if (curChar == '|') {
						splitString[1] = "OR";
					} else if (curChar == '!') {
						splitString[1] = "NOT";
					} else {
						throw new RuntimeException("Didn't reconize operation: " + curChar);
					}

					splitString[0] = statement.substring(1, curIndex).trim();
					splitString[2] = statement.substring(curIndex + 1, statement.length() - 1).trim();

					return splitString;
				}
			}

			// (a & (! b))

			curIndex++;
		}
	}
}
