package com.jackingaming.thestraylightrun.accelerometer.game.scenes.commands;

import android.os.Handler;

import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Entity;

public class MoveDownCommand implements MovementCommand {
    private Entity entity;
    private Handler handler;

    public MoveDownCommand(Entity entity, Handler handler) {
        this.entity = entity;
        this.handler = handler;
    }

    @Override
    public void execute() {
        entity.moveDown(handler);
    }
}
