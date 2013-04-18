package firstTask;

public class Sound {

	private double frequency;
	private int duration;
	
	/**
	 * @param frequency HZ
	 * @param duration millisecond
	 */
	public Sound(double frequency, int duration) {
		this.frequency = frequency;
		this.duration = duration;
	}
	
	public double getFrequency() {
		return frequency;
	}
	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
}
