package es.csic.iiia.normlab.onlinecomm.context;

import java.util.LinkedList;

public class CircularFifoQueue<E> extends LinkedList<E> {

	/**
	 * 
	 */
  private static final long serialVersionUID = -3034494004384802941L;

  private long size;
  
  /**
   * 
   * @param size
   */
  public CircularFifoQueue(long size) {
  	this.size = size;
  }
  
  /**
   * 
   */
  @Override
  public boolean add(E element) {
  	boolean added = super.add(element);
  	
  	if(added) {
	  	if(this.size() > size) {
	  		this.pop();
	  	}
  	}
  	return added;
  }
}
