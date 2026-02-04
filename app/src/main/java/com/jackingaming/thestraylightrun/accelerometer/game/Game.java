package com.jackingaming.thestraylightrun.accelerometer.game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.accelerometer.game.scenes.HomePlayerRoom01Scene;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.HomePlayerRoom02Scene;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.LabScene;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.Scene;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.TitleScreenScene;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.WorldScene;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.sounds.SoundManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Game {
    public static final String TAG = Game.class.getSimpleName();
    public static final String GAME_TITLE = "TheQuietLoop";
    public static final int NUMBER_OF_TILES_ON_SHORTER_SIDE = 12;

    public enum Run {ONE, TWO, THREE, FOUR, FIVE;}

    private Run run = Run.ONE;

    public Run getRun() {
        return run;
    }

    public void setRun(Run run) {
        this.run = run;
    }

    public void incrementRun() {
        switch (run) {
            case ONE:
                run = Run.TWO;
                break;
            case TWO:
                run = Run.THREE;
                break;
            case THREE:
                run = Run.FOUR;
                break;
            case FOUR:
                run = Run.FIVE;
                break;
            case FIVE:
                // Intentionally blank.
                break;
        }
    }

    public enum DailyLoop {
        TELEVISION,
        NOTES_TOPIC,
        GROUP_CHAT,
        COMPUTER,
        GAME_CONSOLE,
        SLEEP_SAVE;
    }

    private DailyLoop dailyLoop = DailyLoop.TELEVISION;

    public DailyLoop getDailyLoop() {
        return dailyLoop;
    }

    public void incrementDailyLoop() {
        switch (dailyLoop) {
            case TELEVISION:
                dailyLoop = DailyLoop.NOTES_TOPIC;
                break;
            case NOTES_TOPIC:
                dailyLoop = DailyLoop.GROUP_CHAT;
                break;
            case GROUP_CHAT:
                dailyLoop = DailyLoop.COMPUTER;
                break;
            case COMPUTER:
                dailyLoop = DailyLoop.GAME_CONSOLE;
                break;
            case GAME_CONSOLE:
                dailyLoop = DailyLoop.SLEEP_SAVE;
                break;
            case SLEEP_SAVE:
                dailyLoop = DailyLoop.TELEVISION;
                break;
        }
    }

    public interface GameListener {
        void onUpdateEntity(Entity e);

        float[] onCheckAccelerometer();

        void onShowDialogFragment(DialogFragment dialogFragment, String tag);

        void onReplaceFragmentInMainActivity(Fragment fragment);

        void onChangeScene(Scene sceneNext);

        void instantiateImageViewForEntities(List<Entity> entitiesToAdd);

        void instantiateParticleExplosionForPlayer(List<Entity> entitiesToAdd,
                                                   int widthSpriteDst, int heightSpriteDst);

        void startParticleExplosionViewForPlayer();

        void addImageViewOfEntityToFrameLayout(int widthSpriteDst, int heightSpriteDst);

        void removeImageViewOfEntityFromFrameLayout();

        Run getRun();

        void incrementRun();

        DailyLoop getDailyLoop();

        void incrementDailyLoop();

        void highlightGroupChatDrawer();

        void highlightJournalDrawer();

        void unhighlightGroupChatDrawer();

        void unhighlightJournalDrawer();

        boolean isStartDrawerOpen();
    }

    private SurfaceHolder holder;
    private Resources resources;
    private Handler handler;
    private SoundManager soundManager;
    private GameListener gameListener;
    private GameCamera gameCamera;
    private int widthSurfaceView, heightSurfaceView;
    private int widthSpriteDst, heightSpriteDst;

    private Scene sceneCurrent;

//    private UpdateableSprite ball;

    public void resetTransferPointCooldown() {
        ((HomePlayerRoom01Scene) sceneCurrent).resetTransferPointCooldown();
    }

    public void resetGroupChatState() {
        HomePlayerRoom01Scene.getInstance().setGroupChatDrawerClosed(true);
        HomePlayerRoom02Scene.getInstance().setGroupChatDrawerClosed(true);
    }

    public void resetJournalState() {
        HomePlayerRoom01Scene.getInstance().setJournalDrawerClosed(true);
        HomePlayerRoom02Scene.getInstance().setJournalDrawerClosed(true);
    }

    public Game(SurfaceHolder holder, Resources resources, Handler handler,
                int widthSurfaceView, int heightSurfaceView) {
        this.holder = holder;
        this.resources = resources;
        this.handler = handler;
        this.widthSurfaceView = widthSurfaceView;
        this.heightSurfaceView = heightSurfaceView;

        widthSpriteDst = Math.min(widthSurfaceView, heightSurfaceView) / NUMBER_OF_TILES_ON_SHORTER_SIDE;
        heightSpriteDst = widthSpriteDst;

        gameCamera = new GameCamera(0f, 0f, widthSurfaceView, heightSurfaceView);

        /////////////////////////////////////////////////////////////////////////////////
//        ball = new UpdateableSprite(widthSurfaceView, heightSurfaceView);
        /////////////////////////////////////////////////////////////////////////////////
    }

    public void changeScene(Scene sceneNext) {
        Scene sceneLeaving = sceneCurrent;
        List<Object> args = sceneLeaving.exit();

        if (sceneNext instanceof WorldScene) {
            args = new ArrayList<>();
            args.add("init");
        }

        sceneNext.enter(args);
        sceneCurrent = sceneNext;
    }

    public void init(SoundManager soundManager, GameListener gameListener) {
        this.soundManager = soundManager;
        this.gameListener = gameListener;

        // SCENES

        WorldScene.getInstance().init(resources, handler, soundManager,
                gameListener, gameCamera,
                widthSurfaceView, heightSurfaceView,
                widthSpriteDst, heightSpriteDst);
        TitleScreenScene.getInstance().init(
                WorldScene.getInstance().getPlayer(),
                resources, handler, soundManager,
                gameListener, gameCamera,
                widthSurfaceView, heightSurfaceView,
                widthSpriteDst, heightSpriteDst);
        LabScene.getInstance().init(
                WorldScene.getInstance().getPlayer(),
                resources, handler, soundManager,
                gameListener, gameCamera,
                widthSurfaceView, heightSurfaceView,
                widthSpriteDst, heightSpriteDst);
        HomePlayerRoom02Scene.getInstance().init(
                WorldScene.getInstance().getPlayer(),
                resources, handler, soundManager,
                gameListener, gameCamera,
                widthSurfaceView, heightSurfaceView,
                widthSpriteDst, heightSpriteDst);
        HomePlayerRoom01Scene.getInstance().init(
                WorldScene.getInstance().getPlayer(),
                resources, handler, soundManager,
                gameListener, gameCamera,
                widthSurfaceView, heightSurfaceView,
                widthSpriteDst, heightSpriteDst);

        sceneCurrent = TitleScreenScene.getInstance();
//        sceneCurrent = WorldScene.getInstance();
//        List<Object> argsSceneTransfer = new ArrayList<>();
//        argsSceneTransfer.add("init");
//        sceneCurrent.enter(argsSceneTransfer);

        /////////////////////////////////////////////////////////////////////////////////
//        Bitmap ballImage = BitmapFactory.decodeResource(resources, R.drawable.ic_coins_l);
//        ball.init(ballImage);
        /////////////////////////////////////////////////////////////////////////////////
    }

    private String savedFileViaUserInputFileName = "savedFileViaUserInput" + GAME_TITLE + ".ser";

    public String getSavedFileViaUserInputFileName() {
        return savedFileViaUserInputFileName;
    }

    public void save(Context context, String fileName) {
        Log.e(TAG, "save(Context context, String fileName) START fileName: " + fileName);
        try (FileOutputStream fs = context.openFileOutput(fileName, Context.MODE_PRIVATE);
             ObjectOutputStream os = new ObjectOutputStream(fs)) {
            os.writeObject(run);
            os.writeObject(dailyLoop);
            os.writeFloat(HomePlayerRoom01Scene.getInstance().getPlayer().getXPos());
            os.writeFloat(HomePlayerRoom01Scene.getInstance().getPlayer().getYPos());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "save(Context context, String fileName) FINISHED fileName: " + fileName);
    }

    public void load(Context context, String fileName) {
        Log.d(TAG, "load(Context context, String fileName) START fileName: " + fileName);
        try (FileInputStream fi = context.openFileInput(fileName);
             ObjectInputStream os = new ObjectInputStream(fi)) {
            run = (Run) os.readObject();
            dailyLoop = (DailyLoop) os.readObject();
            HomePlayerRoom01Scene.getInstance().getPlayer().setXPos(
                    os.readFloat()
            );
            HomePlayerRoom01Scene.getInstance().getPlayer().setYPos(
                    os.readFloat()
            );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "load(Context context, String fileName) FINISHED fileName: " + fileName);
    }

    public void update(long elapsed) {
        if (sceneCurrent.isPaused()) {
            return;
        }

        sceneCurrent.update(elapsed);

        /////////////////////////////////////////////////////////////////////////////////
//        ball.update(elapsed);
        /////////////////////////////////////////////////////////////////////////////////
    }

    public void draw() {
        Canvas canvas = holder.lockCanvas();

        if (canvas != null) {
            canvas.drawColor(Color.MAGENTA);
//            canvas.drawColor(Color.WHITE);

            sceneCurrent.draw(canvas);

            /////////////////////////////////////////////////////////////////////////////////
//            ball.draw(canvas);
            /////////////////////////////////////////////////////////////////////////////////

            holder.unlockCanvasAndPost(canvas);
        }
    }

    public GameCamera getGameCamera() {
        return gameCamera;
    }

    public Scene getSceneCurrent() {
        return sceneCurrent;
    }
}
