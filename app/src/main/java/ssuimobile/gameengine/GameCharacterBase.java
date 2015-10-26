package ssuimobile.gameengine;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import ssuimobile.gameengine.action.FSMAction;
import ssuimobile.gameengine.event.FSMEvent;

public class GameCharacterBase extends GameCharacterPreBase {

	@Override
	public boolean deliverEvent(FSMEvent event) {
		// Guard if there are no states
		if(_FSMStateTable == null || _FSMStateTable.length == 0) return false;

		// Use the current state
		int stateNum = getCurrentState();
		FSMState state = _FSMStateTable[stateNum];

		// Look for a transition that takes the event
		for(int j = 0; j < state._transitions.length; j++) {
			FSMTransition transition = state.getTransitionAt(j);
			if(transition.match(event)) {
				// Call the transition and return true to signify consuming the event
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

		for(FSMAction action : actions) {
			handler.handleAction(action, this, evt);
		}

		// Update the state after running all transition actions
		_currentState = targetState;
	}

	@Override
	public void draw(Canvas canv) {
		// by default, getImage has a null value, so this should be safe
		canv.drawBitmap(getImage(), getX(), getY(), null);
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
