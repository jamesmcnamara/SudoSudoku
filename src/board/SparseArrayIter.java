package board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import android.util.SparseArray;

/**CLASS<br>
 * Extends the functionality of the Android Map implementation, SparseArray
 * to include iterability and cloning
 * @author james
 *
 * @param <T> The type of objects stored in this map
 */
public class SparseArrayIter<T>
extends SparseArray<T> implements Iterable<T> {

	/**CONSTRUCTOR:
	 * Default super constructor
	 */
	public SparseArrayIter() {
		super();
	}
	
	/**CONSTRUCTOR:
	 * Generates a deep clone of the given SparseArrayIter
	 * @param clone a SparseArrayIter to clone
	 */
	public SparseArrayIter(SparseArrayIter<T> clone) {
		for (T t : clone) {	
			put(clone.keyAt(clone.indexOfValue(t)), t);
		}
	}
	
	/**IMPLEMENTS:<br>
	 * Returns an iterator for this SparseArray
	 */
	public Iterator<T> iterator() {
		return this.values().iterator();
	}

	/**EFFECT:
	 * Returns a Collection of this SparseArrayIters keys
	 * @return a Collection of this SparseArrayIters keys
	 */
	public Collection<Integer> keySet()	{		
		ArrayList<Integer> keys = new ArrayList<Integer>();
		for (int i = 0; i < size(); i++) { 
			keys.add(Integer.valueOf(keyAt(i)));
		}
		return keys;
	}

	/**EFFECT:
	 * Returns a Collection of this SparseArrayIters values
	 * @return a Collection of this SparseArrayIters values
	 */
	public Collection<T> values() {
		ArrayList<T> values= new ArrayList<T>();
		for (T t : this) {
			values.add(t);
		}
		return values;
	}
}
