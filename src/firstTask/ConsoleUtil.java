package firstTask;

import java.util.Vector;

public class ConsoleUtil {

	public static Vector<Sound> convertText(String s)
	{
		Vector<Sound> vec =  new Vector<>();
		try{
			s = s.replaceAll("\\n", "");
			String[] allTone = s.split(";");
			
			for(String tmp:allTone)
			{
				String[] pair = tmp.split(",");
				Sound sound = new Sound(Double.valueOf(pair[0]),Integer.parseInt(pair[1]));
				vec.add(sound);
			}
		}
		catch(Exception e)
		{
			System.out.println("Wrong parameter" + e);
			vec = new Vector<Sound>();
		}
		
		return vec;
	}
}
