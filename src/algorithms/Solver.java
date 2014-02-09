package algorithms;

import board.Cell;
import board.Grid;
import board.SparseArrayIter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**CLASS:<br>
 * Multithreaded processor which creates a new thread of execution for 
 * each of the possible values the most constrained cell could assume,
 * and follows each of those threads until they reach a solution or
 * violate the representation invariants
 * @author james
 *
 */
public class Solver
extends Thread {
	boolean continueSearch;
	boolean found;
	Grid grid;
	Solver master;
	Solver parent;
	int threadCount;
	int threadID;

	/**CONSTRUCTOR:<br>
	 * Creates a new solver thread to work on the given Grid and alert the
	 * given parent with the solution found or if a there is a flaw in the 
	 * puzzle 
	 * @param grid Grid to work on
	 * @param parent Parent thread or null if it is the master
	 */
	public Solver(Grid grid, Solver parent)	{
		this.grid = grid;
		this.continueSearch = true;
		this.parent = parent;
		if (parent == null) {
			this.master = this;
			this.found = false;
			this.threadCount = 1;
			this.threadID = 1;
		}
		else {
			this.threadID = this.master.threadCount;
			incrementThreadCount();
			this.master = parent.getMaster();
		}
	}

	/**EFFECT:<br>
	 * Alerts all threads of execution that this thread has 
	 * found the solution to the puzzle, stores the solution in the master
	 * thread 
	 * @param grid the Solution grid for the current puzzle
	 */
	void foundItBitches(Grid paramGrid)	{
		this.master.grid = paramGrid;
		this.master.found = true;
	}

	/**ACCESSOR:<br>
	 * Returns the master thread's grid, presumably after
	 * the solution has been stored there
	 * @return Grid that is stored in the master thread
	 */
	public Grid getGrid() {
		return this.master.grid;
	}

	/**ACCESSOR:<br>
	 * Return the master thread
	 * @return the master thread
	 */
	Solver getMaster() {
		return this.master;
	}

	/**ACCESSOR:<br>
	 * Returns this solver's thread id
	 * @return this solver's thread id
	 */
	public int getThreadID() {
		return this.threadID;
	}

	/**EFFECT:<br>
	 * Determines whether this grid has been solved
	 * @return true if any thread has found the solution
	 */
	boolean hasBeenFound() {
		return this.master.found;

	}

	/**EFFECT:<br>
	 * Increments the thread count
	 */
	public void incrementThreadCount() {
		this.master.threadCount++;
	}

	/**EFFECT:<br>
	 * Signals that all threads of execution spawned with
	 * this thread as a parent should stop searching 
	 */
	public void killThreadAndChildren() {
		this.continueSearch = false;
	}

	/**EFFECT:<br>
	 * It's all in the name 
	 * @return true if the parent thread signals to continue execution
	 */
	boolean parentSignalsContinue()	{
		return this.parent.continueSearch;
	}

	/**
	 * 
	 */
	public void run() {
		if (parentSignalsContinue()) {
			int minPossibilities = 9;
			Cell minCell = null;
			for (Cell cell : grid.getCells()) {
				if (cell.getPossibilities().size() < minPossibilities) {
					minPossibilities = cell.getPossibilities().size();
					minCell = cell;
				}
			}
			
			Grid localGrid = new Grid(this.grid);
			((Cell)localGrid.getCells().get(((Cell)localObject2).getPosition())).setValue(localInteger.intValue());
			new Solver(localGrid, this).start();
			if (!localIterator2.hasNext())
			{
				return;
				label150:
					Cell localCell = (Cell)localIterator1.next();
				int j = localCell.getPossibilities().size();
				if ((j == 0) || (j >= i)) {
					break;
				}
				i = j;
				localObject2 = localCell;
				localObject1 = localCell.getPossibilities().values();
				break;
			}
		}
	}

	public void start()
	{
		AlgorithmSuite.run(this.grid, this);
	}
}


/* Location:           C:\Users\HAL\Documents\sudosudoku\com.example.helloopencv_1.0.apk\classes_dex2jar.jar
 * Qualified Name:     algorithms.Solver
 * JD-Core Version:    0.7.0.1
 */