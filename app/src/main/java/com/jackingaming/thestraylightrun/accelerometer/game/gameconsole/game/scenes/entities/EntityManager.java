package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities;

import android.graphics.Canvas;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EntityManager
        implements Serializable {
    transient private Game game;

    private List<Entity> entities;

    public EntityManager() {
        entities = new ArrayList<Entity>();
    }

    public void loadEntities(List<Entity> entitiesToBeLoaded) {
        entities.clear();
        entities.addAll(entitiesToBeLoaded);
    }

    public void init(Game game) {
        this.game = game;

        Iterator<Entity> iterator = entities.iterator();
        while (iterator.hasNext()) {
            Entity e = iterator.next();

            // Remove previous (before loading) instance of Player.
            if (e instanceof Player) {
                iterator.remove();
                continue;
            }

            e.init(game);
        }

        // Insert new instance of Player.
        Player.getInstance().init(game);
        entities.add(Player.getInstance());
    }

    public void update(long elapsed) {
        Iterator<Entity> iterator = entities.iterator();
        while (iterator.hasNext()) {
            Entity e = iterator.next();

            e.update(elapsed);

            if (!e.isActive()) {
                iterator.remove();
            }
        }
    }

    public void draw(Canvas canvas) {
        for (Entity e : entities) {
            e.draw(canvas);
        }
    }

    public boolean addEntity(Entity e) {
        if (!entities.contains(e)) {
            return entities.add(e);
        }
        return false;
    }

    public boolean removeEntity(Entity e) {
        if (entities.contains(e)) {
            return entities.remove(e);
        }
        return false;
    }

    public List<Entity> getEntities() {
        return entities;
    }
}