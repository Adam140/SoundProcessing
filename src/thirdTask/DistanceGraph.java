package thirdTask;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Random;
//import java.util.Random;

import javax.swing.JPanel;

public class DistanceGraph extends JPanel {

	private double[][] g;
	private BufferedImage image;
	/**
	 * Create the panel.
	 */
	public DistanceGraph(double[][] g) {
        super();
        System.out.println("START: DistanceGraph constructor");
        int i = g.length;
        int j = g[0].length;
        this.g = g;
        initImage();
        Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int xW = (int) (screen.getWidth() / j);
        int xH = (int) (screen.getHeight() / i);
        
        if(xW > 1 && xH > 1)
        	image = scaleImage(image, i * Math.min(xW, xH), j * Math.min(xW, xH), Color.red);
        
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
           
           this.image = new BufferedImage(jLength, iLength, BufferedImage.TYPE_BYTE_GRAY);
           
           for(int i = 0; i < iLength; i++)
           {
        	   for(int j = 0; j < jLength; j++)
        	   {
//        		   int rand = (new Random()).nextInt(255); 
        		   int rand = (int) (g[i][j]);
        		   int col = (rand << 16) | (rand << 8) | rand;
        		   image.setRGB(j, i, col);
        	   }
        	   
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

}
