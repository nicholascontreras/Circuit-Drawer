package logicgates;

import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * @author Nicholas Contreras
 */

public class NamedInput extends LogicGate {

	private String name;

	private boolean secretState;
	
	public NamedInput(String name) {
		super();
		this.name = name;
	}

	@Override
	public int getInputCount() {
		return 0;
	}

	@Override
	public String getStringName() {
		return "NamedInput";
	}
	
	public String getSignalName() {
		return name;
	}

	@Override
	public String toString() {
		return super.toString() + ": " + name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NamedInput) {
			return this.name.equals(((NamedInput) obj).name);
		}
		return false;
	}

	@Override
	public String getImageName() {
		return "emptyGate";
	}
	
	public Point getOutputOffset() {
		return new Point(25, 0);
	}

	@Override
	protected boolean determineState() {
		return secretState;
	}
	
	public void forceStateChange(boolean s) {
		secretState = s;
		updateOutput();
	}
}
