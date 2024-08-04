package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

import com.jackingaming.thestraylightrun.MainActivity;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogueboxes.inputs.RobotDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.InputManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.animations.PoohAnimationManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Creature;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Robot;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.HoneyPot;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Item;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.MysterySeed;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.Shovel;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.items.WateringCan;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.pocketcritters.SceneHome02;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.pocketcritters.SceneWorldMapPart01;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.SceneFarm;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.SceneHouseLevel01;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.BedTile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.growable.GrowableTile;

public class PoohForm
        implements Form {
    public static final String TAG = PoohForm.class.getSimpleName();

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
        player.move();

        // PREPARE_FOR_RENDER
        determineNextImage();
    }

    @Override
    public void draw(Canvas canvas) {
        // Intentionally blank.
    }

    private void doShovelAction(Item itemShovel) {
        Tile tileCurrentlyFacing = player.checkTileCurrentlyFacing();
        Log.e(TAG, "tileCurrentlyFacing's class is " + tileCurrentlyFacing.getClass().getSimpleName());
        if (tileCurrentlyFacing instanceof GrowableTile) {
            GrowableTile growableTile = (GrowableTile) tileCurrentlyFacing;
            Log.e(TAG, "tileCurrentlyFacing has state: " + growableTile.getState());
            if (growableTile.getState() == GrowableTile.State.UNTILLED) {
                Log.e(TAG, "growableTile.changeToTilled()");
                growableTile.changeToTilled();
            }
        } else {
            Log.e(TAG, "tile is NOT GrowableTile... tile's id: " + tileCurrentlyFacing.getId());
        }
    }

    private void doMysterySeedAction(Item itemMysterySeed) {
        Tile tileCurrentlyFacing = player.checkTileCurrentlyFacing();
        Log.e(TAG, "tileCurrentlyFacing's class is " + tileCurrentlyFacing.getClass().getSimpleName());
        if (tileCurrentlyFacing instanceof GrowableTile) {
            GrowableTile growableTile = (GrowableTile) tileCurrentlyFacing;
            Log.e(TAG, "tileCurrentlyFacing has state: " + growableTile.getState());
            if (growableTile.getState() == GrowableTile.State.TILLED) {
                Log.e(TAG, "growableTile.changeToUnwatered()");
                growableTile.changeToUnwatered();
                Log.e(TAG, "growableTile.changeToSeeded()");
                growableTile.changeToSeeded(itemMysterySeed);
            }
        } else {
            Log.e(TAG, "tile is NOT GrowableTile... tile's id: " + tileCurrentlyFacing.getId());
        }
    }

    private void doWateringCanAction(Item itemWateringCan) {
        Tile tileCurrentlyFacing = player.checkTileCurrentlyFacing();
        Log.e(TAG, "tileCurrentlyFacing's class is " + tileCurrentlyFacing.getClass().getSimpleName());
        if (tileCurrentlyFacing instanceof GrowableTile) {
            GrowableTile growableTile = (GrowableTile) tileCurrentlyFacing;
            Log.e(TAG, "tileCurrentlyFacing has state: " + growableTile.getState());
            if (growableTile.getState() == GrowableTile.State.TILLED ||
                    growableTile.getState() == GrowableTile.State.SEEDED ||
                    growableTile.getState() == GrowableTile.State.OCCUPIED) {
                Log.e(TAG, "growableTile.changeToWatered()");
                growableTile.changeToWatered();
            }
        } else {
            Log.e(TAG, "tile is NOT GrowableTile... tile's id: " + tileCurrentlyFacing.getId());
        }
    }

    @Override
    public void interpretInput() {
        // Check InputManager's ButtonPadFragment-specific boolean fields.
        if (game.getInputManager().isJustPressed(InputManager.Button.A)) {
            Log.e(TAG, getClass().getSimpleName() + ".interpretInput() isJustPressed(InputManager.Button.A)");
            if (game.getSceneManager().getCurrentScene() instanceof SceneFarm) {
                Entity entityCurrentlyFacing = player.getEntityCurrentlyFacing();
                if (entityCurrentlyFacing != null) {
                    if (entityCurrentlyFacing instanceof Robot) {
                        game.setPaused(true);

                        RobotDialogFragment robotDialogFragment = RobotDialogFragment.newInstance(new RobotDialogFragment.ButtonListener() {
                            @Override
                            public void onOffButtonClick(View view, RobotDialogFragment robotDialogFragment) {
                                Log.e(TAG, "OFF");
                                ((Robot) entityCurrentlyFacing).changeToOff();
                                game.setPaused(false);
                            }

                            @Override
                            public void onWalkButtonClick(View view, RobotDialogFragment robotDialogFragment) {
                                Log.e(TAG, "WALK");
                                ((Robot) entityCurrentlyFacing).changeToWalk();
                                game.setPaused(false);
                            }

                            @Override
                            public void onRunButtonClick(View view, RobotDialogFragment robotDialogFragment) {
                                Log.e(TAG, "RUN");
                                ((Robot) entityCurrentlyFacing).changeToRun();
                                game.setPaused(false);
                            }
                        });

                        robotDialogFragment.show(
                                ((MainActivity) game.getContext()).getSupportFragmentManager(),
                                RobotDialogFragment.TAG
                        );
                    }
                }
                // TODO: check item occupying StatsDisplayerFragment's button holder.
                else if (game.getItemStoredInButtonHolderA() instanceof Shovel) {
                    Shovel shovel = (Shovel) game.getItemStoredInButtonHolderA();
                    doShovelAction(shovel);
                } else if (game.getItemStoredInButtonHolderA() instanceof MysterySeed) {
                    MysterySeed mysterySeed = (MysterySeed) game.getItemStoredInButtonHolderA();
                    doMysterySeedAction(mysterySeed);
                } else if (game.getItemStoredInButtonHolderA() instanceof WateringCan) {
                    WateringCan wateringCan = (WateringCan) game.getItemStoredInButtonHolderA();
                    doWateringCanAction(wateringCan);
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
                if (((SceneFarm) game.getSceneManager().getCurrentScene()).isInSeedShopState()) {
                    ((SceneFarm) game.getSceneManager().getCurrentScene()).removeSeedShopFragment();
                    game.getTextboxListener().showStatsDisplayer();
                } else {
                    // TODO: check item occupying StatsDisplayerFragment's button holder.
                    if (game.getItemStoredInButtonHolderB() instanceof Shovel) {
                        Shovel shovel = (Shovel) game.getItemStoredInButtonHolderB();
                        doShovelAction(shovel);
                    } else if (game.getItemStoredInButtonHolderB() instanceof MysterySeed) {
                        MysterySeed mysterySeed = (MysterySeed) game.getItemStoredInButtonHolderB();
                        doMysterySeedAction(mysterySeed);
                    } else if (game.getItemStoredInButtonHolderB() instanceof WateringCan) {
                        WateringCan wateringCan = (WateringCan) game.getItemStoredInButtonHolderB();
                        doWateringCanAction(wateringCan);
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
            game.getSceneManager().getCurrentScene().getItemManager().removeItem(item);
            game.incrementCurrency();
        }
    }
}