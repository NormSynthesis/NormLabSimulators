package es.csic.iiia.normlab.onlinecomm.nsm;

import es.csic.iiia.nsm.config.Goal;

/**
 * 
 * Class to describe the Goals defined in the enumeration.
 * 
 * @author Iosu Mendizabal
 *
 */
public class GComplaints extends Goal {

	/**
	 * Method to return a description of the Goals.
	 * 
	 * @return Description.
	 */
	public String getDescription() {
		return "To avoid user complaints";
	}

	/**
	 * 
	 */
	@Override
	public String getName() {
		return "GComplaints";
	}

}
