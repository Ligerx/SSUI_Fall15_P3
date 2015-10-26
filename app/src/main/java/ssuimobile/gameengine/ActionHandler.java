package ssuimobile.gameengine;

import android.graphics.Bitmap;
import android.util.Log;

import ssuimobile.gameengine.action.ChangeImageAction;
import ssuimobile.gameengine.action.DebugAction;
import ssuimobile.gameengine.action.FSMAction;
import ssuimobile.gameengine.action.FSMActionType;
import ssuimobile.gameengine.action.MoveIncAction;
import ssuimobile.gameengine.action.MoveToAction;
import ssuimobile.gameengine.action.RunAnimAction;
import ssuimobile.gameengine.action.SendMessageAction;
import ssuimobile.gameengine.event.FSMEvent;
import ssuimobile.gameengine.event.XYEvent;

public class ActionHandler {

    public void handleAction(FSMAction action, GameCharacter character, FSMEvent event) {
        if (action.getType() == FSMActionType.CHANGE_IMAGE) {
            changeImage(character, ((ChangeImageAction) action).getImage());
        }
        else if(action.getType() == FSMActionType.MOVE_TO) {
            moveTo((MoveToAction) action, character);
        }
        else if(action.getType() == FSMActionType.MOVE_INC) {
            moveInc((MoveIncAction) action, character);
        }
        else if(action.getType() == FSMActionType.FOLLOW_EVENT_POSITION) {
            followEventPosition(character, event);
        }
        else if(action.getType() == FSMActionType.GET_DRAG_FOCUS) {
            getDragFocus(character);
        }
        else if(action.getType() == FSMActionType.DROP_DRAG_FOCUS) {
            dropDragFocus(character);
        }
        else if(action.getType() == FSMActionType.SEND_MESSAGE) {
            sendMessage((SendMessageAction) action, character);
        }
        else if(action.getType() == FSMActionType.DEBUG_MESSAGE) {
            debugMessage((DebugAction) action);
        }
        else if(action.getType() == FSMActionType.RUN_ANIM) {
            runAnim((RunAnimAction) action, character);
        }
    }

    private void changeImage(GameCharacter character, Bitmap image) {
        character.setImage(image);
    }

    private void moveTo(MoveToAction action, GameCharacter character) {
        character.setX(action.getX());
        character.setY(action.getY());
    }

    private void moveInc(MoveIncAction action, GameCharacter character) {
        character.setX(character.getX() + action.getX());
        character.setY(character.getY() + action.getY());
    }

    private void followEventPosition(GameCharacter character, FSMEvent event) {
        // I'm under the assumption that this particular event MUST be a subclass of XYEvent
        XYEvent xy = (XYEvent) event;
        character.setX(xy.getX());
        character.setY(xy.getY());
    }

    private void getDragFocus(GameCharacter character) {
        character.getOwner()._dragFocus = character.getCharacterIndex();
    }

    private void dropDragFocus(GameCharacter character) {
        character.getOwner().releaseDragFocus();
    }

    private void sendMessage(SendMessageAction action, GameCharacter character) {
        character.getOwner().sendMessage(character.getCharacterIndex(), action.getMessage());
    }

    private void debugMessage(DebugAction action) {
        Log.d("ssui", action.getMessage());
    }

    private void runAnim(RunAnimAction action, GameCharacter character) {
        character.getOwner().startNewAnimation(action);
    }
}
