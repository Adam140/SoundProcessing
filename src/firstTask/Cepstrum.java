package firstTask;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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
        int N = 44100;
		long framerate = wf.getSampleRate();
		long nframe = wf.getNumFrames();
		System.out.println("Framerate:" + framerate);
		System.out.println("Frames:" + nframe);
		int[] buf = new int[N];
		double[] data = new double[N];
		
		wf.readFrames(buf, (int) framerate);
		
		double twopi = 8.0*Math.atan(1.0);            
		double arg = twopi/((double)N-1.0);
		
		Complex[] csignal = new Complex[N];
		for (int i = 0; i < N; ++i)
			//hammming window
			csignal[i] = new Complex(buf[i]*(0.54 - 0.46*Math.cos(arg*(double)i)), 0);
		
		csignal = FFT.fft1D(csignal);
		for (int i = 0; i < csignal.length; ++i)
			//power spectrum
			//csignal[i] = new Complex(10.0*Math.log10(Math.pow(csignal[i].abs(),2)+1), 0);
			//complex spectrum
			//csignal[i] = csignal[i].log();
			//real spectrum
			csignal[i] = new Complex(10.0*Math.log10(csignal[i].abs()+1), 0);
		
		
		csignal = FFT.fft1D(csignal);

		
		
		
//		DoubleFFT_1D fft = new DoubleFFT_1D((int) nframe);
//		// OK!
//		for(int i=0;i<nframe;i++)
//		{
//			data[i] = (float)buf[i]*this.HammingWindow(i, (int) nframe);
//		}
//		fft.realForward(data);
//		for(int i=0;i<nframe;i++)
//		{
//			data[i] = Math.log((data[i]));//*this.HammingWindow(i, (int) nframe);
//		}
//		fft.realInverse(data,false);
//		
        
        
//        int N = 1024;
//        float[] samples = new float[1024];
//        FFT fft = new FFT( N, 44100 );
//        float[] temp;
//        
//       
//        while( decoder.readSamples( samples ) > 0 )
//        {
//                fft.forward( samples );
//        		temp = samples;
//        		
//        		for(int i=0;i<N;i++)
//        		{
//        			temp[i] = (float) Math.log(Math.abs(temp[i]));
//        		}
//        		
//        		
//                fft.forward( temp );
//                int a =1;
//                break;
//        }
	}

}
