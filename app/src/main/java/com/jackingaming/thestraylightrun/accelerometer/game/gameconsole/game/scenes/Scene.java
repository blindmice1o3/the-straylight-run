package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.InputManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.CollidingOrbit;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.EntityManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.BugCatchingNet;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.ItemManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.SceneFarm;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.TileManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.time.TimeManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene
        implements Serializable {
    public static final String TAG = Scene.class.getSimpleName();

    transient protected Game game;

    protected TileManager tileManager;
    protected ItemManager itemManager;
    protected EntityManager entityManager;

    protected float xLastKnown;
    protected float yLastKnown;
    protected Creature.Direction directionLastKnown;

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
        directionLastKnown = Player.getInstance().getDirection();

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

        // RESET [offset-of-next-step] TO ZERO (standing still)
        Player player = Player.getInstance();
        player.setxMove(0f);
        player.setyMove(0f);
        player.setMoveSpeed(Creature.MOVE_SPEED_DEFAULT);

        interpretInput();
        entityManager.update(elapsed);
    }

    protected void doJustPressedButtonA() {

    }

    protected void doJustPressedButtonB() {

    }

    protected void doPressingButtonB() {
        Player player = Player.getInstance();
        String idTileCurrentlyFacing = player.checkTileCurrentlyFacing().getId();
//            Log.e(TAG, getClass().getSimpleName() + ".interpretInput() isPressing(InputManager.Button.B) idTileCurrentlyFacing: " + idTileCurrentlyFacing);

        float doubledMoveSpeedDefault = 2 * Creature.MOVE_SPEED_DEFAULT;
        player.setMoveSpeed(doubledMoveSpeedDefault);

        if (game.getGameTitle().equals("Pocket Critters") &&
                game.getSceneManager().getCurrentScene() instanceof SceneFarm) {
            game.getSceneManager().pop();
        }
    }

    protected void doJustPressedButtonMenu() {
        Log.e(TAG, getClass().getSimpleName() + ".interpretInput() isJustPressed(InputManager.Button.MENU)");
        game.getStateManager().toggleMenuState();
    }

    protected void interpretInput() {
        if (game.getInputManager().isJustPressed(InputManager.Button.A)) {
            Log.e(TAG, getClass().getSimpleName() + ".interpretInput() isJustPressed(InputManager.Button.A)");
            doJustPressedButtonA();
        } else if (game.getInputManager().isJustPressed(InputManager.Button.B)) {
            Log.e(TAG, getClass().getSimpleName() + ".interpretInput() isJustPressed(InputManager.Button.B)");
            doJustPressedButtonB();
        } else if (game.getInputManager().isPressing(InputManager.Button.B)) {
            doPressingButtonB();
        } else if (game.getInputManager().isJustPressed(InputManager.Button.MENU)) {
            Log.e(TAG, getClass().getSimpleName() + ".interpretInput() isJustPressed(InputManager.Button.MENU)");
            doJustPressedButtonMenu();
        }

        Player player = Player.getInstance();
        float moveSpeed = player.getMoveSpeed();
        // Check InputManager's DirectionPadFragment-specific boolean fields.
        if (game.getInputManager().isPressing(InputManager.Button.UP)) {
            player.setDirection(Creature.Direction.UP);
            player.setyMove(-moveSpeed);    // vertical NEGATIVE
        } else if (game.getInputManager().isPressing(InputManager.Button.DOWN)) {
            player.setDirection(Creature.Direction.DOWN);
            player.setyMove(moveSpeed);     // vertical POSITIVE
        } else if (game.getInputManager().isPressing(InputManager.Button.LEFT)) {
            player.setDirection(Creature.Direction.LEFT);
            player.setxMove(-moveSpeed);    // horizontal NEGATIVE
        } else if (game.getInputManager().isPressing(InputManager.Button.RIGHT)) {
            player.setDirection(Creature.Direction.RIGHT);
            player.setxMove(moveSpeed);     // horizontal POSITIVE
        } else if (game.getInputManager().isPressing(InputManager.Button.CENTER)) {
            player.setDirection(Creature.Direction.CENTER);
            player.setxMove(0f);            // horizontal ZERO
            player.setyMove(0f);            // vertical ZERO
        } else if (game.getInputManager().isPressing(InputManager.Button.UPLEFT)) {
            player.setDirection(Creature.Direction.UP_LEFT);
            player.setxMove(-moveSpeed);    // horizontal NEGATIVE
            player.setyMove(-moveSpeed);    // vertical NEGATIVE
        } else if (game.getInputManager().isPressing(InputManager.Button.UPRIGHT)) {
            player.setDirection(Creature.Direction.UP_RIGHT);
            player.setxMove(moveSpeed);     // horizontal POSITIVE
            player.setyMove(-moveSpeed);    // vertical NEGATIVE
        } else if (game.getInputManager().isPressing(InputManager.Button.DOWNLEFT)) {
            player.setDirection(Creature.Direction.DOWN_LEFT);
            player.setxMove(-moveSpeed);    // horizontal NEGATIVE
            player.setyMove(moveSpeed);     // vertical POSITIVE
        } else if (game.getInputManager().isPressing(InputManager.Button.DOWNRIGHT)) {
            player.setDirection(Creature.Direction.DOWN_RIGHT);
            player.setxMove(moveSpeed);     // horizontal POSITIVE
            player.setyMove(moveSpeed);     // vertical POSITIVE
        }
    }

    transient private Paint paintLightingColorFilter;

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

    public void setGame(Game game) {
        this.game = game;
    }
}