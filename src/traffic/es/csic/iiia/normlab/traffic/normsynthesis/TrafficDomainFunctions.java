package es.csic.iiia.normlab.traffic.normsynthesis;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import repast.simphony.space.grid.GridPoint;
import es.csic.iiia.normlab.traffic.TrafficSimulator;
import es.csic.iiia.normlab.traffic.agent.monitor.TrafficView;
import es.csic.iiia.normlab.traffic.car.CarAction;
import es.csic.iiia.normlab.traffic.car.context.CarContext;
import es.csic.iiia.normlab.traffic.car.context.TrafficStateManager;
import es.csic.iiia.normlab.traffic.car.context.TrafficStateManager.StateType;
import es.csic.iiia.normlab.traffic.config.Gcols;
import es.csic.iiia.normlab.traffic.factory.CarContextFactory;
import es.csic.iiia.normlab.traffic.norms.tree.TrafficPredicateInconsistencies;
import es.csic.iiia.nsm.agent.AgentAction;
import es.csic.iiia.nsm.agent.AgentContext;
import es.csic.iiia.nsm.agent.language.PredicatesDomains;
import es.csic.iiia.nsm.agent.language.SetOfPredicatesWithTerms;
import es.csic.iiia.nsm.agent.language.TaxonomyOfTerms;
import es.csic.iiia.nsm.config.DomainFunctions;
import es.csic.iiia.nsm.config.Goal;
import es.csic.iiia.nsm.norm.Norm;
import es.csic.iiia.nsm.norm.generation.Conflict;
import es.csic.iiia.nsm.perception.View;
import es.csic.iiia.nsm.perception.ViewTransition;

/**
 * Class implementing the domain functions for the traffic scenario
 * 
 * @author "Javier Morales (jmorales@iiia.csic.es)"
 *
 */
public class TrafficDomainFunctions implements DomainFunctions {

	//---------------------------------------------------------------------------
	// Attributes
	//---------------------------------------------------------------------------

	private CarContextFactory carContextFactory;
	private PredicatesDomains predDomains;

	//---------------------------------------------------------------------------
	// Methods
	//---------------------------------------------------------------------------

	/**
	 * Constructor
	 */
	public TrafficDomainFunctions(PredicatesDomains predDomains,
			CarContextFactory carContextFactory) {
		
		this.carContextFactory = carContextFactory;
		this.predDomains = predDomains;
	}

