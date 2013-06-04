package thirdTask;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.JPanel;
//import java.util.Random;

public class DistanceGraph extends JPanel {

	private double[][] g;
	private double[][] color;
	private BufferedImage image;
	private boolean rangeColor = false;
	/**
	 * Create the panel.
	 */
	public DistanceGraph(DTW dtw, boolean rangeColor) {
        super();
        this.rangeColor = rangeColor;
        System.out.println("START: DistanceGraph constructor");
        dtw.bestPath();
        this.g = dtw.getG();
        int i = g.length;
        int j = g[0].length;
        initImage();
        putLine(dtw.getX(), dtw.getY());
        Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int xW = (int) (screen.getWidth() / j);
        int xH = (int) (screen.getHeight() / i);
        
        if(xW > 1 && xH > 1)
        {
        	System.out.println("Resize image");
        	image = scaleImage(image, i * Math.min(xW, xH), j * Math.min(xW, xH), Color.red);
        }
        
        Dimension dimension = new Dimension(image.getWidth(), image.getHeight());
        setPreferredSize(dimension);
        System.out.println("END: DistanceGraph constructor");
    }
 
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(image, 0, 0, this);
    }
    
    private void initImage()
    {
    	   int iLength = g.length;
           int jLength = g[0].length;
           
           prepareRange();
           
           this.image = new BufferedImage(jLength, iLength, BufferedImage.TYPE_INT_RGB);
           
           for(int i = 0; i < iLength; i++)
           {
        	   for(int j = 0; j < jLength; j++)
        	   {
        		   int col = 0;
        		   
        		   if(rangeColor)
        		   {
        			   col = pickColor(g[i][j]);
        		   }
        		   else
        		   {
        			   col = (int) (g[i][j]);
	        		   if(col > 255)
	        			   col = 255;
	        		   col = (col << 16) | (col << 8) | col;
        		   }
        		   
        		   image.setRGB(j, i, col);
        	   }
        	   
           }
    }
    
    private void putLine(ArrayList<Integer> listX, ArrayList<Integer> listY)
    {
    	for(int i = 1; i < listX.size(); i++)
    	{
    		if(listX.get(i) < image.getWidth() && listY.get(i) < image.getHeight() && listX.get(i) >= 0 && listY.get(i) >= 0)
    			this.image.setRGB(listX.get(i), listY.get(i), Color.blue.getRGB());
    	}
    	int I = image.getHeight();
    	int J = image.getWidth();
    	for(int i = 0; i < image.getWidth(); i++)
    	{
    		int a = 2 * ( i - I) + J;
    		int b = (int) (0.5 * ( i - 1) + 1);
    		int c = 2 * ( i - 1) + 1;
    		int d = (int) (0.5 * ( i - I) + J);
    		if(a < I && a >= 0 && a >= b)
    			this.image.setRGB(i, a, Color.RED.getRGB());
    		if(b < I && b >= 0 && b >= a)
    			this.image.setRGB(i, b, Color.RED.getRGB());
    		if(c < I && c >= 0 && c <= d)
    			this.image.setRGB(i, c, Color.RED.getRGB());
    		if(d < I && d >= 0 && d <= c)
    			this.image.setRGB(i,d, Color.RED.getRGB());
    	}
    }
    
    public BufferedImage scaleImage(BufferedImage img, int width, int height,
            Color background) {
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        if (imgWidth*height < imgHeight*width) {
            width = imgWidth*height/imgHeight;
        } else {
            height = imgHeight*width/imgWidth;
        }
        BufferedImage newImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = newImage.createGraphics();
        try {
//            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setBackground(background);
            g.clearRect(0, 0, width, height);
            g.drawImage(img, 0, 0, width, height, null);
        } finally {
            g.dispose();
        }
        return newImage;
    }
    
    private void prepareRange()
    {
    	HashMap<Double, Integer> map = new HashMap<>();
    	
    	int rows = g.length;
		int columns = g[0].length;

		for (int i = 1; i < rows; i++) {
			for (int j = 1; j < columns; j++) {
				double key = g[i][j];
				Integer val = map.get(key);
				if(val == null)
					map.put(key, 1);
				else
					map.put(key, val + 1);
			}
		}
    	SortedSet<Double> keys = new TreeSet<Double>(map.keySet());
    	
    	int range = 10;
    	this.color = new double[range][2];
    	int partSize = (rows - 1) * (columns - 1) / range;
    	int count = 0;
    	int index = 0;
    	for (Double key : keys) { 
    		   count += map.get(key);
    		   
    		   if( count >= partSize)
    		   {
    			   color[index][0] = key;	// range
    			   color[index][1] = (255d / (double) range) * index; // color
    			   index++;
    			   count = 0;
    		   }
    		}
    	
    	color[range - 1][0] = Double.POSITIVE_INFINITY;
    	color[range - 1][1] = 255d;
    	
    }
    
    private int pickColor(double value)
    {
    	int col = 0;
    	
    	for(int i = 0; i < this.color.length; i++){
    		if(value <= color[i][0])
    		{
    			col = (int) color[i][1];
    			break;
    		}
    	}
    	return (col << 16) | (col << 8) | col;
    }

	public void setRangeColor(boolean rangeColor) {
		this.rangeColor = rangeColor;
	}

}
