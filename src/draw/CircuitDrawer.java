package draw;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import logicgates.Connection;
import logicgates.LogicGate;
import logicgates.NamedInput;
import sun.rmi.runtime.Log;

/**
 * @author Nicholas Contreras
 */

public class CircuitDrawer {

	public static HashMap<NamedInput, Point> inputPositions = new HashMap<NamedInput, Point>();

	public static HashMap<LogicGate, Point> gateLocations = new HashMap<LogicGate, Point>();

	private static boolean inputPosSaved;

	private static Rectangle savedSize;
	public static void calculateLocations(LogicGate finalOutput) {
		
		finalOutput.getInputs()[0].setConnectedInputPos(new Point(-100, 100));
	
//		arrangeGates(finalOutput, -100, 100);
	}
	
	private static void arrangeGates(LogicGate finalOutput) {
		HashSet<LogicGate> thisRound = new HashSet<LogicGate>();
		HashSet<LogicGate> nextRound = new HashSet<LogicGate>();

		thisRound.add(finalOutput);

		int drawX = -100;

		int stagger = 0;

		while (!thisRound.isEmpty()) {
			int drawY = stagger % 100 + 100;
			for (LogicGate curGate : thisRound) {
				
				curGate.getOutput().setConnectedOutputPos(new Point(drawX, drawY));

				for (Connection next : curGate.getInputs()) {
					if (next != null) {
						next.setConnectedInputPos(new Point(drawX, drawY));
						nextRound.add(next.getConnectedOutput());
					}
				}
				drawY += 173;
			}

			thisRound.clear();
			thisRound.addAll(nextRound);
			nextRound.clear();

			drawX -= 173;

			stagger += 37;
		}
	}

	public static BufferedImage renderCircuit(LogicGate finalOutput) {

		BufferedImage image = new BufferedImage(10000, 2000, BufferedImage.TYPE_INT_RGB);

		Graphics2D g2d = image.createGraphics();

		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, image.getWidth(), image.getHeight());

		drawGates(image, finalOutput);
		drawConnections(image, finalOutput.getInputs()[0]);

		image = trimImage(image);

