package ssuimobile.gameengine;

import ssuimobile.gameengine.event.FSMEvent;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public interface GameCharacter {
	public GameEnginePreBase getOwner(); 
	public int getCharacterIndex();
	public float getX();
	public void setX(float xv);
	public float getY(); 
	public void setY(float yv);
	public float getW();
	public void setW(float wv);
	public float getH();
	public void setH(float hv);
	public Bitmap getImage();
	public void setImage(Bitmap newImage);
	public static final int START_STATE = 0;
	public int getCurrentState();
	public void resetState();
	public boolean deliverEvent(FSMEvent event);  
	  // returns true if event was "consumed" (acted upon)
	public void draw(Canvas canv);  
	//xx local coordinates will have been set up prior to calling this 
	//   method, so the top left will be at 0,0 (not x,y)
}
