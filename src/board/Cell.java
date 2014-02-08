package board;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents a single cell in a Sudoku puzzle
 * Each cell has links to its box, row, and column, as well as its grid 
 * @author james
 */
public class Cell {

	private Box box;
	private Line column;
	private Grid grid;
	private int position;
	private ArrayList<Integer> possibilities;
	private Line row;
	private int value;

	/**CONSTRUCTOR
	 * Instantiates a new cell object with the given position, as a member of 
	 * the given grid
	 * @param position the zero denominated integer position of the grid
	 * @param grid the Grid object that this Cell is a member of 
	 */
	public Cell(int position, Grid grid) {
		this.position = position;
		this.grid = grid;

		this.possibilities = new ArrayList<Integer>(Arrays.asList(0,1,2,3,4,5,6,7,8));
	}

	/**OVERRIDE:<br>
	 * Determines whether the given cell is equivalent to this cell
	 * @param cell the cell object for comparison
	 * @return true if the two cells share all same related values, 
	 * not identical objects  
	 */
	public boolean equals(Cell cell)
	{
		return (getPosition() == cell.getPosition()) 
				&& (getValue() == cell.getValue()) 
				&& (getPossibilities().equals(cell.getPossibilities()));
	}

	/**ACCESSOR:<br>
	 * Returns this Cell's Box object
	 * @return Box that this cell belongs to
	 */
	public Box getBox()
	{
		return this.box;
	}

	/**ACCESSOR:<br>
	 * Returns this Cell's Column object
	 * @return Line of the column that this cell belongs to
	 */
	public Line getColumn()
	{
		return this.column;
	}

	/**ACCESSOR:<br>
	 * Returns this Cell's Grid object pointer
	 * @return Grid that this cell belongs to
	 */
	public Grid getGrid()
	{
		return this.grid;
	}

	/**ACCESSOR:<br>
	 * Returns this Cell's Box object
	 * @return Box that this cell belongs to
	 */
	public SparseArrayIter<Cell> getNeighbors()
	{
		ArrayList<Cell> allNeighbors= new ArrayList<Cell>();

		//add all neighbors including dupes
		allNeighbors.addAll(getBox().getCells());
		allNeighbors.addAll(getRow().getCells());
		allNeighbors.addAll(getColumn().getCells());
		SparseArrayIter<Cell> neighbors = new SparseArrayIter<Cell>();

		//curate the list to remove duplicates
		for (Cell cell : allNeighbors)    {
			if (cell.getPosition() != this.getPosition()) {
				neighbors.put(cell.getPosition(), cell);
			}
		}

		return neighbors;
	}

	/**ACCESSOR:<br>
	 * Returns the map of each of the non-zero values of neighbors of this Cell
	 * mapped to the neighbors position  
	 * @return SparseArrayIter mapping neighboring Cell's positions to 
	 * their values
	 */
	public SparseArrayIter<Integer> getNeighborsValues() {
		SparseArrayIter<Cell> neighbors = getNeighbors();
		SparseArrayIter<Integer> neighborsValues= new SparseArrayIter<Integer>();

		for (Cell cell : neighbors) {
			if (cell.getValue() != 0) {
				neighborsValues.put(cell.getValue(), cell.getPosition());
			}
		}

		return neighborsValues;
	}

	/**ACCESSOR:<br>
	 * Returns the position of this cell, zero-denominated to 80
	 * @return the position of this cell
	 */
	public int getPosition() {
		return this.position;
	}

	/**ACCESSOR:<br>
	 * Returns the possible values this cell could assume
	 * @return a ArrayList of the possible values this cell could assume
	 */
	public ArrayList<Integer> getPossibilities() {
		return this.possibilities;
	}

	/**ACCESSOR:<br>
	 * Returns this cell's Row
	 * @return the Line representing the Row of this Cell
	 */
	public Line getRow() {
		return this.row;
	}

	/**ACCESSOR:<br>
	 * Returns this cell's value
	 * @return the value of this Cell
	 */
	public int getValue() {
		return this.value;
	}

	/**INITALIZER:<br>
	 * Sets this Cell's Box the the given Box
	 * @param box Box to set this cells Box field to
	 */
	public void setBox(Box box) {
		this.box = box;
	}

	/**INITALIZER:<br>
	 * Sets this Cell's column the the given Line
	 * @param col Line to set this Cell's column to
	 */
	public void setColumn(Line col) {
		this.column = col;
	}

	/**INITALIZER:<br>
	 * Sets this Cell's Grid the the given Grid
	 * @param grid Grid to set this cells Grid field to
	 */
	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	/**INITALIZER:<br>
	 * Sets this Cell's position the the given argument
	 * @param position int to set this cells position field to
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**INITALIZER:<br>
	 * Sets this Cell's possibilities the the given SparseArrayIter
	 * @param possibilities possibilities to set this cell's possibilities field to
	 */
	public void setPossibilities(ArrayList<Integer> possibilites) {
		this.possibilities = new ArrayList<Integer> (possibilites);
	}

	/**INITALIZER:<br>
	 * Sets this Cell's row to the the given Line
	 * @param col Line to set this Cell's row to
	 */
	public void setRow(Line row) {
		this.row = row;
	}

	/**INITALIZER:<br>
	 * Sets this Cell's value to the given value if valid, clears its
	 * possibilities, and updates all neighbors  
	 * @param val Value to try to set this Cell's value to
	 */
	public void setValue(int val) {
		if ((val != 0) && (getPossibilities().contains(val))) {
			this.possibilities.clear();
			this.value = val;
			updateNeighborsPossibilities(val);
			getGrid().incrementFilled();
		}
	}

	/**OVERRIDE:<br>
	 * Returns a the value of this cell as a String
	 * @return String value of this cell
	 */
	public String toString() {
		return value + "";
	}

	/**MODIFIES:<br>
	 * Updates each of this Cell's neighbors to no longer regard the given 
	 * val as a possibility  
	 * @param val value to remove from each of this Cell's neighbors 
	 * possible values 
	 */
	public void updateNeighborsPossibilities(int val) {
		for (Cell cell : getNeighbors()) {
			cell.updatePossibility(val);
		}
	}

	/**MODIFIES:
	 * Iterates through this Cell's neighbor possibilities to remove this 
	 * Cell's value from each of it's neighbors 
	 */
	public void updatePossibilites() {
		for (Integer val : getNeighborsValues().keySet()) {
			updatePossibility(val);
		}
	}

	/**MODIFIES
	 * Removes the given possibility from this Cell's list of possibilities
	 * @param val value to remove from the possibilities, cast to an 
	 * Integer so as to play nice with ArrayLists remove method
	 */
	public void updatePossibility(Integer val) {
		if (this.possibilities.contains(val)) {
			this.possibilities.remove(val);
			if (this.possibilities.size() == 1) {
				setValue(this.possibilities.get(0));
			}
		}
	}
}

