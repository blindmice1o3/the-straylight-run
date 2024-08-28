package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.jackingaming.thestraylightrun.MainActivity;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogueboxes.inputs.RobotDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.InputManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.animations.PoohAnimationManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.entities.EntityCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.commands.tiles.TileCommand;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Plant;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Robot;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Sellable;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.EntityCommandOwner;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.HoneyPot;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.TileCommandOwner;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.pocketcritters.SceneHome02;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.pocketcritters.SceneWorldMapPart01;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.SceneFarm;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.SceneHouseLevel01;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.growable.GrowableTile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.nonwalkable.BedTile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.nonwalkable.twobytwo.ShippingBinTile;

public class PoohForm
        implements Form {
    public static final String TAG = PoohForm.class.getSimpleName();

    public interface MovementListener {
        void onMove(float xMove, float yMove);
    }

    private MovementListener movementListener;

    public void setMovementListener(MovementListener movementListener) {
        this.movementListener = movementListener;
    }

    transient private Game game;

    private Player player;
    private PoohAnimationManager poohAnimationManager;

    public PoohForm() {
        poohAnimationManager = new PoohAnimationManager();
    }

    @Override
    public void init(Game game) {
        this.game = game;
        player = Player.getInstance();
        poohAnimationManager.init(game);
    }

    @Override
    public void update(long elapsed) {
        // ANIMATION
        poohAnimationManager.update(elapsed);

        // ATTACK_COOLDOWN
        // TODO: placeholder for AttackCooldown

        // RESET [offset-of-next-step] TO ZERO (standing still)
        player.setxMove(0f);
        player.setyMove(0f);
        player.setMoveSpeed(Creature.MOVE_SPEED_DEFAULT);

        // USER_INPUT (determine values of [offset-of-next-step]... potential movement)
        interpretInput();

        // MOVEMENT (check tile, item, entity, and transfer point collisions... actual movement)
        boolean successfulMove = player.move();
        if (successfulMove) {
            movementListener.onMove(player.getxMove(), player.getyMove());
        }

        // PREPARE_FOR_RENDER
        determineNextImage();

        // CARRYABLE
        player.moveCarryable();
    }

    @Override
    public void draw(Canvas canvas, Paint paintLightingColorFilter) {
        // Intentionally blank.
    }

    @Override
    public void interpretInput() {
        // Check InputManager's ButtonPadFragment-specific boolean fields.
        if (game.getInputManager().isJustPressed(InputManager.Button.A)) {
            Log.e(TAG, getClass().getSimpleName() + ".interpretInput() isJustPressed(InputManager.Button.A)");

            if (game.getSceneManager().getCurrentScene() instanceof SceneFarm) {
                Entity entityCurrentlyFacing = player.getEntityCurrentlyFacing();

                if (player.hasCarryable() && player.getCarryable() instanceof Sellable &&
                        entityCurrentlyFacing == null) {
                    Log.e(TAG, "has carryable and carryable is Sellable and entityFacing is null");
                    Tile tileCurrentlyFacing = player.checkTileCurrentlyFacing();
                    if (tileCurrentlyFacing instanceof ShippingBinTile) {
                        Log.e(TAG, "tileCurrentlyFacing instanceof ShippingBinTile");
                        player.placeInShippingBin();
                    } else if (tileCurrentlyFacing.isWalkable()) {
                        player.placeDown();
                    }
                } else if (entityCurrentlyFacing != null &&
                        entityCurrentlyFacing instanceof Plant &&
                        ((Plant) entityCurrentlyFacing).isHarvestable()) {
                    player.pickUp(entityCurrentlyFacing);

                    Tile tileFacing = player.checkTileCurrentlyFacing();
                    if (tileFacing instanceof GrowableTile) {
                        ((GrowableTile) tileFacing).changeToUntilled();
                    } else {
                        Log.e(TAG, "tileFacing NOT instanceof GrowableTile");
                    }
                } else if (entityCurrentlyFacing != null &&
                        entityCurrentlyFacing instanceof Robot) {
                    game.setPaused(true);

                    RobotDialogFragment robotDialogFragment =
                            ((Robot) entityCurrentlyFacing).instantiateRobotDialogFragment();

                    robotDialogFragment.show(
                            ((MainActivity) game.getContext()).getSupportFragmentManager(),
                            RobotDialogFragment.TAG
                    );
                }
                // TODO: check item occupying StatsDisplayerFragment's button holder.
                else if (game.getItemStoredInButtonHolderA() instanceof TileCommandOwner) {
                    TileCommandOwner tileCommandOwner = (TileCommandOwner) game.getItemStoredInButtonHolderA();
                    TileCommand tileCommand = tileCommandOwner.getTileCommand();

                    Tile tileCurrentlyFacing = player.checkTileCurrentlyFacing();
                    Log.e(TAG, "tileCurrentlyFacing's class is " + tileCurrentlyFacing.getClass().getSimpleName());
                    tileCommand.setTile(tileCurrentlyFacing);
                    tileCommand.execute();
                } else if (game.getItemStoredInButtonHolderA() instanceof EntityCommandOwner) {
                    if (entityCurrentlyFacing != null) {
                        EntityCommandOwner entityCommandOwner = (EntityCommandOwner) game.getItemStoredInButtonHolderA();
                        EntityCommand entityCommand = entityCommandOwner.getEntityCommand();

                        Log.e(TAG, "entityCurrentlyFacing's class is " + entityCurrentlyFacing.getClass().getSimpleName());
                        entityCommand.setEntity(entityCurrentlyFacing);
                        entityCommand.execute();
                    }
                } else if (!player.hasCarryable() &&
                        entityCurrentlyFacing == null) {
                    Log.e(TAG, "no carryable and entityFacing is null");
                    Tile tileCurrentlyFacing = player.checkTileCurrentlyFacing();
                    if (tileCurrentlyFacing instanceof ShippingBinTile) {
                        Log.e(TAG, "tileCurrentlyFacing instanceof ShippingBinTile");
                        ShippingBinTile.sellStash();
                    }
                }
            } else if (game.getSceneManager().getCurrentScene() instanceof SceneHouseLevel01) {
                Tile tileCurrentlyFacing = player.checkTileCurrentlyFacing();
                Log.e(TAG, "tileCurrentlyFacing's class is " + tileCurrentlyFacing.getClass().getSimpleName());
                if (tileCurrentlyFacing instanceof BedTile) {
                    ((SceneHouseLevel01) game.getSceneManager().getCurrentScene()).onBedTileClicked();
                }
            } else if (game.getSceneManager().getCurrentScene() instanceof SceneWorldMapPart01) {
                player.doCheckItemCollisionViaClick();
            } else if (game.getSceneManager().getCurrentScene() instanceof SceneHome02) {
                if (player.checkTileCurrentlyFacing().getId().equals("5")) {
                    game.getSceneManager().changeScene("FARM");
                } else if (player.checkTileCurrentlyFacing().getId().equals("4")) {
                    game.getSceneManager().changeScene("COMPUTER");
                }
            }
        } else if (game.getInputManager().isJustPressed(InputManager.Button.B)) {
            Log.e(TAG, getClass().getSimpleName() + ".interpretInput() isJustPressed(InputManager.Button.B)");
            if (game.getSceneManager().getCurrentScene() instanceof SceneFarm) {
                Entity entityCurrentlyFacing = player.getEntityCurrentlyFacing();

                if (((SceneFarm) game.getSceneManager().getCurrentScene()).isInSeedShopState()) {
                    ((SceneFarm) game.getSceneManager().getCurrentScene()).removeSeedShopFragment();
                    game.getTextboxListener().showStatsDisplayer();
                } else {
                    // TODO: check item occupying StatsDisplayerFragment's button holder.
                    if (game.getItemStoredInButtonHolderB() instanceof TileCommandOwner) {
                        TileCommandOwner tileCommandOwner = (TileCommandOwner) game.getItemStoredInButtonHolderB();
                        TileCommand tileCommand = tileCommandOwner.getTileCommand();

                        Tile tileCurrentlyFacing = player.checkTileCurrentlyFacing();
                        Log.e(TAG, "tileCurrentlyFacing's class is " + tileCurrentlyFacing.getClass().getSimpleName());
                        tileCommand.setTile(tileCurrentlyFacing);
                        tileCommand.execute();
                    } else if (game.getItemStoredInButtonHolderB() instanceof EntityCommandOwner) {
                        if (entityCurrentlyFacing != null) {
                            EntityCommandOwner entityCommandOwner = (EntityCommandOwner) game.getItemStoredInButtonHolderB();
                            EntityCommand entityCommand = entityCommandOwner.getEntityCommand();

                            Log.e(TAG, "entityCurrentlyFacing's class is " + entityCurrentlyFacing.getClass().getSimpleName());
                            entityCommand.setEntity(entityCurrentlyFacing);
                            entityCommand.execute();
                        }
                    }
                }
            }
        } else if (game.getInputManager().isPressing(InputManager.Button.B)) {
            String idTileCurrentlyFacing = player.checkTileCurrentlyFacing().getId();
//            Log.e(TAG, getClass().getSimpleName() + ".interpretInput() isPressing(InputManager.Button.B) idTileCurrentlyFacing: " + idTileCurrentlyFacing);

            float doubledMoveSpeedDefault = 2 * Creature.MOVE_SPEED_DEFAULT;
            player.setMoveSpeed(doubledMoveSpeedDefault);

            if (game.getGameTitle().equals("Pocket Critters") &&
                    game.getSceneManager().getCurrentScene() instanceof SceneFarm) {
                game.getSceneManager().pop();
            }
        } else if (game.getInputManager().isJustPressed(InputManager.Button.MENU)) {
            Log.e(TAG, getClass().getSimpleName() + ".interpretInput() isJustPressed(InputManager.Button.MENU)");
            game.getStateManager().toggleMenuState();
        }


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

    @Override
    public void determineNextImage() {
        Bitmap imageNext = poohAnimationManager.getCurrentFrame(player.getDirection(),
                player.getxMove(), player.getyMove());
        player.setImage(imageNext);
    }

    @Override
    public void respondToTransferPointCollision(String key) {
        Log.e(TAG, getClass().getSimpleName() + ".respondToTransferPointCollision(String key) key: " + key);
        // TODO: change scene.
        game.getSceneManager().changeScene(key);
    }

    @Override
    public boolean respondToEntityCollision(Entity e) {
        // TODO: if (e instanceof SignPost) { readSignPost(); }

        if (e instanceof com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.frogger.Log) {
            return false;
        }

//        ((PassingThroughActivity)game.getContext()).runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(game.getContext(), getClass().getSimpleName() + ".respondToEntityCollision(Entity e) testing Toast-message from non-UI thread.", Toast.LENGTH_SHORT).show();
//            }
//        });

//        Handler handler = new Handler(Looper.getMainLooper());
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(game.getContext(), getClass().getSimpleName() + ".respondToEntityCollision(Entity e) testing Toast-message from non-UI thread.", Toast.LENGTH_SHORT).show();
//            }
//        });
        return true;
    }

    @Override
    public void respondToItemCollisionViaClick(Item item) {
        game.addItemToBackpack(item);
        game.getSceneManager().getCurrentScene().getItemManager().removeItem(item);
    }

    @Override
    public void respondToItemCollisionViaMove(Item item) {
        if (item instanceof HoneyPot) {
            game.incrementCurrency(
                    item.getPrice()
            );

            game.getSceneManager().getCurrentScene().getItemManager().removeItem(item);
        }
    }
}