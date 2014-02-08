package board;

import java.util.ArrayList;
import java.util.Iterator;

/**Represents a 3x3 box in a Sudoku board
 * 
 * @author james
 *
 */
public class Box
extends CellCollection {

	/**CONSTRUCTOR:<br>
	 * Creates a Box object at the given position in the grid, with the 
	 * given list of member cells 
	 * @param position zero-denominated position of this box in the grid
	 * @param cells The cells that are members of this box 
	 */
	public Box(int position, ArrayList<Cell> cells)
	{
		super(position, cells);
		
		for (Cell cell : cells) {
			cell.setBox(this);
		}
	}

	/**OVERRIDE:<br>
	 * Returns a String representation of this Box
	 * @return String
	 */
	public String toString()
	{
		String str = "Box " + getPosition() + " :\n";
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				str = str + getCells().get(j + i * 3) + ", ";
			}
		}
		return str;
	}
}
