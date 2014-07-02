package es.csic.iiia.normlab.onlinecomm.distribution;

import java.util.List;

import org.apache.commons.math3.distribution.GammaDistribution;

import es.csic.iiia.normlab.onlinecomm.content.IContent;

/**
 * Gamma distribution
 * 
 * @author Iosu Mendizabal
 * 
 */
public class Gamma {
	
	private double k = 1;
	private double theta = 0;
	private double sample = 0;
	private int num = 0;
	private int size;

	/**
	 * This method calculates a number given a List of contents. It made this using Gamma distribution.
	 * 
	 * @param list
	 * 			List from its size we want to extract the number.
	 * @return
	 * 			A number that is calculated with gamma distribution and is inside of the list size.
	 */
	
	public int getNum(List<IContent> list) {	
		size = list.size();
		//If the size of the list is less than two the number had to be 0
		if(size < 2){
			num = 0;
		}else{
			theta = list.size()/2;
			GammaDistribution gd = new GammaDistribution(k, theta);
			do{
				sample = gd.sample();
			}while(sample > theta);
			
			num = roundNumber(sample);
		}
		return num;
	}
	
	
	/**
	 * This method round the given number to the floor.
	 * 
	 * @param numero
	 * 			Number to round.
	 * @return
	 * 			The rounded number.
	 */
	private int roundNumber(double numero) {
		return (int) Math.floor(numero);
	}
}
