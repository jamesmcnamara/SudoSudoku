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

/**CLASS:<br>
 * Handles the Main display of the app, as well as startup and destroy
 * @author james
 */
public class MainActivity
extends Activity {

	public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/SudoSudoku/";
	public static final String GRABBED_BOARD = "Photo taken";
	private static final String TAG = "HelloOpenCV::Activity";
	public static final String TRAINED_DATA = "tessdata/eng.traineddata";
	protected Button button;

	protected String path;
	protected boolean taken;

	private void initLayout() {
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

	/**ERROR:<br>
	 * Alert the user if the photo could not be digitized, and gives 
	 * the user the option to retake the photo or exit the activity 
	 * @param exception the error the was raised
	 */
	public void displayDialog(String exception) {
		final Context cxt = this;
		new AlertDialog.Builder(this).setTitle("Error").setMessage(exception).setPositiveButton("Retake Photo", 
				new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				Intent intent = new Intent(cxt, MainActivity.class);
				MainActivity.this.startActivity(intent);
			}
		}).create().show();
	}

	/**EFFECT:<br>
	 * If the photo was successfully digitized, start the next activities
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "Result was " + resultCode);
		if (resultCode == -1) {
			onPhotoTaken();
			return;
		}
		Log.v("HelloOpenCV::Activity", "User cancelled sudoku grab");
	}

	/**OVERRIDE:<br>
	 * 
	 */
	protected void onCreate(Bundle bundle) {
		int i = 0;
		String[] paths = {DATA_PATH, DATA_PATH + "tessdata/"};

		//Make sure that the file with all of the training data for Tesseract
		//has been copied over to the SD card
		for (String path : paths) {
			File f = new File (path);
			if (! f.exists()) {
				f.mkdirs();
			}
			try {
				if (!new File(DATA_PATH + TRAINED_DATA).exists()) {

					AssetManager assetManager = getAssets();
					InputStream ocrData = getAssets().open(TRAINED_DATA);
					OutputStream out = new FileOutputStream(DATA_PATH + TRAINED_DATA);
					byte [] buffer = new byte[1024];
					int amt; 
					while((amt = ocrData.read(buffer)) > 0) {
						out.write(buffer);
					}
					ocrData.close();
					out.close();

				}
			} catch (Exception e) {
				Log.v(TAG, "Could not copy trained data to SD");
			}
			
			super.onCreate(bundle);
			Log.v(TAG, "On create completed successfully");
		}
	}

	public boolean onCreateOptionsMenu(Menu paramMenu)
	{
		getMenuInflater().inflate(2131230721, paramMenu);
		return true;
	}

	/**CALLBACK:<br>
	 * After the user takes a picture, attempts to start the board processor and OCR engine
	 * to digitize the board, and start the BoardViewActivity
	 * Catches errors and displays them to the user
	 */
	protected void onPhotoTaken() {
		try	{
			CellOCR ocr = new CellOCR(new BoardProcessor(Highgui.imread(this.path)).processPhoto(), DATA_PATH);
			Intent intent = new Intent(this, BoardView.class);
			intent.putExtra("values", ocr.getPuzzleText());
			startActivity(intent);
		}
		catch (RuntimeException e) {
			displayDialog(e.getMessage());
		}
	}

	/**OVERRIDE:<br>
	 * Call super
	 */
	public void onResume() {
		super.onResume();
	}

	/**EFFECT:<br>
	 * Sets the given text view to have the greatest font ever devised,
	 * Ubuntu Monospaced Bold
	 * @param textView
	 */
	public void setFontUbuntu(TextView textView)
	{
		textView.setTypeface(Typeface.createFromAsset(getAssets(), "UbuntuMono-B.ttf"));
	}

	/**EFFECT:<br>
	 * If the user clicks the skip button, activate the BoardView activity
	 * with a dummy Sudoku board hard coded in 
	 * @param view the current view
	 */
	public void skip(View view) {
		Intent intent = new Intent(this, BoardView.class);
		intent.putExtra("values", "000009000002805370000030040030007000007490050000200000050000700800051030041900580");
		startActivity(intent);
	}

	/**EFFECT:<br>
	 * Starts the user's phone's capture image activity, and tracks whether
	 * it ends with a user taking a picture or not
	 * @param view the current view
	 */
	public void startCameraActivity(View view) {
		Uri puzzle = Uri.fromFile(new File(this.path));
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		intent.putExtra("output", puzzle);
		startActivityForResult(intent, 0);
	}
}
