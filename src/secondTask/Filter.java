package secondTask;

public class Filter {

	
	public static double LFO(double last_value, double value , double smoothing)
	{
		double tmp = (value - last_value) / smoothing;
//		double value = samples[0];
//		for(int i=1;i<samples.length;i++)
//		{
//			double current_value = samples[i];
//			value += (current_value - value) / smoothing; 
//			samples[i] = value;
//		}
		return tmp;
	}
	
}
