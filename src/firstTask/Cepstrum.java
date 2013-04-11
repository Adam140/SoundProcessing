package firstTask;



import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.plaf.SliderUI;

import org.math.plot.Plot2DPanel;
import org.math.plot.utils.Array;

import transforms.FFT;


import com.badlogic.audio.analysis.Complex;
import com.badlogic.audio.analysis.WaveDecoder;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;

public class Cepstrum {
	
	private File f = null;
	private WavFile wf = null;
	private String filePath;
	
	Cepstrum(File f)
	{
		this.f = f;
		this.filePath = f.getAbsolutePath();
		try {
			
			this.wf = WavFile.openWavFile(f);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WavFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

	public double getCepstrum(double[][] signal,int size,boolean sequence) throws Exception
	{

        //WaveDecoder decoder = new WaveDecoder( new FileInputStream( this.filePath ) );
		long framerate = wf.getSampleRate();
		long nframe = signal.length;
		System.out.println("Framerate:" + framerate);
		System.out.println("Frames:" + nframe);

 
        
        int N = size;
        double[] detected_frequency = new double[(int) nframe];
        //float[] samples = new float[N];
  //      FFT fft = new FFT( );
        double[] spectrum = new double[N];

        double[] temp = new double[N];
        double[] cepstrum = new double[N];

        double[] x = new double[N];
        double[] y = new double[N];
        
        double[] x1 = new double[N];
        double[] y1 = new double[N];
        
        double min_value = 9999999;
        double min_index = 0;
        
        
        Complex[] s = new Complex[N];
        Vector< double[] > vec = new Vector< double[]>();
        // 0 - freq , 1- position
        int zero_index = 0;
        double right_max = 99999;
        
        //while( decoder.readSamples( samples ) > 0 )
        for(int f=0;f<nframe;f++)
        {
        		double[] samples = signal[f];
        		if(samples.length != N)
        			break;
        		samples = WindowFunction.Hamming(samples);
                for(int i=0;i<N;i++)
                {
            		//samples[i] = (float) Math.sin(i);
                	temp[i] = samples[i];
                	y[i] = samples[i];
                	x[i] = i ;
                	s[i] = new Complex(samples[i],0);
                	//end[i] = 0;
                }
                s = FFT.fft1D(s);
                //s = FFT.ifft1D(s);


                for(int i=0;i<N;i++)
                {
                	spectrum[i] = Math.log10(Math.abs(s[i].getReal()));
                	s[i] = new Complex(spectrum[i],0);
                }
                s = FFT.ifft1D(s);
                zero_index = 0;
                //right_max = temp[N-1];
                for(int i=0;i<N;i++)
                {
                	
                	temp[i] = s[i].getReal();
                	if(zero_index == 0 && temp[i] < 0)
                		zero_index = i;
                	cepstrum[i] = Math.abs(temp[i]);
                }
                double max_value = 0;
                int max_index = 0;
                
                
                for(int i=zero_index;i<N/2;i++)
                {

                		if(cepstrum[i] > max_value)
                		{
                			max_value = cepstrum[i];
                			max_index = i;
                		}
                		
                }
                detected_frequency[f] = ComputeFrequency(max_index, N, (int) framerate);
              
                //break;
                //vec.add( temp );
                
        }
 
        String sounds = "";
		double samplePeerMs = framerate / 1000;


        boolean going_down = true;
        double max_value = 0;
        int max_index = 0;
        int going_down_index = 0;
        
        boolean right_going_down = true;
        int right_going_down_index = 0;
        
        double precision = 0.1;
//        for(int i=0;i<N;i++)
//        	end[i] = end[i]/N * 10;
        	
        
//

        for(int i=zero_index;i<N/2;i++)
        {
//        	if(going_down && end[i] - end[i-1] < precision)
//        		going_down = true;
//        	else
//        	{
//        		going_down = false;
//        		going_down_index = i;
//        	}
        	
//        	
//        	if(!going_down)
//        	{
        		if(cepstrum[i] > max_value)
        		{
        			max_value = cepstrum[i];
        			max_index = i;
        		}
        	//}
        		
        }
        
//      spectrummax = float(spectrumlen) / pos
//      return float(spectrummax * FRAMERATE) / frames
        double freq = 0;
        max_value =  (double)N / max_index;
        freq = Median(detected_frequency);
  	  	DecimalFormat df = new DecimalFormat("#.##");
  	  	double to_compare = 0;
  	  	double prev = 0;
  	  	int times = 0;
        if(sequence)
	        for(int a=0;a<detected_frequency.length;a++)
	        {
	        	if(a != 0)
	        		prev = detected_frequency[a-1];
	        	
	        	if(times == 0 && to_compare == 0 && Math.abs(prev - detected_frequency[a]) < 5)
	        	{
	        		to_compare = prev;
	        		times++;
	        	}
	        	else if( times != 0 && Math.abs(to_compare - detected_frequency[a]) < 5 )
	        		times++;
	        	else if(times != 0)
	        	{
		            System.out.println(df.format(prev) + " & "+(int)( N/ samplePeerMs)*times+" ms \\\\");

		        	//sounds += detected_frequency[a]+","+(int)( N/ samplePeerMs)*times+";";
		        	times = 0;
		        	to_compare = 0;

	        	}
	        	else
		            System.out.println(df.format(detected_frequency[a]) + " & "+(int)( N/ samplePeerMs)+" ms \\\\");

	        		sounds += detected_frequency[a]+","+(int)( N/ samplePeerMs)+";";
	        }
        else
        	sounds = freq+","+(int)( N/ samplePeerMs)*nframe+";";
        
//        System.out.println("Detected freq: "+Median(detected_frequency)+"Hz");
//        System.out.println("Position: "+max_index);
//
//        System.out.println("zero index: "+zero_index);
//
//        System.out.println("Left going down: "+going_down_index);
//        System.out.println("Right going down: "+right_going_down_index);

        
       x = TrimTable(15,N/2, x);
       spectrum =  TrimTable(15,N/2, cepstrum);
       cepstrum =  TrimTable(zero_index,N/2, cepstrum);

//		Plot2DPanel plot = new Plot2DPanel();
//		plot.addLegend("SOUTH");
//
//		// add a line plot to the PlotPanel
//		//plot.addLinePlot("Normal", x, y);
//		plot.addLinePlot("Spectrum", x, spectrum);
//		//plot.addLinePlot("Cepstrum", x, cepstrum);
//		//plot.addScatterPlot("Point", (double[]])max_index);
//		//plot.addLinePlot("Point",Color.RED, new double[] { max_index,max_index }, new double[] { getMaxValue(end),getMinValue(end) });
//		JFrame frame = new JFrame("a plot panel");
//		frame.setSize(600, 600);
//		frame.setContentPane(plot);
//		frame.setVisible(true);
//		
//		DateFormat dateFormat = new SimpleDateFormat("HH_mm");
//		Date date = new Date();
//		
//		Vector<Sound> sound_vec = ConsoleUtil.convertText(sounds);
//		WavFileGenerator wg = new WavFileGenerator(new File(dateFormat.format(date) + "seq.wav"), sound_vec);
//        wg.write();

		return freq;

	}
	
	
	public static double Median(double[] values)
	{
		//Arrays.asList(table)
		//Arrays values = new Arrays.;
		Arrays.sort(values);
	 
	    if (values.length % 2 == 1)
	    	return (double) values[(values.length+1)/2-1];
	    else
	    {
		double lower = (double) values[values.length/2-1];
		double upper = (double) values[values.length/2];
	 
			return (lower + upper) / 2.0;
	    }	
	}
	
	public double[] TrimTable(int ind,int to,double[] tab)
	{
		int size = tab.length - ind-(tab.length-to);
		double[] tmp = new double[size];
		for(int i=0;i<size;i++)
		{
			tmp[i] = tab[i+ind];
		}
		return tmp;
	}
	
	public double ComputeFrequency(int max_index,int N,int framerate)
	{
        double freq = 0;
        //double max_value =  (double)N / max_index;
        //freq = (max_value * framerate)/N;
        freq = (double)framerate / max_index;
        return freq;
	}
	
	public static double getMaxValue(double[] numbers){  
	    double maxValue = numbers[0];  
	    for(int i=1;i<numbers.length;i++){  
	        if(numbers[i] > maxValue){  
	            maxValue = numbers[i];  
	        }  
	    }  
	    return maxValue;  
	}  
	  
	//Find minimum (lowest) value in array using loop  
	public static double getMinValue(double[] numbers){  
	    double minValue = numbers[0];  
	    for(int i=1;i<numbers.length;i++){  
	        if(numbers[i] < minValue){  
	            minValue = numbers[i];  
	        }  
	    }  
	    return minValue;  
	} 
	
	public double calculateTime(int N,int sampleRate)
	{
		double samplePeerMs = sampleRate / 1000;

		return (int)( N/ samplePeerMs);
	}

}
