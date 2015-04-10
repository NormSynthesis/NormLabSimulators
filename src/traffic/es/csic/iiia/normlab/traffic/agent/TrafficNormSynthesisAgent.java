package es.csic.iiia.normlab.traffic.agent;

import java.util.List;

import es.csic.iiia.nsm.IncorrectSetupException;
import es.csic.iiia.nsm.NormSynthesisMachine;
import es.csic.iiia.nsm.metrics.NormSynthesisMetrics;
import es.csic.iiia.nsm.norm.Norm;
import es.csic.iiia.nsm.norm.NormativeSystem;

/**
 * 
 * @author "Javier Morales (jmorales@iiia.csic.es)"
 *
 */
public interface TrafficNormSynthesisAgent {

	/**
	 * @throws IncorrectSetupException 
	 * 
	 */
	public void step(long timeStep) throws IncorrectSetupException;

	/**
	 * 
	 * @return
	 */
	public NormSynthesisMetrics getMetrics();

	/**
	 * 
	 * @return
	 */
	public List<Norm> getAddedNorms();

	/**
	 * 
	 * @return
	 */
	public List<Norm> getRemovedNorms();

	/**
	 * 
	 * @return
	 */
	public NormativeSystem getNormativeSystem();

	/**
	 * 
	 * @return
	 */
	public NormSynthesisMachine getNormSynthesisMachine();
}
