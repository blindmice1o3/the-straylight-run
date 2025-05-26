package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.entities;

import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Plant;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.SceneFarm;

public class RemoveEntityCommand
        implements EntityCommand {
    public static final String TAG = RemoveEntityCommand.class.getSimpleName();

    private Game game;
    private Entity entity;

    public RemoveEntityCommand(Game game, Entity entity) {
        this.game = game;
        this.entity = entity;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public boolean execute() {
        if (entity instanceof Plant) {
            Log.e(TAG, "RemoveEntityCommand.execute(): entity instanceof Plant");

            if (game.getSceneManager().getCurrentScene() instanceof SceneFarm) {
                SceneFarm sceneFarm = (SceneFarm) game.getSceneManager().getCurrentScene();
                boolean wasRemoved = sceneFarm.getEntityManager().removeEntity(entity);
                Log.e(TAG, entity.getClass().getSimpleName() + " wasRemoved: " + wasRemoved);
            }
        }
        Log.e(TAG, "entity is NOT Plant... entity's class: " + entity.getClass().getSimpleName());
        return false;
    }
}
