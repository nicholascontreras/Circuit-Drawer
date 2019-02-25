package logicgates;

import java.awt.Point;

/**
 * @author Nicholas Contreras
 */

public class Connection {

	private static int NEXT_UUID = 1;

	private int uuid;

	private LogicGate connectedOutput, connectedInput;

	private Point connectedOutputPos, connectedInputPos;

	private int inputID;

	private boolean isActive;

	public Connection() {
		uuid = NEXT_UUID++;
	}

	public int getUUID() {
		return uuid;
	}

	public void setActive(boolean b) {
		isActive = b;
		connectedInput.updateOutput();
	}

	public boolean isActive() {
		return isActive;
	}

	public void setInputID(int id) {
		inputID = id;
	}

	public int getInputID() {
		return inputID;
	}

	public void setConnectedOutputPos(Point pos) {
		connectedOutputPos = pos;
	}

	public void setConnectedInputPos(Point pos) {
		connectedInputPos = pos;
	}

	public Point getConnectedOutputPos() {
		return connectedOutputPos;
	}

	public Point getConnectedInputPos() {
		return connectedInputPos;
	}

	public void setConnectedOutput(LogicGate lg) {
		connectedOutput = lg;
	}

	public void setConnectedInput(LogicGate lg) {
		connectedInput = lg;
	}

	public LogicGate getConnectedOutput() {
		return connectedOutput;
	}

	public LogicGate getConnectedInput() {
		return connectedInput;
	}

}
