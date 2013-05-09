package secondTask;

public class Generator {
	
	private static final int SAMPLE_RATE = 44100;
	//public static Filter filter = null;
	

	static double function(long x, double freq, int phase, WaveType type, boolean filter)
	{
		double value = 0;
		switch(type)
		{
		case SINUSOIDAL:
			value = sin(x, freq, phase);
			break;
		case TRIANGULAR:
			value = triangle(x, freq, phase);
			break;
		case SAWTOOTH:
			value = saw(x, freq, phase);
			break;
		case RECTANGULAR:
			value = rectangular(x, freq, phase);
			break;
		case RED_NOISE:
			BrownNoiseGenerator bng= new BrownNoiseGenerator();
			value = bng.getNext();
			break;
		case WHITE_NOISE:
			value = whitenoise(x, freq, phase);
			break;
		default:
			value = 0;
			break;
		}
		
//		if( MainWindow.chckbxLowpassFilter.isSelected() )
//		{
//			if( Generator.filter == null)
//			{
//				Generator.filter =  new Filter(44100, Double.valueOf(MainWindow.tf_fc.getText()), Double.valueOf(MainWindow.tf_Q.getText()));
//				Generator.filter.setParametersForLPF();
//				Generator.filter.setAmplifiler(Double.valueOf(MainWindow.tf_amplifier.getText()));
//			}
//			value = Generator.filter.calculate(value);
//			//System.out.println("Value: "+ value);
//		}
//		else
//			System.out.println("Value: "+ value);
		return value;
	}
	
	private int phaseShift(long x, double freq, WaveType waveType)
	{
		int phase = 0;
    
    	while(true)
    	{
    		if(function(x, freq, phase, waveType, true) <= -0.99 )
    			break;
    		
    		phase++;
    	}
    	
		return phase;
	}
	
	static double triangle(long x, double freq, int phase)
	{
		double result = 0;
		int period = (int) (SAMPLE_RATE / freq);
		int halfPeriod = period / 2;
		x += phase;
		
		result = (2.0 / halfPeriod) * (x % period) - 1;
		
		if(result > 1.0)
			result = (-2.0 / halfPeriod) * (x % period) + 3;
		
		return result;
	}
	
	static double sin(long x, double freq, int phase)
	{
		double result = 0;
		
		result = Math.sin(2.0 * Math.PI * freq * (x + phase) / SAMPLE_RATE);
		
		return result;
	}
	
	static double rectangular(long x, double freq, int phase)
	{
		double result = 0;
		
		double sin = sin(x,freq,phase);
		
		if(sin >= 0)
			return 1;
		else
			return -1;
	}
	
	static double saw(long x, double freq, int phase)
	{
		double result =0;
		int period = (int) (SAMPLE_RATE / freq);
		x += phase;
		result = (2.0 / period) * (x % period) - 1;
		
		return result;
	}
	
	static double whitenoise(long x, double freq, int phase)
	{
		float amp = 1;
	    double result = amp * (2 * (float) Math.random() - 1);

		return result;
	}

}
