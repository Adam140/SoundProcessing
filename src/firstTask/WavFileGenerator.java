package firstTask;

import java.awt.EventQueue;
import java.io.File;
import java.util.Iterator;
import java.util.Vector;

import secondTask.BrownNoiseGenerator;
import secondTask.Filter;

public class WavFileGenerator {
	private File file;
	private Vector<Sound> vector = new Vector<Sound>();
	private static boolean phaseShift = true;	// czy uwzgledniac dopasowanie fazowe
	private int option;	// pobierane z okna radio czy cos
	// 0 - sinus
	// 1 - trojkatne
	// 2 - piloksztaltne
	// 3 - prostokantne
	// 4 - noise
	private static int sampleRate = 44100;    // Samples per second
	private static double eightDivideByPI = 8 / Math.pow(Math.PI,2);
	BrownNoiseGenerator bng= new BrownNoiseGenerator();
	private double last_value = -99999;


	public WavFileGenerator() {
		super();
		
	}
	
	public WavFileGenerator(File file, Vector<Sound> vec, int option) {
		super();
		this.file = file;
		this.vector = vec;
		this.option = option;
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
		this.bng = new BrownNoiseGenerator();
		try
	      {
	          int duration = 0;     // Seconds
	          int sampleDuration = sampleRate / 1000; // number of sample for millisecond
	          
	          Iterator<Sound> it = vector.iterator();
	          
	          
	          while(it.hasNext())
	          {
	        	  duration+=it.next().getDuration();
	          }
	          
	          it = vector.iterator();
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
	          double min = 0;
	          // Loop until all frames written
	          while (frameCounter < numFrames)
	          {
	             // Determine how many frames to write, up to a maximum of the buffer size
	             long remaining = wavFile.getFramesRemaining();
	             int toWrite = (remaining > 100) ? 100 : (int) remaining;

	             // Fill the buffer, one tone per channel
	             for (int s=0 ; s<toWrite ; s++, frameCounter++)
	             {
	                buffer[s] = function(frameCounter, sound.getFrequency(), phase,true);

	                if(buffer[s] < min)
	                	min = buffer[s];
	                if(phaseShift && buffer[s] >= -0.99)	// element przesuniecia fazowego
	                	continue;			// zeby konczylo sie zawsze na dole wykresu
	                
	                if(duration + sound.getDuration() * sampleDuration <= frameCounter && it.hasNext())
	                {
	                	System.out.println(min);
	                	min = 0;
	                	sound = it.next();
	                	duration+=sound.getDuration() * sampleDuration;
	                	
	                	if(phaseShift)
	                	{
	                		phase = phaseShift(frameCounter, sound.getFrequency());
	                	}
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
	
	private double function(long x, double freq, int phase, boolean filter)
	{

		double value = 0;
		switch(option)
		{
		case 0:
			value = sin(x, freq, phase);
			break;
		case 1:
			value = triangle(x, freq, phase);
			break;
		case 2:
			value = saw(x, freq, phase);
			break;
		case 3:
			value = rectangular(x, freq, phase);
			break;
		case 4:
			value = this.bng.getNext();
			break;
		case 5:
			value = whitenoise(x, freq, phase);
			break;
		default:
			value = 0;
			break;
			
		}
//		if(filter && this.last_value != -99999)
//		{
//			value = Filter.LFO(this.last_value, value, 25);
//			
//		}
		this.last_value = value;
		return value;
	}
	
	private int phaseShift(long x, double freq)
	{
		int phase = 0;
    
    	while(true)
    	{
    		if(function(x, freq, phase, true) <= -0.99 )
    			break;
    		
    		phase++;
    	}
    	
		return phase;
	}
	
	static double triangle(long x, double freq, int phase)
	{
		double result = 0;
		int period = (int) (sampleRate / freq);
		int halfPeriod = period / 2;
		x += phase;
		
		result = (2.0 / halfPeriod) * (x % period) - 1;
		
		if(x % period > halfPeriod)
			result = (-2.0 / halfPeriod) * (x % period) - 1;
		
		return result;
	}
	
	static double sin(long x, double freq, int phase)
	{
		double result = 0;
		
		result = Math.sin(2.0 * Math.PI * freq * (x + phase) / sampleRate);
		
		return result;
	}
	
	static double rectangular(long x, double freq, int phase)
	{
		double result = 0;
		
		double sin = sin(x,freq,phase);
		
		if(sin >= 0)
			return 1;
		else
			return -1;
	}
	
	static double saw(long x, double freq, int phase)
	{
		double result =0;
		int period = (int) (sampleRate / freq);
		x += phase;
		result = (2.0 / period) * (x % period) - 1;
		
		return result;
	}
	
	static double whitenoise(long x, double freq, int phase)
	{
		float amp = 1;
	    double result = amp * (2 * (float) Math.random() - 1);

		return result;
	}
}
