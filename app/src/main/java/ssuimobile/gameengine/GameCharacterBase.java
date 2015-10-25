package ssuimobile.gameengine;

import ssuimobile.gameengine.event.FSMEvent;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameCharacterBase extends GameCharacterPreBase {

	@Override
	public boolean deliverEvent(FSMEvent event) {
		return false;
	}

	@Override
	protected void makeFSMTransition(FSMTransition transition, FSMEvent evt) {
		
	}

	@Override
	public void draw(Canvas canv) {
		
	}


	public GameCharacterBase(
			GameEnginePreBase owner,
			int index,
			int x, int y, 
			int w, int h, 
			FSMState[] states, 
			Bitmap img) 
	{

	}

}
