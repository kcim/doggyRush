package doggy.rush;

import android.graphics.Canvas;
import android.util.Log;


public class DRThread extends Thread{
	static final int fps=30;
	private DRView view;
	private boolean isRunning=false;
	
	public DRThread(DRView view){
		this.view=view;
	}
	
	public void setRunning(boolean run){
		isRunning=run;
	}
	
	@Override
	public void run(){
			int tps = 1000/fps;
			long startTime;
			long sleepTime;
			while (isRunning){
				Canvas c=null;
				startTime=System.currentTimeMillis();
				try{
					c=view.getHolder().lockCanvas();
					synchronized (view.getHolder()){
						view.onDraw(c);
					}
				}finally{
					if (c!=null){
						view.getHolder().unlockCanvasAndPost(c);
					}
				}
				sleepTime=tps-(System.currentTimeMillis()-startTime);
				try{
					if (sleepTime>0)
						sleep(sleepTime);
					else
							sleep(10);
				}catch (Exception e){}
				
			}
			Log.d("Thread","exiting");
	}
}
