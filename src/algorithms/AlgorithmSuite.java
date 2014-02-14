package algorithms;

import board.Grid;

//Starts and manages the heuristic search for a solution to the given puzzle   
public class AlgorithmSuite {

	/**EFFECT<br>
	 * Determines whether this grid has been solved or not
	 * Also checks whether the grid has violated any of the representation
	 * invariants of Sudoku puzzle (repeated values in a single line) and
	 * if so, kills the given thread of execution 
	 * @param grid Sudoku puzzle to check if solved
	 * @param solver thread of execution that contains the given grid
	 * @return
	 */
	public static boolean checkGridStatus(Grid grid, Solver solver) {
		if (grid.solved()) {
			solver.foundItBitches(grid);
			return true;
		}

		if (!grid.repOK()) {
			solver.killThreadAndChildren();
		}
		return false;
	}

	/**EFFECT:<br>
	 * Begins the given solver search on the given grid
	 * @param grid the Sudoku puzzle to run on 
	 * @param solver the current thread of execution
	 */
	public static void run(Grid grid, Solver solver) {
		if (checkGridStatus(grid, solver)) {
			return;
		}
		new Solver(grid, solver).run();
	}

	/**EFFECT:<br>
	 * Returns the solution of the given description of a Sudoku
	 * puzzle as a String with the i-th character representing
	 * the value in the ith cell
	 * @param puzzle an 81 character string of values, with the i-th value
	 * representing the given value at the i-th cell. If no value is given or 
	 * known represent the space with a 0 
	 */
	public static String solvePuzzle(String puzzle) {
		return solvePuzzle(Grid.stringToIntegerArray(puzzle));
	}

	/**EFFECT:<br>
	 * Returns the solution of the given description of a Sudoku
	 * puzzle as a String with the i-th character representing
	 * the value in the ith cell
	 * @param values an length 9 array of length 9 integer arrays, with the 
	 * i,j-th value representing the given value at the i * 9 + j-th cell
	 * If no value is given or known, represent the space with a 0 
	 */
	public static String solvePuzzle(Integer[][] values) {
		Grid grid = new Grid();
		grid.setValues(values);
		Solver solver = new Solver(grid, null);
		solver.start();
		while (solver.isAlive()) {}
		return solver.getGrid().toString();
	}
}
