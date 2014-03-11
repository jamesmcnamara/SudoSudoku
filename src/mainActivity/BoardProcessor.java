package mainActivity;

import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
/**CLASS:<br>
 * Handles the image processing of the board with the goal of
 * extracting out 81 black and white cells 
 * @author james
 *
 */
public class BoardProcessor {
	private final String TAG = "SudoSudoku::BoardProcessor";
	private double cellHeight;
	private double cellSize;
	private double cellWidth;
	private Mat puzzle;
	private double threshold;

	/**CONSTRUCTOR:<br>
	 * Initializes the BoardProcessor
	 * @param puzzle the image to parse
	 */
	protected BoardProcessor(Mat puzzle) {
		this.puzzle = puzzle;
	}

	/**EFFECT:<br>
	 * 
	 * @param paramMat
	 * @return
	 */
	public static Mat cleanBoard(Mat puzzle) {
		Mat localMat1 = new Mat(new Size(1024, 1024), CvType.CV_8UC1);
		new Mat(new Size(1024.0D, 1024.0D), CvType.CV_8UC1);
		Mat localMat2 = new Mat(new Size(1024.0D, 1024.0D), CvType.CV_8UC1);
		Imgproc.resize(puzzle, localMat1, new Size(1024.0D, 1024.0D));
		Highgui.imwrite(Environment.getExternalStorageDirectory() + "/SudoSudoku/puzzle1Resize.jpg", localMat1);
		Imgproc.medianBlur(localMat1, localMat1, 5);
		Highgui.imwrite(Environment.getExternalStorageDirectory() + "/SudoSudoku/puzzle2Blur.jpg", localMat1);
		Core.subtract(localMat1, localMat1, localMat2);
		Highgui.imwrite(Environment.getExternalStorageDirectory() + "/SudoSudoku/puzzle3Subtracted.jpg", localMat2);
		Imgproc.threshold(localMat2, localMat2, -1.0D, 255.0D, 9);
		Highgui.imwrite(Environment.getExternalStorageDirectory() + "/SudoSudoku/puzzle4Otsu.jpg", localMat2);
		return localMat2;
	}

	/**EFFECT:<br>
	 * Returns a Rect that outlines the boundary of the board in the given
	 * puzzle image
	 * @param puzzle the puzzle image to find a board boundary for
	 * @return Bounding rect for the sudoku board in the given puzzle
	 */
	private static Rect getBoardBound(Mat puzzle) {
		ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.GaussianBlur(puzzle, puzzle, new Size(3, 3), 0);
		Imgproc.adaptiveThreshold(puzzle, puzzle, 255, 0, 1, 19, 7);
		Imgproc.findContours(puzzle.clone(), contours, new Mat(), 0, 2);
		MatOfPoint largestContour = findLargestContour(contours);
		return Imgproc.boundingRect(largestContour);
	}

	/**EFFECT:<br>
	 * Returns the largest contour in the given array of contours
	 * @param contours an array of contours
	 * @return the largest contour
	 */
	public static MatOfPoint findLargestContour(ArrayList<MatOfPoint> contours) {
		MatOfPoint largest = new MatOfPoint();
		double runningLargest = 0;
		for (MatOfPoint contour : contours) {
			if (Imgproc.contourArea(contour) > runningLargest) {
				runningLargest = Imgproc.contourArea(contour);
				largest = contour;
			}
		}
		return largest;
	}

	/**EFFECT:<br>
	 * returns an array of contours in the given image
	 * @param puzzle the image to extract contours from
	 * @return an array of contours
	 */
	private static ArrayList<MatOfPoint> getBoardContours(Mat puzzle) {
		ArrayList<MatOfPoint> contours= new ArrayList<MatOfPoint>();
		Imgproc.GaussianBlur(puzzle, puzzle, new Size(3, 3),3, 3);
		Imgproc.findContours(puzzle.clone(), contours, new Mat(), 1, 2);
		return contours;
	}


	private int getCellID(double paramDouble1, double paramDouble2) {
		int i = (int)paramDouble1 / (int)this.cellHeight;
		int j = (int)paramDouble2;
		int k = 1 + (int)Math.log10(j);
		String str = i;
		for (int m = 0;; m++)
		{
			if (m >= 6 - k) {
				return Integer.parseInt(str + j);
			}
			str = str + "0";
		}
	}

	/**EFFECT:<br>
	 * Consumes a board image and returns the bounding rect of each cell that
	 * it can identify
	 * 
	 * @param puzzle binary puzzle image
	 * @param contours potential cells that have been marked off
	 * @return array of rects that may contain cells
	 */
	private ArrayList<Rect> getCellRects(Mat puzzle, ArrayList<MatOfPoint> contours) {
		ArrayList<Rect> bounds = new ArrayList<Rect>();
		Log.v(TAG, "Cell rects have expected size: " + this.cellSize + " and threshold of: " + this.threshold);
		for (MatOfPoint contour : contours) {
			if (Math.abs(Imgproc.contourArea(contour) - this.cellSize) < this.threshold) {
				bounds.add(Imgproc.boundingRect(contour));
			}
		}
		return bounds;
	}

	/**EFFECT:<br>
	 * Consumes an array of bounding Rects and returns an array of
	 * 81 cell images
	 * @param bounds array list of rects bounding cells
	 * @return array of 81 cell images in order of appearance
	 */
	private ArrayList<Mat> getCells(ArrayList<Rect> bounds)	{
		if (bounds.size() > 81) {
			throw new RuntimeException("Too many cells detected, please retake photo");
		}
		else if (bounds.size() < 81) {
			return padBoard(bounds);
		}
		else {
			return submatArray(bounds);
		}

	}

