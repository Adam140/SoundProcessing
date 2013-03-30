package firstTask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;
import org.math.plot.Plot3DPanel;

public class PhaseSpace {
	private double[] point;
	private int k;
	private int dimensions;

	public PhaseSpace(double[] point, int k, int dimensions) {
		this.point = point;
		this.k = k;
		if (k <= 0)
			this.k = 10;
		this.dimensions = dimensions;
	}

	public void draw2D() {

		// define your data
		int lenght = point.length;
		double[] x = new double[lenght - k];
		double[] y = new double[lenght - k];
		int last = 0;
		List meeting = new ArrayList();
		meeting.add(0);
		boolean correct;
		for (int i = 0; i < x.length; i++) {
			x[i] = point[i + k];
			y[i] = point[i];

			if (i != 0 && x[0] == x[i] && y[0] == y[i]) {
				meeting.add(i);
			}
		}

		// System.out.println("Czestotliwosc podstawowa to  " + lenght / (int)
		// meeting.get(1));
		// create your PlotPanel (you can use it as a JPanel)
		Plot2DPanel plot = new Plot2DPanel();

		// define the legend position
		plot.addLegend("SOUTH");

		// add a line plot to the PlotPanel
		plot.addLinePlot("phase space", x, y);

		// put the PlotPanel in a JFrame like a JPanel
		JFrame frame = new JFrame("a plot panel");
		frame.setSize(600, 600);
		frame.setContentPane(plot);
		frame.setVisible(true);

	}

	public void draw3D() {
		int lenght = point.length;
		double[] x = new double[lenght - 2*k];
		double[] y = new double[lenght - 2*k];
		double[] z = new double[lenght - 2*k];
		int last = 0;
		boolean correct = true;
		for (int i = 0; i < x.length; i++) {
			x[i] = point[i + 2*k];
			y[i] = point[i + k];
			z[i] = point[i];

			if (correct && i != 0 && x[0] == x[i] && y[0] == y[i] && z[0] == z[i]) {
//				meeting.add(i);
				System.out.println("Czestotliwosc podstawowa to  " + lenght / i + " \t " + i);
				correct = false;
			}
		}
		// create your PlotPanel (you can use it as a JPanel) with a legend at
		// SOUTH
		Plot3DPanel plot = new Plot3DPanel("SOUTH");

		// add grid plot to the PlotPanel
		plot.addLinePlot("Phase space", x, y, z);

		// put the PlotPanel in a JFrame like a JPanel
		JFrame frame = new JFrame("a plot panel");
		frame.setSize(600, 600);
		frame.setContentPane(plot);
		frame.setVisible(true);

	}

	private void checkMeeting(List meeting, int length) {
		int prev = 0;
		double prim = 0;
		boolean result = false;

		for (int i = 0; i < meeting.size(); i++) {
			if (meeting.size() - 1 > i) {
				prev = (int) meeting.get(i + 1) - (int) meeting.get(i);
			}
		}

		if (result)
			System.out.println("Czestotliwosc podstawowa to " + prim);
		else
			System.out.println("Zmien k lub dodaj wymiar");
	}
}
