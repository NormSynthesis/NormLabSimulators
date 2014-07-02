package es.csic.iiia.normlab.traffic.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import repast.simphony.space.grid.GridPoint;

/**
 * Matrix - General class with basic matrix operations heavily used throughout application
 *
 * @author Jan Koeppen (jankoeppen@gmx.net)
 *
 */

public class Matrix<T> implements CloneablePublic<Matrix<T>>{
	public enum enumRotation{
		rot0, rot90, rot180, rot240;

		public enumRotation getInverse(){
			switch(this){
			case rot90: return rot240;
			case rot180: return rot180;
			case rot240: return rot90;
			}
			return rot0;
		}

		public int steps90Degree(){
			switch(this){
			case rot90: return 1;
			case rot180: return 2;
			case rot240: return 3;
			}
			return 0;
		}

		public static enumRotation from90DegreeSteps(int steps){
			switch(steps%4){
			case 1: return rot90;
			case 2: return rot180;
			case 3: return rot240;
			}
			return rot0;
		}

		public static int to90DegreeSteps(enumRotation rot){
			switch(rot){
			case rot90: return 1;
			case rot180: return 2;
			case rot240: return 3;
			}
			return 0;
		}
	}

	private int dimX = 0, dimY =0;
	protected ArrayList<List<T>> matrix = null;
	String name = null;

	public Matrix(int dimX, int dimY, String name) {
		this.dimX = dimX;
		this.dimY = dimY;
		this.name = name;
		matrix = new ArrayList<List<T>>();
		for(int y = 0; y<dimY; y++){
			ArrayList<T> xList = new ArrayList<T>();
			for(int x = 0; x<dimX; x++){
				xList.add(null);
			}
			matrix.add(xList);
		}
	}
	
	private void setMatrix(ArrayList<List<T>> m){
		this.matrix = m;
	}

	public T get(int x, int y){
		List<T> l = matrix.get(y);
		return l.get(x);
	}

	public void set(int x, int y, T val){
		List<T> l = matrix.get(y);
		l.set(x, val);
	}

	public Matrix<T> getRotateMatrix(enumRotation rot){
		Matrix<T> m = this.getClone(false);
		m.rotate(rot);
		return m;
	}

	public void rotate(enumRotation rot){
		Matrix<T> m = this;
		for(int i = 0;i<rot.steps90Degree();i++)
			m=m.rot90();
		this.matrix = m.matrix;
	}

	// TODO only rotates square matrices at the moment :-P
	private Matrix<T> rot90(){
		Matrix<T> m = new Matrix<T>(dimY,dimX,name+"90");
		for(int x = 0; x<dimX; x++)
			for(int y = 0; y<dimY; y++)
				m.set(x, y, this.get(y,dimX-1-x));
		return m;
	}

	// create identical copy
	@SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
	public Matrix<T> getClone(boolean deepCopy){
		ArrayList<List<T>> cloneList = new ArrayList<List<T>>();
		for(int y = 0; y<dimY; y++){
			Iterator<T> origRow = matrix.get(y).iterator();
			ArrayList<T> cloneRow = new ArrayList<T>();
			while(origRow.hasNext()){
				T obj = origRow.next();
				if(deepCopy){
					if(obj instanceof CloneablePublic)
						obj = (T)((CloneablePublic)obj).getClone(true);
				}
				cloneRow.add(obj);
			}

			cloneList.add(cloneRow);
		}
		Matrix clone = new Matrix<T>(dimX,dimY,name);
		clone.setMatrix(cloneList);
		return clone;
	}

	public Iterator<List<T>> rowIterator(){
		return matrix.iterator();
	}

	public String toString(){
		String s = "";
		Iterator<List<T>> matIt = matrix.iterator();
		while(matIt.hasNext()){
			Iterator<T> rowIt = matIt.next().iterator();
			while(rowIt.hasNext()){
				T elem = rowIt.next();
				if(elem==null)
					s+="*";
				else
					s+=elem.toString();
			}
			if(matIt.hasNext())
				s+="\n";
		}
		return s;
	}

