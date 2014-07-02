package es.csic.iiia.normlab.traffic.utils;

import java.util.ArrayList;
import java.util.Collections;

import es.csic.iiia.nsm.norm.Norm;
import es.csic.iiia.nsm.norm.NormativeSystem;

/**
 * 
 * @author Javi
 *
 */
public class StringList extends ArrayList<String> {

	/**
   * 
   */
  private static final long serialVersionUID = 6671884896416835092L;

  /**
   * 
   */
  public StringList() {
  	super();
  }
  
  /**
   * 
   * @param norms
   */
  public StringList(NormativeSystem norms) {
  	super();
  	
  	for(Norm norm : norms)
  	{
  		this.add(norm.getDescription());
  	}
  }
  
	/**
	 * 
	 * @return
	 */
	public boolean equals(StringList otherList) {
		
		if(this.size() != otherList.size())
			return false;
		
		for(String s : this)
		{
			boolean exists = false;
			
			for(String other : otherList)
			{
				if(s.equals(other))
					exists = true;
			}
			if(!exists)
				return false;
		}
	
		// Lists are equal
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	public boolean add(String s) {
		boolean flag = super.add(s);
		
		// Sort
		Collections.sort(this);
		
		return flag;
	}
}
