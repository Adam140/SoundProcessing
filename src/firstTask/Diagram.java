package firstTask;

import java.awt.*;
import java.io.File;

import javax.swing.*;
import javax.swing.event.*;

class Diagram extends JPanel {

	int xLenght;
	int[] y;
	int maxHeight;
	int wid;
	double hstep = 1.0;
	int samplingRate;

	boolean once = false;

	public Diagram(int maxHeight, int width) {
		super();
		this.maxHeight = maxHeight;
		this.wid = width;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);


		int maxWidth = getWidth();
		hstep = maxWidth / getWidth();
		g.setColor(Color.BLUE);
		for (int i = 1; i < xLenght; i++) {
			int x1 = (int) ((i - 1) * hstep);
			int x2 = (int) (i * hstep);
			int y1 = y[i - 1];
			int y2 = y[i];
			g.drawLine(x1, y1, x2, y2);
		}
		g.setColor(Color.black);
		g.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2); // linia pozioma
		
		if(samplingRate > 0)
		for(int i = 0; i < xLenght / samplingRate; i++)
		{
			g.drawLine(i * samplingRate, 0, i * samplingRate, getHeight()); // linia pozioma
		}
	}

	public void recountPoint(double[] table, int samplingRate) {
		this.samplingRate = samplingRate;
		this.xLenght = table.length;
		this.y = new int[xLenght];
		this.setSize(y.length, getHeight());
		this.wid = y.length;
		this.setPreferredSize(new Dimension(wid, getHeight()));
		int maxHeight = getHeight();
		
		for(int i = 0; i < xLenght; i++)
		{
			y[i] = (int) (table[i] * maxHeight / 2
					* 0.95 + maxHeight / 2);
		}
	}
}
