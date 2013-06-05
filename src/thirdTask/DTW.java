package thirdTask;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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

	private ArrayList<Integer> x;
	private ArrayList<Integer> y;
	private double[][] g;
	private double[] t; // array of model signal
	private double[] s; // array of analyzed signal
	private boolean itakura;
	
	public double minimalPath = 0;

	public DTW(String tFile, String sFile, boolean itakura) {
		super();
		this.itakura = itakura;
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

	public DTW(double[] t, double[] s, boolean itakura) {
		super();
		this.itakura = itakura;
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
//				if(!itakuraConstraint(i, j, t.length, s.length))
//				{
//					g[i][j] = Double.POSITIVE_INFINITY;
//					continue;
//				}
				g[i][j] = euclideanDistance(t[i], s[j]) + min(g[i][j - 1], g[i - 1][j - 1], g[i - 1][j]);
			}
		}

		setPrecision(2);
		System.out.println("Minimal path = " + g[t.length - 1][s.length - 1]);
		this.minimalPath = g[t.length - 1][s.length - 1];
		return g;
	}

	public void setPrecision(int afterDot) {
		int factor = (int) Math.pow(10d, (double) afterDot);
		for (int i = 1; i < t.length; i++) {
			for (int j = 1; j < s.length; j++) {
				if(g[i][j] != Double.POSITIVE_INFINITY)
				g[i][j] = Math.round(g[i][j] * factor) / (double) factor;
			}
		}

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

	public void plotGraph() {
		bestPath();
		plotGraph(arrayToDouble(x), arrayToDouble(y));
	}

	public static void plotGraph(double[] x, double[] y) {
		Plot2DPanel plot = new Plot2DPanel();
		plot.addLinePlot("my plot", x, y);

		// put the PlotPanel in a JFrame, as a JPanel
		JFrame frame = new JFrame("a plot panel");
		frame.setContentPane(plot);
		frame.setSize(new Dimension(800, 600));
		frame.setVisible(true);
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

	public void toFile(String fileS) {
		try {
			File file = new File(fileS);

			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

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

				str += "|\n";
				bw.write(str);
			}
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void bestPath() {
		this.x = new ArrayList<>();
		this.y = new ArrayList<>();

		int j = g.length;
		int i = g[0].length;
		int J = g.length;
		int I = g[0].length;
		x.add(i);
		y.add(j);
		j--;
		i--;

		while ((j >= 0) || (i >= 0)) {
			final double diagCost;
			final double leftCost;
			final double upCost;

			if ((j > 0) && (i > 0) && itakuraConstraint(i - 1, j - 1, I, J))
				diagCost = g[j - 1][i - 1];
			else
				diagCost = Double.POSITIVE_INFINITY;

			if (j > 0 && itakuraConstraint(i, j - 1, I, J))
				upCost = g[j - 1][i];
			else
				upCost = Double.POSITIVE_INFINITY;

			if (i > 0 && itakuraConstraint(i - 1, j, I, J))
				leftCost = g[j][i - 1];
			else
				leftCost = Double.POSITIVE_INFINITY;

			if ((diagCost <= leftCost) && (diagCost <= upCost)) {
				j--;
				i--;
			} else if ((leftCost < diagCost) && (leftCost < upCost))
				i--;
			else if ((upCost < diagCost) && (upCost < leftCost))
				j--;
//			 else if (i <= j) // leftCost==rightCost > diagCost
//			 j--;
			 else // leftCost==rightCost > diagCost
			 i--;

			x.add(i);
			y.add(j);
		}

		// Collections.reverse(x);
		// Collections.reverse(y);

	}


	public static double[] arrayToDouble(ArrayList array) {
		double[] res = new double[array.size()];

		for (int i = 0; i < array.size(); i++)
			res[i] = (double) (int) array.get(i);

		return res;
	}
	
	public boolean itakuraConstraint(int i, int j, int I, int J)
	{
		if(!this.itakura)
			return true;
		
		final int a = 2 * (i - J) + I;
		final int b = (int) (0.5 * (i - 1) + 1);
		final int c = 2 * (i - 1) + 1;
		final int d = (int) (0.5 * (i - J) + I);
		
		return j >= a && j >= b && j <= c && j <= d;
	}
	public ArrayList<Integer> getX() {
		return x;
	}
	public ArrayList<Integer> getY() {
		return y;
	}
	
	public double[][] getG() {
		return g;
	}
}
