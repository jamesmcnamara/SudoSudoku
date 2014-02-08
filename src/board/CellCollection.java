package board;

import java.util.ArrayList;

/**CLASS<br>
 * Represents a collection of cells 
 * @author james
 *
 */
public abstract class CellCollection { 
  protected ArrayList<Cell> cells = new ArrayList<Cell>();
  private int position;
  
  /**CONSTRUCTOR:<br>
   * Creates a new instance of a CellCollection with the given position 
   * in the grid and the collection's constituent list of cells
   * @param position the CellCollection's position in the Grid
   * @param cells an ArrayList of constituent Cells 
   */
  CellCollection(int position, ArrayList<Cell> cells) {
    this.position = position;
    this.cells = cells;
  }
  
  /**EFFECT:<br>
   * Determines whether or not this cell collection is equivalent to 
   * the given cell collection    
   * @param coll cell collection to compare this CellCollection to
   * @return true if all cells in the given collection are equal to all 
   * cells in this collection
   */
  public boolean equals(CellCollection coll) {
    return getCells().equals(coll.getCells());
  }
  
  /**ACCESSOR:<br>
   * Returns this collection's constituent cell
   * @return this collection's constituent cells
   */
  public ArrayList<Cell> getCells() {
    return this.cells;
  }

  /**ACCESSOR:<br>
   * Returns this collection's position
   * @return this collection's position in the grid
   */
  public int getPosition() {
    return this.position;
  }
}
