package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.dinos;

import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.State;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.StateManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.dinos.attacks.AttackState;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.dinos.attacks.IdleState;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.dinos.movements.FallState;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.dinos.movements.JumpState;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.beasties.dinos.movements.WalkState;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BubblunStateManager extends StateManager {
    public static final String WALK_STATE = "WalkState";
    public static final String JUMP_STATE = "JumpState";
    public static final String FALL_STATE = "FallState";
    public static final String IDLE_STATE = "IdleState";
    public static final String ATTACK_STATE = "AttackState";

    public enum StateLayer {
        MOVEMENT,
        ATTACK;
    }

    private Bubblun bubblun;
    private Map<String, State> statesMovement = new HashMap<>();
    private Map<String, State> statesAttack = new HashMap<>();
    private List<State> stateStackMovement = new ArrayList<>();
    private List<State> stateStackAttack = new ArrayList<>();

    @Override
    public void init(Game game, Entity e) {
        statesMovement.put(WALK_STATE, new WalkState());
        statesMovement.put(JUMP_STATE, new JumpState());
        statesMovement.put(FALL_STATE, new FallState());
        statesAttack.put(IDLE_STATE, new IdleState());
        statesAttack.put(ATTACK_STATE, new AttackState());

        stateStackMovement.add(
                statesMovement.get(WALK_STATE)
        );
        stateStackAttack.add(
                statesAttack.get(IDLE_STATE)
        );

        if (e instanceof Bubblun) {
            bubblun = (Bubblun) e;

            for (State stateMovement : statesMovement.values()) {
                stateMovement.init(game, bubblun);
            }
            for (State stateAttack : statesAttack.values()) {
                stateAttack.init(game, bubblun);
            }
        }
    }

    public void changeState(StateLayer stateLayer, String nextStateTag) {
        Log.i(TAG, "changeState(StateLayer, String)");

        getCurrentState(stateLayer).exit();

        switch (stateLayer) {
            case MOVEMENT:
                State nextStateMovement = statesMovement.get(nextStateTag);
                stateStackMovement.add(nextStateMovement);

                getCurrentState(stateLayer).enter();
                break;
            case ATTACK:
                State nextStateAttack = statesAttack.get(nextStateTag);
                stateStackAttack.add(nextStateAttack);

                getCurrentState(stateLayer).enter();
                break;
            default:
                Log.e(TAG, "switch(stateLayer)'s default-clause for (stateLayer, nextStatetag): (" + stateLayer.name() + ", " + nextStateTag + ").");
                break;
        }
    }

    public int getSizeOfStateStackMovement() {
        return stateStackMovement.size();
    }

    public void popState(StateLayer stateLayer) {
        Log.i(TAG, "popState(StateLayer)");

        getCurrentState(stateLayer).exit();

        switch (stateLayer) {
            case MOVEMENT:
                int indexMovementLast = stateStackMovement.size() - 1;
                stateStackMovement.remove(indexMovementLast);

                getCurrentState(stateLayer).enter();
                break;
            case ATTACK:
                int indexAttackLast = stateStackAttack.size() - 1;
                stateStackAttack.remove(indexAttackLast);

                getCurrentState(stateLayer).enter();
                break;
            default:
                Log.e(TAG, "switch(stateLayer)'s default-clause for stateLayer: " + stateLayer.name() + ".");
                break;
        }
    }

    public State getCurrentState(StateLayer stateLayer) {
        Log.i(TAG, "getCurrentState(StateLayer)");

        switch (stateLayer) {
            case MOVEMENT:
                int indexMovementLast = stateStackMovement.size() - 1;
                return stateStackMovement.get(indexMovementLast);
            case ATTACK:
                int indexAttackLast = stateStackAttack.size() - 1;
                return stateStackAttack.get(indexAttackLast);
            default:
                Log.e(TAG, "switch(stateLayer)'s default-clause for stateLayer: " + stateLayer.name() + ".");
                return null;
        }
    }

    @Override
    public void update(long elapsed) {
        getCurrentState(StateLayer.MOVEMENT).update(elapsed);
        getCurrentState(StateLayer.ATTACK).update(elapsed);
    }

    @Override
    public boolean respondToEntityCollision(Entity e) {
        // TODO:
        return true;
    }

    @Override
    public void respondToItemCollisionViaClick(Item i) {
        // TODO:
    }

    @Override
    public void respondToItemCollisionViaMove(Item i) {
        // TODO:
    }

    @Override
    public void respondToTransferPointCollision(String key) {
        // TODO:
    }
}
