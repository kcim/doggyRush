package doggy.rush;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class DRDog {

	private static final int POWER_TOTAL = 400;
	private static final int FIRE_CONSUME_POWER = 40;
	private static final int FIRE_TOTAL_TIME = 20;
	private static final int REST_TOTAL_TIME = 50;
	private static final int DOG_SPEED = 25;
	public static final int DOG_NUM = 3;
	
	private DRSprite dogs[]=new DRSprite[3], fire;
	private double targetX, targetY, currentX, currentY;
	private int fireRemaining, dogsRemaining, restRemaining, power;
	private double scale;
	
	public DRDog(Resources resources, BitmapFactory.Options options) {

		dogs[0] = new DRSprite(BitmapFactory.decodeResource(resources, R.drawable.dog1, options),8);
		dogs[1] = new DRSprite(BitmapFactory.decodeResource(resources, R.drawable.dog2, options),8);
		dogs[2] = new DRSprite(BitmapFactory.decodeResource(resources, R.drawable.dog3, options),8);
		fire = new DRSprite(BitmapFactory.decodeResource(resources, R.drawable.fire, options),2);
		reset();
		scale = options.inTargetDensity/options.inDensity;
	}
	public void reset(){
		power = POWER_TOTAL;
		fireRemaining = 0;
		restRemaining = 0;
		dogsRemaining = 3;
	}

	public void draw(Canvas canvas){
		
		this.updateStatus();
		int alpha = restRemaining>0 ? 150 : 255;
		
		double offset = 0;
		if (dogsRemaining>0){
			offset = dogs[1].getWidth()*0.8+dogs[2].getWidth();
			dogs[0].drawObject(canvas, currentX, currentY, alpha);
		}
		if (dogsRemaining>1){
			offset = dogs[2].getWidth()*0.8;
			dogs[1].drawObject(canvas, currentX+dogs[0].getWidth()*0.8, currentY, alpha);
		}
		if (dogsRemaining>2){
			offset = 0;
			dogs[2].drawObject(canvas, currentX+dogs[1].getWidth()*0.8+dogs[0].getWidth()*0.8, currentY, alpha);
		}
		if (fireRemaining>0) fire.drawObject(canvas, currentX-offset, currentY, 150);
	}
	
	public void setCurrentPosition(double x, double y){
		
		currentX = x; currentY = y;
	}
	
	private void updateStatus(){
		
		double yDiff=currentY - targetY;
		currentY = (yDiff > DOG_SPEED/2) ? currentY-scale*DOG_SPEED : (yDiff < DOG_SPEED/-2) ? currentY+scale*DOG_SPEED : targetY;
		
		double xDiff=currentX - targetX;
		currentX = (xDiff > DOG_SPEED/2) ? currentX-scale*DOG_SPEED : (xDiff < DOG_SPEED/-2) ? currentX+scale*DOG_SPEED : targetX;
		
		if (fireRemaining > 0) fireRemaining--;
		if (restRemaining > 0) restRemaining--;
		power = power+1>=POWER_TOTAL ? POWER_TOTAL : power+1;
		
	}
	
	public void setTargetX(double x){
		targetX=x;
	}
	public void setTargetY(double y){
		targetY=y;
	}
	public boolean hitsObject(Rect object){
		
		Integer dogGroupX[] = new Integer[4], dogGroupY[] = new Integer[4];
		dogGroupX[0] = dogGroupX[1] = dogs[dogsRemaining-1].getFrameRect().left;
		dogGroupX[2] = dogGroupX[3] = dogs[dogsRemaining-1].getFrameRect().right;
		dogGroupY[0] = dogGroupY[3] = dogs[dogsRemaining-1].getFrameRect().top;
		dogGroupY[1] = dogGroupY[2] = dogs[dogsRemaining-1].getFrameRect().bottom;
		
		for(int j=0; j<4;j++){//4 corners
			if(dogGroupX[j] > object.left + 25*scale && dogGroupX[j] < object.right - 25*scale && dogGroupY[j] > object.top + 25*scale && dogGroupY[j] < object.bottom - 25*scale)
				return true;
		}
		return false;
	}

	public void setOnFire(){
		if (power>=FIRE_CONSUME_POWER && fireRemaining<FIRE_TOTAL_TIME) fireRemaining = FIRE_TOTAL_TIME;
		power = power-FIRE_CONSUME_POWER <= 0 ? 0 : power-FIRE_CONSUME_POWER;
	}
	public double getPowerRatio(){
		return (double)power/POWER_TOTAL;
	}
	public boolean canKill(){
		return fireRemaining==0 && restRemaining==0;
	}
	public int kill(){
		restRemaining = REST_TOTAL_TIME;
		return --dogsRemaining;
	}
	public void revive(){
		if (dogsRemaining<DOG_NUM){
			dogsRemaining++;
		}
		fireRemaining=150;
	}
	public void fillPower(){
		fireRemaining=150;
		power = POWER_TOTAL;
	}
	public int getDogsRemaining(){
		return dogsRemaining;
	}
	
}
