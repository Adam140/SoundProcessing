package thirdTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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
	private int recordingMode;
	private double threshold;
	public MainWindow cheat;
	private boolean exit = false;
	final static double AMPLIFIER_RATE = 1.5;

	// 1 - start on button
	// 2 - start on speak

	public CaptureThread(File file, int mode, double threshold, MainWindow mainWindow) throws LineUnavailableException {
		super();
		this.audioFile = file;
		this.recordingMode = mode;
		this.threshold = threshold;
		float sampleRate = 44100.0F; // 8000,11025,16000,22050,44100
		int sampleSizeInBits = 16; // 8,16
		int channels = 1; // 1,2
		boolean signed = true; // true,false
		boolean bigEndian = false; // true,false
		this.audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
		DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
		this.targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
		this.cheat = mainWindow;
	}

	public CaptureThread(String fileName,int mode, double threshold, MainWindow mainWindow) throws LineUnavailableException {
		this(new File(fileName), mode, threshold, mainWindow);
	}

	public void run() {
		try {
			targetDataLine.open(audioFormat);
			targetDataLine.start();
		} catch (LineUnavailableException e) {
		}
		
		switch(recordingMode)
		{
		case 1:
			try {
				AudioSystem.write(new AudioInputStream(targetDataLine), AudioFileFormat.Type.WAVE, audioFile);
			} catch (IOException e) {
			}
			break;
		case 2:
			onVoiceRecording();
			break;
		}
	}

	public synchronized void exit() {
		exit = true;
		targetDataLine.stop();
		targetDataLine.close();
	}


	private void onVoiceRecording()
	{

		try {
			while(!exit)
			{
			final int bufferInMS = 250; 
			int bufferSize = (int)( audioFormat.getSampleRate() * audioFormat.getFrameSize() * ( bufferInMS / 1000.0));
			ArrayList<byte[]> list = new ArrayList<>();
			boolean recording = false;
			cheat.setIconReady();
			while (true) {
				byte buffer[] = new byte[bufferSize];
				int count = targetDataLine.read(buffer, 0, buffer.length);
				if(count == 0)
					continue;
//				double maxAmp = maxAmplitude(buffer);
//				System.out.println("Max A = " + maxAmp);
				double maxAmp = volumeRMS(bytesToDouble(buffer));
				System.out.println("Volumne rms = " + maxAmp);
				
				if(!recording && maxAmp > threshold)
				{
					recording = true;
					System.out.println("VOICE DETECTED - START RECORDING");
					cheat.setIconRec();
				}
				
				if(recording)
				{
					list.add(buffer);
				}
				else{
					list.add(buffer);
					if(list.size() == 2)
						list.remove(0);
				}
				
				if(recording && maxAmp < threshold)
				{
					System.out.println("STOP RECORDING");
					
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
					for(int i = 0; i < list.size(); i++)
						outputStream.write(list.get(i));
					InputStream input = new ByteArrayInputStream(outputStream.toByteArray());
					AudioSystem.write(new AudioInputStream(input, audioFormat, outputStream.size() / audioFormat.getFrameSize()), AudioFileFormat.Type.WAVE, audioFile);
					cheat.findBest();
					cheat.setIconStop();
					break;
				}

			}
			}
			
			exit();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Computes the RMS (Root mean square) volume of a group of signal sizes ranging from -1 to 1. */
	public double volumeRMS(double[] raw) {
		double sum = 0d;
		if (raw.length == 0) {
			return sum;
		} else {
			for (int ii = 0; ii < raw.length; ii++) {
				sum += raw[ii];
			}
		}
		double average = sum / raw.length;

		double sumMeanSquare = 0d;
		for (int ii = 0; ii < raw.length; ii++) {
			sumMeanSquare += Math.pow(raw[ii] - average, 2d);
		}
		double averageMeanSquare = sumMeanSquare / raw.length;
		double rootMeanSquare = Math.pow(averageMeanSquare, 0.5d);

		return rootMeanSquare;
	}


	/**
	 * Only for 16 bit peer sample convert array of bytes to array of double
	 * 
	 * @param bytes
	 * @return
	 */
	private double[] bytesToDouble(byte[] bytes) {
		if (bytes.length % 2 != 0)
			return null;

		double[] result = new double[bytes.length / 2];

		for (int i = 0; i < result.length; i++) {
			double temp = bytesToDouble(bytes[i * 2], bytes[i * 2 + 1]);
			result[i] = temp;
		}
		return result;
	}

	private double bytesToDouble(byte firstByte, byte secondByte) {
		// convert two bytes to one short (little endian)
		short s = (short) ((secondByte << 8) | firstByte);
		// convert to range from -1 to (just below) 1
		return s / 32768.0;
	}
	
	@SuppressWarnings("unused")
	private byte[] toArray(ArrayList<byte[]> list)
	{
		int lenght = list.get(0).length;
		byte[] result = new byte[lenght * list.size()];
		for(int i = 0; i < list.size(); i++)
		{
			byte[] temp = list.get(i);
			for(int j = 0; j < temp.length; j++)
				result[i*lenght + j] = temp[j];
		}
		
		return result;
	}

	@SuppressWarnings("unused")
	private double maxAmplitude(byte[] bytes) {
		double min = 1d, max = -1d;
		
		for (int i = 0; i < bytes.length; i = i + 2) 
		{
			double temp = bytesToDouble(bytes[i], bytes[i + 1]);
			if (temp > max)
				max = temp;
			if (temp < min)
				min = temp;
		}
		
		min = Math.abs(min);
		max = Math.abs(max);
		
		return Math.max(min,max);
	}

	public synchronized void setThreshold(double threshold) {
		this.threshold = threshold;
	}
}