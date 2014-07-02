package es.csic.iiia.normlab.traffic.agent;

import java.util.ArrayList;
import java.util.List;

import es.csic.iiia.normlab.traffic.agent.monitor.TrafficCamera;
import es.csic.iiia.normlab.traffic.normsynthesis.TrafficDomainFunctions;
import es.csic.iiia.normlab.traffic.normsynthesis.TrafficNormSynthesisSettings;
import es.csic.iiia.nsm.IncorrectSetupException;
import es.csic.iiia.nsm.NormSynthesisMachine;
import es.csic.iiia.nsm.NormSynthesisMachine.NormGeneralisationMode;
import es.csic.iiia.nsm.agent.language.PredicatesDomains;
import es.csic.iiia.nsm.config.DomainFunctions;
import es.csic.iiia.nsm.config.NormSynthesisSettings;
import es.csic.iiia.nsm.metrics.NormSynthesisMetrics;
import es.csic.iiia.nsm.norm.Norm;
import es.csic.iiia.nsm.norm.NormativeSystem;

/**
 * 
 * @author "Javier Morales (jmorales@iiia.csic.es)"
 *
 */
public class DefaultTrafficNormSynthesisAgent
implements TrafficNormSynthesisAgent {

	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------

	private NormSynthesisMachine nsm;
	private NormSynthesisSettings nsmSettings;
	private DomainFunctions dmFunctions;
	private NormativeSystem normativeSystem;
	private List<Norm> addedNorms;
	private List<Norm> removedNorms;

	//---------------------------------------------------------------------------
	// Methods
	//---------------------------------------------------------------------------

	/**
	 * 
	 */
	public DefaultTrafficNormSynthesisAgent(List<TrafficCamera> cameras,
			PredicatesDomains predDomains) {

		this.normativeSystem = new NormativeSystem();
		this.addedNorms = new ArrayList<Norm>();
		this.removedNorms = new ArrayList<Norm>();
		this.nsmSettings = new TrafficNormSynthesisSettings();
		this.dmFunctions = new TrafficDomainFunctions();

		/* 1. Create norm synthesis machine */
		this.nsm = new NormSynthesisMachine(nsmSettings, predDomains,
				dmFunctions, true);
		
		/* 2. Add sensors to the monitor of the norm synthesis machine */
		for(TrafficCamera camera : cameras) {
			this.nsm.addSensor(camera);	
		}
		
		/* 3. Set the norm synthesis strategy */
		this.setNormSynthesisStrategy();
	}

	/**
	 * @throws IncorrectSetupException 
	 * 
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

		// Check norm removals
		for(Norm norm : normativeSystem) {
			if(!newNormativeSystem.contains(norm)) {
				this.removedNorms.add(norm);
			}
		}	

		// Remove norms from normative systems
		for(Norm norm : removedNorms) {
			this.normativeSystem.remove(norm);
		}
	}

	/**
	 * Sets the norm synthesis strategy
	 */
	protected void setNormSynthesisStrategy() {
		NormGeneralisationMode nGenMode = nsmSettings.getNormGeneralisationMode();
		int nGenStep = nsmSettings.getNormGeneralisationStep();

		/* Finally, set norm synthesis strategy */
		switch(TrafficNormSynthesisSettings.NORM_SYNTHESIS_STRATEGY) {

		case 0:
			this.setCustomNormSynthesisStrategy();
			break;

			/* IRON strategy */ 
		case 1:
			this.nsm.useIRONNormSynthesisStrategy();
			break;

			/* SIMON strategy */
		case 2:
			this.nsm.useSIMONNormSynthesisStrategy(nGenMode, nGenStep);
			break;
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

	/**
	 * 
	 * @return
	 */
	public DomainFunctions getDomainFunctions() {
		return this.dmFunctions;
	}

	/**
	 * Sets a custom norm synthesis strategy
	 */
	protected void setCustomNormSynthesisStrategy() {
		
	}
}
