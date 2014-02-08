package mainActivity;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Log;
import com.googlecode.tesseract.android.TessBaseAPI;
import java.util.ArrayList;
import java.util.Iterator;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.imgproc.Imgproc;

public class CellOCR
{
  private final String TAG = "SudoSudoku::OCR";
  private ArrayList<Mat> cells;
  private TessBaseAPI ocr;
  private String puzzleString;
  
  public CellOCR(ArrayList<Mat> paramArrayList, String paramString)
  {
    this.cells = cropCells(paramArrayList, 5);
    this.ocr = new TessBaseAPI();
    this.ocr.init(paramString, "eng");
    this.ocr.setVariable("tessedit_char_whitelist", "123456789");
    this.puzzleString = "";
    if (paramArrayList.size() != 81) {
      throw new RuntimeException("Not enough cells in table");
    }
  }
  
  private ArrayList<Boolean> cellsWithNumbers()
  {
    ArrayList localArrayList = new ArrayList();
    int i = getAverageWhite();
    Iterator localIterator = this.cells.iterator();
    if (!localIterator.hasNext()) {
      return localArrayList;
    }
    if (Core.countNonZero((Mat)localIterator.next()) > i) {}
    for (boolean bool = true;; bool = false)
    {
      localArrayList.add(Boolean.valueOf(bool));
      break;
    }
  }
  
  private static ArrayList<Mat> cropCells(ArrayList<Mat> paramArrayList, int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = paramArrayList.iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return localArrayList;
      }
      Mat localMat = (Mat)localIterator.next();
      localArrayList.add(localMat.submat(paramInt, localMat.rows() - paramInt, paramInt, localMat.cols() - paramInt));
    }
  }
  
  private int getAverageWhite()
  {
    int i = 0;
    Iterator localIterator = this.cells.iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return i / 81;
      }
      i += Core.countNonZero((Mat)localIterator.next());
    }
  }
  
  private String parseValue(Mat paramMat)
  {
    if (setOCR(paramMat))
    {
      Log.v("SudoSudoku::OCR", "First pass sucess. Value is " + this.ocr.getUTF8Text() + " with confidence: " + this.ocr.meanConfidence());
      return this.ocr.getUTF8Text().substring(0, 1);
    }
    return parseValueAdvanced(paramMat);
  }
  
  private String parseValueAdvanced(Mat paramMat)
  {
    ArrayList localArrayList = new ArrayList();
    double d1 = 0.0D;
    Object localObject = new MatOfPoint();
    Imgproc.findContours(paramMat.clone(), localArrayList, new Mat(), 0, 1);
    Iterator localIterator = localArrayList.iterator();
    for (;;)
    {
      if (!localIterator.hasNext())
      {
        if (d1 != 0.0D) {
          break;
        }
        Log.v("SudoSudoku::OCR", "No contours found");
        return "0";
      }
      MatOfPoint localMatOfPoint = (MatOfPoint)localIterator.next();
      double d2 = Imgproc.contourArea(localMatOfPoint);
      if (d2 > d1)
      {
        localObject = localMatOfPoint;
        d1 = d2;
      }
    }
    if (setOCR(paramMat.submat(Imgproc.boundingRect((MatOfPoint)localObject))))
    {
      Log.v("SudoSudoku::OCR", "Second pass sucess. Value is " + this.ocr.getUTF8Text() + " with confidence: " + this.ocr.meanConfidence());
      return this.ocr.getUTF8Text().substring(0, 1);
    }
    return "0";
  }
  
  private boolean setOCR(Mat paramMat)
  {
    Bitmap localBitmap = Bitmap.createBitmap(paramMat.width(), paramMat.height(), Bitmap.Config.ARGB_8888);
    Utils.matToBitmap(paramMat, localBitmap);
    this.ocr.setImage(localBitmap);
    return this.ocr.meanConfidence() > 75;
  }
  
  public String getPuzzleText()
  {
    ArrayList localArrayList = cellsWithNumbers();
    int i = 0;
    Iterator localIterator = localArrayList.iterator();
    if (!localIterator.hasNext()) {
      return this.puzzleString;
    }
    if (((Boolean)localIterator.next()).booleanValue()) {}
    for (this.puzzleString += parseValue((Mat)this.cells.get(i));; this.puzzleString += 0)
    {
      i++;
      break;
      Log.v("SudoSudoku::OCR", "Cell " + i + " is blank");
    }
  }
}


/* Location:           C:\Users\HAL\Documents\sudosudoku\com.example.helloopencv_1.0.apk\classes_dex2jar.jar
 * Qualified Name:     mainActivity.CellOCR
 * JD-Core Version:    0.7.0.1
 */