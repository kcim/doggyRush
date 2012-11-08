package doggy.rush;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

public class DRBone{
	
	private static final int FOOD_SPEED = 15;
	private static final int COOLDOWN_MAX = 700;
	private static final int COOLDOWN_MIN = 300;
	
	private DRSprite foodSprite;
	private double currentX, currentY;
	private double scale;
	private int coolDown = 0;
	private int coolDownScale;
	private boolean consumed;
	
public DRBone(Resources resources, BitmapFactory.Options options, int id , int frameNumber) {
		
		foodSprite = new DRSprite(BitmapFactory.decodeResource(resources, id, options),frameNumber);
		scale = options.inTargetDensity/options.inDensity;
		reset();
		consumed = false;
	}
	public void reset(){
		currentX = -1000;
		coolDownScale = 1;
		if (coolDown <= 0) coolDown = COOLDOWN_MIN+(int)Math.random()*COOLDOWN_MAX;
	}
	public void draw(Canvas canvas){
		
		if (currentX < -1*foodSprite.getWidth()){//food moved away
			if (coolDown <= 0){
				consumed = false;
				currentY = DRRoad.getLanePosition(canvas.getHeight(), (int)Math.round(Math.random()*3));
				currentX = canvas.getWidth();
				coolDown = COOLDOWN_MIN+(int)Math.round(Math.random()*COOLDOWN_MAX);
			}else{
				coolDown-=coolDownScale;
			}
			Log.d("DRFood", "CD: "+coolDown);
		}else{
			currentX-=scale*FOOD_SPEED;
			if (!consumed) foodSprite.drawObject(canvas, currentX, currentY);
		}
	}
	public boolean isTouched(float x,float y){
		if (foodSprite.isCoveringPoint(x, y) && !consumed){
			consumed = true;
			return true;
		}
		return false;
	}
	public void reduceCoolDown(){
		coolDownScale = 2;
	}

}
