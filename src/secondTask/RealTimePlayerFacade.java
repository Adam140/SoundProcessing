package secondTask;

import java.util.Properties;

public class RealTimePlayerFacade {

	private RealTimePlayer realTimePlayer;
	private Properties waves;
	
	public RealTimePlayerFacade(Properties waves) {
		this.waves = waves;
		this.realTimePlayer = new RealTimePlayer(waves);
	}
	
	public void play()
	{
		realTimePlayer.exit();
		realTimePlayer = new RealTimePlayer(waves);
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
	

}
