package es.csic.iiia.normlab.onlinecomm.agents.norms;

import java.util.Collections;

import repast.simphony.engine.environment.RunEnvironment;
import es.csic.iiia.normlab.onlinecomm.context.ContextData;
import es.csic.iiia.normlab.onlinecomm.nsm.CommunityNormSynthesisSettings;
import es.csic.iiia.normlab.onlinecomm.nsm.agent.CommunityAgentAction;
import es.csic.iiia.normlab.onlinecomm.nsm.agent.CommunityAgentContext;
import es.csic.iiia.nsm.agent.language.PredicatesDomains;
import es.csic.iiia.nsm.agent.language.SetOfPredicatesWithTerms;
import es.csic.iiia.nsm.norm.Norm;
import es.csic.iiia.nsm.norm.reasoning.NormEngine;

/**
 * Reasoner for the agent. Contains the rule engine to do reasoning
 * 
 * @author Javier Morales (jmoralesmat@gmail.com)
 *
 */
public class CommunityAgentReasoner extends NormEngine {

	//------------------------------------------------------------
	// Attributes																															
	//------------------------------------------------------------

	/**
	 * The state of the reasoner
	 */
	private CommunityAgentReasonerState state;

	/**
	 * Template to define a car (and noCar) and its properties for JESS rules
	 */
	private Norm normToApply;
	private Norm normToViolate;
	private ContextData contextData;
	
	//---------------------------------------------------------------------------
	// Constructors
	//---------------------------------------------------------------------------

	/**
	 * 
	 * @param dmFunctions
	 */
	public CommunityAgentReasoner(PredicatesDomains predDomains,
			ContextData contextData) {
		super(predDomains);
		
		this.state = CommunityAgentReasonerState.NoNormActivated;
		this.contextData = contextData; 
	}

	//---------------------------------------------------------------------------
	// Methods
	//---------------------------------------------------------------------------

	/**
	 * Does reasoning to activate rules in base of the facts in the knowledge base 
	 */
	public CommunityAgentAction decideAction(CommunityAgentContext context) {
		this.state = CommunityAgentReasonerState.NoNormActivated;
		boolean violate;
		int violateProb;
		
		this.reset();

		// Add world facts
		SetOfPredicatesWithTerms predicates = context.getDescription();
		
//		CommunityNormSynthesisAgent.
//				getFactFactory().generatePredicates(context);
//		
//		context.getDescription();
//		
		this.addFacts(predicates);

		// Reason about applicable norms
		this.reason();

		// Remove random component of JESS
		Collections.sort(applicableNorms);

		// Obtain next supposed action to do according to the norm specification
		for(Norm n : applicableNorms){
			violate = false;

			if(RunEnvironment.getInstance().isBatch()){
				// Decide if applying the norm or not 
				violateProb = (int)(CommunityNormSynthesisSettings.SIM_NORM_VIOLATION_RATE * 100);
			}else{
				// Decide if applying the norm or not 
				violateProb = (int)(CommunityNormSynthesisSettings.SIM_NORM_VIOLATION_RATE * 100);
			}
			
			int num = this.contextData.nextIntRandom(100)+1;
			violate = (num <= violateProb) ? true : false;

			// Randomly choose if applying the norm or not. Case apply the norm
			if(violate){
				this.normToViolate = n;
				state = CommunityAgentReasonerState.NormWillBeViolated;
				
				return CommunityAgentAction.Upload;
			}else{
				this.normToApply = n;
				state = CommunityAgentReasonerState.NormWillBeApplied;

				return CommunityAgentAction.Nothing;
			}
		}
		// Let the facts base empty and return the action chosen by the agent
		applicableNorms.clear();

		return CommunityAgentAction.Upload;
	}
	
	/**
	 * Returns true if the last applicable norm was finally applied. False else
	 * 
	 * @return
	 */
	public CommunityAgentReasonerState getState() {
		return state;
	}

	/**
	 * Returns the last norm that has been applied by the car
	 * 
	 * @return
	 */
	public Norm getNormToApply() {
		return this.normToApply;
	}

	/**
	 * Returns the last norm that has been violated by the car
	 * @return
	 */
	public Norm getNormToViolate() {
		return this.normToViolate;
	}
}
