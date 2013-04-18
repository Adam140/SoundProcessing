package secondTask;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class Player {

	public static void play(File file) {
		try {
			AudioInputStream stream;
			AudioFormat format;
			DataLine.Info info;
			Clip clip;

			stream = AudioSystem.getAudioInputStream(file);
			format = stream.getFormat();
			info = new DataLine.Info(Clip.class, format);
			clip = (Clip) AudioSystem.getLine(info);
			clip.open(stream);
			clip.start();
			clip.close();
			stream.close();
		} catch (Exception e) {
			// whatevers
		}
	}
}
