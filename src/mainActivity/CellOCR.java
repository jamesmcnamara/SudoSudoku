package mainActivity;

import java.util.ArrayList;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.imgproc.Imgproc;

import android.graphics.Bitmap;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

/**CLASS:<br>
 * Handles the OCR for extracting values out of the cells of the Sudoku board
 * @author james
 *
 */
public class CellOCR {
	private final String TAG = "SudoSudoku::OCR";
	private ArrayList<Mat> cells;
	private TessBaseAPI ocr;
	private String puzzleString;

	/**CONSTRUCTOR:<br>
	 * Initializes the OCR engine with the given list of 81 cells and the 
	 * given training file 
	 * @param cells an array of 81 Mats of cells  
	 * @param trained_data the name of the training data file in the assets
	 */
	public CellOCR(ArrayList<Mat> cells, String trained_data) {
		this.cells = cropCells(cells, 5);
		this.ocr = new TessBaseAPI();
		this.ocr.init(trained_data, "eng");
		this.ocr.setVariable("tessedit_char_whitelist", "123456789");
		this.puzzleString = "";
		if (cells.size() != 81) {
			throw new RuntimeException("Not enough cells in table");
		}
	}

	/**EFFECT:<br>
	 * Returns an 81 value array that reflect whether the corresponding
	 * square in the Sudoku puzzle contains a number or not
	 * 
	 * @return an 81 value array that reflect whether the corresponding
	 * square in the Sudoku puzzle contains a number or not
	 */
	private ArrayList<Boolean> cellsWithNumbers() {
		ArrayList<Boolean> cellsWithNumbers = new ArrayList<Boolean>();
		int threshold = getAverageWhite();
		for (Mat cell : cells) {
			cellsWithNumbers.add(Core.countNonZero(cell) > threshold);
		}
		return cellsWithNumbers;
	}

	/**EFFECT:<br>
	 * removes a border from every cell in the given list of size
	 * given by the crop
	 * @param cells ArrayList to crop
	 * @param crop size of the border to remove from every cell
	 * @return array of cropped mats
	 */
	private static ArrayList<Mat> cropCells(ArrayList<Mat> cells, int crop) {
		ArrayList<Mat> croppedCells = new ArrayList<Mat>();
		for (Mat cell : cells) {
			croppedCells.add(cell.submat(crop, cell.rows() - crop, crop, cell.cols() - crop));
		}
		return croppedCells;
	}

	/**EFFECT:<br>
	 * Returns the average amount of white pixels that occur in all cells
	 * 
	 * @return the threshold amount of white that is necessary to flag a 
	 * cell as white
	 */
	private int getAverageWhite() {
		int avg = 0;
		for (Mat cell : cells) {
			avg += Core.countNonZero(cell); 
		}
		return avg / 81;
	}

	/**EFFECT:<br>
	 * Attempts to extract out the number in the given cell 
	 * @param cell the image to parse for an image
	 * @return the string of the value in the cell, e.g. "6" for 6
	 */
	private String parseValue(Mat cell) {
		if (setOCR(cell)) {
			Log.v(TAG, "First pass sucess. Value is " + this.ocr.getUTF8Text() +
					" with confidence: " + this.ocr.meanConfidence());
			return this.ocr.getUTF8Text().substring(0, 1);
		}
		return parseValueAdvanced(cell);
	}

	/**EFFECT:<br>
	 * Applies more rigorous procedures to parse out the numeric value in a mat
	 * @param cell the Mat to analyze
	 * @return the string value in the cell 
	 */
	private String parseValueAdvanced(Mat cell) {
		ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.findContours(cell.clone(), contours, new Mat(), 0, 1);
		MatOfPoint largestContour = BoardProcessor.findLargestContour(contours);
		
		//If no contours, log and return 0
		if (largestContour.empty()) {
			Log.v(TAG, "No contours found");
		}
		else {
			
			//If the tighter cropped cell can be read, return it's value
			if (setOCR(cell.submat(Imgproc.boundingRect(largestContour)))) {
				Log.v("SudoSudoku::OCR", "Second pass sucess. Value is " + 
						this.ocr.getUTF8Text() + " with confidence: " + 
						this.ocr.meanConfidence());
				return this.ocr.getUTF8Text().substring(0, 1);
			}
		}

		return "0";
	}

	/**MODIFIES:<br>
	 * primes the TessOCR with the given image, and returns whether or not 
	 * the cell can be read with confidence over 3/4
	 * @param cell to be read
	 * @return was this cell read reliably?
	 */
	private boolean setOCR(Mat cell) {
		Bitmap bitmap = Bitmap.createBitmap(cell.width(), cell.height(), 
				Bitmap.Config.ARGB_8888);
		Utils.matToBitmap(cell, bitmap);
		this.ocr.setImage(bitmap);
		return this.ocr.meanConfidence() > 75;
	}

	/**EFFECT:<br>
	 * Main purpose of the class. Attempts to read the values out of each 
	 * of the 81 cells and return a string of their values
	 *  
	 * @return an 81 character string of values
	 */
	public String getPuzzleText() {
		ArrayList<Boolean> cellsToCheck = cellsWithNumbers();
		int counter = 0;
		for (boolean checkCell : cellsToCheck) {
			if (checkCell) {
				puzzleString += parseValue(cells.get(counter));
			}
			else {
				puzzleString += 0;
				Log.v("SudoSudoku::OCR", "Cell " + counter + " is blank");
			}
		}
		return puzzleString;
	}
}
