package thirdTask;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;
import org.math.plot.utils.Array;

/**
 * Dynamic Time Warping
 * 
 * @author Adam
 * 
 */
public class DTW {

	private double[][] g;
	private double[] t; // array of model signal
	private double[] s; // array of analyzed signal

	public DTW(String tFile, String sFile) {
		super();
		System.out.println("START: DTW construktor");
		System.out.println("Read from file");
		this.t = fromFile(tFile);
		System.out.println("T = " + Array.toString(t));
		this.s = fromFile(sFile);
		System.out.println("S = " + Array.toString(s));
		this.g = new double[t.length][s.length];
		intialG();
		System.out.println("END: DTW construktor");
	}

	public DTW(double[] t, double[] s) {
		super();
		this.t = t;
		this.s = s;
		this.g = new double[t.length][s.length];
		intialG();
	}

	private void intialG() {
		for (int i = 1; i < t.length; i++)
			g[i][0] = Double.POSITIVE_INFINITY;

		for (int j = 1; j < s.length; j++)
			g[0][j] = Double.POSITIVE_INFINITY;

		g[0][0] = 1;
	}

	private double euclideanDistance(double a, double b) {
		return Math.sqrt(Math.pow(Math.abs(a - b), 2));
	}

	private double min(double... values) {
		double min = Double.POSITIVE_INFINITY;

		for (int i = 0; i < values.length; i++) {
			if (values[i] < min)
				min = values[i];
		}
		return min;
	}

	public double[][] calculateG() {
		for (int i = 1; i < t.length; i++) {
			for (int j = 1; j < s.length; j++) {
				g[i][j] = euclideanDistance(t[i], s[j]) + min(g[i][j - 1], g[i - 1][j - 1], g[i - 1][j]);
			}
		}
		return g;
	}

	public static String arrayToString(double[][] a) {
		try {
			int rows = a.length;
			int columns = a[0].length;
			String str = "";

			for (int i = 0; i < rows; i++) {
				str += "|\t";
				for (int j = 0; j < columns; j++) {
					if (a[i][j] == Double.POSITIVE_INFINITY)
						str += " - \t";
					else
						str += a[i][j] + "\t";
				}

				str += "|\n";
			}

			return str;
		} catch (Exception e) {
			return "Matrix is empty!";
		}

	}
	
	public void printG() {
		try {
			int rows = g.length;
			int columns = g[0].length;
			
			for (int i = 0; i < rows; i++) {
				String str = "|\t";
				for (int j = 0; j < columns; j++) {
					if (g[i][j] == Double.POSITIVE_INFINITY)
						str += " - \t";
					else
						str += g[i][j] + "\t";
				}
				
				str += "|";
				System.out.println(str);
			}
			
		} catch (Exception e) {
		}
		
	}

	public static void plotGraph(double[] x, double[] y) {
		Plot2DPanel plot = new Plot2DPanel();
		plot.addLinePlot("my plot", x, y);

		// put the PlotPanel in a JFrame, as a JPanel
		JFrame frame = new JFrame("a plot panel");
		frame.setContentPane(plot);
		frame.setVisible(true);
	}

	public static void main(String[] agr) {
		double[] s = { 1d, 1d, 2d, 3d, 2d, 0d };
		double[] t = { 0d, 1d, 1d, 2d, 3d, 2d, 1d };

		DTW dtw = new DTW(t, s);
		System.out.println(arrayToString(dtw.calculateG()));

	}

	@Override
	public String toString() {
		return "DTW:\n t=" + Arrays.toString(t) + "\n s=" + Arrays.toString(s) + "\n" + arrayToString(g);
	}

	private double[] fromFile(String file) {
		ArrayList<Double> array = new ArrayList<>();

		FileInputStream fstream;
		try {
			fstream = new FileInputStream(file);

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String strLine;

			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				array.add(Double.parseDouble(strLine));
			}

			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double[] res = new double[array.size()];

		for (int i = 0; i < array.size(); i++)
			res[i] = (double) array.get(i);

		return res;
	}
}
