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
   File file;
   
   boolean once = false;
   
  public Diagram(int maxHeight, int width) {
	super();
	this.maxHeight = maxHeight;
	this.wid = width;
	this.file = new File("./wav/artificial/easy/225Hz.wav");

}

public void paintComponent(Graphics g) {
    super.paintComponent(g);
    
    if(!once)
    {
    generateWaves();
    once = true;
    }
    
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
    g.drawLine(0, getHeight()/2, getWidth(), getHeight()/2);
  }
  
  public void generateWaves()
  {
		try{
		// Open the wav file specified as the first argument
      WavFile wavFile = WavFile.openWavFile(file);

      // Display information about the wav file
//      wavFile.display();

      // Get the number of audio channels in the wav file
      int numChannels = wavFile.getNumChannels();
      xLenght = (int)wavFile.getNumFrames();
//      xLenght = 100;
      y = new int[xLenght];
//      System.out.println(getHeight());
      this.setSize(y.length, getHeight());
      this.wid = y.length;
      this.setPreferredSize(new Dimension(wid, getHeight()));
      // Create a buffer of 100 frames
      double[] buffer = new double[100 * numChannels];

      int framesRead;
//      double min = Double.MAX_VALUE;
//      double max = Double.MIN_VALUE;
      
//      double hstep = (double) maxWidth / (double) points;
      int maxHeight = getHeight();
      System.out.println(maxHeight);
      int i = 0;
      do
      {
    	  
         // Read frames into buffer
         framesRead = wavFile.readFrames(buffer, 100);

         // Loop through frames and look for minimum and maximum value
         for (int s=0 ; s<framesRead * numChannels ; s++)
         {
        	 try
        	 {
        	y[100*i+s]=(int) (buffer[s] * maxHeight / 2 * 0.95 + maxHeight / 2);
//            if (buffer[s] > max) max = buffer[s];
//            if (buffer[s] < min) min = buffer[s];
        	 }
        	 catch(Exception e)
        	 {
        		 e.printStackTrace();
        		 System.out.println("I: " + i + " S: "+ s + "\n" + (i*100+s));
        	 }
         }
         i++;
//         break;
         
      }
      while (framesRead != 0);

      // Close the wavFile
      wavFile.close();

      // Output the minimum and maximum value
//      System.out.printf("Min: %f, Max: %f\n", min, max);
   }
   catch (Exception e)
   {
      System.err.println(e);
   }

  }
}
