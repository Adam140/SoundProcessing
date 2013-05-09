package secondTask;

import java.util.Properties;

public class RealTimePlayerFacade {

	private RealTimePlayer realTimePlayer;
	private Properties waves;
	private Properties amplifier;
	private Filter filter;
	
	public RealTimePlayerFacade(Properties waves, Properties amplifier, Filter filte) {
		this.waves = waves;
		this.realTimePlayer =  new RealTimePlayer(waves, amplifier, filter);
		this.filter = filte;
		this.amplifier = amplifier;
	}
	
	public void play()
	{
		if(realTimePlayer != null)
			realTimePlayer.exit();
		realTimePlayer = new RealTimePlayer(waves, amplifier, filter);
		realTimePlayer.start();
	}
	
	public void stop()
	{
		realTimePlayer.exit();
	}
	
	public void changeParams()
	{
		realTimePlayer.printParams();
	}
	
	public double getValue(long x)
	{
		return realTimePlayer.calculateValue(x);
	}
	

}
