package es.csic.iiia.normlab.traffic.utils;

/**
 * CloneablePublic - Helper interface to create deep clones
 *
 * @author Jan Koeppen (jankoeppen@gmx.net)
 *
 */

public interface CloneablePublic<T> {

	public T getClone(boolean deepCopy);
}
