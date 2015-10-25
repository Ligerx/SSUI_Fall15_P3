package ssuimobile.gameengine;

import ssuimobile.gameengine.action.FSMAction;
import ssuimobile.gameengine.event.FSMEvent;
import ssuimobile.gameengine.eventmatch.FSMEventMatcher;

/**
 * This class holds information describing one transition in a finite state
 * machine.
 *
 */
public class FSMTransition {
	protected FSMEventMatcher _matcher = null;
	public boolean match(FSMEvent evt) {
		return _matcher != null && _matcher.match(evt);
	}
	
	protected FSMAction[] _action = null;
	public FSMAction[] getAction() {return _action;}
	
	protected int _targetState = 0;
	public int getTargetState() {return _targetState;}
	
	public FSMTransition(
			FSMEventMatcher matcher, 
			FSMAction[]       act, 
			int             targetSt) 
	{
		_matcher = matcher;
		_action = act;
		_targetState = targetSt;
	}
}
