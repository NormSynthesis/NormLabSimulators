package es.csic.iiia.normlab.traffic.map;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ejml.simple.SimpleMatrix;

import es.csic.iiia.normlab.traffic.car.context.TrafficStateCodifier;
import es.csic.iiia.normlab.traffic.car.context.TrafficStateManager;
import es.csic.iiia.normlab.traffic.utils.Direction;
import repast.simphony.space.grid.GridPoint;

/**
 * 
 * @author javi
 *
 */
public class TrafficMatrix {

	/**
	 * 
	 */
	private SimpleMatrix matrix;
		
	/**
	 * 
	 * @param rows
	 * @param cols
	 */
	public TrafficMatrix(int rows, int cols)
	{
		this.matrix = new SimpleMatrix(rows, cols);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getNumRows()
	{
		return this.matrix.numRows();
	}
	
	/**
	 * 
	 * @return
	 */
	public int getNumCols()
	{
		return this.matrix.numCols();
	}
	
	/**
	 * 
	 * @param x
	 * @return
	 */
	public String get(int element)
	{
		String codDesc = Long.toBinaryString((long)this.matrix.get(element));
		return StringUtils.leftPad(codDesc, 63, '0');
	}
	
	/**
	 * 
	 * @param value
	 */
	public void set(String codDesc)
	{
		this.matrix.set(Long.valueOf(codDesc,2));
	}
	
	/**
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public String get(int row, int col)
	{
		String codDesc = Long.toBinaryString((long)this.matrix.get(row, col));
		return StringUtils.leftPad(codDesc, 63, '0'); 
	}
	
	/**
	 * 
	 * @param value
	 */
	public void set(int row, int col, String codDesc)
	{
		this.matrix.set(row, col, Long.valueOf(codDesc, 2));
	}
	
	/**
	 * 
	 * @param startRow
	 * @param stopRow
	 * @param startColumn
	 * @param stopColumn
	 * @return
	 */
	public SimpleMatrix getSubMatrix(int startRow, int stopRow, int startColumn, int stopColumn)
	{
		return this.matrix.extractMatrix(startRow, stopRow, startColumn, stopColumn);
	}
	
	/**
	 * 
	 * @param subMatrix
	 */
	public void set(SimpleMatrix matrix)
	{
		this.matrix = matrix;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getNumElements()
	{
		return this.matrix.getNumElements();
	}
		
	/**
	 * Returns the position of the specified agent
	 */
	public GridPoint getCarPosition(long carId)
	{
		int rows = this.getNumRows();
		int cols = this.getNumCols();
		
		for(int row=0; row<rows; row++)
		{
			for(int col=0; col<cols; col++)
			{
				String codDesc = this.get(row, col);
				List<Long> ids = TrafficStateManager.getCarIds(codDesc);
				
				for(Long id : ids)
				{
					if(id == carId) {
						return new GridPoint(col, row);
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param carId
	 * @return
	 */
	public Direction getCarHeading(long carId)
	{
		for(int e=0; e<this.getNumElements(); e++)
		{
			String codDesc = this.get(e);
			List<Long> ids = TrafficStateManager.getCarIds(codDesc);
			
			for(Long id : ids)
			{
				if(id == carId) {
					return TrafficStateManager.getCarHeading(id, codDesc);
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 */
	public void clear()
	{
		this.matrix.set(0);
	}
	
	/**
	 * 
	 * @param otherMatrix
	 */
	public void copy(TrafficMatrix otherMatrix)
	{
		this.matrix = otherMatrix.matrix.copy();
	}
	
	/**
	 * 
	 * @return
	 */
	public int getDistance(TrafficMatrix otherMatrix)
	{
		int diffCount = 0;
		for(int x=0; x<this.matrix.getNumElements(); x++)
		{
			if(this.matrix.get(x) != otherMatrix.matrix.get(x))
				diffCount++;
		}
		return diffCount;		
	}
	
	/**
	 * 
	 */
	public String toString()
	{
		String s = "";
		int rows = this.getNumRows();
		int cols = this.getNumCols();
		
		for(int row=0; row<rows; row++)
		{
			for(int col=0; col<cols; col++)
			{
				String codDesc = this.get(row, col);
				s += TrafficStateCodifier.decodify(codDesc);
			}
			if(row<rows-1)
				s += "\n";
		}
		return s;
	}
}
