package logicgates;

import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashSet;

import draw.ImageManager;

/**
 * @author Nicholas Contreras
 */

public abstract class LogicGate {

	private static int NEXT_UUID = 1;

	private int uuid;

	private Connection[] inputs;
	private Connection output;

	private boolean isActive;

	public LogicGate() {

		uuid = NEXT_UUID;
		NEXT_UUID++;

		inputs = new Connection[getInputCount()];
	}

	abstract public int getInputCount();

	abstract public String getStringName();

	abstract public String getImageName();

	abstract protected boolean determineState();

	public void updateOutput() {
		isActive = determineState();
		if (output != null) {
			output.setActive(isActive);
		}
	}

	public boolean isActive() {
		return isActive;
	}

	public Point getOutputOffset() {
		return new Point(45, 0);
	}

	public Point[] getInputOffsets() {
		return new Point[] { new Point(-45, 10), new Point(-45, -10) };
	}

	public BufferedImage getImage() {
		return ImageManager.getGateImage(getImageName() + (isActive ? "On" : "Off") + ".png");
	}

	public void setInput(int index, Connection c) {
		inputs[index] = c;
	}

	public void setOutput(Connection c) {
		output = c;
	}

	public Connection[] getInputs() {
		return inputs;
	}

	public Connection getOutput() {
		return output;
	}

	public int getUUID() {
		return uuid;
	}

	@Override
	public String toString() {
		String s = "LogicGate: UUID: " + uuid + " Type: " + getStringName();
		return s;
	}

	public static void printCircut(LogicGate output) {
		printCircut(0, output);
	}

	private static void printCircut(int indent, LogicGate node) {
		for (int i = 0; i < indent; ++i) {
			System.out.print(" ");
		}

		if (!(node instanceof NamedInput)) {
			System.out.println("(" + node);

			for (Connection c : node.inputs) {
				printCircut(indent + 4, c.getConnectedOutput());
			}

			// we have a new line so print the indent again
			for (int i = 0; i < indent; ++i) {
				System.out.print(" ");
			}
			System.out.println(")");
		} else if (node instanceof NamedInput) {
			System.out.println(node);
		} else { // empty/non existing node
			System.out.println("()");
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LogicGate) {
			return this.uuid == ((LogicGate) obj).uuid;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return uuid;
	}
}
