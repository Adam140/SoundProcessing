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
	
	
	public static double[] Triangular(double[] array)
	{
		int N = array.length;
		double[] result = new double[array.length];
		
		for(int i = 0; i < result.length; i++)
		{
			result[i] = 1 - Math.abs( (i - 0.5*(N-1)) / 0.5*(N+1) );

		}
		return result;
	}
	
	
	public static float[] Hamming(float[] array)
	{
		float[] result = new float[array.length];
		
		for(int i = 0; i < result.length; i++)
		{
			result[i] = (float) (0.53836 - 0.46164 * Math.cos((2 * Math.PI * i) / (array.length - 1 )));
			result[i] = (float) (result[i] * array[i]);
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
	
	public static double[][] sampling(double[] array, int sampRate)
	{
		int samplingRate = sampRate;
		if(isPowerOfTwo(sampRate))
		{
			samplingRate = sampRate;
		}
		
		double[][] result = new double[ (int) Math.ceil(array.length / (double) samplingRate)][];
		
		for(int i = 0; i < result.length; i++){
			if(i * samplingRate + samplingRate <= array.length)
				result[i] = new double[samplingRate];
			else
				result[i] = new double[array.length - i * samplingRate];
				
			for(int j = 0; j < samplingRate; j++)
			{
				if( i * samplingRate + j >= array.length)
					break;
				
				result[i][j] = array[i * samplingRate + j];
			}
		}
		
		return result;
	}
	
    public static boolean isPowerOfTwo(int n) {  
        return ((n & (n - 1)) == 0) && n > 0;  
    }  

}
