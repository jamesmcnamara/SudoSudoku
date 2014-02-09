package board;

import java.util.ArrayList;

/**CLASS:
 * Represents a a game board in a Sudoku Game
 * @author james
 *
 */
public class Grid {
	private ArrayList<Box> boxes = new ArrayList<Box>();
	private ArrayList<Cell> cells = new ArrayList<Cell>();
	private ArrayList<Line> columns = new ArrayList<Line>();
	private ArrayList<Line> rows = new ArrayList<Line>();
	private int filledCount;

	/**CONSTRUCTOR:<br>
	 * Creates a new Grid with empty cells and initializes the relations
	 */
	public Grid() {
		for (int i = 0; i < 81; i++) {
			this.cells.add(new Cell(i, this));
		}

		makeBoxes();
		makeLines();
	}

	/**CONSTRUCTOR:<br>
	 * Creates a deep clone of the given Grid
	 * @param grid Grid to clone
	 */
	public Grid(Grid grid) {
		this();
		setValues(grid.getValues());

		//update any other possibilities which have been eliminated 
		for (Cell cell : getCells()) {
			int i = cell.getPosition();
			cell.setPossibilities(grid.getCells().get(i).getPossibilities());
		}
	}

	/**REPOK:<br>
	 * Check that the given CellCollection does not violate the representation
	 * invariants of a Sudoku puzzle (each value 1-9 appearing only once)
	 * @param coll A CellCollection in this puzzle to check for violations
	 * @return true if this is a valid cell collection
	 */
	private boolean checkRepLine(CellCollection coll) {
		ArrayList<Boolean> trues = getTrueArray();
		boolean repOk = true;

		//Make sure that no value appears twice in the collection
		for (Cell cell : coll.getCells()) {
			if (cell.getValue() != 0) {
				repOk &= trues.get(cell.getValue());
				trues.set(cell.getValue(), false);
			}
		}

		//Make sure that no cell has a value in their list of possibilities 
		//that another cell in the collection has already assumed
		for (Cell cell : coll.getCells()) {
			for (Integer poss : cell.getPossibilities()) {
				repOk &= trues.get(poss);
			}
		}

		return repOk;
	}

	/**EFFECT:<br>
	 * Returns an ArrayList of 10 true values
	 * @return an ArrayList of 10 true values
	 */
	private static ArrayList<Boolean> getTrueArray() {
		ArrayList<Boolean> trues = new ArrayList<Boolean>();
		for (int i = 0; i <=9; i++) {
			trues.add(true);
		}

		return trues;
	}

