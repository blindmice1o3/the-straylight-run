package com.jackingaming.thestraylightrun.accelerometer.game;

import static com.jackingaming.thestraylightrun.accelerometer.game.entities.Direction.DOWN;
import static com.jackingaming.thestraylightrun.accelerometer.game.entities.Direction.LEFT;
import static com.jackingaming.thestraylightrun.accelerometer.game.entities.Direction.RIGHT;
import static com.jackingaming.thestraylightrun.accelerometer.game.entities.Direction.UP;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.choiceboxes.ChoiceDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogueboxes.TypeWriterDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogueboxes.views.TypeWriterTextView;
import com.jackingaming.thestraylightrun.accelerometer.game.entities.Direction;
import com.jackingaming.thestraylightrun.accelerometer.game.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.entities.controllables.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.entities.npcs.NonPlayableCharacter;
import com.jackingaming.thestraylightrun.accelerometer.game.sounds.SoundManager;
import com.jackingaming.thestraylightrun.accelerometer.game.tiles.Tile;
import com.jackingaming.thestraylightrun.accelerometer.game.tiles.TileMapLoader;
import com.jackingaming.thestraylightrun.sequencetrainer.SequenceTrainerFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class World extends FrameLayout {
    public static final String TAG = World.class.getSimpleName();
    public static int durationOfFrameInMilli = 420;

    public interface ShowDialogListener {
        FragmentManager onShowDialog();
    }

    private ShowDialogListener listener;

    private GameCamera gameCamera;
    private int widthDeviceScreen, heightDeviceScreen;
    private int widthSpriteDst, heightSpriteDst;
    private int widthWorldInTiles, heightWorldInTiles;
    private Tile[][] tiles;
    private List<Entity> entities;
    private GestureDetector gestureDetector;
    private SoundManager soundManager;

    private Bitmap[][] sprites;
    private Bitmap spriteCoin;
    private Bitmap spriteTileSolid, spriteTileWalkable, spriteTileBoulder;

    static class SpriteInitializer {
        private static final int WIDTH_SPRITE_SHEET_ACTUAL = 187;
        private static final int HEIGHT_SPRITE_SHEET_ACTUAL = 1188;
        private static final int COLUMNS = 10;
        private static final int ROWS = 56; // section: Characters
        private static final int X_OFFSET_INIT = 9;
        private static final int Y_OFFSET_INIT = 34;
        private static final int WIDTH_SPRITE = 16;
        private static final int HEIGHT_SPRITE = 16;
        private static final int WIDTH_DIVIDER = 1;
        private static final int HEIGHT_DIVIDER = 1;

        private static final int xSolidTile = 10;
        private static final int ySolidTile = 1147;
        private static final int xWalkableTile = 10;
        private static final int yWalkableTile = 1164;
        private static final int xBoulderTile = 112;
        private static final int yBoulderTile = 1088;

        public static Bitmap[][] initSprites(Resources resources, int widthSpriteDst, int heightSpriteDst) {
            Bitmap[][] sprites = new Bitmap[COLUMNS][ROWS];

            Bitmap spriteSheet = BitmapFactory.decodeResource(resources,
                    R.drawable.gbc_pokemon_red_blue_characters_overworld);

            float ratioHorizontal = (float) spriteSheet.getWidth() / WIDTH_SPRITE_SHEET_ACTUAL;
            float ratioVertical = (float) spriteSheet.getHeight() / HEIGHT_SPRITE_SHEET_ACTUAL;

            int widthSpriteConverted = (int) (WIDTH_SPRITE * ratioHorizontal);
            int heightSpriteConverted = (int) (HEIGHT_SPRITE * ratioVertical);

            int yOffset = Y_OFFSET_INIT;
            int xOffset = X_OFFSET_INIT;
            for (int i = 0; i < COLUMNS; i++) {
                for (int j = 0; j < ROWS; j++) {
                    int xOffsetConverted = (int) (xOffset * ratioHorizontal);
                    int yOffsetConverted = (int) (yOffset * ratioVertical);

                    Bitmap sprite = Bitmap.createBitmap(spriteSheet,
                            xOffsetConverted, yOffsetConverted, widthSpriteConverted, heightSpriteConverted);
                    sprites[i][j] = Bitmap.createScaledBitmap(sprite, widthSpriteDst, heightSpriteDst, true);

                    yOffset += (HEIGHT_SPRITE + HEIGHT_DIVIDER);
                }
                yOffset = Y_OFFSET_INIT;
                xOffset += (WIDTH_SPRITE + WIDTH_DIVIDER);
            }

            return sprites;
        }

        public static Bitmap initCoinSprite(Resources resources) {
            Bitmap spriteCoin = BitmapFactory.decodeResource(resources,
                    R.drawable.ic_coins_l);

            return spriteCoin;
        }

        public static Bitmap initSolidTileSprite(Resources resources) {
            return initTileSprite(resources, xSolidTile, ySolidTile);
        }

        public static Bitmap initWalkableTileSprite(Resources resources) {
            return initTileSprite(resources, xWalkableTile, yWalkableTile);
        }

        public static Bitmap initBoulderTileSprite(Resources resources) {
            return initTileSprite(resources, xBoulderTile, yBoulderTile);
        }

        private static Bitmap initTileSprite(Resources resources, int xTile, int yTile) {
            Bitmap spriteSheet = BitmapFactory.decodeResource(resources,
                    R.drawable.gbc_pokemon_red_blue_characters_overworld);

            float ratioHorizontal = (float) spriteSheet.getWidth() / WIDTH_SPRITE_SHEET_ACTUAL;
            float ratioVertical = (float) spriteSheet.getHeight() / HEIGHT_SPRITE_SHEET_ACTUAL;

            int widthSpriteConverted = (int) (WIDTH_SPRITE * ratioHorizontal);
            int heightSpriteConverted = (int) (HEIGHT_SPRITE * ratioVertical);

            Bitmap tileWalkable = Bitmap.createBitmap(spriteSheet,
                    (int) (xTile * ratioHorizontal), (int) (yTile * ratioVertical),
                    (int) (widthSpriteConverted - ratioHorizontal), (int) (heightSpriteConverted - ratioVertical));

            return tileWalkable;
        }
    }

    private void stopEntityAnimations() {
        for (Entity e : entities) {
            e.stopAnimations();
        }
    }

    private void startEntityAnimations() {
        for (Entity e : entities) {
            e.startAnimations();
        }
    }

    public World(@NonNull Context context) {
        super(context);
    }

    public World(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector != null) {
            return gestureDetector.onTouchEvent(event);
        } else {
            return super.onTouchEvent(event);
        }
    }

    public void init(int widthDeviceScreen, int heightDeviceScreen,
                     int widthSpriteDst, int heightSpriteDst,
                     int resIdTileCollisionModel,
                     int resIdTileCollisionBackground,
                     SoundManager soundManager,
                     OnSwipeListener bottomDrawerSwipeListener,
                     GameFragment.ReplaceFragmentListener replaceFragmentListener) {
        this.widthDeviceScreen = widthDeviceScreen;
        this.heightDeviceScreen = heightDeviceScreen;
        this.widthSpriteDst = widthSpriteDst;
        this.heightSpriteDst = heightSpriteDst;
        this.soundManager = soundManager;
        gestureDetector = new GestureDetector(getContext(), bottomDrawerSwipeListener);

        loadWorld(resIdTileCollisionModel, resIdTileCollisionBackground, replaceFragmentListener);

        gameCamera = new GameCamera(0f, 0f,
                widthDeviceScreen, heightDeviceScreen,
                getWidthWorldInPixels(), getHeightWorldInPixels());
    }

    public void loadWorld(int resIdTileCollisionSource, int resIdTileCollisionBackground,
                          GameFragment.ReplaceFragmentListener replaceFragmentListener) {
        sprites = SpriteInitializer.initSprites(getResources(), widthSpriteDst, heightSpriteDst);
        spriteCoin = SpriteInitializer.initCoinSprite(getResources());
        spriteTileSolid = SpriteInitializer.initSolidTileSprite(getResources());
        spriteTileWalkable = SpriteInitializer.initWalkableTileSprite(getResources());
        spriteTileBoulder = SpriteInitializer.initBoulderTileSprite(getResources());

        // TILES
        Tile.init(widthSpriteDst, heightSpriteDst);
        String stringOfTilesIDs = TileMapLoader.loadFileAsString(getResources(),
                resIdTileCollisionSource);
        Bitmap fullWorldMap = BitmapFactory.decodeResource(getResources(),
                resIdTileCollisionBackground);
        tiles = TileMapLoader.convertStringToTileIDs(stringOfTilesIDs, fullWorldMap);
        widthWorldInTiles = tiles.length;
        heightWorldInTiles = tiles[0].length;

        // ENTITIES
        List<Entity> entities = generateEntities(replaceFragmentListener);
        // TODO:
        Entity.init(
                entities,
                widthSpriteDst, heightSpriteDst,
                getWidthWorldInPixels(), getHeightWorldInPixels()
        );
    }

    private List<Entity> generateEntities(GameFragment.ReplaceFragmentListener replaceFragmentListener) {
        Entity.CollisionListener collisionListener = new Entity.CollisionListener() {
            @Override
            public void onJustCollided(Entity collided) {
                if (collided instanceof NonPlayableCharacter) {
                    if (((NonPlayableCharacter) collided).getId().equals("coin")) {
                        soundManager.sfxPlay(soundManager.sfxGetItem);
                    }
                } else if (collided instanceof Player) {
                    soundManager.sfxPlay(soundManager.sfxHorn);
                }
            }
        };
        Entity.CollisionListener collisionListenerCoin = new Entity.CollisionListener() {
            @Override
            public void onJustCollided(Entity collided) {
                if (collided instanceof Player) {
                    soundManager.sfxPlay(soundManager.sfxHorn);
                }
            }
        };
        Entity.MovementListener movementListener = new Entity.MovementListener() {
            @Override
            public boolean onMove(int[] futureCorner1, int[] futureCorner2) {
                int xFutureCorner1 = futureCorner1[0];
                int yFutureCorner1 = futureCorner1[1];
                int xFutureCorner2 = futureCorner2[0];
                int yFutureCorner2 = futureCorner2[1];

                int xIndex1 = xFutureCorner1 / Tile.widthSpriteDst;
                int yIndex1 = yFutureCorner1 / Tile.heightSpriteDst;
                int xIndex2 = xFutureCorner2 / Tile.widthSpriteDst;
                int yIndex2 = yFutureCorner2 / Tile.heightSpriteDst;

                boolean isWalkableCorner1 = checkIsWalkableTile(xIndex1, yIndex1);
                boolean isWalkableCorner2 = checkIsWalkableTile(xIndex2, yIndex2);

                return (isWalkableCorner1 && isWalkableCorner2);
            }
        };

        ////////////////////////////////////////////////////////////
        NonPlayableCharacter npcRival = generateNonPlayableCharacter("rival", 3,
                200, 12,
                false, DOWN,
                collisionListener,
                movementListener);
        NonPlayableCharacter npcCoin = generateNonPlayableCharacter("coin", -1,
                201, 14,
                true, DOWN,
                collisionListenerCoin,
                movementListener);
        NonPlayableCharacter npcRivalLeader = generateNonPlayableCharacter("rival leader", 28,
                201, 15,
                true, LEFT,
                collisionListener,
                movementListener);
        NonPlayableCharacter npcJrTrainer = generateNonPlayableCharacter("jr trainer", 11,
                201, 19,
                true, LEFT,
                collisionListener,
                movementListener);
        NonPlayableCharacter npcLass02 = generateNonPlayableCharacter("lass02", 17,
                200, 22,
                true, RIGHT,
                collisionListener,
                movementListener);
        NonPlayableCharacter npcYoungster = generateNonPlayableCharacter("youngster", 10,
                201, 25,
                true, LEFT,
                collisionListener,
                movementListener);
        NonPlayableCharacter npcLass01 = generateNonPlayableCharacter("lass01", 17,
                200, 28,
                true, RIGHT,
                collisionListener,
                movementListener);
        NonPlayableCharacter npcBugCatcher = generateNonPlayableCharacter("bug catcher", 10,
                201, 31,
                true, LEFT,
                collisionListener,
                movementListener);
        Player player = generatePlayer(200, 34, replaceFragmentListener);

        entities = new ArrayList<>();
        entities.add(npcRival);
        entities.add(npcCoin);
        entities.add(npcRivalLeader);
        entities.add(npcJrTrainer);
        entities.add(npcLass02);
        entities.add(npcYoungster);
        entities.add(npcLass01);
        entities.add(npcBugCatcher);
        entities.add(player);
        ////////////////////////////////////////////////////////////

        return entities;
    }

    private NonPlayableCharacter generateNonPlayableCharacter(String id, int yIndexForSprites,
                                                              int xIndexSpawn, int yIndexSpawn,
                                                              boolean isStationary, Direction directionFacing,
                                                              Entity.CollisionListener entityCollisionListener,
                                                              Entity.MovementListener entityMovementListener) {
        Resources resources = getResources();

        AnimationDrawable animationDrawableUp = new AnimationDrawable();
        animationDrawableUp.setOneShot(false);
        AnimationDrawable animationDrawableDown = new AnimationDrawable();
        animationDrawableDown.setOneShot(false);
        AnimationDrawable animationDrawableLeft = new AnimationDrawable();
        animationDrawableLeft.setOneShot(false);
        AnimationDrawable animationDrawableRight = new AnimationDrawable();
        animationDrawableRight.setOneShot(false);
        if (yIndexForSprites >= 0) {
            animationDrawableUp.addFrame(
                    new BitmapDrawable(resources, sprites[3][yIndexForSprites]), durationOfFrameInMilli);
            animationDrawableUp.addFrame(
                    new BitmapDrawable(resources, sprites[4][yIndexForSprites]), durationOfFrameInMilli);
            animationDrawableUp.addFrame(
                    new BitmapDrawable(resources, sprites[5][yIndexForSprites]), durationOfFrameInMilli);

            animationDrawableDown.addFrame(
                    new BitmapDrawable(resources, sprites[0][yIndexForSprites]), durationOfFrameInMilli);
            animationDrawableDown.addFrame(
                    new BitmapDrawable(resources, sprites[1][yIndexForSprites]), durationOfFrameInMilli);
            animationDrawableDown.addFrame(
                    new BitmapDrawable(resources, sprites[2][yIndexForSprites]), durationOfFrameInMilli);

            animationDrawableLeft.addFrame(
                    new BitmapDrawable(resources, sprites[6][yIndexForSprites]), durationOfFrameInMilli);
            animationDrawableLeft.addFrame(
                    new BitmapDrawable(resources, sprites[7][yIndexForSprites]), durationOfFrameInMilli);

            animationDrawableRight.addFrame(
                    new BitmapDrawable(resources, sprites[8][yIndexForSprites]), durationOfFrameInMilli);
            animationDrawableRight.addFrame(
                    new BitmapDrawable(resources, sprites[9][yIndexForSprites]), durationOfFrameInMilli);
        } else {
            animationDrawableUp.addFrame(
                    new BitmapDrawable(resources, spriteCoin), durationOfFrameInMilli);
            animationDrawableUp.addFrame(
                    new BitmapDrawable(resources, spriteCoin), durationOfFrameInMilli);
            animationDrawableUp.addFrame(
                    new BitmapDrawable(resources, spriteCoin), durationOfFrameInMilli);

            animationDrawableDown.addFrame(
                    new BitmapDrawable(resources, spriteCoin), durationOfFrameInMilli);
            animationDrawableDown.addFrame(
                    new BitmapDrawable(resources, spriteCoin), durationOfFrameInMilli);
            animationDrawableDown.addFrame(
                    new BitmapDrawable(resources, spriteCoin), durationOfFrameInMilli);

            animationDrawableLeft.addFrame(
                    new BitmapDrawable(resources, spriteCoin), durationOfFrameInMilli);
            animationDrawableLeft.addFrame(
                    new BitmapDrawable(resources, spriteCoin), durationOfFrameInMilli);

            animationDrawableRight.addFrame(
                    new BitmapDrawable(resources, spriteCoin), durationOfFrameInMilli);
            animationDrawableRight.addFrame(
                    new BitmapDrawable(resources, spriteCoin), durationOfFrameInMilli);
        }

        Map<Direction, AnimationDrawable> animationsByDirection = new HashMap<>();
        animationsByDirection.put(UP, animationDrawableUp);
        animationsByDirection.put(DOWN, animationDrawableDown);
        animationsByDirection.put(LEFT, animationDrawableLeft);
        animationsByDirection.put(RIGHT, animationDrawableRight);
        NonPlayableCharacter nonPlayableCharacter = new NonPlayableCharacter(id,
                animationsByDirection,
                directionFacing,
                entityCollisionListener,
                entityMovementListener);

        nonPlayableCharacter.setxPos(xIndexSpawn * widthSpriteDst);
        nonPlayableCharacter.setyPos(yIndexSpawn * heightSpriteDst);
        if (isStationary) {
            nonPlayableCharacter.turnStationaryOn();
        }

        return nonPlayableCharacter;
    }

    private Player generatePlayer(int xIndexSpawn, int yIndexSpawn,
                                  GameFragment.ReplaceFragmentListener replaceFragmentListener) {
        Resources resources = getResources();

        AnimationDrawable animationDrawableUp = new AnimationDrawable();
        animationDrawableUp.setOneShot(false);
        animationDrawableUp.addFrame(
                new BitmapDrawable(resources, sprites[3][1]), durationOfFrameInMilli);
        animationDrawableUp.addFrame(
                new BitmapDrawable(resources, sprites[4][1]), durationOfFrameInMilli);
        animationDrawableUp.addFrame(
                new BitmapDrawable(resources, sprites[5][1]), durationOfFrameInMilli);

        AnimationDrawable animationDrawableDown = new AnimationDrawable();
        animationDrawableDown.setOneShot(false);
        animationDrawableDown.addFrame(
                new BitmapDrawable(resources, sprites[0][1]), durationOfFrameInMilli);
        animationDrawableDown.addFrame(
                new BitmapDrawable(resources, sprites[1][1]), durationOfFrameInMilli);
        animationDrawableDown.addFrame(
                new BitmapDrawable(resources, sprites[2][1]), durationOfFrameInMilli);

        AnimationDrawable animationDrawableLeft = new AnimationDrawable();
        animationDrawableLeft.setOneShot(false);
        animationDrawableLeft.addFrame(
                new BitmapDrawable(resources, sprites[6][1]), durationOfFrameInMilli);
        animationDrawableLeft.addFrame(
                new BitmapDrawable(resources, sprites[7][1]), durationOfFrameInMilli);

        AnimationDrawable animationDrawableRight = new AnimationDrawable();
        animationDrawableRight.setOneShot(false);
        animationDrawableRight.addFrame(
                new BitmapDrawable(resources, sprites[8][1]), durationOfFrameInMilli);
        animationDrawableRight.addFrame(
                new BitmapDrawable(resources, sprites[9][1]), durationOfFrameInMilli);

        Map<Direction, AnimationDrawable> animationsByDirection = new HashMap<>();
        animationsByDirection.put(UP, animationDrawableUp);
        animationsByDirection.put(DOWN, animationDrawableDown);
        animationsByDirection.put(LEFT, animationDrawableLeft);
        animationsByDirection.put(RIGHT, animationDrawableRight);
        Player player = new Player(animationsByDirection,
                new Entity.CollisionListener() {
                    @Override
                    public void onJustCollided(Entity collided) {
                        if (collided instanceof NonPlayableCharacter) {
                            if (((NonPlayableCharacter) collided).getId().equals("coin")) {
                                soundManager.sfxPlay(soundManager.sfxGetItem);
                            } else if (((NonPlayableCharacter) collided).getId().equals("rival")) {
//                                soundManager.sfxPlay(soundManager.sfxHorn);
                            } else if (((NonPlayableCharacter) collided).getId().equals("rival leader")) {
                                FragmentManager fragmentManager = listener.onShowDialog();

                                TypeWriterDialogFragment typeWriterDialogFragment =
                                        (TypeWriterDialogFragment) fragmentManager.findFragmentByTag(TypeWriterDialogFragment.TAG);

                                if (typeWriterDialogFragment != null) {
                                    Log.e(TAG, "typeWriterDialogFragment != null");

                                    if (!typeWriterDialogFragment.isVisible()) {
                                        Log.e(TAG, "!typeWriterDialogFragment.isVisible()... show TypeWriterDialogFragment.");
                                        typeWriterDialogFragment.show(fragmentManager, TypeWriterDialogFragment.TAG);
                                    } else {
                                        Log.e(TAG, "typeWriterDialogFragment.isVisible()... do nothing.");
                                    }
                                } else {
                                    Log.e(TAG, "typeWriterDialogFragment == null");
                                    Log.e(TAG, "First time instantiating TypeWriterDialogFragment... show TypeWriterDialogFragment.");

                                    String message = "Congratulations! You beat our 5 contest trainers! You just earned a fabulous prize! [Player] received a NUGGET! By the way, would you like to join TEAM ROCKET? We're a group dedicated to evil using POKEMON! Want to join? Are you sure? Come on, join us! I'm telling you to join! OK, you need convincing! I'll make you an offer you can't refuse! \n\nWith your ability, you could become a top leader in TEAM ROCKET!";

                                    TypeWriterDialogFragment typeWriterDialogFragmentRivalLeader =
                                            TypeWriterDialogFragment.newInstance(50L, message, new TypeWriterDialogFragment.DismissListener() {
                                                        @Override
                                                        public void onDismiss() {
                                                            Log.e(TAG, "onDismiss()");

                                                            startEntityAnimations();
                                                        }
                                                    },
                                                    new TypeWriterTextView.TextCompletionListener() {
                                                        @Override
                                                        public void onAnimationFinish() {
                                                            Log.e(TAG, "onAnimationFinish()");

                                                            // TODO:
                                                            ChoiceDialogFragment choiceDialogFragmentYesOrNo =
                                                                    ChoiceDialogFragment.newInstance(
                                                                            new ChoiceDialogFragment.ChoiceListener() {
                                                                                @Override
                                                                                public void onChoiceYesSelected(View view, ChoiceDialogFragment choiceDialogFragment) {
                                                                                    Log.e(TAG, "YES selected");

                                                                                    soundManager.sfxPlay(soundManager.sfxGetItem);

                                                                                    choiceDialogFragment.dismiss();
                                                                                    TypeWriterDialogFragment typeWriterDialogFragmentRivalLeaderFromFM =
                                                                                            (TypeWriterDialogFragment) fragmentManager.findFragmentByTag(TypeWriterDialogFragment.TAG);
                                                                                    typeWriterDialogFragmentRivalLeaderFromFM.dismiss();

                                                                                    // TODO:
                                                                                    replaceFragmentListener.onReplaceFragment(SequenceTrainerFragment.newInstance(null, null));
                                                                                }

                                                                                @Override
                                                                                public void onChoiceNoSelected(View view, ChoiceDialogFragment choiceDialogFragment) {
                                                                                    Log.e(TAG, "NO selected");

                                                                                    soundManager.sfxPlay(soundManager.sfxCollision);

                                                                                    choiceDialogFragment.dismiss();
                                                                                    TypeWriterDialogFragment typeWriterDialogFragmentRivalLeaderFromFM =
                                                                                            (TypeWriterDialogFragment) fragmentManager.findFragmentByTag(TypeWriterDialogFragment.TAG);
                                                                                    typeWriterDialogFragmentRivalLeaderFromFM.dismiss();

                                                                                    // TODO:
                                                                                }
                                                                            });
                                                            choiceDialogFragmentYesOrNo.show(fragmentManager, ChoiceDialogFragment.TAG);
                                                        }
                                                    });
                                    stopEntityAnimations();
                                    typeWriterDialogFragmentRivalLeader.show(fragmentManager, TypeWriterDialogFragment.TAG);
                                }
                            }
                        }
                    }
                },
                new Entity.MovementListener() {
                    @Override
                    public boolean onMove(int[] futureCorner1, int[] futureCorner2) {
                        int xFutureCorner1 = futureCorner1[0];
                        int yFutureCorner1 = futureCorner1[1];
                        int xFutureCorner2 = futureCorner2[0];
                        int yFutureCorner2 = futureCorner2[1];

                        int xIndex1 = xFutureCorner1 / Tile.widthSpriteDst;
                        int yIndex1 = yFutureCorner1 / Tile.heightSpriteDst;
                        int xIndex2 = xFutureCorner2 / Tile.widthSpriteDst;
                        int yIndex2 = yFutureCorner2 / Tile.heightSpriteDst;

                        boolean isWalkableCorner1 = checkIsWalkableTile(xIndex1, yIndex1);
                        boolean isWalkableCorner2 = checkIsWalkableTile(xIndex2, yIndex2);

                        return (isWalkableCorner1 && isWalkableCorner2);
                    }
                });

        player.setxPos(xIndexSpawn * widthSpriteDst);
        player.setyPos(yIndexSpawn * heightSpriteDst);

        return player;
    }

    public boolean checkIsWalkableTile(int x, int y) {
        Tile tile = tiles[x][y];
        boolean isWalkable = !tile.isSolid();
        return isWalkable;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int xStart = (int) Math.max(0, gameCamera.getxOffset() / Tile.widthSpriteDst);
        int xEnd = (int) Math.min(widthWorldInTiles, ((gameCamera.getxOffset() + widthDeviceScreen) / Tile.widthSpriteDst) + 1);
        int yStart = (int) Math.max(0, gameCamera.getyOffset() / Tile.heightSpriteDst);
        int yEnd = (int) Math.min(heightWorldInTiles, ((gameCamera.getyOffset() + heightDeviceScreen) / Tile.heightSpriteDst) + 1);

        for (int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++) {
                tiles[x][y].render(canvas,
                        (int) (x * Tile.widthSpriteDst - gameCamera.getxOffset()),
                        (int) (y * Tile.heightSpriteDst - gameCamera.getyOffset()));
            }
        }
    }

    public void setListener(ShowDialogListener listener) {
        this.listener = listener;
    }

    public GameCamera getGameCamera() {
        return gameCamera;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public int getWidthWorldInPixels() {
        return widthWorldInTiles * Tile.widthSpriteDst;
    }

    public int getHeightWorldInPixels() {
        return heightWorldInTiles * Tile.heightSpriteDst;
    }
}
