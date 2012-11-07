package doggy.rush;

import android.app.AlertDialog.Builder;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class DRActivity extends Activity{
	
    /** Called when the activity is first created. */
	DRView view;
	SharedPreferences settings;
	GestureDetector gestureDetector;
	OnTouchListener gestureListener;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.main);
        view=(DRView) findViewById(R.id.DRView);
       
        settings = getSharedPreferences("Preferences", 0);
        view.setOldScore(settings.getInt("Score", 0));
 
        gestureDetector = new GestureDetector(new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            };
        };
        view.setOnTouchListener(gestureListener);
       // int ScrWidth  =  // Display width
       // Log.d("DoggyRush","Screen Width: "+ScrWidth);
        
    }


	@Override
	public void onPause(){
		super.onPause();
	} 
	
	public void onBackPressed(){
		if (view.back()){
			showExitDialog();
		}
	}
	
	private void showExitDialog(){
		 Builder MyAlertDialog = new AlertDialog.Builder(this);
		 MyAlertDialog.setTitle("Exit");
		 MyAlertDialog.setMessage("Are you sure?");
		 DialogInterface.OnClickListener alertClick = new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
			 	finish();
			}
		 };
		 MyAlertDialog.setPositiveButton("Yes",alertClick );
		 MyAlertDialog.setNegativeButton("No",null );
		 MyAlertDialog.show();
	}
	
	public void onDown(MotionEvent e){
		
		int score = view.touch(e.getX(), e.getY());
		if(score > 0){
			settings.edit().putInt("Score", score).commit();
			view.setOldScore(score);
		}
	}

	class MyGestureDetector extends SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 90;
        private static final int SWIPE_MAX_OFF_PATH = 300;
        private static final int SWIPE_THRESHOLD_VELOCITY = 150;
        private MotionEvent mLastOnDownEvent = null;
 
        @Override
        public boolean onDown(MotionEvent e) {
            //Android 4.0 bug means e1 in onFling may be NULL due to onLongPress eating it.
            mLastOnDownEvent = e;
            DRActivity.this.onDown(e);
            return true;
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1==null)
                e1 = mLastOnDownEvent;
            if (e1==null || e2==null)
                return false;
            float dX = e2.getX()-e1.getX();
            float dY = e1.getY()-e2.getY();
            if (Math.abs(dX)<SWIPE_MAX_OFF_PATH &&
                Math.abs(velocityY)>=SWIPE_THRESHOLD_VELOCITY &&
                Math.abs(dY)>=SWIPE_MIN_DISTANCE ) {
                if (dY>0) {
                	DRActivity.this.view.moveDogUp();
                	
                } else {
                	DRActivity.this.view.moveDogDown();

                }
                return true;
            }
            return false;
        }
    }
}
