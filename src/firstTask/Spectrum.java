package firstTask;

import java.io.File;
import java.io.IOException;
import com.badlogic.audio.analysis.*;

public class Spectrum {
	
	private File f = null;
	private WavFile wf = null;

	//NumSamples = NumBytes / (NumChannels * BitsPerSample / 8)
	
	
	Spectrum(File f)
	{
		this.f = f;
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
	
	public void GetFFT()
	{
		//NumSamples = NumBytes / (NumChannels * BitsPerSample / 8)
		System.out.println("Frames: "+ Integer.toString((int)wf.getSampleRate()));
        float[] samples = new float[1024];
        FFT fft = new FFT( 1024, wf.getSampleRate() );
        
//        while( wf
//        {
//                fft.forward( samples );
//                fft.inverse( samples );
//                device.writeSamples( samples );
//        }

	}
	
	
	
	

}
