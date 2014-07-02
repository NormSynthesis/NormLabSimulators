package es.csic.iiia.normlab.traffic.config;

import es.csic.iiia.nsm.config.Goal;

/**
 * 
 * @author "Javier Morales (jmorales@iiia.csic.es)"
 *
 */
public class Gcols extends Goal {

	/**
	 * 
	 */
	@Override
  public String getName() {
	  return "Gcols";
  }
	
	/**
	 * 
	 */
	public String getDescription()	{
		return "Avoid collisions between cars";
	}
}