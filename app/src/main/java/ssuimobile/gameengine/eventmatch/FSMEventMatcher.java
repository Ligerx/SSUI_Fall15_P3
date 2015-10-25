/**
 * 
 */
package ssuimobile.gameengine.eventmatch;

import ssuimobile.gameengine.event.FSMEvent;

/**
 *
 */
public interface FSMEventMatcher {
	public boolean match(FSMEvent evt);
}
