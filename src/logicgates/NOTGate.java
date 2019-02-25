package logicgates;

import java.awt.Point;

/**
 * @author Nicholas Contreras
 */

public class NOTGate extends LogicGate {

	@Override
	public int getInputCount() {
		return 1;
	}

	@Override
	public String getStringName() {
		return "NOT";
	}

	@Override
	public String getImageName() {
		return "notGate";
	}

	@Override
	protected boolean determineState() {
		return !getInputs()[0].isActive();
	}
	
	public Point[] getInputOffsets() {
		return new Point[] { new Point(-45, 0)};
	}
}
