package firstTask;

import java.awt.*;
import java.io.File;

import javax.swing.*;
import javax.swing.event.*;

class Diagram extends JPanel {
   static final int SCALEFACTOR = 10;
   int cycles;
   int points;
   double[] sines;
   int[] pts;
   boolean once = false;
   Diagram() {
   
  }
//  public  void setCycles(int newCycles) {
//    cycles = newCycles;
//    points = SCALEFACTOR * cycles * 2;
//    sines = new double[points];
//    for (int i = 0; i < points; i++) {
//    double radians = (Math.PI / SCALEFACTOR) * i;
//    sines[i] = Math.sin(radians);
//    }
//    repaint();
//  }
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    
    int maxWidth = getWidth();
    double hstep = (double) maxWidth / (double) points;
    int maxHeight = getHeight();
    if(!once)
    {
    calculatePoint();
    once = true;
    }
//    pts = new int[points];
//    for (int i = 0; i < points; i++)
//      pts[i] = (int) (sines[i] * maxHeight / 2 * .95 + maxHeight / 2);
    g.setColor(Color.BLUE);
    for (int i = 1; i < points; i++) {
      int x1 = (int) ((i - 1) * hstep);
      int x2 = (int) (i * hstep);
      int y1 = pts[i - 1];
      int y2 = pts[i];
      g.drawLine(x1, y1, x2, y2);
    }
  }
  
  public void calculatePoint()
  {
		try{
		// Open the wav file specified as the first argument
      WavFile wavFile = WavFile.openWavFile(new File("./wav/artificial/easy/1708Hz.wav"));

      // Display information about the wav file
      wavFile.display();

      // Get the number of audio channels in the wav file
      int numChannels = wavFile.getNumChannels();
      points = (int)wavFile.getNumFrames();
      points = 100;
      pts = new int[points+200];
      // Create a buffer of 100 frames
      double[] buffer = new double[100 * numChannels];

      int framesRead;
      double min = Double.MAX_VALUE;
      double max = Double.MIN_VALUE;
      
      int maxWidth = getWidth();
//      double hstep = (double) maxWidth / (double) points;
      int maxHeight = getHeight();
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
        	pts[100*i+s]=(int) (buffer[s] * maxHeight / 2 * .95 + maxHeight / 2);
            if (buffer[s] > max) max = buffer[s];
            if (buffer[s] < min) min = buffer[s];
        	 }
        	 catch(Exception e)
        	 {
        		 e.printStackTrace();
        		 System.out.println("I: " + i + " S: "+ s + "\n" + (i*100+s));
        	 }
         }
         i++;
         break;
         
      }
      while (framesRead != 0);

      // Close the wavFile
      wavFile.close();

      // Output the minimum and maximum value
      System.out.printf("Min: %f, Max: %f\n", min, max);
   }
   catch (Exception e)
   {
      System.err.println(e);
   }

  }
}
public class SineWave {
   public static void main(String[] args) {
   MainWindow frame = new MainWindow();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(500, 200);
    Diagram sines = new Diagram();
//	frame.getPanel().add(sines);
//	sines.setMaximumSize(new Dimension(32767, 50));
	sines.setPreferredSize(new Dimension(1000, 50));
	sines.setBackground(Color.LIGHT_GRAY);
	sines.setMinimumSize(new Dimension(1000, 50));
	sines.setBounds(new Rectangle(0, 0, 1000, 50));
	sines.repaint();
//	sines.setLayout(null);
	frame.getScrollPane().setRowHeaderView(sines);
//	sines.setVisible(true);
//	frame.getContentPane().add(sines);
    frame.setVisible(true);
  }
} 



