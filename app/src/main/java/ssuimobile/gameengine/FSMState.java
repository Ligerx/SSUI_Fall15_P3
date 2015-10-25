package ssuimobile.gameengine;

/**
 * This class holds information that describes a single finite state machine
 * state.
 *
 */
public class FSMState {
	protected int _stateNum = 0;
	public int getStateNum() {return _stateNum;}
	public boolean isStartState() {return _stateNum == 0;}
	
	protected String _name = "$UNNAMED$";
	public String getName() {return _name;}
	
	protected FSMTransition[] _transitions = null;
	public FSMTransition getTransitionAt(int indx) {
		if (_transitions == null || indx < 0 || indx >= _transitions.length)
			throw new IndexOutOfBoundsException("Index of " + indx + 
					" passed to FSMState.getTransitionAt() is out of bounds");
		return _transitions[indx];
	}

	public FSMState(int stNum, String stName, FSMTransition[] trans) {
		_stateNum = stNum;
		if (stName != null) _name = stName;
		_transitions = trans;
	}
}