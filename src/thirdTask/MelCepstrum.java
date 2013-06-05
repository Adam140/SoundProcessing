package thirdTask;


import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

import transforms.FFT;

import com.badlogic.audio.analysis.Complex;

import firstTask.WindowFunction;

public class MelCepstrum {
	
	private String filePath;
	private double ck;
	private double lk;
	private double rk;
	
	MelCepstrum()
	{

	}
	

	public double[] getMelCepstrum(double[][] signal,int size,boolean sequence, int fs, int blocks) throws Exception
	{

        //WaveDecoder decoder = new WaveDecoder( new FileInputStream( this.filePath ) );
		int framerate = fs;
		long nframe = signal.length;
		System.out.println("Framerate:" + framerate);
		System.out.println("Frames:" + nframe);
		int K = 30;
		int D = 100;
		this.setParameters(K, D);
		double[] co = new double[(int) nframe];
 
        
        int N = size;
		//int N = 256;
        //float[] samples = new float[N];
  //      FFT fft = new FFT( );
        double[] spectrum = new double[N];

        double[] temp = new double[N];
        double[] cepstrum = new double[N];
        
        
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
                	s[i] = new Complex(samples[i],0);
                	//end[i] = 0;
                }
                s = FFT.fft1D(s);
                //s = FFT.ifft1D(s);

                //spectrum = WindowFunction.Triangular(spectrum);
                double c;
                for(int i=0;i<N;i++)
                {
                	//spectrum[i] = toMels(s[i].getReal());
                	spectrum[i] = s[i].getReal();

                	//powerSpectrum[i] = (1/N) * Math.pow(Math.abs(spectrum[i]),2);
                	//s[i] = new Complex(spectrum[i],0);
                }
            	c = cosin(spectrum, (int)framerate, K, D, 5);
            	co[f] = c;
  
        }

		return co;

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
	
	public double u(double in)
	{
		return 700 * ( Math.pow(10, (in/2595.00)) -1);
	}
	
	public void setParameters(double k, double d)
	{
		this.ck = u(k*d);
		this.lk = u((k-1)*d);
		this.rk = u((k+1)*d);
	}
	
	public double h(double f)
	{
		if(f >= this.lk && f <= this.ck )
			return (f - this.lk)/(this.ck - this.lk);
		else if(f > this.ck && f <= this.lk )
			return (this.rk - f)/(this.rk - this.ck);
		else
			return 0.0;
	}
	
	public double s(double[] signal, int fs, int k, int d )
	{
		this.setParameters(k, d);
		double result = 0;
		for(int i=0;i<signal.length/2;i++)
		{
			result += signal[i]*h( (fs/signal.length)*i);
		}
		return result;
		
		
	}
	
	public double s_prim(double[] signal, int fs, int k, int d )
	{
		double s1 = this.s(signal, fs,  k, d );
		double s2 = Math.log( Math.abs(s1) );
		return Math.pow( s2 ,2);
	}
	
	public double cosin(double[] signal, int fs, int K, int d,int F )
	{
		double[] c = new double[F];
		for(int f =0;f<1;f++)
		{
			double result = 0;
			for(int k=0;k<K-1;k++)
			{
				double s_prim_value = s_prim(signal,fs,k,d);
				result += s_prim_value * Math.cos( 2 * Math.PI * ( ( (2*k+1)*f )/4*K) );
			}
			return result;
		}
		return 0;
	}
	
	public double toMels(double f)
	{
		return 1125 * Math.log( 1 + f/700 );
	}
	
	public double fromMels(double m)
	{
		return 700 * (Math.exp(m/1125) -1);
	}

}
