package es.csic.iiia.normlab.traffic.car;

/**
 * 
 * @author Javier Morales (jmoralesmat@gmail.com)
 *
 */
public enum CarReasonerState {

	/**
	 * No norms have been neither fired nor applied in the current tick
	 */
	NoNormActivated,

	/**
	 * The fired norm will be applied
	 */
	NormWillBeApplied,

	/**
	 * The fired norm will be violated
	 */
	NormWillBeViolated;
}