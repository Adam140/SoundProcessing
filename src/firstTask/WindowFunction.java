package firstTask;

public class WindowFunction {
	
	public static double[] Hamming(double[] array)
	{
		double[] result = new double[array.length];
		
		for(int i = 0; i < result.length; i++)
		{
			result[i] = 0.53836 - 0.46164 * Math.cos((2 * Math.PI * i) / (array.length - 1 ));
			result[i] = result[i] * array[i];
		}
		return result;
	}
	
	public static double[] Hanning(double[] array)
	{
		double[] result = new double[array.length];
		
		for(int i = 0; i < result.length; i++)
		{
			result[i] = 0.5 * (1 - Math.cos((2 * Math.PI * i) / (array.length - 1 )));
			result[i] = result[i] * array[i];
		}
		return result;
	}

}
