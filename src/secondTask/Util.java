package secondTask;

public class Util {

	static public boolean isNumber(String x)
	{
		if( x.equals(""))
			return false;
		
		try{
			Double.parseDouble(x);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
}
