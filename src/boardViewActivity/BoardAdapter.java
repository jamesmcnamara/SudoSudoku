package boardViewActivity;

import android.content.Context;
import android.content.res.Resources;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import board.Grid;
import java.lang.reflect.Array;

public class BoardAdapter
  extends BaseAdapter
{
  private Context context;
  private Integer[][] values = (Integer[][])Array.newInstance(Integer.class, new int[] { 9, 9 });
  
  public BoardAdapter(Context paramContext, String paramString)
  {
    this.context = paramContext;
    if (paramString.length() != 81) {
      throw new RuntimeException("String does not have enough values");
    }
    this.values = Grid.stringToIntegerArray(paramString);
  }
  
  private void formatCell(TextView paramTextView, int paramInt)
  {
    paramTextView.setBackground(this.context.getResources().getDrawable(2130837504));
  }
  
  public int getCount()
  {
    return 81;
  }
  
  public Integer getItem(int paramInt)
  {
    return this.values[(paramInt / 9)][(paramInt % 9)];
  }
  
  public long getItemId(int paramInt)
  {
    return paramInt / 9;
  }
  
  public Integer[][] getValues()
  {
    return this.values;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    EditText localEditText;
    if (paramView == null)
    {
      localEditText = new EditText(this.context);
      localEditText.setLayoutParams(new AbsListView.LayoutParams(100, 100));
      localEditText.setTextSize(18.0F);
      localEditText.setGravity(17);
      localEditText.setOnEditorActionListener(new CellEditListener(paramInt, null));
    }
    for (;;)
    {
      formatCell(localEditText, paramInt + 1);
      Integer localInteger = getItem(paramInt);
      if (localInteger.intValue() != 0) {
        localEditText.setText(localInteger.toString());
      }
      return localEditText;
      localEditText = (EditText)paramView;
    }
  }
  
  private class CellEditListener
    implements TextView.OnEditorActionListener
  {
    int position;
    
    private CellEditListener(int paramInt)
    {
      this.position = paramInt;
    }
    
    public boolean onEditorAction(TextView paramTextView, int paramInt, KeyEvent paramKeyEvent)
    {
      if (paramKeyEvent.getKeyCode() == 66)
      {
        String str = paramTextView.toString();
        if (str.matches("[0-9]")) {
          BoardAdapter.this.values[(this.position / 9)][(this.position % 9)] = Integer.valueOf(Integer.parseInt(str));
        }
      }
      return false;
    }
  }
}


/* Location:           C:\Users\HAL\Documents\sudosudoku\com.example.helloopencv_1.0.apk\classes_dex2jar.jar
 * Qualified Name:     boardViewActivity.BoardAdapter
 * JD-Core Version:    0.7.0.1
 */