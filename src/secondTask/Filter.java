package secondTask;

public class Filter {

	private double a0;
	private double a1;
	private double a2;
	private double b1;
	private double b2;
	private double X1 = -9999; // xn-1
	private double X2 = -9999; // xn-2
	private double Y1 = -9999;// yn -1 
	private double Y2 = -9999; // yn-2
	//given data
	private double fs = 44100.00; // given sampling frequency
	private double fc; // desired cutoff freguency
	private double Q; // resonance
	// additional parameters
	private double s;
	private double c;
	private double alpha;
	private double r;
	private double amplifiler = 1;
	
	
	/**
	 * 
	 * @param fs - Sampling frequency
	 * @param fc - Descired cutoff frequnecy
	 * @param Q - Resonance
	 */
	public Filter(double fs, double fc, double Q)
	{
		this.fs = fs;
		this.fc = fc;
		this.Q = Q;
		this.updateParameters();
	}
	
	public Filter() {
		this(44100.0, 100.0, 1.0);
		this.amplifiler = 1.0;
		setParametersForLPF();
	}
	/**
	 *  Update additional parameters (from instruction to task)
	 */
	private void updateParameters()
	{
		this.s = Math.sin(2*Math.PI*this.fc / this.fs);
		this.c = Math.cos(2*Math.PI*this.fc / this.fs);
		this.alpha = this.s / (2*this.Q);
		this.r = 1 / (1+this.alpha);
	}
	
	/**
	 * 
	 * @param fc - Desired cutoff frequency
	 * @param Q - resonance
	 */
	public void updateDate(double fc, double Q)
	{
		this.fc = fc;
		this.Q = Q;
		this.updateParameters();
	}
	
	/**
	 *  Set parameters to use Low Pass Filter
	 */
	public void setParametersForLPF()
	{
		this.a0 = 0.5 * (1 - this.c)*this.r;
		this.a1 = (1 - this.c)*this.r;
		this.a2 = this.a0;
		this.b1 = -2*this.c*this.r;
		this.b2 = (1 - this.alpha)*this.r;
	}
	
	/**
	 *  Set parameters to use High Pass Filter
	 */
	public void setParametersForHPF()
	{
		this.a0 = 0.5 * (1 + this.c)*this.r;
		this.a1 = -(1 + this.c)*r;
		this.a2 = this.a0;
		this.b1 = -2*this.c*this.r;
		this.b2 = (1 - this.alpha)*this.r;
	}
	
	public double calculate(double x)
	{
		double y = x;
		if( this.Y1 == -9999 && this.X1 == -9999)
		{
			y = this.a0 * x;
			this.Y1 = y;
			this.X1 = x;
		}
		else if( this.Y2 == -9999 && this.X2 == -9999)
		{
			this.Y2 = this.Y1;
			this.X2 = this.X1;
			
			y = this.a0 * x + this.a1 * this.X1 - this.b1 * this.Y1;
			this.Y1 = y;
			this.X1 = x;
		}
		else
		{
			y = this.a0 * x + this.a1 * this.X1 + this.a2*this.X2 - this.b1 * this.Y1 - this.b2*this.Y2;
			this.X2 = this.X1;
			this.Y2 = this.Y1;
			
			this.X1 = x;
			this.Y1 = y;
		}
		
		return y * this.amplifiler;
		
	}
	
	@Override
	public String toString() {
		return "Filter [a0=" + a0 + ", a1=" + a1 + ", a2=" + a2 + ", b1=" + b1
				+ ", b2=" + b2 + ", X1=" + X1 + ", X2=" + X2 + ", Y1=" + Y1
				+ ", Y2=" + Y2 + ", fs=" + fs + ", fc=" + fc + ", Q=" + Q
				+ ", s=" + s + ", c=" + c + ", alpha=" + alpha + ", r=" + r
				+ ", amplifiler=" + amplifiler + "]";
	}
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
	public double getAmplifiler() {
		return amplifiler;
	}
	public void setAmplifiler(double amplifiler) {
		this.amplifiler = amplifiler;
	}
	
}
