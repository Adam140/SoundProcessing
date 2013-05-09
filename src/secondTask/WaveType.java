package secondTask;

public enum WaveType {
	SINUSOIDAL("SINUSOIDAL"),
	TRIANGULAR("TRIANGULAR"),
	SAWTOOTH("SAWTOOTH"),
	RECTANGULAR("RECTANGULAR"),
	WHITE_NOISE("WHITE_NOISE"),
	RED_NOISE("RED_NOISE");
	
    private WaveType(final String text) {
        this.text = text;
    }
    
    private final String text;
    
    @Override
    public String toString() {
        return text;
    }
}
