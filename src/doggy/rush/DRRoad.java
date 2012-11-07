package doggy.rush;

import java.util.ArrayList;
import java.util.Arrays;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class DRRoad {

	private static final int SPEED_CONSTANT = 10;
	private static final int MAX_SPEED = 80;
	private static final int CARS_NUM = 3;
	private static final int CARS_COOLDOWN_MAX = 150;
	
	private DRSprite carSprites[]=new DRSprite[CARS_NUM];
	private DRSprite roadSprite;
	private ArrayList<Integer> occupiedLane;
	private double currentX[] = new double[CARS_NUM];
	private double currentY[] = new double[CARS_NUM];
	private int coolDown[] = new int[CARS_NUM];
	private int speed;
	private double scale;
	private boolean carExists;
	
	public DRRoad(Resources resources, BitmapFactory.Options options) {
		
		for (int i=0; i<CARS_NUM; i++){
			carSprites[i] = new DRSprite(BitmapFactory.decodeResource(resources, R.drawable.truck, options),2);
		}
		roadSprite = new DRSprite(BitmapFactory.decodeResource(resources, R.drawable.road, options),10);
		reset();
		scale = options.inTargetDensity/options.inDensity;
	}
	public void reset(){
		carExists=false;
		for (int i=0; i<CARS_NUM; i++){
			currentX[i] = -1000;
			coolDown[i] = (int)(Math.random()*CARS_COOLDOWN_MAX);
		}
		occupiedLane = new ArrayList<Integer>(Arrays.asList( -1, -1, -1 ));
	}
	public static double getLanePosition(int height, int lane){
		
		if(lane==0)
			return height/6;
		else if(lane==1)
			return height/2;
		else 
			return 5*height/6;
	
	}
	public void draw(Canvas canvas){
		
		roadSprite.drawRoad(canvas);
		if (!carExists) return;
		
		int canvasWidth=canvas.getWidth();
		for (int i=0; i<CARS_NUM; i++){
					
			if (currentX[i] <= -1*carSprites[i].getWidth()){//car moved away
				if (occupiedLane.contains(i)){//check lane has car
					occupiedLane.set(occupiedLane.indexOf(i), -1);//remove car from lane
					
				}else if (coolDown[i] <= 0){//lane does not has car and cooldown finishes
					
					int laneIndex=(int)Math.round(Math.random()*2);//pick a lane
					if (occupiedLane.get(laneIndex) == -1){//lane has no car
						
						occupiedLane.set(laneIndex,i);//register lane
						currentX[i]= canvasWidth;//init x
						currentY[i]=canvas.getHeight()*((double)(laneIndex*2+1)/6);//init y
						coolDown[i] = (int)(Math.random()*CARS_COOLDOWN_MAX);
					}
					
				}else{//no car on any lanes, but coolDown not finished
					coolDown[i]--;
				}
			}else{// car still here
				currentX[i]-=(int)(speed+Math.random()*speed);
				carSprites[i].drawObject(canvas, currentX[i], currentY[i]);
			}
			
		}
		
	}
	public boolean carHitDog(DRDog dog){
		
		for (int i=0; i<CARS_NUM; i++){
			if (dog.hitsObject(carSprites[i].getFrameRect())) return true;
		}
		return false;
	}
	public void carOn(){
		carExists = true;
	}
	public void setSpeedByScore(int score){
		double factor = scale*SPEED_CONSTANT+((double)score*SPEED_CONSTANT/5000);
		speed = (int) (factor+Math.random()*factor);
		if (speed>scale*MAX_SPEED) speed = (int) scale*MAX_SPEED;
	}
}
