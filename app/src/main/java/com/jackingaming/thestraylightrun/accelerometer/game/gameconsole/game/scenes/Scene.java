package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes;

import android.graphics.Canvas;
import android.graphics.Color;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.CollidingOrbit;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.EntityManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.ItemManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.TileManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene
        implements Serializable {
    transient protected Game game;

    protected TileManager tileManager;
    protected ItemManager itemManager;
    protected EntityManager entityManager;

    protected float xLastKnown;
    protected float yLastKnown;

    public Scene() {
        tileManager = new TileManager();
        itemManager = new ItemManager();
        entityManager = new EntityManager();
        xLastKnown = 0f;
        yLastKnown = 0f;
    }

    public abstract void init(Game game);

    public List<Object> exit() {
        xLastKnown = Player.getInstance().getX();
        yLastKnown = Player.getInstance().getY();

        List<Object> args = new ArrayList<>();
        if (entityManager.getCollidingOrbit() != null) {
            CollidingOrbit collidingOrbit = entityManager.getCollidingOrbit();
            args.add(0, collidingOrbit);
        }
        return args;
    }

    boolean isJustEntered = false;

    public void enter(List<Object> args) {
        GameCamera.getInstance().init(Player.getInstance(),
                game.getWidthViewport(), game.getHeightViewport(),
                tileManager.getWidthScene(), tileManager.getHeightScene());

        if (args != null) {
            if (args.get(0) instanceof CollidingOrbit) {
                isJustEntered = true;

                CollidingOrbit collidingOrbit = (CollidingOrbit) args.get(0);
                entityManager.addEntity(
                        collidingOrbit
                );
            }
        }
    }

    public void update(long elapsed) {
        if (isJustEntered) {
            isJustEntered = false;

            CollidingOrbit collidingOrbit = entityManager.getCollidingOrbit();
            collidingOrbit.setX(
                    Player.getInstance().getX()
            );
            collidingOrbit.setY(
                    Player.getInstance().getY() + (2 * Tile.HEIGHT)
            );
        }

        entityManager.update(elapsed);
    }

    public void drawCurrentFrame(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        tileManager.draw(canvas);
        itemManager.draw(canvas);
        entityManager.draw(canvas);
    }

    public TileManager getTileManager() {
        return tileManager;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}