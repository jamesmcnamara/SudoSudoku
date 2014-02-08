package algorithms;

import board.Cell;
import board.Grid;
import board.SparseArrayIter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class Solver
  extends Thread
{
  boolean continueSearch;
  boolean found;
  Grid grid;
  Solver master;
  Solver parent;
  int threadCount;
  int threadID;
  
  public Solver(Grid paramGrid, Solver paramSolver)
  {
    this.grid = paramGrid;
    this.continueSearch = true;
    this.parent = paramSolver;
    if (paramSolver == null)
    {
      this.master = this;
      this.found = false;
    }
    for (;;)
    {
      this.threadID = this.master.threadCount;
      incrementThreadCount();
      return;
      this.master = paramSolver.getMaster();
    }
  }
  
  void foundItBitches(Grid paramGrid)
  {
    try
    {
      this.master.grid = paramGrid;
      this.master.found = true;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public Grid getGrid()
  {
    try
    {
      Grid localGrid = this.master.grid;
      return localGrid;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  /* Error */
  Solver getMaster()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 26	algorithms/Solver:parent	Lalgorithms/Solver;
    //   6: astore_2
    //   7: aload_2
    //   8: ifnonnull +9 -> 17
    //   11: aload_0
    //   12: astore_3
    //   13: aload_0
    //   14: monitorexit
    //   15: aload_3
    //   16: areturn
    //   17: aload_0
    //   18: getfield 26	algorithms/Solver:parent	Lalgorithms/Solver;
    //   21: getfield 28	algorithms/Solver:master	Lalgorithms/Solver;
    //   24: astore_3
    //   25: goto -12 -> 13
    //   28: astore_1
    //   29: aload_0
    //   30: monitorexit
    //   31: aload_1
    //   32: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	33	0	this	Solver
    //   28	4	1	localObject	Object
    //   6	2	2	localSolver1	Solver
    //   12	13	3	localSolver2	Solver
    // Exception table:
    //   from	to	target	type
    //   2	7	28	finally
    //   17	25	28	finally
  }
  
  public int getThreadID()
  {
    return this.threadID;
  }
  
  boolean hasBeenFound()
  {
    try
    {
      boolean bool = this.master.found;
      return bool;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void incrementThreadCount()
  {
    try
    {
      Solver localSolver = this.master;
      localSolver.threadCount = (1 + localSolver.threadCount);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void killThreadAndChildren()
  {
    this.continueSearch = false;
  }
  
  /* Error */
  boolean parentSignalsContinue()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 26	algorithms/Solver:parent	Lalgorithms/Solver;
    //   6: ifnonnull +25 -> 31
    //   9: aload_0
    //   10: getfield 30	algorithms/Solver:found	Z
    //   13: istore 5
    //   15: iconst_0
    //   16: istore_3
    //   17: iload 5
    //   19: ifeq +7 -> 26
    //   22: aload_0
    //   23: monitorexit
    //   24: iload_3
    //   25: ireturn
    //   26: iconst_1
    //   27: istore_3
    //   28: goto -6 -> 22
    //   31: aload_0
    //   32: getfield 24	algorithms/Solver:continueSearch	Z
    //   35: istore_2
    //   36: iconst_0
    //   37: istore_3
    //   38: iload_2
    //   39: ifeq -17 -> 22
    //   42: aload_0
    //   43: getfield 26	algorithms/Solver:parent	Lalgorithms/Solver;
    //   46: invokevirtual 53	algorithms/Solver:parentSignalsContinue	()Z
    //   49: istore 4
    //   51: iconst_0
    //   52: istore_3
    //   53: iload 4
    //   55: ifeq -33 -> 22
    //   58: iconst_1
    //   59: istore_3
    //   60: goto -38 -> 22
    //   63: astore_1
    //   64: aload_0
    //   65: monitorexit
    //   66: aload_1
    //   67: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	68	0	this	Solver
    //   63	4	1	localObject	Object
    //   35	4	2	bool1	boolean
    //   16	44	3	bool2	boolean
    //   49	5	4	bool3	boolean
    //   13	5	5	bool4	boolean
    // Exception table:
    //   from	to	target	type
    //   2	15	63	finally
    //   31	36	63	finally
    //   42	51	63	finally
  }
  
  public void run()
  {
    int i;
    Object localObject1;
    Object localObject2;
    Iterator localIterator1;
    Iterator localIterator2;
    if (parentSignalsContinue())
    {
      i = 10;
      Integer[] arrayOfInteger = new Integer[9];
      arrayOfInteger[0] = Integer.valueOf(1);
      arrayOfInteger[1] = Integer.valueOf(2);
      arrayOfInteger[2] = Integer.valueOf(3);
      arrayOfInteger[3] = Integer.valueOf(4);
      arrayOfInteger[4] = Integer.valueOf(5);
      arrayOfInteger[5] = Integer.valueOf(6);
      arrayOfInteger[6] = Integer.valueOf(7);
      arrayOfInteger[7] = Integer.valueOf(8);
      arrayOfInteger[8] = Integer.valueOf(9);
      localObject1 = new ArrayList(Arrays.asList(arrayOfInteger));
      localObject2 = new Cell(0, null);
      localIterator1 = this.grid.getCells().iterator();
      if (localIterator1.hasNext()) {
        break label150;
      }
      localIterator2 = ((Collection)localObject1).iterator();
    }
    for (;;)
    {
      if (!localIterator2.hasNext())
      {
        return;
        label150:
        Cell localCell = (Cell)localIterator1.next();
        int j = localCell.getPossibilities().size();
        if ((j == 0) || (j >= i)) {
          break;
        }
        i = j;
        localObject2 = localCell;
        localObject1 = localCell.getPossibilities().values();
        break;
      }
      Integer localInteger = (Integer)localIterator2.next();
      Grid localGrid = new Grid(this.grid);
      ((Cell)localGrid.getCells().get(((Cell)localObject2).getPosition())).setValue(localInteger.intValue());
      new Solver(localGrid, this).start();
    }
  }
  
  public void start()
  {
    AlgorithmSuite.run(this.grid, this);
  }
}


/* Location:           C:\Users\HAL\Documents\sudosudoku\com.example.helloopencv_1.0.apk\classes_dex2jar.jar
 * Qualified Name:     algorithms.Solver
 * JD-Core Version:    0.7.0.1
 */