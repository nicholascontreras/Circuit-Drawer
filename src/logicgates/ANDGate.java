package logicgates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
* @author Nicholas Contreras
*/

public class ANDGate extends LogicGate {

	@Override
	public int getInputCount() {
		return 2;
	}

	@Override
	public String getStringName() {
		return "AND";
	}

	@Override
	public String getImageName() {
		return "andGate";
	}

	@Override
	protected boolean determineState() {
		return getInputs()[0].isActive() && getInputs()[1].isActive();
	}

}
