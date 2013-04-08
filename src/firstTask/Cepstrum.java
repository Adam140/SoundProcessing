package firstTask;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
	

	private float HammingWindow(float i,int nframes)
	{
		return (float) (0.5*(1-Math.cos(2*Math.PI*i/(nframes-1))));
	}
	
	public double getCepstrum(int size) throws Exception
	{
        WaveDecoder decoder = new WaveDecoder( new FileInputStream( this.filePath ) );
		long framerate = wf.getSampleRate();
		long nframe = wf.getNumFrames();
		System.out.println("Framerate:" + framerate);
		System.out.println("Frames:" + nframe);

 
        
        int N = size;
        float[] samples = new float[N];
  //      FFT fft = new FFT( );
        double[] spectrum = new double[N];

        double[] temp = new double[N];
        double[] end = new double[N];

        double[] x = new double[N];
        double[] y = new double[N];
        
        double[] x1 = new double[N];
        double[] y1 = new double[N];
        
        double min_value = 9999999;
        double min_index = 0;
        
        
        Complex[] s = new Complex[N];
        Vector< double[] > vec = new Vector< double[]>();
        
       
        while( decoder.readSamples( samples ) > 0 )
        {
        	
        		samples = WindowFunction.Hamming(samples);
                for(int i=0;i<N;i++)
                {
            		//samples[i] = (float) Math.sin(i);
                	temp[i] = samples[i];
                	y[i] = samples[i];
                	x[i] = i ;
                	s[i] = new Complex(samples[i],0);
                }
                s = FFT.fft1D(s);
                //s = FFT.ifft1D(s);


                for(int i=0;i<N;i++)
                {
                	temp[i] = Math.log10(Math.abs(s[i].getReal()));
                	s[i] = new Complex(temp[i],0);
                }
                s = FFT.ifft1D(s);
                for(int i=0;i<N;i++)
                {
                	
                	temp[i] = Math.abs(s[i].getReal());
                	end[i] += temp[i];

                }
                
              
                
                vec.add( temp );
                
        }
 
        boolean going_down = true;
        double max_value = 0;
        int max_index = 0;
        int left_min_index = 0;
        
        double precision = 0.1;
//        for(int i=0;i<N;i++)
//        	end[i] = end[i]/N;
        	
        

        for(int i=1;i<N/2;i++)
        {
        	if(going_down && end[i] - end[i-1] < precision)
        		going_down = true;
        	else
        		going_down = false;
        	
        	if(!going_down)
        	{
        		if(end[i] > max_value)
        		{
        			max_value = end[i];
        			max_index = i;
        		}
        	}
        		
        }
//      spectrummax = float(spectrumlen) / pos
//      return float(spectrummax * FRAMERATE) / frames
        double freq = 0;
        max_value =  (double)N / max_index;
        freq = (max_value * framerate)/N;
        System.out.println("Detected freq: "+freq+"Hz");
        

        
		Plot2DPanel plot = new Plot2DPanel();
		plot.addLegend("SOUTH");

		// add a line plot to the PlotPanel
		//plot.addLinePlot("Normal", x, y);
		plot.addLinePlot("Spec", x, end);
		//plot.addScatterPlot("Spec", temp, y);

		JFrame frame = new JFrame("a plot panel");
		frame.setSize(600, 600);
		frame.setContentPane(plot);
		frame.setVisible(true);
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

}
