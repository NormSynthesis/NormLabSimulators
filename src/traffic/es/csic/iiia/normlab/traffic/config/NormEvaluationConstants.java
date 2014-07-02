package es.csic.iiia.normlab.traffic.config;

/**
 * 
 * @author Javier Morales (jmoralesmat@gmail.com)
 *
 */
public class NormEvaluationConstants {

	private float Keff, Kineff;
	private float Knec, Kunnec;
	
	/**
	 * 
	 */
	public NormEvaluationConstants(float Keff, float Kineff, float Knec, float Kunnec) {
		this.Keff = Keff;
		this.Kineff = Kineff;
		this.Knec = Knec;
		this.Kunnec = Kunnec;
	}
	
	/**
	 * 
	 * @return
	 */
	public float getKeff() {
  	return Keff;
  }
	
	/**
	 * 
	 * @param kEff
	 */
	public void setKeff(float kEff) {
  	Keff = kEff;
  }
	
	/**
	 * 
	 * @return
	 */
	public float getKineff() {
  	return Kineff;
  }
	
	/**
	 * 
	 * @param kIneff
	 */
	public void setKineff(float kIneff) {
  	Kineff = kIneff;
  }
	
	/**
	 * 
	 * @return
	 */
	public float getKnec() {
  	return Knec;
  }
	
	/**
	 * 
	 * @param kNec
	 */
	public void setKnec(float kNec) {
  	Knec = kNec;
  }
	
	/**
	 * 
	 * @return
	 */
	public float getKunnec() {
  	return Kunnec;
  }
	
	/**
	 * 
	 * @param kUnnec
	 */
	public void setKunnec(float kUnnec) {
  	Kunnec = kUnnec;
  }	
}
