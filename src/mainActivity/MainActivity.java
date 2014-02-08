package mainActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import boardViewActivity.BoardView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.OpenCVLoader;
import org.opencv.highgui.Highgui;

public class MainActivity
  extends Activity
{
  public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/SudoSudoku/";
  public static final String GRABBED_BOARD = "Photo taken";
  private static final String TAG = "HelloOpenCV::Activity";
  public static final String TRAINED_DATA = "tessdata/eng.traineddata";
  protected Button button;
  private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this)
  {
    public void onManagerConnected(int paramAnonymousInt)
    {
      switch (paramAnonymousInt)
      {
      default: 
        super.onManagerConnected(paramAnonymousInt);
        return;
      }
      Log.i("HelloOpenCV::Activity", "OpenCV Manager Intialized Successfully");
    }
  };
  protected String path;
  protected boolean taken;
  
  private void initLayout()
  {
    setContentView(2130903041);
    TextView localTextView1 = (TextView)findViewById(2130968583);
    TextView localTextView2 = (TextView)findViewById(2130968584);
    Button localButton = (Button)findViewById(2130968586);
    this.button = ((Button)findViewById(2130968585));
    setFontUbuntu(localTextView1);
    setFontUbuntu(localTextView2);
    setFontUbuntu(localButton);
    setFontUbuntu(this.button);
    this.path = (DATA_PATH + "/sudoku.jpg");
  }
  
  public void displayDialog(String paramString)
  {
    new AlertDialog.Builder(this).setTitle("Error").setMessage(paramString).setPositiveButton("Retake Photo", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        paramAnonymousDialogInterface.cancel();
      }
    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        Intent localIntent = new Intent(jdField_this, MainActivity.class);
        MainActivity.this.startActivity(localIntent);
      }
    }).create().show();
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    Log.i("HelloOpenCV::Activity", "Result was " + paramInt2);
    if (paramInt2 == -1)
    {
      onPhotoTaken();
      return;
    }
    Log.v("HelloOpenCV::Activity", "User cancelled sudoku grab");
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    int i = 0;
    String[] arrayOfString = new String[2];
    arrayOfString[0] = DATA_PATH;
    arrayOfString[1] = (DATA_PATH + "tessdata/");
    int j = arrayOfString.length;
    if ((i < j) || (!new File(DATA_PATH + "tessdata/eng.traineddata").exists())) {}
    for (;;)
    {
      try
      {
        InputStream localInputStream = getAssets().open("tessdata/eng.traineddata");
        localFileOutputStream = new FileOutputStream(DATA_PATH + "tessdata/eng.traineddata");
        arrayOfByte = new byte[1024];
        k = localInputStream.read(arrayOfByte);
        if (k > 0) {
          continue;
        }
        localInputStream.close();
        localFileOutputStream.close();
        Log.v("HelloOpenCV::Activity", "Copied Trained Data file to local SD");
      }
      catch (Exception localException)
      {
        FileOutputStream localFileOutputStream;
        byte[] arrayOfByte;
        int k;
        String str;
        File localFile;
        Log.e("HelloOpenCV::Activity", "Could not copy trained data to SD: " + localException);
        continue;
      }
      super.onCreate(paramBundle);
      initLayout();
      Log.v("HelloOpenCV::Activity", "On create completed");
      return;
      str = arrayOfString[i];
      localFile = new File(str);
      if (!localFile.exists())
      {
        if (!localFile.mkdirs())
        {
          Log.v("HelloOpenCV::Activity", "Error Creating: " + str);
          return;
        }
        Log.v("HelloOpenCV::Activity", str + " created successfully");
      }
      i++;
      break;
      localFileOutputStream.write(arrayOfByte, 0, k);
    }
  }
  
  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(2131230721, paramMenu);
    return true;
  }
  
  protected void onPhotoTaken()
  {
    try
    {
      CellOCR localCellOCR = new CellOCR(new BoardProcessor(Highgui.imread(this.path, 0)).processPhoto(), DATA_PATH);
      Intent localIntent = new Intent(this, BoardView.class);
      localIntent.putExtra("values", localCellOCR.getPuzzleText());
      startActivity(localIntent);
      return;
    }
    catch (RuntimeException localRuntimeException)
    {
      displayDialog(localRuntimeException.getMessage());
    }
  }
  
  public void onResume()
  {
    super.onResume();
    OpenCVLoader.initAsync("2.4.5", this, this.mLoaderCallback);
  }
  
  public void setFontUbuntu(TextView paramTextView)
  {
    paramTextView.setTypeface(Typeface.createFromAsset(getAssets(), "UbuntuMono-B.ttf"));
  }
  
  public void skip(View paramView)
  {
    Intent localIntent = new Intent(this, BoardView.class);
    localIntent.putExtra("values", "000009000002805370000030040030007000007490050000200000050000700800051030041900580");
    startActivity(localIntent);
  }
  
  public void startCameraActivity(View paramView)
  {
    Uri localUri = Uri.fromFile(new File(this.path));
    Intent localIntent = new Intent("android.media.action.IMAGE_CAPTURE");
    localIntent.putExtra("output", localUri);
    startActivityForResult(localIntent, 0);
  }
}


/* Location:           C:\Users\HAL\Documents\sudosudoku\com.example.helloopencv_1.0.apk\classes_dex2jar.jar
 * Qualified Name:     mainActivity.MainActivity
 * JD-Core Version:    0.7.0.1
 */