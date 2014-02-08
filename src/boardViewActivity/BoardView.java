package boardViewActivity;

import algorithms.AlgorithmSuite;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

public class BoardView
  extends Activity
{
  public final String TAG = "BoardView::Activity";
  String boardValues;
  GridView gl;
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903040);
    this.boardValues = getIntent().getStringExtra("values");
    this.gl = ((GridView)findViewById(2130968580));
    this.gl.setAdapter(new BoardAdapter(this, this.boardValues));
    this.gl.setGravity(17);
    ((Button)findViewById(2130968581)).setTypeface(Typeface.createFromAsset(getAssets(), "UbuntuMono-B.ttf"));
  }
  
  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(2131230720, paramMenu);
    return true;
  }
  
  public void solvePuzzle(View paramView)
  {
    this.boardValues = AlgorithmSuite.solvePuzzle(((BoardAdapter)this.gl.getAdapter()).getValues());
    this.gl.setAdapter(new BoardAdapter(this, this.boardValues));
  }
}


/* Location:           C:\Users\HAL\Documents\sudosudoku\com.example.helloopencv_1.0.apk\classes_dex2jar.jar
 * Qualified Name:     boardViewActivity.BoardView
 * JD-Core Version:    0.7.0.1
 */