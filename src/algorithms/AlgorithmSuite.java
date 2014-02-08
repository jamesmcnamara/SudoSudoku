package algorithms;

import board.Grid;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

public class AlgorithmSuite
{
  public static boolean checkGridStatus(Grid paramGrid, Solver paramSolver)
  {
    if (paramGrid.solved())
    {
      paramSolver.foundItBitches(paramGrid);
      return true;
    }
    if (!paramGrid.repOK()) {
      paramSolver.killThreadAndChildren();
    }
    return false;
  }
  
  public static void loopRun(Grid[] paramArrayOfGrid)
  {
    long l1 = 0L;
    long l2 = 1000000L;
    long l3 = 0L;
    int i = 0;
    int j = 0;
    ArrayList localArrayList = new ArrayList();
    int k = paramArrayOfGrid.length;
    int m = 0;
    Iterator localIterator;
    if (m >= k)
    {
      System.out.println("*************STATS*************");
      System.out.println("Total Puzzles:" + i);
      System.out.println("Solved Puzzles: " + j);
      localIterator = localArrayList.iterator();
    }
    for (;;)
    {
      if (!localIterator.hasNext())
      {
        long l5 = l1 / localArrayList.size();
        System.out.println("Average time: " + l5 + " milliseconds");
        System.out.println("Minimum time: " + l2 + " milliseconds");
        System.out.println("Maximum time: " + l3 + " milliseconds");
        return;
        Grid localGrid = paramArrayOfGrid[m];
        i++;
        long l4 = System.currentTimeMillis();
        Solver localSolver = new Solver(localGrid, null);
        localSolver.start();
        while (localSolver.isAlive()) {}
        if (localSolver.getGrid().solved())
        {
          j++;
          System.out.println(localSolver.getGrid());
        }
        localArrayList.add(Long.valueOf(System.currentTimeMillis() - l4));
        m++;
        break;
      }
      Long localLong = (Long)localIterator.next();
      l1 += localLong.longValue();
      l2 = Math.min(l2, localLong.longValue());
      l3 = Math.max(l3, localLong.longValue());
    }
  }
  
  public static void run(Grid paramGrid, Solver paramSolver)
  {
    if (checkGridStatus(paramGrid, paramSolver)) {
      return;
    }
    new Solver(paramGrid, paramSolver).run();
  }
  
  public static String solvePuzzle(String paramString)
  {
    return solvePuzzle(Grid.stringToIntegerArray(paramString));
  }
  
  public static String solvePuzzle(Integer[][] paramArrayOfInteger)
  {
    Grid localGrid = new Grid();
    localGrid.setValues(paramArrayOfInteger);
    Solver localSolver = new Solver(localGrid, null);
    localSolver.start();
    while (localSolver.isAlive()) {}
    return localSolver.getGrid().toString();
  }
}


/* Location:           C:\Users\HAL\Documents\sudosudoku\com.example.helloopencv_1.0.apk\classes_dex2jar.jar
 * Qualified Name:     algorithms.AlgorithmSuite
 * JD-Core Version:    0.7.0.1
 */