	/**
	 * Returns true if a set of predicates with terms is consistent.
	 * 
	 * @param agentContext the set of predicates and terms
	 * 
	 * @return true if the set of predicates and terms is consistent
	 */
	@Override
	public boolean isConsistent(SetOfPredicatesWithTerms agentContext) {
		Map<Integer, List<String>> inconsistencies = TrafficPredicateInconsistencies.getAll();

		/* Check that the term in each predicate belongs to the predicate's domain */
		for(String predicate : agentContext.getPredicates()) {
			for(String term : agentContext.getTerms(predicate)) {
				TaxonomyOfTerms predDomain = 
						(TaxonomyOfTerms)this.predDomains.getDomain(predicate);

				if(!predDomain.contains(term)) {
					return false;
				}
			}
		}		
		/* Check semantic inconsistencies */
		for(Integer incKey : inconsistencies.keySet()) {
			List<String> inc = inconsistencies.get(incKey);
			int matches = 0;

			for(String p : agentContext.getPredicates()) {
				String term = agentContext.getTerms(p).get(0);

				if(term.toString().equals(inc.get(matches))) {
					matches++;
					if((matches + 1) > inc.size()) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Returns the local context of a reference car in a given view
	 * 
	 * @param carId the id of the reference car
	 * @param view the view in which the car perceives its local context
	 * 
	 * @return a description of the car's local context 
	 */
	@Override
	public AgentContext agentContextFunction(long carId, View view) {
		TrafficView tView = (TrafficView)view;
		CarContext context = carContextFactory.getCarContextIn(tView,
				carId, CarContext.Type.Front);

		return context;
	}

	/**
	 * Returns the actions that the reference car performed in the transition
	 * from one time step to another in the view stream
	 * 
	 * @param carId the id of the reference car
	 * @param viewStream the view stream in which the car performed the actions
	 * 
	 * @return the actions that the car performed
	 */
	@Override
	public List<AgentAction> agentActionFunction(long carId, 
			ViewTransition viewStream) {

		List<AgentAction> actions = new ArrayList<AgentAction>();

		/* Get the position of the car in the previous and the current view */
		TrafficView pView = (TrafficView)viewStream.getView(-1);
		TrafficView view = (TrafficView)viewStream.getView(0);
		GridPoint pPos = pView.getCarPosition(carId);
		GridPoint pos = view.getCarPosition(carId);

		/* If the car is not found in the previous time step, then it has moved 
		 * (since now it is perceived, and hence it has entered 
		 * into the sensor's scope) */
		if(pPos == null && pos != null) {
			actions.add(CarAction.Go);
		}

		/* If the car has not moved, then it performed action "Stop". 
		 * Else, it performed action "Go" */
		if(pPos.getX() == pos.getX() && pPos.getY() == pos.getY()) {
			actions.add(CarAction.Stop);
		}
		else {
			actions.add(CarAction.Go);
		}
		return actions;
	}

	/**
	 * Returns a list with the "new" conflicts in the view stream.
	 * That is, those conflicts that are not regulated by any norm
	 * 
	 * @return a list of the non regulated conflicts
	 */
	@Override
	public List<Conflict> getConflicts(Goal goal,	ViewTransition vTrans) {

		List<Conflict> conflicts = new ArrayList<Conflict>();
		View viewTimeT = vTrans.getView(0);
		TrafficView tfView = (TrafficView) viewTimeT;

		if(goal instanceof Gcols) {
			int numElems = tfView.getNumElements();

			for(int x=0; x<numElems; x++) {
				String desc = tfView.get(x);

				/* If there is a collision in the given position, get the id's
				 * of the cars in the collision and randomly choose one of them
				 * to generate norms for it */
				if(TrafficStateManager.getType(desc) == StateType.Collision) {

					List<Long> ids = TrafficStateManager.getCarIds(desc);
					List<Long> conflictingAgents = new ArrayList<Long>();

					/* Randomly choose the conflicting agent */
					//					Random rnd = Main.getRandom();
					//					conflictingAgents.add(ids.get(rnd.nextInt(ids.size())));
					conflictingAgents.addAll(ids);

					/* Generate new conflict, adding the onflicting agent */
					Conflict conflict = new Conflict(vTrans.getSensor(), viewTimeT,
							vTrans, conflictingAgents);
					conflicts.add(conflict);
					break;
				}
			}
		}
		return conflicts;
	}

	/**
	 * Returns true if the given car has collided in a view with any other car
	 * 
	 * @param view the view in which to check if the car has collided
	 * @param agentId the reference car 
	 * @param goal the goal 
	 * @return true if the given agent is in conflict in the view
	 */
	@Override
	public boolean hasConflict(View view, long agentId, Goal goal) {
		TrafficView tfView = (TrafficView) view;
		List<Long> agentIds;

		if(goal instanceof Gcols) {
			int numElems = tfView.getNumElements();
			for(int x=0; x<numElems; x++)
			{
				String desc = tfView.get(x);

				if(TrafficStateManager.getType(desc) == StateType.Collision ||
						TrafficStateManager.getType(desc) == StateType.ViolCollision) {

					agentIds = TrafficStateManager.getCarIds(desc);
					if(agentIds.contains(agentId))
						return true;
				}
			}
		}
		return false;
	}

	//---------------------------------------------------------------------------
	// Private methods
	//---------------------------------------------------------------------------

//	/**
//	 * Creates the predicate and their domains for the traffic scenario
//	 */
//	private void createPredicatesDomains() {
//
//		/* Predicate "left" domain */
//		TaxonomyOfTerms leftPredTaxonomy = new TaxonomyOfTerms("l");
//		leftPredTaxonomy.addTerm("*", 6);
//		leftPredTaxonomy.addTerm("<", 5);
//		leftPredTaxonomy.addTerm(">", 4);
//		leftPredTaxonomy.addTerm("-", 3);
//		leftPredTaxonomy.addRelationship("<", "*");
//		leftPredTaxonomy.addRelationship(">", "*");
//		leftPredTaxonomy.addRelationship("-", "*");
//
//		/* Predicate "front" domain*/
//		TaxonomyOfTerms frontPredTaxonomy = new TaxonomyOfTerms("f", leftPredTaxonomy);
//		frontPredTaxonomy.addTerm("^", 2);
//		frontPredTaxonomy.addRelationship("^", "*");
//
//		/* Predicate "right" domain*/
//		TaxonomyOfTerms rightPredTaxonomy = new TaxonomyOfTerms("r", leftPredTaxonomy);
//		rightPredTaxonomy.addTerm("w", 1);
//		rightPredTaxonomy.addRelationship("w", "*");
//
//		this.predDomains = new PredicatesDomains();
//		this.predDomains.addPredicateDomain("l", leftPredTaxonomy);
//		this.predDomains.addPredicateDomain("f", frontPredTaxonomy);
//		this.predDomains.addPredicateDomain("r", rightPredTaxonomy);
//	}

	/**
	 * 
	 */
	@Override
	public JPanel getNormDescriptionPanel(Norm norm) {
		//	  JPanel normDescPanel = new JPanel();
		//    ImageIcon icon = new ImageIcon("misc/traffic/icons/car.png");
		//    
		//    
		//    GroupLayout layout = new javax.swing.GroupLayout(normDescPanel);
		//    normDescPanel.setLayout(layout);
		//    
		//    JLabel lblIcon = new JLabel();
		//    lblIcon.setIcon(icon);
		//    
		//    layout.addLayoutComponent(lblIcon);
		//	  return normDescPanel;
		//	  

		JPanel panel;

		try {
			panel = new JPanel(new BorderLayout()) {

				private static final long serialVersionUID = 5978138542151276211L;

				final BufferedImage bi = ImageIO.read(new File("misc/traffic/icon/bluecar.png"));
				@Override
				public Dimension getPreferredSize() {
					return new Dimension(bi.getWidth(), bi.getHeight());
				}

				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					Graphics2D g2 = (Graphics2D) g;
					g2.rotate(Math.PI / 4, bi.getWidth() / 2, bi.getHeight() / 2);
					g2.drawImage(bi, 0, 0, null);
				}
			};
		}
		catch (IOException e) {
			return null;
		}

		return panel;
	}
}
