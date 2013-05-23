package thirdTask;

import java.io.File;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

class CaptureThread extends Thread {

	private TargetDataLine targetDataLine;
	private AudioFormat audioFormat;
	private File audioFile;

	public CaptureThread(File file) throws LineUnavailableException {
		super();
		this.audioFile = file;
		float sampleRate = 44100.0F; // 8000,11025,16000,22050,44100
		int sampleSizeInBits = 16; // 8,16
		int channels = 1; // 1,2
		boolean signed = true; // true,false
		boolean bigEndian = false; // true,false
		this.audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
		DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
		this.targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
	}
	
	public CaptureThread(String fileName) throws LineUnavailableException
	{
		this(new File(fileName));
	}

	public void run() {
		AudioFileFormat.Type fileType = null;

		fileType = AudioFileFormat.Type.WAVE;

		try {
			targetDataLine.open(audioFormat);
			targetDataLine.start();
			AudioSystem.write(new AudioInputStream(targetDataLine), fileType, audioFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
       
	}
	
	public void exit()
	{ 
		targetDataLine.stop();
        targetDataLine.close();
	}
	
	
}