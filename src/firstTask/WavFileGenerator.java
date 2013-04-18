package firstTask;

import java.awt.EventQueue;
import java.io.File;
import java.util.Iterator;
import java.util.Vector;

public class WavFileGenerator {
	private File file;
	private Vector<Sound> vector = new Vector<Sound>();


	public WavFileGenerator() {
		super();
		
	}
	
	public WavFileGenerator(File file, Vector<Sound> vec) {
		super();
		this.file = file;
		this.vector = vec;
	}
//	public static void main(String[] args) {
//		WavFileGenerator w = new WavFileGenerator();
//			
//		w.file = new File("s.wav");
//		w.vector.add(new Sound(2000.0,2000));
//		w.vector.add(new Sound(1000.0,2000));
//		w.vector.add(new Sound(2000.0,2000));
//		w.write();
//	}
	public void write() {
		try
	      {
	          int sampleRate = 44100;    // Samples per second
	          int duration = 0;     // Seconds
	          int sampleDuration = sampleRate / 1000; // number of sample for millisecond
	          
	          Iterator<Sound> it = vector.iterator();
	          
	          
	          while(it.hasNext())
	          {
	        	  duration+=it.next().getDuration();
	          }
	          
	          it = vector.iterator();
	          long counter = 0;
	          // Calculate the number of frames required for specified duration
	          long numFrames = (long)((double)duration / 1000.0 * sampleRate);

	          // Create a wav file with the name specified as the first argument
	          WavFile wavFile = WavFile.newWavFile(file, 1, numFrames, 16, sampleRate);

	          // Create a buffer of 100 frames
	          double[] buffer = new double[100];

	          // Initialise a local frame counter
	          long frameCounter = 0;
	          int phase = 0;
	          Sound sound = it.next();
	          duration = 0;
	          // Loop until all frames written
	          while (frameCounter < numFrames)
	          {
	             // Determine how many frames to write, up to a maximum of the buffer size
	             long remaining = wavFile.getFramesRemaining();
	             int toWrite = (remaining > 100) ? 100 : (int) remaining;

	             // Fill the buffer, one tone per channel
	             for (int s=0 ; s<toWrite ; s++, frameCounter++)
	             {
	                buffer[s] = Math.sin(2.0 * Math.PI * sound.getFrequency() * (frameCounter + phase) / sampleRate);
	                if(duration + sound.getDuration() * sampleDuration <= frameCounter && it.hasNext())
	                {
	                	sound = it.next();
	                	duration+=sound.getDuration() * sampleDuration;
	                	
	                }
	             }
	             
	             // Write the buffer
	             wavFile.writeFrames(buffer, toWrite);
	          }

	          // Close the wavFile
	          wavFile.close();
	       }
	       catch (Exception e)
	       {
	          System.err.println(e);
	       }
	    }
	

}