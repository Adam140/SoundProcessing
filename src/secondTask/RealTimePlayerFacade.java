package secondTask;

import java.util.Properties;

public class RealTimePlayerFacade {

	private RealTimePlayer realTimePlayer;
	private Properties waves;
	private Filter filter;
	
	public RealTimePlayerFacade(Properties waves, Filter filte) {
		this.waves = waves;
		this.realTimePlayer =  new RealTimePlayer(waves, filter);
		this.filter = filte;
	}
	
	public void play()
	{
		if(realTimePlayer != null)
			realTimePlayer.exit();
		realTimePlayer = new RealTimePlayer(waves, filter);
		realTimePlayer.start();
	}
	
	public void stop()
	{
		realTimePlayer.exit();
	}
	
	public void changeParams()
	{
		realTimePlayer.changeParams();
	}
	
	public double getValue(long x)
	{
		return realTimePlayer.calculateValue(x);
	}
	

}
