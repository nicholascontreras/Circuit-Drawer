package logicgates;

import java.awt.Point;
import java.awt.image.BufferedImage;

/**
* @author Nicholas Contreras
*/

public class FinalOutput extends LogicGate {

	@Override
	public int getInputCount() {
		return 1;
	}

	@Override
	public String getStringName() {
		return "FinalOutput";
	}

	@Override
	public String getImageName() {
		return "emptyGate";
	}

	public Point[] getInputOffsets() {
		return new Point[] { new Point(-25, 0)};
	}

	@Override
	protected boolean determineState() {
		return getInputs()[0].isActive();
	}
}
