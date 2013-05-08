package secondTask;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class Mixer {

	public static double mix(Properties soundWaves) {
		double result = 0.0;
		int numberOfWaves = 0;

		Enumeration<?> e = soundWaves.propertyNames();

		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			String value = soundWaves.getProperty(key, "");

			if (!"".equals(value)) {
				numberOfWaves += Double.parseDouble(value);
				numberOfWaves++;
			}

		}

		if (numberOfWaves != 0) {
			result /= numberOfWaves; // to avoid cutoff graph
		}

		return result;
	}

	public static double mix(double... array) {
		double result = 0.0;
		int numberOfWaves = array.length;

		for (int i = 0; i < array.length; i++)
			result += array[i];

		if (numberOfWaves != 0) {
			result /= numberOfWaves; // to avoid cutoff graph
		}

		return result;
	}

	public static double mix(Vector<Object> vector) {
		double result = 0.0;
		int numberOfWaves = vector.size();

		for (int i = 0; i < vector.size(); i++) {
			Object object = vector.get(i);

			if (object instanceof String)
				result += Double.parseDouble(object.toString());
			else if (object instanceof Double)
				result += (Double) object;
		}

		if (numberOfWaves != 0) {
			result /= numberOfWaves; // to avoid cutoff graph
		}

		return result;
	}

}
