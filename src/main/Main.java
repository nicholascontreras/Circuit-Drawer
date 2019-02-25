package main;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import draw.CircuitDrawer;
import draw.DisplayPanel;
import logicgates.LogicGate;
import stringparser.StringParser;

/**
 * @author Nicholas Contreras
 */

public class Main {

	public static void main(String[] args) {

		// String s = JOptionPane.showInputDialog(null, "Enter boolean logic
		// statement:");

		LogicGate output = StringParser.parseString("((b | ((c | (! (g | h))) & (! d))) & (e | (! (f & a))))");

		// LogicGate output = StringParser.parseString("(a & (! b))");

		LogicGate.printCircut(output);

		new DisplayPanel(output);
		// }
	}

}