		return image;
	}

	private static void drawGates(BufferedImage image, LogicGate finalOutput) {

		Graphics2D g2d = image.createGraphics();

		HashSet<LogicGate> thisRound = new HashSet<LogicGate>();
		HashSet<LogicGate> nextRound = new HashSet<LogicGate>();

		thisRound.add(finalOutput);

		int drawX = image.getWidth() - 200;

		int stagger = 0;

		while (!thisRound.isEmpty()) {
			int drawY = stagger % 100 + 100;
			for (LogicGate curGate : thisRound) {

				BufferedImage curImg = curGate.getImage();
				g2d.drawImage(curImg, drawX - curImg.getWidth() / 2, drawY - curImg.getHeight() / 2, null);

				gateLocations.put(curGate, new Point(drawX, drawY));

				if (curGate instanceof NamedInput) {

					if (!inputPosSaved) {
						inputPositions.put((NamedInput) curGate, new Point(drawX, drawY));
					}
					g2d.setColor(Color.BLACK);
					g2d.setFont(new Font("SansSerif", Font.BOLD, 24));

					String name = ((NamedInput) curGate).getSignalName();
					int textWidth = g2d.getFontMetrics().stringWidth(name);
					g2d.drawString(name, drawX - textWidth / 2, drawY);
				}

				if (curGate.getOutput() != null) {
					curGate.getOutput().setConnectedOutputPos(new Point(drawX, drawY));
				}

				for (Connection next : curGate.getInputs()) {
					if (next != null) {
						next.setConnectedInputPos(new Point(drawX, drawY));
						nextRound.add(next.getConnectedOutput());
					}
				}
				drawY += 173;
			}

			thisRound.clear();
			thisRound.addAll(nextRound);
			nextRound.clear();

			drawX -= 173;

			stagger += 37;
		}
		inputPosSaved = true;
	}

	private static void drawConnections(BufferedImage image, Connection finalConnection) {

		Graphics2D g2d = image.createGraphics();

		HashSet<Connection> thisRound = new HashSet<Connection>();
		HashSet<Connection> nextRound = new HashSet<Connection>();

		thisRound.add(finalConnection);

		while (!thisRound.isEmpty()) {
			for (Connection curConnection : thisRound) {

				g2d.setColor(curConnection.isActive() ? Color.RED : Color.BLACK);
				g2d.setStroke(new BasicStroke(2));

				int outputX = curConnection.getConnectedOutputPos().x
						+ curConnection.getConnectedOutput().getOutputOffset().x;
				int outputY = curConnection.getConnectedOutputPos().y
						+ curConnection.getConnectedOutput().getOutputOffset().y;

				int inputX = curConnection.getConnectedInputPos().x
						+ curConnection.getConnectedInput().getInputOffsets()[curConnection.getInputID()].x;
				int inputY = curConnection.getConnectedInputPos().y
						+ curConnection.getConnectedInput().getInputOffsets()[curConnection.getInputID()].y;

				double totalCount = curConnection.getConnectedInput().getInputCount() + 1;
				int myIndex = curConnection.getInputID() + 1;
				int myUUID = curConnection.getUUID();

				int midPointX = (outputX) + ((myUUID * 13) % (inputX - outputX));

				g2d.drawLine(outputX, outputY, midPointX, outputY);
				g2d.drawLine(midPointX, outputY, midPointX, inputY);
				g2d.drawLine(midPointX, inputY, inputX, inputY);

				for (Connection c : curConnection.getConnectedOutput().getInputs()) {
					nextRound.add(c);
				}
			}

			thisRound.clear();
			thisRound.addAll(nextRound);
			nextRound.clear();
		}
	}

	private static BufferedImage trimImage(BufferedImage image) {

		if (savedSize != null) {
			return image.getSubimage(savedSize.x, savedSize.y, savedSize.width, savedSize.height);
		}

		Rectangle boundingBox = new Rectangle(0, 0, image.getWidth(), image.getHeight());

		for (int x = 0; x < image.getWidth(); x++) {
			if (isColBlank(image, x)) {
				boundingBox.x++;
				boundingBox.width--;
			} else {
				break;
			}
		}

		for (int x = image.getWidth() - 1; x >= 0; x--) {
			if (isColBlank(image, x)) {
				boundingBox.width--;
			} else {
				break;
			}
		}

		for (int y = 0; y < image.getHeight(); y++) {
			if (isRowBlank(image, y)) {
				boundingBox.y++;
				boundingBox.height--;
			} else {
				break;
			}
		}

		for (int y = image.getHeight() - 1; y >= 0; y--) {
			if (isRowBlank(image, y)) {
				boundingBox.height--;
			} else {
				break;
			}
		}

		boundingBox.x -= 50;
		boundingBox.y -= 50;
		boundingBox.width += 100;
		boundingBox.height += 100;

		boundingBox.x = Math.max(0, boundingBox.x);
		boundingBox.y = Math.max(0, boundingBox.y);

		savedSize = boundingBox;

		for (NamedInput ni : inputPositions.keySet()) {
			Point old = inputPositions.get(ni);
			inputPositions.put(ni, new Point(old.x - boundingBox.x, old.y - boundingBox.y));
		}

		for (LogicGate l : gateLocations.keySet()) {
			Point old = gateLocations.get(l);
			gateLocations.put(l, new Point(old.x - boundingBox.x, old.y - boundingBox.y));
		}

		return image.getSubimage(boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
	}

	private static boolean isColBlank(BufferedImage image, int col) {
		for (int row = 0; row < image.getHeight(); row++) {
			if (image.getRGB(col, row) != Color.WHITE.getRGB()) {
				return false;
			}
		}
		return true;
	}

	private static boolean isRowBlank(BufferedImage image, int row) {
		int[] rowRGB = image.getRGB(0, row, image.getWidth(), 1, null, 0, image.getWidth());

		for (int cur : rowRGB) {
			if (cur != Color.WHITE.getRGB()) {
				return false;
			}
		}
		return true;
	}
}
