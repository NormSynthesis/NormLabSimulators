package es.csic.iiia.normlab.traffic.agent;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.engine.environment.RunEnvironment;
import es.csic.iiia.normlab.traffic.agent.monitor.TrafficCamera;
import es.csic.iiia.normlab.traffic.custom.MySecondStrategy;
import es.csic.iiia.normlab.traffic.metrics.TrafficMetrics;
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
import es.csic.iiia.nsm.strategy.NormSynthesisStrategy;

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
	private NormSynthesisSettings nsSettings;
	private NormativeSystem normativeSystem;
	private List<Norm> addedNorms;
	private List<Norm> removedNorms;
	private TrafficMetrics nsMetrics;

	//---------------------------------------------------------------------------
	// Methods
	//---------------------------------------------------------------------------

	/**
	 * 
	 */
	public DefaultTrafficNormSynthesisAgent(List<TrafficCamera> cameras,
			PredicatesDomains predDomains, DomainFunctions dmFunctions, 
			NormSynthesisSettings nsSettings, long randomSeed) {

		this.nsSettings = nsSettings;
		
		this.normativeSystem = new NormativeSystem();
		this.addedNorms = new ArrayList<Norm>();
		this.removedNorms = new ArrayList<Norm>();
		
		/* 1. Create norm synthesis machine */
		this.nsm = new NormSynthesisMachine(nsSettings, predDomains,
				dmFunctions, !RunEnvironment.getInstance().isBatch(), randomSeed);

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
	public void step(long timeStep) throws IncorrectSetupException {
		double startTime, finishTime, compTime;
		
		this.addedNorms.clear();
		this.removedNorms.clear();

		/* Save time just before executing the strategy */
		startTime = System.nanoTime();
		
		/* Execute strategy and obtain new normative system */
		NormativeSystem newNormativeSystem = nsm.executeStrategy(timeStep);

		/* Compute elapsed time during the strategy execution */
		finishTime = System.nanoTime();
		compTime = (finishTime - startTime) / 1000000;
		
		this.nsm.getNormSynthesisMetrics().addNewComputationTime(compTime);
		
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
		NormGeneralisationMode genMode = nsSettings.getNormGeneralisationMode();
		int genStep = nsSettings.getNormGeneralisationStep();
		NormSynthesisStrategy.Option option = null;

		this.nsMetrics = new TrafficMetrics(nsm);
		
		/* Finally, set norm synthesis strategy */
		switch(TrafficNormSynthesisSettings.NORM_SYNTHESIS_STRATEGY) {

		case 0:
			
			/* Create and setup custom norm synthesis strategy */
			NormSynthesisStrategy strategy = this.createCustomNormSynthesisStrategy();
			this.nsm.setup(strategy, nsMetrics, null, null);
			return;
			
		case 1:			option = NormSynthesisStrategy.Option.IRON;				break;
		case 2:			option = NormSynthesisStrategy.Option.SIMON;			break;
		case 3:			option = NormSynthesisStrategy.Option.LION;				break;
		}

		/* Setup predefined norm synthesis strategy */
		this.nsm.setup(option, genMode, genStep, this.nsMetrics, null, null);
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
	 * Sets a custom norm synthesis strategy
	 */
	protected NormSynthesisStrategy createCustomNormSynthesisStrategy() {
		return new MySecondStrategy(nsm);
	}
}
