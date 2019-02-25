package draw;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import logicgates.LogicGate;
import logicgates.NamedInput;

/**
 * @author Nicholas Contreras
 */

@SuppressWarnings("serial")
public class DisplayPanel extends JPanel implements MouseListener, MouseMotionListener {

	private LogicGate circuit;
	private HashMap<NamedInput, Point> inputs;
	private HashMap<LogicGate, Point> gates;
	
	private LogicGate selected;

	public DisplayPanel(LogicGate circuit) {

		this.circuit = circuit;

		JFrame frame = new JFrame("Circuit Drawer");

		JScrollPane sp = new JScrollPane(this);
		
		frame.add(sp);

		BufferedImage image = CircuitDrawer.renderCircuit(circuit);
		inputs = CircuitDrawer.inputPositions;
		gates = CircuitDrawer.gateLocations;

		this.setPreferredSize(new Dimension(Math.min(image.getWidth(), 1500), Math.min(image.getHeight(), 900)));

		sp.addMouseListener(this);

		frame.pack();

		frame.setResizable(false);

		frame.setVisible(true);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				repaint();
			}
		}, 0, 50);
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(CircuitDrawer.renderCircuit(circuit), 0, 0, null);
	}

	private static double getDist(int x1, int y1, int x2, int y2) {
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		for (NamedInput ni : inputs.keySet()) {
			Point p = inputs.get(ni);
			if (getDist(p.x, p.y, e.getX(), e.getY()) < 100) {
				boolean state = ni.isActive();
				for (NamedInput n : inputs.keySet()) {
					if (n.equals(ni)) {
						n.forceStateChange(!state);
					}
				}
				break;
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
