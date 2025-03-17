package com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.controllables;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Direction;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.npcs.NonPlayableCharacter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Player extends Entity {
    public interface PartyMovementListener {
        void onPartyLeaderMove(Player player, Handler handler);
    }

    private List<NonPlayableCharacter> partyMembers;

    public void addPartyMember(NonPlayableCharacter newPartyMember) {
        for (NonPlayableCharacter currentPartyMember : partyMembers) {
            String idCurrentPartyMember = currentPartyMember.getId();
            String idNewPartyMember = newPartyMember.getId();
            if (idCurrentPartyMember.equals(idNewPartyMember)) {
                Log.e(TAG, "already in party... idCurrentPartyMember: " + idCurrentPartyMember);
                return;
            }
        }

        Log.e(TAG, "adding " + newPartyMember.getId() + " to party.");
        partyMembers.add(newPartyMember);
    }

    public int getSizeOfPartyMembers() {
        return partyMembers.size();
    }

    public Player(Map<Direction, AnimationDrawable> animationsByDirection) {
        super(animationsByDirection, null, null);

        partyMembers = new ArrayList<>();

        startAnimations();
    }

    @Override
    public void collided(Entity collider) {

    }

    @Override
    public void update(Handler handler) {

    }

    public void updateViaSensorEvent(Handler handler, float xDelta, float yDelta) {
        if (animatorMovement.isRunning()) {
            return;
        }
        // DIRECTION
        if (Math.abs(yDelta) >= Math.abs(xDelta)) {
            // more vertical than horizontal (or equal)
            direction = (yDelta < 0) ? Direction.UP : Direction.DOWN;
        } else {
            // more horizontal than vertical
            direction = (xDelta < 0) ? Direction.LEFT : Direction.RIGHT;
        }

        doMoveBasedOnDirection(handler);
    }

    @Override
    protected void moveAfterValidation(Handler handler, String propertyName, float valueStart, float valueEnd) {
        for (NonPlayableCharacter partyMembers : partyMembers) {
            partyMembers.onPartyLeaderMove(this, handler);
        }
        super.moveAfterValidation(handler, propertyName, valueStart, valueEnd);
    }
}
