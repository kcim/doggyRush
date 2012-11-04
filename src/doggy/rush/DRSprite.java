package doggy.rush;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class DRSprite {
	
	private static final int COVER_THRESHOLD = 20;
	
	private Bitmap src;
	private int currentFrame=0;
	private int frameWidth, frameHeight;
	private int frameNumber=1;
	private int currentFramePosition=0;
	private Canvas canvas;
	private int x = -1000, y = -1000;

	
	public DRSprite(Bitmap src, int frameNumber){
		this.src=src;
		this.frameWidth=src.getWidth()/frameNumber;
		this.frameHeight=src.getHeight();
		this.frameNumber=frameNumber;
	}
	
	public int getWidth(){
		return frameWidth;
	}
	public int getHeight(){
		return frameHeight;
	}
	private void draw(int alpha){
		Rect frame=new Rect(currentFramePosition, 0, currentFramePosition+frameWidth, frameHeight);
		Rect target=new Rect(x, y ,x+frameWidth, y+frameHeight);
		Paint p=new Paint();
		p.setAlpha(alpha);
		canvas.drawBitmap(src, frame, target, p);
	}
	
	public Rect getFrameRect(){
		return new Rect(x, y ,x+frameWidth, y+frameHeight);
	}
	
	public void drawObject(Canvas canvas, double x, double y){
		currentFrame= (currentFrame+1)%frameNumber;
		currentFramePosition=currentFrame*frameWidth;
		this.y=(int)y-frameHeight/2;
		this.x=(int)x;
		this.canvas=canvas;
		draw(255);
	}
	
	public void drawObject(Canvas canvas, double x, double y, int alpha){
		currentFrame= (currentFrame+1)%frameNumber;
		currentFramePosition=currentFrame*frameWidth;
		this.y=(int)y-frameHeight/2;
		this.x=(int)x;
		this.canvas=canvas;
		draw(alpha);
	}
	
	
	public void drawObjectCentered(Canvas canvas, double x, double y){
		currentFrame= (currentFrame+1)%frameNumber;
		currentFramePosition=currentFrame*frameWidth;
		this.y=(int)y-frameHeight/2;
		this.x=(int)x-frameWidth/2;
		this.canvas=canvas;
		draw(255);
	}
	
	public boolean isCoveringPoint(float x, float y){
		Rect frame = this.getFrameRect();
		return (x >= frame.left-COVER_THRESHOLD && x <= frame.right+COVER_THRESHOLD && y <= frame.bottom+COVER_THRESHOLD && y>= frame.top-COVER_THRESHOLD);
		
	}
	public void drawRoad(Canvas canvas){
		this.frameWidth=src.getWidth()/2;
		currentFrame= (currentFrame+1)%frameNumber;
		currentFramePosition= frameWidth/frameNumber*(frameNumber-currentFrame);
		this.y=0;
		this.x=0;
		this.canvas=canvas;
		
		draw(255);
	}

}
