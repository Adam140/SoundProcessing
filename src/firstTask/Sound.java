package firstTask;

public class Sound {

	private Double frequency;
	private int duration;
	
	/**
	 * @param frequency HZ
	 * @param duration millisecond
	 */
	public Sound(Double frequency, int duration) {
		this.frequency = frequency;
		this.duration = duration;
	}
	
	public Double getFrequency() {
		return frequency;
	}
	public void setFrequency(Double frequency) {
		this.frequency = frequency;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
}
