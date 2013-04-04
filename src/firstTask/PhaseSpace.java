package firstTask;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;
import org.math.plot.Plot3DPanel;

public class PhaseSpace {
	private double[] points;
	private double[][] dividedPoints;
	private int numberOfSamples;
	private int k;
	private int dimensions;

	public PhaseSpace(double[][] points, int k, int dimensions) {
		this.dividedPoints = points;
		this.k = k;
		if (k <= 0)
			this.k = 10;
		this.dimensions = dimensions;
		this.numberOfSamples = dividedPoints.length;
	}
	
	public void draw2D(int sampleIndex) {

		points = dividedPoints[sampleIndex];
		// define your data
		int lenght = points.length;
		double[] x = new double[lenght - k];
		double[] y = new double[lenght - k];
		double[] x1 = new double[25];
		double[] y1 = new double[25];
		double[] x2 = new double[25];
		double[] y2 = new double[25];
		
		for (int i = 0; i < x.length; i++) {
			x[i] = points[i + k];
			y[i] = points[i];
		}
		for (int i = 0; i < x1.length; i++) {
			x1[i] = points[i + k];
			y1[i] = points[i];
		}
		for (int i = 0; i < x1.length; i++) {
			x2[i] = x[490 + i];
			y2[i] = y[490+ i];
		}


		searchPrimFreq(x, y,x1,y1);
		
		Plot2DPanel plot = new Plot2DPanel();

		// define the legend position
		plot.addLegend("SOUTH");

		// add a line plot to the PlotPanel
//		plot.addLinePlot("phase space", x, y);
		plot.addScatterPlot("Phase space 2D", x, y);
		plot.addScatterPlot("first", x1, y1);
		plot.addScatterPlot("second", x2, y2);
		// put the PlotPanel in a JFrame like a JPanel
		JFrame frame = new JFrame("a plot panel");
		frame.setSize(600, 600);
		frame.setContentPane(plot);
		frame.setVisible(true);

	}

	public void draw3D(int sampleIndex) {
		
		points = dividedPoints[sampleIndex];
		int lenght = points.length;
		double[] x = new double[lenght - 2*k];
		double[] y = new double[lenght - 2*k];
		double[] z = new double[lenght - 2*k];
		
		for (int i = 0; i < x.length; i++) {
			x[i] = points[i + 2*k];
			y[i] = points[i + k];
			z[i] = points[i];

		}
		
		searchPrimFreq(x, y, z);
		// SOUTH
		Plot3DPanel plot = new Plot3DPanel("SOUTH");

//		plot.addLinePlot("Phase space", x, y, z);
		plot.addScatterPlot("Phase space", x, y, z);

		// put the PlotPanel in a JFrame like a JPanel
		JFrame frame = new JFrame("a plot panel");
		frame.setSize(600, 600);
		frame.setContentPane(plot);
		frame.setVisible(true);
		

	}
	
	private void searchPrimFreq(double[] x, double[] y, double[] fx, double[] fy)
	{
		int similarSize = 10;
		double[] copyX = new double[similarSize], copyY = new double[similarSize];
		Vector freq = new Vector<>();
		
		int i = 0;
		for(i = 0; i < similarSize; i ++)
		{
			copyX[i] = x[i];
			copyY[i] = y[i];
		}
		i = 0;
		for(int j = similarSize; j < x.length; j++)
		{
			while( i < copyX.length && i + j < x.length && distanceBetweenPoints(copyX[i], copyY[i], x[j + i], y[j + i], 0.2))
			{
//				if(freq.size()==0)
//				{
//					fx[i] = x[j+i];
//					fy[i] = y[j+i];
//				}
				i++;
			}
			
			if( i == similarSize)
			{
				freq.add(j);
			}
//			if(freq.size()==0)
//			{
//				Arrays.fill(fx, 2);	
//				Arrays.fill(fy, 2);
//			}
			i = 0;
		}
		
		if(freq.size() != 0)
		{
			System.out.println("\nPrimary freq = " + Math.round(44100.0 / (double)(int)freq.get(0) ) + "Hz");
			
//			for(i = 1; i < freq.size(); i++)
//				System.out.print( x.length / ((int)freq.get(i) - (int)freq.get(i-1)) + "Hz, ");
		}
	}
	
	private void searchPrimFreq(double[] x, double[] y, double[] z)
	{
		int similarSize = 25;
		double[] copyX = new double[similarSize], copyY = new double[similarSize], copyZ = new double[similarSize];
		Vector freq = new Vector<>();
		
		int i = 0;
		for(i = 0; i < similarSize; i ++)
		{
			copyX[i] = x[i];
			copyY[i] = y[i];
			copyZ[i] = z[i];
		}
		i = 0;
		for(int j = similarSize; j < x.length; j++)
		{
			while( i < copyX.length && i + j < x.length && distanceBetweenPoints(copyX[i], copyY[i], copyZ[i], x[j + i], y[j + i], z[j + i], 0.2))
			{
				i++;
			}
			
			if( i == similarSize)
			{
				freq.add(j);
			}
			
			i = 0;
		}
		
		if(freq.size() != 0)
		{
			System.out.println("\nPrimary freq = " + Math.round((double)x.length / (double)(int)freq.get(0) ) + "Hz");
			
//			for(i = 1; i < freq.size(); i++)
//				System.out.print( x.length / ((int)freq.get(i) - (int)freq.get(i-1)) + "Hz, ");
		}
	}
	
	private boolean distanceBetweenPoints(double x1, double y1, double x2, double y2, double tolerance )
	{
		double distance = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
		
		if(distance <= tolerance)
			return true;
		else
			return false;
	}
	
	private boolean distanceBetweenPoints(double x1, double y1, double z1, double x2, double y2, double z2, double tolerance )
	{
		double distance = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) + Math.pow(z1 - z2, 2));
		
		if(distance <= tolerance)
			return true;
		else
			return false;
	}
	
	private boolean checkDirection(double[] x1, double[] y1, double[] x2, double[] y2)
	{
		for(int i = 0; i < x1.length; i++)
		{
			
		}
		return true;
	}
}
