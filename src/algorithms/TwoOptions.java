package algorithms;

import board.Box;
import board.Cell;
import board.Grid;
import board.Line;
import board.SparseArrayIter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public final class TwoOptions
{
  private static ArrayList<Cell> findTwoOptions(Box paramBox)
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = paramBox.getCells().iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return localArrayList;
      }
      Cell localCell = (Cell)localIterator.next();
      if (localCell.getPossibilities().size() == 2) {
        localArrayList.add(localCell);
      }
    }
  }
  
  private static Integer[] getTwoOptions(Cell paramCell)
  {
    Integer[] arrayOfInteger = new Integer[2];
    return (Integer[])paramCell.getPossibilities().values().toArray(arrayOfInteger);
  }
  
  public static void run(Grid paramGrid)
  {
    Iterator localIterator1 = paramGrid.getBoxes().iterator();
    for (;;)
    {
      if (!localIterator1.hasNext()) {
        return;
      }
      ArrayList localArrayList = findTwoOptions((Box)localIterator1.next());
      if (localArrayList.size() >= 2)
      {
        Iterator localIterator2 = sameLineTwoOptionCells(localArrayList).entrySet().iterator();
        while (localIterator2.hasNext())
        {
          Map.Entry localEntry = (Map.Entry)localIterator2.next();
          Cell localCell1 = (Cell)localEntry.getKey();
          Cell localCell2 = (Cell)localEntry.getValue();
          if ((localCell1.getValue() == 0) && (localCell2.getValue() == 0)) {
            updatePossibilities(localCell1, localCell2);
          }
        }
      }
    }
  }
  
  private static HashMap<Cell, Cell> sameLineTwoOptionCells(ArrayList<Cell> paramArrayList)
  {
    HashMap localHashMap = new HashMap();
    Iterator localIterator1 = paramArrayList.iterator();
    for (;;)
    {
      if (!localIterator1.hasNext()) {
        return localHashMap;
      }
      Cell localCell1 = (Cell)localIterator1.next();
      Iterator localIterator2 = paramArrayList.iterator();
      while (localIterator2.hasNext())
      {
        Cell localCell2 = (Cell)localIterator2.next();
        if ((validTwoOptionsPair(localCell1, localCell2)) && (!localHashMap.containsKey(localCell2))) {
          localHashMap.put(localCell1, localCell2);
        }
      }
    }
  }
  
  private static boolean sameOptions(Integer[] paramArrayOfInteger1, Integer[] paramArrayOfInteger2)
  {
    int i = Math.min(paramArrayOfInteger1[0].intValue(), paramArrayOfInteger1[1].intValue());
    int j = Math.max(paramArrayOfInteger1[0].intValue(), paramArrayOfInteger1[1].intValue());
    int k = Math.min(paramArrayOfInteger2[0].intValue(), paramArrayOfInteger2[1].intValue());
    int m = Math.max(paramArrayOfInteger2[0].intValue(), paramArrayOfInteger2[1].intValue());
    return (i == k) && (j == m);
  }
  
  private static void updateCollectionPossibilities(Cell paramCell1, Cell paramCell2, Integer[] paramArrayOfInteger, Line paramLine)
  {
    Iterator localIterator = paramLine.getCells().iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return;
      }
      Cell localCell = (Cell)localIterator.next();
      if ((localCell.getPosition() != paramCell1.getPosition()) && (localCell.getPosition() != paramCell2.getPosition()))
      {
        localCell.updatePossibility(paramArrayOfInteger[0].intValue());
        localCell.updatePossibility(paramArrayOfInteger[1].intValue());
      }
    }
  }
  
  private static void updatePossibilities(Cell paramCell1, Cell paramCell2)
  {
    Integer[] arrayOfInteger = getTwoOptions(paramCell1);
    if (paramCell1.getRow().getPosition() == paramCell2.getRow().getPosition())
    {
      updateCollectionPossibilities(paramCell1, paramCell2, arrayOfInteger, paramCell1.getRow());
      return;
    }
    updateCollectionPossibilities(paramCell1, paramCell2, arrayOfInteger, paramCell1.getColumn());
  }
  
  private static boolean validTwoOptionsPair(Cell paramCell1, Cell paramCell2)
  {
    return (!paramCell1.equals(paramCell2)) && ((paramCell1.getRow().equals(paramCell2.getRow())) || (paramCell1.getColumn().equals(paramCell2.getColumn()))) && (paramCell1.getPossibilities().size() == 2) && (paramCell2.getPossibilities().size() == 2) && (sameOptions(getTwoOptions(paramCell1), getTwoOptions(paramCell2)));
  }
}


/* Location:           C:\Users\HAL\Documents\sudosudoku\com.example.helloopencv_1.0.apk\classes_dex2jar.jar
 * Qualified Name:     algorithms.TwoOptions
 * JD-Core Version:    0.7.0.1
 */