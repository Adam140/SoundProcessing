package firstTask;

import java.awt.EventQueue;
import java.io.File;
import java.util.Iterator;
import java.util.Vector;

public class WavFileGenerator {
	private File file;
	private Vector<Sound> vector = new Vector<Sound>();
	private static boolean phaseShift = false;	// czy uwzgledniac dopasowanie fazowe
	private int option = 1;	// pobierane z okna radio czy cos
	// 0 - sinus
	// 1 - trojkatne
	// 2 - piloksztaltne
	// 3 - prostokantne
	// 4 - noise
	private static int sampleRate = 44100;    // Samples per second
	private static double eightDivideByPI = 8 / Math.pow(Math.PI,2);


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
	          // Loop until all frames written
	          while (frameCounter < numFrames)
	          {
	             // Determine how many frames to write, up to a maximum of the buffer size
	             long remaining = wavFile.getFramesRemaining();
	             int toWrite = (remaining > 100) ? 100 : (int) remaining;

	             // Fill the buffer, one tone per channel
	             for (int s=0 ; s<toWrite ; s++, frameCounter++)
	             {
//	            	buffer[s] = sin(frameCounter, sound.getFrequency(), phase);
	            	 double last = 0;
	            	 if(s == 0)
	            	 {
	            		 if(frameCounter!=0)
	            			 last = buffer[buffer.length - 1];
	            	 }
	            	 else
	            		 last = buffer[s - 1];
	            	 
	                buffer[s] = function(frameCounter, sound.getFrequency(), phase);

	                if(duration + sound.getDuration() * sampleDuration <= frameCounter && it.hasNext())
	                {
	                	sound = it.next();
	                	duration+=sound.getDuration() * sampleDuration;
	                	
	                	if(phaseShift)
	                	{
//		                	double last = 0;
		                	if(s == 0)
		                		last = buffer[buffer.length - 1];
		                	else
		                		last = buffer[s - 1];
		                	
		                	boolean growing = true;
		                	if(buffer[s] - last > 0)
		                		growing = true;
		                	else
		                		growing = false;
		                	
		                	phase = 0;
		                	double temp,temp2;
		                	while(true)
		                	{
		                		temp = function(frameCounter, sound.getFrequency(), phase);
		                		temp2 = function(frameCounter, sound.getFrequency(), phase + 1);
		                		if(temp2 - temp > 0 == growing)	// nastepna czesc wykresu powinna miec taka sama monotonicznosc
		                		{
		                			while(true)
		                			{
		                				temp = function(frameCounter, sound.getFrequency(), phase);
		                				if(growing && (temp > buffer[s] || Math.abs(temp - buffer[s]) <= 0.01))
		                				{
		                					break;
		                				}
		                				else if(!growing && (temp < buffer[s] || Math.abs(temp - buffer[s]) <= 0.01))
		                				{
		                					break;
		                				}
		                				phase++;
		                			}
		                			
		                			break;
		                		}
		                		phase++;
		                	}
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
	
	private double function(long x, double freq, int phase)
	{
		switch(option)
		{
		case 0:
			return sin(x, freq, phase);
		case 1:
			return triangle(x, freq, phase);
		case 2:
			return 0;
		case 3:
			return rectangular(x, freq, phase);
		case 4:
			return 0;
		default:
			return 0;
		}
	}
	
	static double triangle(long x, double freq, int phase)
	{
		double result = 0;
		int period = (int) (sampleRate / freq);
		int halfPeriod = period / 2;
		
		result = (1.0 / halfPeriod) * (x % period) - 1;
		
		if(x % period > halfPeriod)
			result = -1 * result;
//		double a = 1 / freq / 2.0; // period 2a
//		
//		result = 2 / a * (x - a * Math.floor(x/a + 0.5)) * Math.pow(-1,Math.floor(x/a + 0.5));
//		int x1, x2 = (int) x;
//		do
//		{
//			result = sin(x1, freq, phase);
//			x1++; // miejsce zerowe
//		}
//		while(Math.abs(result) <= 0.01);
//		
//		x2 = x1;
//		do
//		{
//			result = sin(x2, freq, phase);
//			x2++; // nastepne miejsce zerowe
//		}
//		while(Math.abs(result) <= 0.01);
//		
//		result = lastValue / eightDivideByPI;
//		x = x / sampleRate;
//		double temp = Math.sin((2*x + 1) * freq * x);
//		temp = temp / (Math.pow(2 * x + 1, 2));
//		temp = temp * Math.pow(-1, x);
//		
//		result = temp + result;
//		result = eightDivideByPI * result;
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
		result = sin(x,freq,phase) - Math.floor(sin(x,freq,phase));
		
		return result;
	}
	

}