	/**EFFECT:<br>
	 * Recognizes where there should be a cell and that hasn't been detected, and 
	 * places an empty cell in the space
	 * @param bounds detected cell boundaries
	 * @return array of 81 cell images in order of appearance
	 */
	private ArrayList<Mat> padBoard(ArrayList<Rect> bounds)	{
		ArrayList<Mat> board = new ArrayList<Mat>();
		ArrayList<Rect> row = new ArrayList<Rect>();
		int lastX = 0;

		for (Rect rect : bounds) {
			if (rect.x > lastX) {
				row.add(rect);
				lastX = rect.x;
			}
			else {
				board.addAll(padRow(row));
				row.clear();
				row.add(rect);
				lastX = 0;
			}
		}
		
		return board;
	}

	/** EFFECT:<br>
	 * identifies where in this row there is a potentially missing
	 * cell and inserts a blank cell into the space
	 * @param rowRect bounding rects of the identified cells in a 
	 * given row
	 * @return Array of 9 cell images, with blank cells input where
	 * a bounding rect is missing
	 */
	private ArrayList<Mat> padRow(ArrayList<Rect> rowRect) {
		ArrayList<Mat> row;
		if (rowRect.size() == 9) {
			row = submatArray(rowRect);
		}
		
		
		double d1;
		double d2;
		Iterator localIterator;
		for (;;)
		{
			return localArrayList;
			localArrayList = new ArrayList();
			d1 = 2.0D * this.cellWidth;
			d2 = 0.0D;
			localIterator = paramArrayList.iterator();
			if (localIterator.hasNext()) {
				break;
			}
			if (localArrayList.size() > 9) {
				break label252;
			}
			while (localArrayList.size() < 9)
			{
				localArrayList.add(Mat.zeros(new Size(this.cellWidth, this.cellHeight), CvType.CV_8UC1));
				Log.v(TAG, "Adding blanks");
			}
		}
		Rect localRect = (Rect)localIterator.next();
		for (;;)
		{
			if (localRect.x - d2 <= d1)
			{
				localArrayList.add(this.puzzle.submat(localRect));
				d2 = localRect.x;
				Log.v("SudoSudoku::BoardProcessor", "Adding " + localRect.x);
				break;
			}
			Log.v("SudoSudoku::BoardProcessor", "Adding blank before " + localRect.x);
			localArrayList.add(Mat.zeros(new Size(this.cellWidth, this.cellHeight), CvType.CV_8UC1));
			d2 += this.cellWidth;
		}
		label252:
			throw new RuntimeException("Row is too long");
	}

	/**INITIALIZER:<br>
	 * Initializes this processors thresholds and stats
	 */
	private void setCellStats()	{
		this.cellHeight = (this.puzzle.height() / 9);
		this.cellWidth = (this.puzzle.width() / 9);
		this.cellSize = (this.cellHeight * this.cellWidth);
		this.threshold = (0.75D * this.cellSize);
	}

	private ArrayList<Rect> sortRects(ArrayList<Rect> paramArrayList)
	{
		SparseArray localSparseArray = new SparseArray();
		ArrayList localArrayList1 = new ArrayList();
		Iterator localIterator1 = paramArrayList.iterator();
		ArrayList localArrayList2;
		Iterator localIterator2;
		if (!localIterator1.hasNext())
		{
			Collections.sort(localArrayList1);
			localArrayList2 = new ArrayList();
			localIterator2 = localArrayList1.iterator();
		}
		for (;;)
		{
			if (!localIterator2.hasNext())
			{
				return localArrayList2;
				Rect localRect = (Rect)localIterator1.next();
				int i = getCellID(localRect.y, localRect.x);
				localSparseArray.put(i, localRect);
				localArrayList1.add(Integer.valueOf(i));
				break;
			}
			localArrayList2.add((Rect)localSparseArray.get(((Integer)localIterator2.next()).intValue()));
		}
	}

	/**EFFECT:<br>
	 * Returns an array of images extracted from the given array of 
	 * rects
	 * @param bounds bounding rects to extract matching images 
	 * @return array of Mat images bounded by the given rects
	 */
	private ArrayList<Mat> submatArray(ArrayList<Rect> bounds) {
		ArrayList<Mat> cells = new ArrayList<Mat>();
		for (Rect rect : bounds) {
			cells.add(this.puzzle.submat(rect));
		}
		return cells;
	}

	/**EFFECT:<br>
	 * Main function of the class. Transforms the camera captured image 
	 * into an array of 81 images extracted from the puzzle
	 * @return an array of 81 images extracted from the puzzle
	 */
	protected ArrayList<Mat> processPhoto() {
		this.puzzle = this.puzzle.submat(getBoardBound(this.puzzle));
		setCellStats();
		ArrayList<MatOfPoint> contours = getBoardContours(this.puzzle);
		ArrayList<Mat> cells = getCells(sortRects(getCellRects(this.puzzle, contours)));
		for (int i = 0; i < cells.size(); i++) {
			Highgui.imwrite(Environment.getExternalStorageDirectory() 
					+ "/SudoSudoku/Cell" + i + ".jpg", cells.get(i));
		}
		return cells;
	}
}
