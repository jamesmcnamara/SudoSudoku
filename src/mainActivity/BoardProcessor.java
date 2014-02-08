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

public class BoardProcessor
{
  private final String TAG = "SudoSudoku::BoardProcessor";
  private double cellHeight;
  private double cellSize;
  private double cellWidth;
  private Mat puzzle;
  private double threshold;
  
  protected BoardProcessor(Mat paramMat)
  {
    this.puzzle = paramMat;
  }
  
  public static Mat cleanBoard(Mat paramMat)
  {
    Mat localMat1 = new Mat(new Size(1024.0D, 1024.0D), CvType.CV_8UC1);
    new Mat(new Size(1024.0D, 1024.0D), CvType.CV_8UC1);
    Mat localMat2 = new Mat(new Size(1024.0D, 1024.0D), CvType.CV_8UC1);
    Imgproc.resize(paramMat, localMat1, new Size(1024.0D, 1024.0D));
    Highgui.imwrite(Environment.getExternalStorageDirectory() + "/SudoSudoku/puzzle1Resize.jpg", localMat1);
    Imgproc.medianBlur(localMat1, localMat1, 5);
    Highgui.imwrite(Environment.getExternalStorageDirectory() + "/SudoSudoku/puzzle2Blur.jpg", localMat1);
    Core.subtract(localMat1, localMat1, localMat2);
    Highgui.imwrite(Environment.getExternalStorageDirectory() + "/SudoSudoku/puzzle3Subtracted.jpg", localMat2);
    Imgproc.threshold(localMat2, localMat2, -1.0D, 255.0D, 9);
    Highgui.imwrite(Environment.getExternalStorageDirectory() + "/SudoSudoku/puzzle4Otsu.jpg", localMat2);
    return localMat2;
  }
  
  private static Rect getBoardBound(Mat paramMat)
  {
    ArrayList localArrayList = new ArrayList();
    double d1 = 0.0D;
    Object localObject = new MatOfPoint();
    Imgproc.GaussianBlur(paramMat, paramMat, new Size(3.0D, 3.0D), 0.0D);
    Imgproc.adaptiveThreshold(paramMat, paramMat, 255.0D, 0, 1, 19, 7.0D);
    Imgproc.findContours(paramMat.clone(), localArrayList, new Mat(), 0, 2);
    Iterator localIterator = localArrayList.iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return Imgproc.boundingRect((MatOfPoint)localObject);
      }
      MatOfPoint localMatOfPoint = (MatOfPoint)localIterator.next();
      if (Imgproc.contourArea(localMatOfPoint) > d1)
      {
        Rect localRect = Imgproc.boundingRect(localMatOfPoint);
        double d2 = localRect.area();
        if (Math.abs(d2 - localRect.width * localRect.width) < 0.15D * d2)
        {
          d1 = Imgproc.contourArea(localMatOfPoint);
          localObject = localMatOfPoint;
        }
      }
    }
  }
  
  private static ArrayList<MatOfPoint> getBoardContours(Mat paramMat)
  {
    ArrayList localArrayList = new ArrayList();
    Imgproc.GaussianBlur(paramMat, paramMat, new Size(3.0D, 3.0D), 3.0D, 3.0D);
    Imgproc.findContours(paramMat.clone(), localArrayList, new Mat(), 1, 2);
    return localArrayList;
  }
  
  private int getCellID(double paramDouble1, double paramDouble2)
  {
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
  
  private ArrayList<Rect> getCellRects(Mat paramMat, ArrayList<MatOfPoint> paramArrayList)
  {
    ArrayList localArrayList = new ArrayList();
    Log.v("SudoSudoku::BoardProcessor", "Cell rects have expected size: " + this.cellSize + " and threshold of: " + this.threshold);
    Iterator localIterator = paramArrayList.iterator();
    for (;;)
    {
      if (!localIterator.hasNext())
      {
        Log.v("SudoSudoku::BoardProcessor", "Cell count is " + localArrayList.size());
        return localArrayList;
      }
      MatOfPoint localMatOfPoint = (MatOfPoint)localIterator.next();
      if (Math.abs(Imgproc.contourArea(localMatOfPoint) - this.cellSize) < this.threshold) {
        localArrayList.add(Imgproc.boundingRect(localMatOfPoint));
      }
    }
  }
  
  private ArrayList<Mat> getCells(ArrayList<Rect> paramArrayList)
  {
    if (paramArrayList.size() > 81) {
      throw new RuntimeException("Too many cells detected, please retake photo");
    }
    if (paramArrayList.size() < 81) {
      return padBoard(paramArrayList);
    }
    return submatArray(paramArrayList);
  }
  
  private ArrayList<Mat> padBoard(ArrayList<Rect> paramArrayList)
  {
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    int i = 0;
    Iterator localIterator = paramArrayList.iterator();
    for (;;)
    {
      if (!localIterator.hasNext())
      {
        localArrayList1.addAll(padRow(localArrayList2));
        return localArrayList1;
      }
      Rect localRect = (Rect)localIterator.next();
      if (localRect.x > i)
      {
        Log.v("SudoSudoku::BoardProcessor", localRect.x + " is greater than " + i);
        localArrayList2.add(localRect);
        i = localRect.x;
      }
      else
      {
        localArrayList1.addAll(padRow(localArrayList2));
        localArrayList2.clear();
        localArrayList2.add(localRect);
        i = 0;
      }
    }
  }
  
  private ArrayList<Mat> padRow(ArrayList<Rect> paramArrayList)
  {
    ArrayList localArrayList;
    if (paramArrayList.size() == 9) {
      localArrayList = submatArray(paramArrayList);
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
        Log.v("SudoSudoku::BoardProcessor", "Adding blanks");
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
  
  private void setCellStats()
  {
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
  
  private ArrayList<Mat> submatArray(ArrayList<Rect> paramArrayList)
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = paramArrayList.iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return localArrayList;
      }
      Rect localRect = (Rect)localIterator.next();
      localArrayList.add(this.puzzle.submat(localRect));
    }
  }
  
  protected ArrayList<Mat> processPhoto()
  {
    this.puzzle = this.puzzle.submat(getBoardBound(this.puzzle));
    setCellStats();
    ArrayList localArrayList1 = getBoardContours(this.puzzle);
    ArrayList localArrayList2 = getCells(sortRects(getCellRects(this.puzzle, localArrayList1)));
    int i = 1;
    Iterator localIterator = localArrayList2.iterator();
    for (;;)
    {
      if (!localIterator.hasNext())
      {
        Log.v("SudoSudoku::BoardProcessor", "Cell count is: " + localArrayList2.size());
        return localArrayList2;
      }
      Mat localMat = (Mat)localIterator.next();
      StringBuilder localStringBuilder = new StringBuilder().append(Environment.getExternalStorageDirectory()).append("/SudoSudoku/Cell");
      int j = i + 1;
      Highgui.imwrite(i + ".jpg", localMat);
      i = j;
    }
  }
}


/* Location:           C:\Users\HAL\Documents\sudosudoku\com.example.helloopencv_1.0.apk\classes_dex2jar.jar
 * Qualified Name:     mainActivity.BoardProcessor
 * JD-Core Version:    0.7.0.1
 */