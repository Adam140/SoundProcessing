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
	private double[][] t; // array of model signal
	private double[][] s; // array of analyzed signal
	private boolean itakura;

	public double minimalPath = 0;
	public double minimalPath2 = 0;

	public DTW(String tFile, String sFile, boolean itakura) {
		super();
		this.itakura = itakura;
		System.out.println("START: DTW construktor");
		System.out.println("Read from file");
		this.t = fromFile(tFile);
//		System.out.println("T = " + Array.toString(t));
		this.s = fromFile(sFile);
//		System.out.println("S = " + Array.toString(s));
		this.g = new double[t.length][s.length];
		intialG();
		System.out.println("END: DTW construktor");
	}

	public DTW(double[][] t, double[][] s, boolean itakura) {
		super();
		this.itakura = itakura;
		this.t = t;
		this.s = s;
		this.g = new double[t.length][s.length];
		intialG();
	}

	private void intialG() {
		// g[0][0] = euclideanDistance(t[0], s[0]);
		g[0][0] = 0;

		for (int i = 1; i < t.length; i++)
			g[i][0] = Double.POSITIVE_INFINITY;
		// g[i][0] = g[i - 1][0] + euclideanDistance(t[i], s[0]);

		for (int j = 1; j < s.length; j++)
			g[0][j] = Double.POSITIVE_INFINITY;
		// g[0][j] = g[0][j - 1] + euclideanDistance(t[0], s[j]);

	}

	private double euclideanDistance(double a, double b) {
		return Math.sqrt(Math.pow(Math.abs(a - b), 2));
	}
	
	private double euclideanDistance(double[] a, double[] b) {
		double sum = 0d;
		for(int i = 0; i < a.length; i++)
		{
			sum += Math.pow(Math.abs(a[i] - b[i]), 2);
		}
		return Math.sqrt(sum);
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
		for (int j = 1; j < t.length; j++) {
			for (int i = 1; i < s.length; i++) {
				// PRZY GENEROWANIU TABLICY
				if (!itakuraConstraint(i, j, s.length, t.length)) {
					g[j][i] = Double.POSITIVE_INFINITY;
					continue;
				}
				g[j][i] = euclideanDistance(t[j], s[i]) + min(g[j][i - 1], g[j - 1][i - 1], g[j - 1][i]);
			}
		}

		setPrecision(2);
//		System.out.println("Minimal path = " + g[t.length - 1][s.length - 1]);
		this.minimalPath = g[t.length - 1][s.length - 1];
		// normalizacja
		this.minimalPath = minimalPath / (t.length + s.length);
		bestPath();
		return g;
	}

	public void setPrecision(int afterDot) {
		int factor = (int) Math.pow(10d, (double) afterDot);
		for (int i = 1; i < t.length; i++) {
			for (int j = 1; j < s.length; j++) {
				if (g[i][j] != Double.POSITIVE_INFINITY)
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
		Plot2DPanel plot = new Plot2DPanel();
		plot.addLinePlot("The warping function", arrayListToDouble2(x), arrayListToDouble2(y));

		double[] xt = new double[t.length * t[0].length];
		double[] xs = new double[s.length * s[0].length];

		for (int i = 0; i < s.length; i++)
			for(int j = 0; j < s[i].length; j++)
				xs[i * s[i].length + j] = i * s[i].length + j;

		for (int i = 0; i < t.length; i++)
			for(int j = 0; j < t[i].length; j++)
				xt[i * t[i].length + j] = i * t[i].length + j;

		Plot2DPanel plotS = new Plot2DPanel();
		plotS.addLinePlot("Model signal", xt, twoDtoOneD(t));
		Plot2DPanel plotT = new Plot2DPanel();
		plotT.addLinePlot("analyzed signal", xs, twoDtoOneD(s));

		// put the PlotPanel in a JFrame, as a JPanel
		JFrame frame = new JFrame("The warping function plot");
		frame.setContentPane(plot);
		frame.setSize(new Dimension(800, 600));
		frame.setVisible(true);

		JFrame frameS = new JFrame("Analyzed signal plot");
		frameS.setContentPane(plotS);
		frameS.setBounds(400, 400, 800, 600);
		frameS.setVisible(true);

		JFrame frameT = new JFrame("Model signal plpot");
		frameT.setContentPane(plotT);
		frameT.setBounds(800, 0, 800, 600);
		frameT.setVisible(true);
	}

	@Override
	public String toString() {
		return "DTW:\n t=" + Arrays.toString(t) + "\n s=" + Arrays.toString(s) + "\n" + arrayToString(g);
	}

	private double[][] fromFile(String file) {
		ArrayList<Double[]> array = new ArrayList<>();

		FileInputStream fstream;
		try {
			fstream = new FileInputStream(file);

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String strLine;

			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				String [] row = strLine.split(",");
				
				if(row.length > 1)
					array.add(arrayToDouble(row));
				else
					array.add(new Double[]{Double.parseDouble(strLine)});
			}

			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return arrayListToDouble(array);
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

		double sum = 0d;
		try {
			x.add(i);
			y.add(j);
			j--;
			i--;

			while ((j >= 0) && (i >= 0)) {
				final double diagCost;
				final double leftCost;
				final double upCost;

				if ((j > 0) && (i > 0))// && itakuraConstraint(i - 1, j - 1, I,
										// J))
					diagCost = g[j - 1][i - 1];
				else
					diagCost = Double.POSITIVE_INFINITY;

				if (j > 0)// && itakuraConstraint(i, j - 1, I, J))
					upCost = g[j - 1][i];
				else
					upCost = Double.POSITIVE_INFINITY;

				if (i > 0)// && itakuraConstraint(i - 1, j, I, J))
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
				// else if (i <= j) // leftCost==rightCost > diagCost
				// j--;
				else
					// leftCost==rightCost > diagCost
					i--;

				x.add(i);
				y.add(j);

				if (i >= 0 && j >= 0) {
					sum += g[j][i];
				}
			}
		} catch (ArrayIndexOutOfBoundsException a) {
			System.out.println("j: " + j + " i: " + i);
			a.printStackTrace();
		}
		minimalPath2 = sum / (t.length + s.length);
	}

	public static double[][] arrayListToDouble(ArrayList<Double[]> array) {
		double[][] res = new double[array.size()][array.get(0).length];

		for (int i = 0; i < array.size(); i++)
			res[i] = arrayToDouble(array.get(i));

		return res;
	}
	
	public static double[] arrayListToDouble2(ArrayList<Integer> array) {
		double[] res = new double[array.size()];

		for (int i = 0; i < array.size(); i++)
			res[i] = array.get(i);

		return res;
	}
	
	public static Double[] arrayToDouble(String[] array)
	{
		Double[] res = new Double[array.length];

		for (int i = 0; i < array.length; i++)
			res[i] = Double.parseDouble(array[i]);

		return res;
	}
	
	public static double[] arrayToDouble(Double[] array)
	{
		double[] res = new double[array.length];
		
		for (int i = 0; i < array.length; i++)
			res[i] = array[i];
		
		return res;
	}
	
	public static double[] twoDtoOneD(ArrayList<Double[]> array)
	{
		double[] res = new double[array.size() * array.get(0).length];
		
		for (int i = 0; i < array.size(); i++)
			for( int j = 0; j < array.get(i).length; j++)
			{
				res[i * array.get(i).length + j] = array.get(i)[j];
			}
		
		return res;
	}
	
	public static double[] twoDtoOneD(double[][] array)
	{
		double[] res = new double[array.length * array[0].length];
		
		for (int i = 0; i < array.length; i++)
			for( int j = 0; j < array[i].length; j++)
			{
				res[i * array[i].length + j] = array[i][j];
			}
		
		return res;
	}

	public boolean itakuraConstraint(int i, int j, int I, int J) {
		if (!this.itakura)
			return true;

		final int a = 2 * (i - I) + J;
		final int b = (int) (0.5 * (i - 1) + 1);
		final int c = 2 * (i - 1) + 1;
		final int d = (int) (0.5 * (i - I) + J);

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
