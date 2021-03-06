package firstTask;

import java.awt.Container;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import org.math.plot.Plot2DPanel;
import org.math.plot.Plot3DPanel;
import org.math.plot.PlotPanel;

public class PhaseSpace {
	private double[] points;
	private double[][] dividedPoints;
	private List<Integer> mode = new ArrayList();
	private double tolerance;
	private int indexOfFrame;
	private int k;
	private int dimensions;
	private int fileSize;
	private JTextArea console;

	public PhaseSpace(double[][] points, int size, int k, int dimensions,
			double tolerance, int index, JTextArea console) {
		this.dividedPoints = points;
		this.tolerance = tolerance;
		this.k = k;
		if (k <= 0)
			this.k = 10;
		this.dimensions = dimensions;
		this.indexOfFrame = index;
		this.console = console;
		this.fileSize = size;
	}

	public void calculate() {

		console.setText("");
		int temp = 0;
		
		if(indexOfFrame!=0)
			temp = 1;
		
		for (int a = indexOfFrame - temp; a < dividedPoints.length; a++) 
		{
			Vector<double[]> pointsVec = new Vector<>();
			int lenght = dividedPoints[a].length;

			for (int i = 0; i < dimensions; i++)
				pointsVec.add(new double[lenght - dimensions * k]);

			points = dividedPoints[a]; // number of frame
			
			for (int j = 0; j < pointsVec.size(); j++) {
			for (int i = 0; i < lenght - dimensions * k; i++) {
					pointsVec.get(j)[i] = points[i + j * k];
				}
			}

			console.setText(console.getText() + (a + 1)  + "\t");
			searchPrimFreq(pointsVec);
			
			if(	indexOfFrame!=0	)
			{
				if (dimensions == 2 || dimensions == 3)
					draw(pointsVec);
				break;
			}
		}
		
		
		console.setText(console.getText() + "Primary freq = "+ mode(mode) + " Hz");
	}

	public void draw(Vector<double[]> pointsVec) {

		PlotPanel plot = null;
		if (dimensions == 2) {
			plot = new Plot2DPanel("SOUTH");
			((Plot2DPanel) plot).addScatterPlot("Phase space 2D",
					pointsVec.get(0), pointsVec.get(1));
		} else if (dimensions == 3) {
			plot = new Plot3DPanel("SOUTH");
			((Plot3DPanel) plot).addScatterPlot("Phase space 3D",
					pointsVec.get(0), pointsVec.get(1), pointsVec.get(2));
		}
		// put the PlotPanel in a JFrame like a JPanel
		JFrame frame = new JFrame("a plot panel");
		frame.setSize(600, 600);
		frame.setContentPane(plot);
		frame.setVisible(true);

	}

	private void searchPrimFreq(Vector<double[]> pointsVec) {
		int wait = 10; // amount of points where we sure that cycle doesn't
						// occur
		int lenght = pointsVec.get(0).length; // amount of points in every
												// dimension
//		int amountOfRandom = 100;	
		int amountOfRandom = lenght;	
		List<Integer> allCycles = new ArrayList<>();

		for (int i = 0; i < amountOfRandom; i++) {
			int randIndex = 0;
//			do randIndex = randInt(0, lenght - 1); while (randIndex < 0 || randIndex >= lenght);
			randIndex = i;
			
			int cycle = 0; // if is -1 when cycle didn't detected
			boolean forward = true; // direction of searching

			for (int j = randIndex; 0 <= j && j <= lenght;) {
				if (forward)
					j++;
				else
					j--;

				if (j < 0) {
					cycle = -1;
					break;
				} else if (j >= lenght) {
					forward = false;
					continue;
				}

				if (Math.abs(randIndex - j) >= wait
						&& distanceBetweenPoints(getPoints(pointsVec, randIndex), getPoints(pointsVec, j))) 
				{
					cycle = Math.abs(randIndex - j);
					break;
				}
			}

			// cycle after 20 sample is 2 200 Hz
			// cycle after 1000 sample is 40 Hz
			// this is range of frequency in this task
			if( cycle > 20 && cycle < 1000 && cycle != -1 )
				allCycles.add(cycle);
		}
		
		
		String buffer = console.getText() + "Cycles detected ";
//		for(int i = 0; i < allCycles.length; i++)
//		{
//			buffer+= Math.round( fileSize / (double) allCycles[i] ) + ", ";
//		}
		mode.add(fileSize / mode(allCycles));
		buffer+= "mode=" + fileSize / mode(allCycles); //+ " avg=" + fileSize / average(allCycles) + " median=" + fileSize / median(allCycles);
		buffer+="\n";
		console.setText(buffer);
	}

	private boolean distanceBetweenPoints(double[] firstPoint,
			double[] secondPoint) {
		if (firstPoint.length != secondPoint.length)
			return false;

		double distance = 0.0; 
		for (int i = 0; i < firstPoint.length; i++) {
			distance += Math.pow(firstPoint[i] - secondPoint[i], 2);
		}

		distance = Math.sqrt(distance);

		if (distance <= tolerance)
			return true;
		else
			return false;
	}

	/**
	 * zwraca wszystkie punkty z kazdego wymiaru (np. x y z) w formie tablicy
	 * double
	 * 
	 * @param pointsVec
	 * @param index
	 * @return
	 */
	private static double[] getPoints(Vector<double[]> pointsVec, int index) {
		double[] result = new double[pointsVec.size()];

		for (int i = 0; i < result.length; i++) {
			result[i] = pointsVec.get(i)[index];
		}

		return result;
	}

	public static int randInt(int a, int b) {
		int result = 0;
		result = a + (int) (Math.random() * ((b - a) + 1));
		return result;
	}
	
	public static int median(List list)
	{
		int length = list.size();
		Collections.sort(list);
		
		if(length == 1)
			return (int) list.get(0);
		else if(length == 0)
			return 0;
		else if(length % 2 == 0)
			return ((int)list.get(length / 2 - 1) + (int)list.get(length / 2 + 1))  / 2;
		else
			return (int) list.get(length / 2);
	}
	
	public static double average(List list)
	{
		double result = 0.0;
		
		for(int i = 0; i < list.size(); i++)
			result+=(int)list.get(i);
		
		return result / list.size();
	}
	
	public static int mode(List list)
	{
	
		if(list.size()==0)
			return -1;
		
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		
		for(int i = 0; i < list.size(); i++)
		{
			Integer value = (Integer) list.get(i);
			if(map.containsKey(value))
			{
				map.put(value, map.get(value) + 1);
			}
			else
			{
				map.put(value, 1);
			}
		}
		
		List<Entry<Integer, Integer>> entries = new ArrayList<Entry<Integer, Integer>>(map.entrySet());
		Collections.sort(entries, new Comparator<Entry<Integer, Integer>>() {
		    public int compare(Entry<Integer, Integer> e1, Entry<Integer, Integer> e2) {
		        return -1 * e1.getValue().compareTo(e2.getValue());
		    }
		});		
		

		return entries.get(0).getKey();
	}

	public List<Integer> getMode() {
		return mode;
	}
}
