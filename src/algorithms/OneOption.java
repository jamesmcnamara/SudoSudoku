package algorithms;

import board.Box;
import board.Cell;
import board.CellCollection;
import board.Grid;
import board.Line;
import board.SparseArrayIter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class OneOption
{
  private static ArrayList<String> getBlankPossArray()
  {
    ArrayList localArrayList = new ArrayList();
    for (int i = 0;; i++)
    {
      if (i > 9) {
        return localArrayList;
      }
      localArrayList.add("");
    }
  }
  
  private static ArrayList<String> getPossStrings(CellCollection paramCellCollection)
  {
    ArrayList localArrayList = getBlankPossArray();
    Iterator localIterator1 = paramCellCollection.getCells().iterator();
    for (;;)
    {
      if (!localIterator1.hasNext()) {
        return localArrayList;
      }
      Cell localCell = (Cell)localIterator1.next();
      Iterator localIterator2 = localCell.getPossibilities().values().iterator();
      while (localIterator2.hasNext())
      {
        Integer localInteger = (Integer)localIterator2.next();
        String str = (String)localArrayList.get(localInteger.intValue()) + localCell.getPosition();
        localArrayList.set(localInteger.intValue(), str);
      }
    }
  }
  
  public static void run(Grid paramGrid)
  {
    Iterator localIterator1 = paramGrid.getRows().iterator();
    Iterator localIterator2;
    label25:
    Iterator localIterator3;
    if (!localIterator1.hasNext())
    {
      localIterator2 = paramGrid.getColumns().iterator();
      if (localIterator2.hasNext()) {
        break label68;
      }
      localIterator3 = paramGrid.getBoxes().iterator();
    }
    for (;;)
    {
      if (!localIterator3.hasNext())
      {
        return;
        setOneOptions(paramGrid, (Line)localIterator1.next());
        break;
        label68:
        setOneOptions(paramGrid, (Line)localIterator2.next());
        break label25;
      }
      setOneOptions(paramGrid, (Box)localIterator3.next());
    }
  }
  
  private static void setOneOptions(Grid paramGrid, CellCollection paramCellCollection)
  {
    ArrayList localArrayList = getPossStrings(paramCellCollection);
    Iterator localIterator = localArrayList.iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return;
      }
      String str = (String)localIterator.next();
      if (str.length() == 1)
      {
        int i = Integer.parseInt(str);
        ((Cell)paramGrid.getCells().get(i)).setValue(localArrayList.indexOf(str));
      }
    }
  }
}


/* Location:           C:\Users\HAL\Documents\sudosudoku\com.example.helloopencv_1.0.apk\classes_dex2jar.jar
 * Qualified Name:     algorithms.OneOption
 * JD-Core Version:    0.7.0.1
 */