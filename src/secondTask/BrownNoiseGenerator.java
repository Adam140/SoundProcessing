package secondTask;

import java.util.Random;

/**
* A simple brown noise generator.
* Generates brown noise by integrating white noise.
*
* @Author Christian d'Heureuse, Inventec Informatik AG, Zurich, Switzerland, www.source-code.biz
**/
public class BrownNoiseGenerator {

private double     minValue;
private double     maxValue;
private double     slope;
private double     hpFilter;
private double     valueRange;
private double     centerValue;
private double     currentValue;
private Random     random;

/**
* Creates a brown noise generator with an output range of -1 .. +1.
*/
public BrownNoiseGenerator() {
   this(-1.0, 1.0); }

/**
* Creates a brown noise generator with a specified output value range.
*
* @param minValue
*    Minimum output value.
* @param maxValue
*    Maximum output value.
*/
public BrownNoiseGenerator (double minValue, double maxValue) {
   this(minValue, maxValue, (maxValue - minValue) / 20, 0.02); }

/**
* Creates a brown noise generator.
*
* @param minValue
*    Minimum output value.
* @param maxValue
*    Maximum output value.
* @param slope
*    Maximum slope of the output signal.
*    This parameter defines the maximum difference between two consecutive output values.
*    It controls the amplitude of the output signal.
* @param hpFilter
*    High pass filter factor.
*    A simple first-order high pass filter is used to reduce DC drift of the output signal
*    and avoid frequent clipping.
*    If this parameter is zero, the high pass filter is disabled.
*/
public BrownNoiseGenerator (double minValue, double maxValue, double slope, double hpFilter) {
   this.minValue = minValue;
   this.maxValue = maxValue;
   this.slope = slope;
   this.hpFilter = hpFilter;
   if (minValue >= maxValue) {
      throw new IllegalArgumentException("Invalid minValue/maxValue."); }
   valueRange = maxValue - minValue;
   if (slope <= 0 || slope >= valueRange / 2) {
      throw new IllegalArgumentException("Invalid slope."); }
      // slope must be less than valueRange/2 because of the way we handle clipping.
   if (hpFilter < 0 || hpFilter >= 1) {
      throw new IllegalArgumentException("Invalid hpFilter value."); }
   centerValue = (minValue + maxValue) / 2;
   currentValue = centerValue;
   random = new Random(); }

/**
* Returns the next output value of the noise generator.
*/
public double getNext() {
   double whiteNoise = (random.nextFloat() * 2 - 1) * slope;
      // We use Random.nextFloat() because it's faster than nextDouble() and we
      // don't need the extra precision here.
   double v = currentValue;
   if (hpFilter > 0) {
      v -= (v - centerValue) * hpFilter; }                 // first-order high pass filter
   double next = v + whiteNoise;                           // integrate white noise
   if (next < minValue || next > maxValue) {               // clipping
      next = v - whiteNoise; }
   currentValue = next;
   return next; }

} // end class BrownNoiseGenerator