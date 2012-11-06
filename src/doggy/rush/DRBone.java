package doggy.rush;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

public class DRBone{
	
	private static final int FOOD_SPEED = 15;
	private static final int COOLDOWN_MAX = 360;
	
	private DRSprite foodSprite;
	private double currentX, currentY;
	private double scale;
	private int coolDown;
	private boolean consumed;
	
public DRBone(Resources resources, BitmapFactory.Options options, int id , int frameNumber) {
		
		foodSprite = new DRSprite(BitmapFactory.decodeResource(resources, id, options),frameNumber);
		scale = options.inTargetDensity/options.inDensity;
		currentX = -1000;
		coolDown = (int) Math.random()*COOLDOWN_MAX;
		consumed = false;
	}
	public void reset(){
		currentX = -1000;
		if (coolDown == 0) coolDown = (int)Math.random()*COOLDOWN_MAX;
	}
	public void draw(Canvas canvas){
		
		if (consumed){
			if (coolDown > 0){
				coolDown--;
				return;
			}else{
				consumed = false;
			}
		}

		if (currentX < -1*foodSprite.getWidth()){//food moved away
			if (coolDown == 0){
				currentY = DRRoad.getLanePosition(canvas.getHeight(), (int)Math.round(Math.random()*3));
				currentX = canvas.getWidth();
				coolDown = (int)Math.round(Math.random()*1000);
			}else{
				coolDown--;
			}
			//Log.d("DRFood", "CD: "+coolDown);
		}else{
			Log.d("DRFood", "drawFood!");
			currentX-=scale*FOOD_SPEED;
			foodSprite.drawObject(canvas, currentX, currentY);
		}
	}
	public boolean isTouched(float x,float y){
		if (foodSprite.isCoveringPoint(x, y) && !consumed){
			consumed = true;
			return true;
		}
		return false;
	}

	
}
