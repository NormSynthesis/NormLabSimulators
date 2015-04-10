package es.csic.iiia.normlab.traffic.examples.ex2;

import java.util.ArrayList;
import java.util.List;

import es.csic.iiia.normlab.traffic.agent.TrafficNormSynthesisAgent;
import es.csic.iiia.normlab.traffic.agent.monitor.TrafficCamera;
import es.csic.iiia.normlab.traffic.normsynthesis.TrafficNormSynthesisSettings;
import es.csic.iiia.nsm.IncorrectSetupException;
import es.csic.iiia.nsm.NormSynthesisMachine;
import es.csic.iiia.nsm.config.NormSynthesisSettings;
import es.csic.iiia.nsm.metrics.NormSynthesisMetrics;
import es.csic.iiia.nsm.norm.Norm;
import es.csic.iiia.nsm.norm.NormativeSystem;

/**
 * 
 * @author "Javier Morales (jmorales@iiia.csic.es)"
 *
 */
public class TrafficNSExample2_NSAgent implements TrafficNormSynthesisAgent {

	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------

	private NormSynthesisMachine nsm;
	private NormSynthesisSettings nsmSettings;
	private NormativeSystem normativeSystem;
	private List<Norm> addedNorms;
	private List<Norm> removedNorms;

	//---------------------------------------------------------------------------
	// Methods
	//---------------------------------------------------------------------------

	/**
	 * Constructor of the example
	 */
	public TrafficNSExample2_NSAgent(List<TrafficCamera> cameras) {
		
		this.normativeSystem = new NormativeSystem();
		
		/* Create lists to control additions and
		 * removals of the normative system */
		this.addedNorms = new ArrayList<Norm>();
		this.removedNorms = new ArrayList<Norm>();
		
		/* Create norm synthesis settings */
		this.nsmSettings = new TrafficNormSynthesisSettings();
		
		/* Create norm synthesis machine */
		this.nsm = new NormSynthesisMachine(nsmSettings, null, null, true);
		
		/* Set the norm synthesis strategy */
		TrafficNSExample2_NSStrategy strategy =	new TrafficNSExample2_NSStrategy(this.nsm);
		this.nsm.useStrategy(strategy);
	}

	/**
	 * Called at every tick of the simulation
	 * 
	 * @throws IncorrectSetupException 
	 */
	public void step() throws IncorrectSetupException {
		this.addedNorms.clear();
		this.removedNorms.clear();

		/* Execute strategy and obtain new normative system */
		NormativeSystem newNormativeSystem = nsm.executeStrategy();

		/* Check norm additions to the normative system */ 
		for(Norm norm : newNormativeSystem) {
			if(!normativeSystem.contains(norm)) {
				this.normativeSystem.add(norm);
				this.addedNorms.add(norm);
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public NormSynthesisMetrics getMetrics() {
		return this.nsm.getNormSynthesisMetrics();
	}

	/**
	 * 
	 * @return
	 */
	public List<Norm> getAddedNorms() {
		return this.addedNorms;
	}

	/**
	 * 
	 * @return
	 */
	public List<Norm> getRemovedNorms() {
		return this.removedNorms;
	}

	/**
	 * 
	 * @return
	 */
	public NormativeSystem getNormativeSystem() {
		return normativeSystem;
	}

	/**
	 * 
	 * @return
	 */
	public NormSynthesisMachine getNormSynthesisMachine() {
		return this.nsm;
	}
}
