package thirdTask;


import java.util.Arrays;
import java.util.Vector;

import transforms.FFT;

import com.badlogic.audio.analysis.Complex;

import firstTask.WindowFunction;

public class MelCepstrum {
	
	private double ck;
	private double lk;
	private double rk;
	
	MelCepstrum()
	{

	}
	

	/**
	 * @param signal
	 * @param size of window
	 * @param sequence - ? 
	 * @param fs - framerate (44 100 Hz)
	 * @param blocks - ?
	 * @return
	 * @throws Exception
	 */
	public double[][] getMelCepstrum(double[][] signal,int size,boolean sequence, int fs, int blocks) throws Exception
	{

        //WaveDecoder decoder = new WaveDecoder( new FileInputStream( this.filePath ) );
		int framerate = fs;
		int nframe = signal.length;
		if(signal[nframe - 1].length != size)
			nframe--;
		System.out.println("Framerate:" + framerate);
		System.out.println("Frames:" + nframe);
		final int K = 30;
		final int D = 100;
		final int F = 12;
		this.setParameters(K, D);
		
		double[][] co = new double[(int) nframe][F];
 
        
        int N = size;
		//int N = 256;
        //float[] samples = new float[N];
  //      FFT fft = new FFT( );
        double[] spectrum = new double[N];

        double[] temp = new double[N];
        Complex[] s = new Complex[N];
        // 0 - freq , 1- position
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
                double[] c;
                for(int i=0;i<N;i++)
                {
                	//spectrum[i] = toMels(s[i].getReal());
                	spectrum[i] = s[i].getReal();

                	//powerSpectrum[i] = (1/N) * Math.pow(Math.abs(spectrum[i]),2);
                	//s[i] = new Complex(spectrum[i],0);
                }
            	c = functionC(spectrum, (int)framerate, K, D, F);
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
		else if(f > this.ck && f <= this.rk )
			return (this.rk - f)/(this.rk - this.ck);
		else
			return 0.0;
	}
	
	public double s(double[] signal, int fs, int k, int d )
	{
		this.setParameters(k, d);
		double result = 0;
		for(int i = 0; i < signal.length / 2; i++)
		{
			result += Math.abs(signal[i]) * h( (fs / signal.length) * i);
		}
		return result;
		
		
	}
	
	public double s_prim(double[] signal, int fs, int k, int d )
	{
		double s1 = this.s(signal, fs,  k, d );
		double s2 = Math.log( s1 );
		return Math.pow( s2 ,2);
	}
	
	/**
	 * @param signal
	 * @param fs
	 * @param K - const
	 * @param d - const
	 * @param F - const -the number of the MFCC coefficients
	 * @return
	 */
	public double[] functionC(double[] signal, int fs, int K, int d,int F )
	{
		double[] c = new double[F];
		for(int n = 1; n <= c.length; n++)
		{
			double result = 0;
			for(int k = 0; k < K - 1; k++)
			{
				double s_prim_value = s_prim(signal,fs,k,d);
				result += s_prim_value * Math.cos(Math.toRadians( 2 * Math.PI * ( ( 2 * k + 1 ) * n ) / 4 * K ));
			}
			c[n - 1] = result;
		}
//		System.out.println(Arrays.toString(c));
		return c;
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
