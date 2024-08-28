package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.CollidingOrbit;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.EntityManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.BugCatchingNet;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.ItemManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.TileManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.time.TimeManager;

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
        // Add any necessary arguments.
        return args;
    }

    boolean isJustEntered = false;

    public void enter(List<Object> args) {
        isJustEntered = true;

        GameCamera.getInstance().init(Player.getInstance(),
                game.getWidthViewport(), game.getHeightViewport(),
                tileManager.getWidthScene(), tileManager.getHeightScene());

        // CollidingOrbit currently off.
        if (entityManager.getCollidingOrbit() == null) {
            if ((game.getItemStoredInButtonHolderA() != null && game.getItemStoredInButtonHolderA() instanceof BugCatchingNet) ||
                    (game.getItemStoredInButtonHolderB() != null && game.getItemStoredInButtonHolderB() instanceof BugCatchingNet)) {
                // Add CollidingOrbital to EntityManager
                CollidingOrbit collidingOrbit = new CollidingOrbit(
                        (int) Player.getInstance().getX(), (int) (Player.getInstance().getY() + (2 * Tile.HEIGHT)),
                        Player.getInstance());
                // CollidingOrbit.init() is called in Scene.update().
                game.getSceneManager().getCurrentScene().getEntityManager().addEntity(collidingOrbit);
            }
        }
        // CollidingOrbit currently on.
        else {
            if ((game.getItemStoredInButtonHolderA() == null && game.getItemStoredInButtonHolderB() == null) ||
                    (game.getItemStoredInButtonHolderA() == null &&
                            (game.getItemStoredInButtonHolderB() != null && !(game.getItemStoredInButtonHolderB() instanceof BugCatchingNet))) ||
                    (game.getItemStoredInButtonHolderB() == null &&
                            (game.getItemStoredInButtonHolderA() != null && !(game.getItemStoredInButtonHolderA() instanceof BugCatchingNet))) ||
                    (game.getItemStoredInButtonHolderA() != null && !(game.getItemStoredInButtonHolderA() instanceof BugCatchingNet)) &&
                            (game.getItemStoredInButtonHolderB() != null && !(game.getItemStoredInButtonHolderB() instanceof BugCatchingNet))) {
                // Remove CollidingOrbit from EntityManager.
                game.getSceneManager().getCurrentScene().getEntityManager().removeCollidingOrbit();
            }
        }
    }

    public void update(long elapsed) {
        if (isJustEntered) {
            isJustEntered = false;

            CollidingOrbit collidingOrbit = entityManager.getCollidingOrbit();
            if (collidingOrbit != null) {
                collidingOrbit.init(game);
                collidingOrbit.setX(
                        Player.getInstance().getX()
                );
                collidingOrbit.setY(
                        Player.getInstance().getY() + (2 * Tile.HEIGHT)
                );
            }
        }

        entityManager.update(elapsed);
    }

    private Paint paintLightingColorFilter;

    public void updatePaintLightingColorFilter(TimeManager.ModeOfDay modeOfDay) {
        switch (modeOfDay) {
            case DAYLIGHT:
                paintLightingColorFilter = null;
                break;
            case TWILIGHT:
                paintLightingColorFilter = new Paint();
                paintLightingColorFilter.setColorFilter(
                        new LightingColorFilter(0xFFFFF000, 0x00000000)
                );
                break;
            case NIGHT:
                paintLightingColorFilter = new Paint();
                paintLightingColorFilter.setColorFilter(
                        new LightingColorFilter(0xFF00FFFF, 0x00000000)
                );
                break;
        }
    }

    public void drawCurrentFrame(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        tileManager.draw(canvas, paintLightingColorFilter);
        itemManager.draw(canvas, paintLightingColorFilter);
        entityManager.draw(canvas, paintLightingColorFilter);
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