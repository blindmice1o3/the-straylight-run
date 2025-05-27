package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EntityManager
        implements Serializable {
    transient private Game game;

    private List<Entity> entities;
    private CollidingOrbit collidingOrbit;

    public EntityManager() {
        entities = new ArrayList<Entity>();
    }

    public void loadEntities(List<Entity> entitiesToBeLoaded) {
        entities.clear();

        for (Entity e : entitiesToBeLoaded) {
            if (e instanceof CollidingOrbit) {
                collidingOrbit = (CollidingOrbit) e;
            }
        }

        entities.addAll(entitiesToBeLoaded);
    }

    public void init(Game game) {
        Log.e("EntityManager", "init()");

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

    private List<Entity> entitiesToBeAdded = new ArrayList<>();

    public void update(long elapsed) {
        Iterator<Entity> iterator = entities.iterator();
        while (iterator.hasNext()) {
            Entity e = iterator.next();

            e.update(elapsed);

            if (!e.isActive()) {
                iterator.remove();
            }
        }

        if (!entitiesToBeAdded.isEmpty()) {
            entities.addAll(entitiesToBeAdded);
            entitiesToBeAdded.clear();
        }
    }

    public void draw(Canvas canvas, Paint paintLightingColorFilter) {
        for (Entity e : entities) {
            e.draw(canvas, paintLightingColorFilter);
        }
    }

    public boolean addEntity(Entity e) {
        if (!entities.contains(e)) {
            if (e instanceof CollidingOrbit) {
                collidingOrbit = (CollidingOrbit) e;
            }
            return entitiesToBeAdded.add(e);
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

    public CollidingOrbit getCollidingOrbit() {
        return collidingOrbit;
    }

    public void removeCollidingOrbit() {
        Iterator<Entity> iterator = entities.iterator();
        while (iterator.hasNext()) {
            Entity e = iterator.next();

            if (e instanceof CollidingOrbit) {
                iterator.remove();
            }
        }

        collidingOrbit = null;
    }

    public void setCollidingOrbit(CollidingOrbit collidingOrbit) {
        this.collidingOrbit = collidingOrbit;
    }
}