	public String toStringInv(){
		String s = "Matrix plot '" + name + "'(inverse):\n";
		String matS = "", rowS = "";
		Iterator<List<T>> matIt = matrix.iterator();
		while(matIt.hasNext()){
			Iterator<T> rowIt = matIt.next().iterator();
			rowS="";
			while(rowIt.hasNext()){
				T elem = rowIt.next();
				if(elem==null)
					rowS+="*";
				else
					rowS+=elem.toString();
			}
			matS = rowS+"\n"+matS;
		}
		return s + matS;
	}

	public void setName(String name){
		this.name = name;
	}

	public void clear(){
		Iterator<List<T>> matIt = matrix.iterator();
		while(matIt.hasNext()){
			ArrayList<T> row = (ArrayList<T>)matIt.next();
			row.clear();
			for(int i = 0; i<dimX;i++)
				row.add(null);
		}
	}

	public List<List<T>> getMatrix(){
		return matrix;
	}

	public int getDimX() {
		return dimX;
	}

	public int getDimY() {
		return dimY;
	}

	public String getName() {
		return name;
	}

	public Matrix<T> getSubMap(GridPoint x1, GridPoint x2){
		Matrix<T> subMap = new Matrix<T>(x2.getX()-x1.getX()+1, x2.getY()-x1.getY()+1,this.name+".submap");
		for(int y = 0;y<subMap.getDimY();y++)
			for(int x = 0;x<subMap.getDimX();x++)
				subMap.set(x, y, this.get(x+x1.getX(), y+x1.getY()));
		return subMap;
	}

	public GridPoint getPositionOf(T obj){
		Iterator<List<T>> matIt = matrix.iterator();
		int y = 0;
		while(matIt.hasNext()){
			int x = 0;
			Iterator<T> rowIt = matIt.next().iterator();
			while(rowIt.hasNext()){
				T elem = rowIt.next();
				if(elem!=null)
					if(elem.equals(obj))
						return new GridPoint(x,y);
				x++;
			}
			y++;
		}
		return null;
	}

	public ArrayList<T> getAllElements(){
		ArrayList<T> ret = new ArrayList<T>();

		Iterator<List<T>> matIt = matrix.iterator();
		while(matIt.hasNext()){
			Iterator<T> rowIt = matIt.next().iterator();
			while(rowIt.hasNext()){
				T elem = rowIt.next();
				if(elem!=null)
					ret.add(elem);
			}
		}
		return ret;
	}

	@SuppressWarnings("unused")
  public void fill(T value){
		for(List<T> l:this.matrix)
			for(T v:l)
				v=value;
	}
	
	/**
	 * 
	 * @return
	 */
	public Matrix<T> transpose() {
		Matrix<T> tMatrix = new Matrix<T>(this.getDimY(), this.getDimX(), this.name);
		
		for(int x = 0; x<dimX; x++) {
			for(int y = 0; y<dimY; y++) {
				tMatrix.set(y, x, this.get(x,y));
			}
		}			
		return tMatrix;
	}
	
	/**
	 * 
	 * @return
	 */
	public Matrix<T> flipHorizontal() {
		Matrix<T> fMatrix = new Matrix<T>(this.getDimX(), this.getDimY(), this.name);
		int maxX = (int)(dimX/2);
		
		for(int y = 0; y<dimY; y++) {
			for(int x = 0; x<maxX; x++) {
				int fx = (dimX-1-x);
				fMatrix.set(x, y, this.get(fx, y));
				fMatrix.set(fx, y, this.get(x,y));
			}
			if((dimX%2) > 0) {
				fMatrix.set(maxX, y, this.get(maxX,y));
			}
		}
		return fMatrix;
		
	}
}
