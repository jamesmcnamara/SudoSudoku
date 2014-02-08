package board;

import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Grid
{
  private ArrayList<Box> boxes = new ArrayList();
  private ArrayList<Cell> cells = new ArrayList();
  private ArrayList<Line> columns = new ArrayList();
  private int filledCount;
  private ArrayList<Line> rows = new ArrayList();
  
  public Grid()
  {
    for (int i = 0;; i++)
    {
      if (i >= 81)
      {
        makeBoxes();
        makeLines();
        return;
      }
      this.cells.add(new Cell(i, this));
    }
  }
  
  public Grid(Grid paramGrid)
  {
    this();
    setValues(paramGrid.getValues());
    Iterator localIterator = getCells().iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return;
      }
      Cell localCell = (Cell)localIterator.next();
      int i = localCell.getPosition();
      localCell.setPossibilities(((Cell)paramGrid.getCells().get(i)).getPossibilities());
    }
  }
  
  private boolean checkRepLine(CellCollection paramCellCollection)
  {
    ArrayList localArrayList = getTrueArray();
    boolean bool = true;
    Iterator localIterator1 = paramCellCollection.getCells().iterator();
    Iterator localIterator2;
    Cell localCell;
    do
    {
      if (!localIterator1.hasNext())
      {
        localIterator2 = paramCellCollection.getCells().iterator();
        if (localIterator2.hasNext()) {
          break;
        }
        return bool;
      }
      localCell = (Cell)localIterator1.next();
    } while (localCell.getValue() == 0);
    if ((bool) && (((Boolean)localArrayList.get(localCell.getValue())).booleanValue())) {}
    for (bool = true;; bool = false)
    {
      localArrayList.set(localCell.getValue(), Boolean.valueOf(false));
      break;
    }
    Iterator localIterator3 = ((Cell)localIterator2.next()).getPossibilities().values().iterator();
    label135:
    if (localIterator3.hasNext())
    {
      Integer localInteger = (Integer)localIterator3.next();
      if ((!bool) || (!((Boolean)localArrayList.get(localInteger.intValue())).booleanValue())) {
        break label184;
      }
    }
    label184:
    for (bool = true;; bool = false)
    {
      break label135;
      break;
    }
  }
  
  private static ArrayList<Boolean> getTrueArray()
  {
    ArrayList localArrayList = new ArrayList();
    for (int i = 0;; i++)
    {
      if (i > 9) {
        return localArrayList;
      }
      localArrayList.add(Boolean.valueOf(true));
    }
  }
  
  public static String integerArrayToString(Integer[][] paramArrayOfInteger)
  {
    String str = "";
    int i = paramArrayOfInteger.length;
    int j = 0;
    if (j >= i) {
      return str;
    }
    Integer[] arrayOfInteger = paramArrayOfInteger[j];
    int k = arrayOfInteger.length;
    for (int m = 0;; m++)
    {
      if (m >= k)
      {
        j++;
        break;
      }
      Integer localInteger = arrayOfInteger[m];
      str = str + localInteger;
    }
  }
  
  private Box makeBox(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 27 * (paramInt / 3) + 3 * (paramInt % 3);
    int j = 0;
    if (j >= 3) {
      return new Box(i, localArrayList);
    }
    for (int k = 0;; k++)
    {
      if (k >= 3)
      {
        j++;
        break;
      }
      int m = k + (i + j * 9);
      localArrayList.add((Cell)this.cells.get(m));
    }
  }
  
  private void makeBoxes()
  {
    int i = 0;
    if (i >= 9) {
      return;
    }
    Box localBox = makeBox(i);
    Iterator localIterator = localBox.getCells().iterator();
    for (;;)
    {
      if (!localIterator.hasNext())
      {
        this.boxes.add(localBox);
        i++;
        break;
      }
      ((Cell)localIterator.next()).setBox(localBox);
    }
  }
  
  private Line makeLine(int paramInt, boolean paramBoolean)
  {
    ArrayList localArrayList = new ArrayList();
    int j;
    if (paramBoolean)
    {
      j = 0;
      if (j < 9) {}
    }
    for (;;)
    {
      return new Line(paramInt, localArrayList, paramBoolean);
      localArrayList.add((Cell)this.cells.get(j + paramInt * 9));
      j++;
      break;
      for (int i = 0; i < 9; i++) {
        localArrayList.add((Cell)this.cells.get(paramInt + i * 9));
      }
    }
  }
  
  private void makeLines()
  {
    int i = 0;
    if (i >= 9) {
      return;
    }
    Line localLine1 = makeLine(i, true);
    Iterator localIterator1 = localLine1.getCells().iterator();
    label24:
    Line localLine2;
    Iterator localIterator2;
    if (!localIterator1.hasNext())
    {
      this.rows.add(localLine1);
      localLine2 = makeLine(i, false);
      localIterator2 = localLine2.getCells().iterator();
    }
    for (;;)
    {
      if (!localIterator2.hasNext())
      {
        this.columns.add(localLine2);
        i++;
        break;
        ((Cell)localIterator1.next()).setRow(localLine1);
        break label24;
      }
      ((Cell)localIterator2.next()).setColumn(localLine2);
    }
  }
  
  public static Integer[][] stringToIntegerArray(String paramString)
  {
    if (paramString.length() == 81)
    {
      ArrayList localArrayList = new ArrayList();
      Integer[][] arrayOfInteger = (Integer[][])Array.newInstance(Integer.class, new int[] { 9, 9 });
      int i = 0;
      int j = 0;
      if (j >= 9) {
        return arrayOfInteger;
      }
      localArrayList.clear();
      for (int k = 0;; k++)
      {
        if (k >= 9)
        {
          localArrayList.toArray(arrayOfInteger[j]);
          j++;
          break;
        }
        localArrayList.add(Integer.valueOf(Integer.parseInt(paramString.substring(i, i + 1))));
        i++;
      }
    }
    throw new RuntimeException("Values string is not long enough");
  }
  
  public boolean equals(Grid paramGrid)
  {
    return this.cells.equals(paramGrid.cells);
  }
  
  public ArrayList<Box> getBoxes()
  {
    return this.boxes;
  }
  
  public ArrayList<Cell> getCells()
  {
    return this.cells;
  }
  
  public ArrayList<Line> getColumns()
  {
    return this.columns;
  }
  
  public int getFilledCount()
  {
    return this.filledCount;
  }
  
  public int getPossibilitiesCount()
  {
    int i = 0;
    Iterator localIterator = getCells().iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return i;
      }
      i += ((Cell)localIterator.next()).getPossibilities().size();
    }
  }
  
  public ArrayList<Line> getRows()
  {
    return this.rows;
  }
  
  public Integer[][] getValues()
  {
    Integer[][] arrayOfInteger = (Integer[][])Array.newInstance(Integer.class, new int[] { 9, 9 });
    int i = 0;
    if (i >= 9) {
      return arrayOfInteger;
    }
    for (int j = 0;; j++)
    {
      if (j >= 9)
      {
        i++;
        break;
      }
      arrayOfInteger[i][j] = Integer.valueOf(((Cell)getCells().get(j + i * 9)).getValue());
    }
  }
  
  public void incrementFilled()
  {
    this.filledCount = (1 + this.filledCount);
  }
  
  public String prettyPrint()
  {
    String str = "";
    Iterator localIterator = getRows().iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return str;
      }
      Line localLine = (Line)localIterator.next();
      str = str + localLine.toString();
    }
  }
  
  public boolean repOK()
  {
    boolean bool = true;
    Iterator localIterator1 = getRows().iterator();
    Iterator localIterator2;
    Iterator localIterator3;
    if (!localIterator1.hasNext())
    {
      localIterator2 = getColumns().iterator();
      if (!localIterator2.hasNext())
      {
        localIterator3 = getBoxes().iterator();
        if (localIterator3.hasNext()) {
          break label126;
        }
        return bool;
      }
    }
    else
    {
      Line localLine1 = (Line)localIterator1.next();
      if ((bool) && (checkRepLine(localLine1))) {}
      for (bool = true;; bool = false) {
        break;
      }
    }
    Line localLine2 = (Line)localIterator2.next();
    if ((bool) && (checkRepLine(localLine2))) {}
    for (bool = true;; bool = false) {
      break;
    }
    label126:
    Box localBox = (Box)localIterator3.next();
    if ((bool) && (checkRepLine(localBox))) {}
    for (bool = true;; bool = false) {
      break;
    }
  }
  
  public void setBoxes(ArrayList<Box> paramArrayList)
  {
    this.boxes = paramArrayList;
  }
  
  public void setCells(ArrayList<Cell> paramArrayList)
  {
    this.cells = paramArrayList;
  }
  
  public void setColumns(ArrayList<Line> paramArrayList)
  {
    this.columns = paramArrayList;
  }
  
  public void setRows(ArrayList<Line> paramArrayList)
  {
    this.rows = paramArrayList;
  }
  
  public void setValues(Integer[][] paramArrayOfInteger)
  {
    int i = 0;
    if (i >= 9) {
      return;
    }
    int j = 0;
    for (;;)
    {
      if (j >= 9)
      {
        i++;
        break;
      }
      try
      {
        ((Cell)this.cells.get(j + i * 9)).setValue(paramArrayOfInteger[i][j].intValue());
        j++;
      }
      catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
      {
        System.out.println(localIndexOutOfBoundsException.getLocalizedMessage());
        System.out.println("Values argument requires dimesions[9][9], but has dimensions[" + paramArrayOfInteger.length + "][" + paramArrayOfInteger[1].length + "]");
      }
    }
  }
  
  public boolean solved()
  {
    return (repOK()) && (getFilledCount() == 81);
  }
  
  public String toString()
  {
    String str = "";
    Iterator localIterator = getCells().iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return str;
      }
      Cell localCell = (Cell)localIterator.next();
      str = str + localCell.getValue();
    }
  }
}


/* Location:           C:\Users\HAL\Documents\sudosudoku\com.example.helloopencv_1.0.apk\classes_dex2jar.jar
 * Qualified Name:     board.Grid
 * JD-Core Version:    0.7.0.1
 */