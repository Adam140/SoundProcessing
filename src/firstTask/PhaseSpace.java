package firstTask;

import java.awt.Container;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import org.math.plot.Plot2DPanel;
import org.math.plot.Plot3DPanel;
import org.math.plot.PlotPanel;

public class PhaseSpace {
	private double[] points;
	private double[][] dividedPoints;
	private double tolerance;
	private int numberOfSamples;
	private int k;
	private int dimensions;
	private JTextArea console;

	public PhaseSpace(double[][] points, int k, int dimensions, double tolerance, JTextArea console) {
		this.dividedPoints = points;
		this.tolerance = tolerance;
		this.k = k;
		if (k <= 0)
			this.k = 10;
		this.dimensions = dimensions;
		this.numberOfSamples = dividedPoints.length;
		this.console = console;
	}
	
	public void calculate()
	{
		Vector<double[]> pointsVec = new Vector<>();
		int lenght = dividedPoints[0].length; 
		
		for(int i = 0; i < dimensions; i++)
			pointsVec.add(new double[lenght - dimensions * k]);
		
		points = dividedPoints[0];
		
		for(int i = 0; i < lenght - dimensions * k; i++)
		{
			for(int j = 0; j < pointsVec.size(); j++)
			{
				pointsVec.get(j)[i] = points[i + j*k];
			}
		}
		
		if(dimensions == 2 || dimensions == 3)
			draw(pointsVec, 0);
		
	}
	
	public void draw(Vector<double[]> pointsVec, int sampleIndex) {
//		searchPrimFreq(pointsVec.get(0), pointsVec.get(1));
		
		PlotPanel plot = null;
		if(dimensions == 2)
		{
			plot = new Plot2DPanel("SOUTH");
			((Plot2DPanel) plot).addScatterPlot("Phase space 2D", pointsVec.get(0), pointsVec.get(1));
		}
		else if(dimensions == 3){
			plot = new Plot3DPanel("SOUTH");
			((Plot3DPanel) plot).addScatterPlot("Phase space 3D",pointsVec.get(0), pointsVec.get(1),pointsVec.get(2));
		}
		// put the PlotPanel in a JFrame like a JPanel
		JFrame frame = new JFrame("a plot panel");
		frame.setSize(600, 600);
		frame.setContentPane(plot);
		frame.setVisible(true);

	}
	
	private void searchPrimFreq(double[] x, double[] y)
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
			while( i < copyX.length && i + j < x.length && distanceBetweenPoints(new double[]{copyX[i], copyY[i]},new double[]{ x[j + i], y[j + i]}))
			{
//				if(i == similarSize -1 && freq.size()==0)
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
			console.setText("\nPrimary freq = " + Math.round(44100.0 / (double)(int)freq.get(0) ) + "Hz");
			
			for(i = 0; i < freq.size(); i++)
				console.setText( console.getText() + "\n" + x.length / ((int)freq.get(i)) + "Hz, ");
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
			while( i < copyX.length && i + j < x.length && distanceBetweenPoints(new double[]{copyX[i], copyY[i], copyZ[i]},new double[]{x[j + i], y[j + i], z[j + i]}))
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
			console.setText("\nPrimary freq = " + Math.round((double)x.length / (double)(int)freq.get(0) ) + "Hz");
			
//			for(i = 1; i < freq.size(); i++)
//				console.setText( x.length / ((int)freq.get(i) - (int)freq.get(i-1)) + "Hz, ");
		}
	}
	
	private boolean distanceBetweenPoints(double[] firstPoint, double[] secondPoint)
	{
		if(firstPoint.length != secondPoint.length)
			return false;
		
		double distance = 0.0; // Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) + Math.pow(z1 - z2, 2));
		for(int i = 0; i < firstPoint.length; i++)
		{
			distance += Math.pow(firstPoint[i] - secondPoint[i], 2);
		}
		
		distance = Math.sqrt(distance);
		
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
