package ssuimobile.gameengine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import ssuimobile.gameengine.action.FSMAction;
import ssuimobile.gameengine.event.FSMEvent;

public class GameCharacterBase extends GameCharacterPreBase {

	@Override
	public boolean deliverEvent(FSMEvent event) {
		Log.d("deliverEvent", "In deliverEvent");

		// Guard if there are no states
		if(_FSMStateTable == null || _FSMStateTable.length == 0) {
			Log.d("deliverEvent", "_FSMStateTable was null or empty, so escape");
			return false;
		}

		// Use the current state
		int stateNum = getCurrentState();
		FSMState state = _FSMStateTable[stateNum];

		Log.d("deliverEvent", "current state is: " + stateNum + " - " + state.getName());

		// Look for a transition that takes the event
		for(int j = 0; j < state._transitions.length; j++) {
			FSMTransition transition = state.getTransitionAt(j);
			if(transition.match(event)) {
				// Call the transition and return true to signify consuming the event
				Log.d("deliverEvent", "transition matches event");
				makeFSMTransition(transition, event);
				return true;
			}
		}

		return false;
	}

	@Override
	protected void makeFSMTransition(FSMTransition transition, FSMEvent evt) {
		FSMAction[] actions = transition.getAction();
		int targetState = transition.getTargetState();
		ActionHandler handler = new ActionHandler();

		Log.d("makeFSMTransition", "--------- Handling " + actions.length + " action(s): Character# "
				+ getCharacterIndex() + " ----------------");

		for(FSMAction action : actions) {
			Log.d("makeFSMTransition", "--- handling an action ---");
			handler.handleAction(action, this, evt);
		}

		// Update the state after running all transition actions
		_currentState = targetState;
	}

	@Override
	public void draw(Canvas canv) {
		// by default, getImage has a null value, so this should be safe
		canv.drawBitmap(getImage(), getX(), getY(), null);

		// The requirements say that the bitmap should stay original size
		// and that the character width and height should not transform the image.
	}


	public GameCharacterBase(
			GameEnginePreBase owner,
			int index,
			int x, int y, 
			int w, int h, 
			FSMState[] states, 
			Bitmap img) 
	{
		super(owner, index, x, y, w, h, states, img);
	}

}