	/**EFFECT:<br>
	 * Returns a string representing this Grid
	 * @param values an Integer array of arrays, all of length 9
	 * @return a string with the values of the arrays extracted in 
	 * alphabetical order
	 */
	public static String integerArrayToString(Integer[][] values) {
		String str = "";
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				str += values[i][j];
			}
		}
		return str;
	}

	/**INITIALIZER:<br>
	 * Initializes the relationships between the cells in this grid
	 * that belong to the box with the given position
	 * @param position from left to right, top to bottom
	 * @return the Box with the given position, and initialized connections
	 */
	private Box makeBox(int position) {
		ArrayList<Cell> cells = new ArrayList<Cell>();
		int seed = 27 * (position / 3) + 3 * (position % 3);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				int memberPosition = seed + (i * 9) + j;
				cells.add(getCells().get(memberPosition));
			}
		}
		return new Box(position, cells);
	}

	/**INITIALIZER:<br>
	 * Initializes each of the boxes in this Grid
	 */
	private void makeBoxes() {
		for (int i = 0; i < 9; i++) {
			Box box = makeBox(i);
			for (Cell cell : box.getCells()) {
				cell.setBox(box);
				boxes.add(box);
			}
		}
	}

	/**INITIALIZER:<br>
	 * Creates a line in this grid with the given position and initializes
	 * the references to the given cells 
	 * @param position from 0-8
	 * @param row is this Line a row?
	 * @return Line initialized with the given value 
	 */
	private Line makeLine(int position, boolean row) {
		ArrayList<Cell> cells = new ArrayList<Cell>();
		for (int i=0; i < 9; i++) {
			if (row) {
				cells.add(getCells().get(position * 9 + i));
			}
			else {
				cells.add(getCells().get(i * 9 + position));
			}
		}
		return new Line(position, cells, row);
	}

	/**INITIALIZER:<br>
	 * Initializes the line and their relationships in this grid
	 */
	private void makeLines() {
		for (int i = 0; i < 9; i++) {
			Line row = makeLine(i, true);
			Line col = makeLine(i, false);
			for (Cell cell : row.getCells()) {
				cell.setRow(row);
			}
			for (Cell cell : col.getCells()) {
				cell.setColumn(col);
			}
			rows.add(row);
			columns.add(col);
		}
	}

	/**EFFECT:<br>
	 * Returns an integer array of arrays from the string representing
	 * the values of the cell
	 * @param values string of 81 characters concatenated, with 0s 
	 * representing blanks 
	 * @return a 9 value Array of 9 value integer arrays, filled from 
	 * the strings values in the relative positions
	 */
	public static Integer[][] stringToIntegerArray(String values) {
		if (values.length() == 81) {
			ArrayList<Integer> valuesList = new ArrayList<Integer>();
			Integer[][] valuesArray = new Integer[9][9];
			for (int i = 0; i < 0; i++)	{
				for (int j = 0; j < 0; j++) {
					int pos = i * 9 + j;
					valuesList.add(Integer.parseInt(values.substring(pos, pos + 1)));
				}
				valuesList.toArray(valuesArray[i]);
			}
			return valuesArray;
		}
		throw new RuntimeException("Values string is not long enough");
	}

	/**EFFECT:<br>
	 * Determines if the given Grid's array of Cells matches
	 * this Grid's
	 * @param grid a Grid to compare this Grid to 
	 * @return true if every cell in the given grid is equal to every
	 * pairwise cell in this grid
	 */
	public boolean equals(Grid grid) {
		return this.cells.equals(grid.cells);
	}

	/**ACCESSOR:<br>
	 * Returns this Grid's Boxes
	 * @return this Grid's Boxes as an ArrayList
	 */
	public ArrayList<Box> getBoxes() {
		return this.boxes;
	}

	/**ACCESSOR:<br>
	 * Returns this Grid's Cells
	 * @return this Grid's Cells as an ArrayList
	 */
	public ArrayList<Cell> getCells() {
		return this.cells;
	}

	/**ACCESSOR:<br>
	 * Returns this Grid's Cells
	 * @return this Grid's Cells as an ArrayList
	 */
	public ArrayList<Line> getColumns()	{
		return this.columns;
	}

	/**ACCESSOR:<br>
	 * Returns the number of Cells in this Grid that have 
	 * been assigned a value
	 * @return this count of filled cells
	 */
	public int getFilledCount()	{
		return this.filledCount;
	}

	/**ACCESSOR:<br>
	 * Returns the count of the number possibilities that exist
	 * @return the count of possible values that exist
	 */
	public int getPossibilitiesCount() {
		int possibilityValues= 0;
		for (Cell cell : getCells()) {
			possibilityValues += cell.getPossibilities().size();
		}
		return possibilityValues;
	}

	/**ACCESSOR:<br>
	 * Returns this Grid's Rows
	 * @return this Grid's Rows as an ArrayList
	 */
	public ArrayList<Line> getRows() {
		return this.rows;
	}

	/**ACCESSOR:<br>
	 * Returns the values of this Grid's Cells
	 * @return the value of this Grid's Cells as a Integer Array
	 */
	public Integer[][] getValues() {
		Integer[][] valuesArray = new Integer[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				valuesArray[i][j] = getCells().get(i * 9 + j).getValue();
			}
		}
		return valuesArray;
	}

	/**EFFECT:<br>
	 * Increments the count of cells that have been assigned values
	 */
	public void incrementFilled() {
		filledCount++;
	}

	/**EFFECT:<br>
	 * Generates a human readable output of the Grid in it's current
	 * state
	 * @return String of the values of this Grid
	 */
	public String prettyPrint() {
		String str = "";
		for (Line row : getRows()) {
			str += row.toString();
		}
		return str;
	}

	/**REP-OK:
	 * Determines whether Grid conforms to the rules that are required of a 
	 * valid Sudoku Puzzle
	 * @return true if no two neighboring cells have the same value and no
	 * cell has the possibility of assuming a value it's neighbor has
	 */
	public boolean repOK() {
		boolean repOk = true;
		for (Line row : getRows()) {
			repOk &= checkRepLine(row);
		}
		for (Line col : getColumns()) {
			repOk &= checkRepLine(col);
		}
		for (Box box : getBoxes()) {
			repOk &= checkRepLine(box);
		}
		return repOk;
	}

	/**INITIALIZER:<br>
	 * Sets this grid's list of boxes to the given list of boxes 
	 * @param boxes ArrayList of boxes to set this grid's boxes to 
	 */
	public void setBoxes(ArrayList<Box> boxes)	{
		this.boxes = boxes;
	}

	/**INITIALIZER:<br>
	 * Sets this grid's list of cells to the given list of cells
	 * @param cells ArrayList of cells to set this grid's cells to 
	 */
	public void setCells(ArrayList<Cell> cells) 	{
		this.cells = cells;
	}

	/**INITIALIZER:<br>
	 * Sets this grid's list of columns to the given list of columns
	 * @param cols ArrayList of columns to set this grid's columns to 
	 */
	public void setColumns(ArrayList<Line> cols) {
		this.columns = cols;
	}

	/**INITIALIZER:<br>
	 * Sets this grid's list of rows to the given list of rows 
	 * @param boxes ArrayList of rows to set this grid's rows to 
	 */
	public void setRows(ArrayList<Line> rows)
	{
		this.rows = rows;
	}

	/**INITIALIZER:<br>
	 * Sets the cells in this grid to each of the corresponding
	 * values in the given integer array of arrays 
	 * @param values 9 value Array of Integer Arrays; each containing 9
	 * indices
	 */
	public void setValues(Integer[][] values) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				this.cells.get(i * 9 + j).setValue(values[i][j]);
			}
		}
	}

	/**EFFECT:<br>
	 * Determines if this puzzle has been legally solved 
	 * @return true if the puzzle has been assigned all 81, non-overlapping 
	 * neighbors
	 */
	public boolean solved()	{
		return repOK() && getFilledCount() == 81;
	}

	/**EFFECT:<br>
	 * Returns a string of 81 characters; one for each value in the grid
	 * @return String representation of the puzzles values
	 */
	public String toString() {
		String str = "";
		for (Cell cell : getCells()) {
			str += cell.getValue();
		}
		return str;
	}
}
