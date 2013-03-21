package firstTask;

import java.nio.ByteBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;



class WaveGenerator extends Thread {

   final static public int SAMPLING_RATE = 44100;
   final static public int SAMPLE_SIZE = 2;                 //Sample size in bytes
   //You can play with the size of this buffer if you want.  Making it smaller speeds up
   //the response to the slider movement, but if you make it too small you will get 
   //noise in your output from buffer underflows, etc...
   final static public double BUFFER_DURATION = 0.100;      //About a 100ms buffer

   public WaveGenerator(double fFreq) {
	super();
	this.fFreq = fFreq;
}



// Size in bytes of sine wave samples we'll create on each loop pass         
   final static public int SINE_PACKET_SIZE = (int)(BUFFER_DURATION*SAMPLING_RATE*SAMPLE_SIZE);           
   
   SourceDataLine line;
   public double fFreq;                                    //Set from the pitch slider
   public boolean bExitThread = false;
   
   
   //Get the number of queued samples in the SourceDataLine buffer
   private int getLineSampleCount() {
      return line.getBufferSize() - line.available();
   }
   

   //Continually fill the audio output buffer whenever it starts to get empty, SINE_PACKET_SIZE/2
   //samples at a time, until we tell the thread to exit
   public void run() {
      //Position through the sine wave as a percentage (i.e. 0-1 is 0-2*PI)
      double fCyclePosition = 0;
      
      //Open up the audio output, using a sampling rate of 44100hz, 16 bit samples, mono, and big 
      // endian byte ordering.   Ask for a buffer size of at least 2*SINE_PACKET_SIZE
      try {
         AudioFormat format = new AudioFormat(44100, 16, 1, true, true);
         DataLine.Info info = new DataLine.Info(SourceDataLine.class, format, SINE_PACKET_SIZE*2);

         if (!AudioSystem.isLineSupported(info))
            throw new LineUnavailableException();

         line = (SourceDataLine)AudioSystem.getLine(info);
         line.open(format);  
         line.start();
      }
      catch (LineUnavailableException e) {
         System.out.println("Line of that type is not available");
         e.printStackTrace();            
         System.exit(-1);
      }

      System.out.println("Requested line buffer size = " + SINE_PACKET_SIZE*2);            
      System.out.println("Actual line buffer size = " + line.getBufferSize());


      ByteBuffer cBuf = ByteBuffer.allocate(SINE_PACKET_SIZE);

      //On each pass main loop fills the available free space in the audio buffer
      //Main loop creates audio samples for sine wave, runs until we tell the thread to exit
      //Each sample is spaced 1/SAMPLING_RATE apart in time
      while (bExitThread==false) {

         double fCycleInc = fFreq/SAMPLING_RATE;   //Fraction of cycle between samples

         cBuf.clear();                             //Toss out samples from previous pass

         //Generate SINE_PACKET_SIZE samples based on the current fCycleInc from fFreq
         for (int i=0; i<SINE_PACKET_SIZE/SAMPLE_SIZE; i++) {
            cBuf.putShort((short)(Short.MAX_VALUE * Math.sin(2*Math.PI * fCyclePosition)));

            fCyclePosition += fCycleInc;
            if (fCyclePosition > 1)
               fCyclePosition -= 1;
         }

         //Write sine samples to the line buffer
         // If the audio buffer is full, this would block until there is enough room,
         // but we are not writing unless we know there is enough space.
         line.write(cBuf.array(), 0, cBuf.position());    


         //Wait here until there are less than SINE_PACKET_SIZE samples in the buffer
         //(Buffer size is 2*SINE_PACKET_SIZE at least, so there will be room for 
         // at least SINE_PACKET_SIZE samples when this is true)
         try {
            while (getLineSampleCount() > SINE_PACKET_SIZE) 
               Thread.sleep(1);                          // Give UI a chance to run (?)
         }
         catch (InterruptedException e) {                // We don't care about this
         }
      }

      line.drain();
      line.close();
   }
   
   
   
   public void exit() {
      bExitThread=true;
   }
}
