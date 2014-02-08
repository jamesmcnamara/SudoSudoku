package board;

import java.util.ArrayList;

/**CLASS:<br>
 * Represents a collection of 9 Cell's in a single line, either 
 * vertically as a column or horizontally as a row
 * @author james
 *
 */
public class Line
extends CellCollection
{
	private boolean row;

	/**CONSTRUCTOR:<br>
	 * Creates a new line with the given position, the given list of cells
	 * and a boolean determining if it is a row
	 * @param position the of this line in the grid
	 * @param cells ArrayList of Cells
	 * @param isRow is this line a row?
	 */
	public Line(int position, ArrayList<Cell> cells, boolean isRow) {
		super(position, cells);
		this.row = isRow;

		if (isRow) {
			for (Cell cell : cells) {
				cell.setRow(this);
			}
		}
		else {
			for (Cell cell : cells) {
				cell.setColumn(this);
			}
		}
	}

	/**OVERRIDE:<br>
	 * Returns a human-readable string representation of this Line
	 * @return a String rep of this Line
	 */
	public String toString() {
		String s = "";
		
		if (this.row) {
			s = "Row ";
		}
		else {
			s = "Column ";
		}
		
		s = s + getPosition() + " has: \n\t";
		
		for (Cell cell : cells) {
			s += cell.getValue() + ", ";
		}
		
		return s;
	}
}
