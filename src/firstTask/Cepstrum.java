package firstTask;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

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
	
	public void getCepstrum() throws Exception
	{
        WaveDecoder decoder = new WaveDecoder( new FileInputStream( this.filePath ) );
		long framerate = wf.getSampleRate();
		long nframe = wf.getNumFrames();
		System.out.println("Framerate:" + framerate);
		System.out.println("Frames:" + nframe);

 
        
        int N = 1024;
        float[] samples = new float[N];
  //      FFT fft = new FFT( );
        double[] temp = new double[N];
        double[] x = new double[N];
        double[] y = new double[N];
        
        double[] x1 = new double[N];
        double[] y1 = new double[N];
        
        
        Complex[] s = new Complex[N];
        Vector< double[] > vec = new Vector< double[]>();
        
       
        while( decoder.readSamples( samples ) > 0 )
        {
        	
        		samples = WindowFunction.Hamming(samples);
                for(int i=0;i<N;i++)
                {
            		samples[i] = (float) Math.sin(i);
                	temp[i] = samples[i];
                	y[i] = samples[i];
                	x[i] = i ;
                	s[i] = new Complex(samples[i],0);
                }
                s = FFT.fft1D(s);
                //s = FFT.ifft1D(s);
        		//DoubleFFT_1D fft = new DoubleFFT_1D(N);
        		//fft.realForward(temp);
        		//fft.realInverse(temp,true);

//                s = FFT.ifft1D(s);

                for(int i=0;i<N;i++)
                {
                	//freq = max_index * Fs / N
                	//System.out.println( i * framerate / N );
                	temp[i] = Math.log10(Math.abs(s[i].getReal()));
                	s[i] = new Complex(temp[i],0);
                }
                s = FFT.ifft1D(s);
                for(int i=0;i<N;i++)
                {
                	
                	temp[i] = Math.abs(s[i].getReal());

                }
                vec.add( temp );
                
//                FFT.ifft1D(s);
//                for(int i=0;i<N;i++)
//                {
//                	
//                	y1[i] = s[i].getReal();
//                	if(i == 0)
//                		x1[i] = 0;
//                	else
//                		x1[i] = i*framerate/N;
//                }
               // break;
        }
        System.out.print( vec.size() );
        // AVG
        for(int i =0 ; i < vec.size(); i++)
        {
        	double[] t = vec.elementAt(i);
        	for(int j=0;j<t.length;j++)
        	{
        		temp[i] += t[j];
        	}
        	temp[i] = temp[i]/t.length;
        }
        
        
//        for(int i=0;i<N;i++)
//        {
//        	y[i] = samples[i];
//        	x[i] = i;
//        }
        
		Plot2DPanel plot = new Plot2DPanel();
		plot.addLegend("SOUTH");

		// add a line plot to the PlotPanel
		//plot.addLinePlot("Normal", x, y);
		plot.addLinePlot("Spec", x, temp);
		//plot.addScatterPlot("Spec", temp, y);

		JFrame frame = new JFrame("a plot panel");
		frame.setSize(600, 600);
		frame.setContentPane(plot);
		frame.setVisible(true);

	}

}
