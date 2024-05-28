package com.jackingaming.thestraylightrun.accelerometer.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.view.SurfaceHolder;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.Scene;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.WorldScene;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.sounds.SoundManager;

public class Game {
    public static final String TAG = Game.class.getSimpleName();
    public static final int NUMBER_OF_TILES_ON_SHORTER_SIDE = 12;

    public interface GameListener {
        void onUpdateEntity(Entity e);

        float[] onCheckAccelerometer();

        void onShowDialogFragment(DialogFragment dialogFragment, String tag);

        void onReplaceFragmentInMainActivity(Fragment fragment);
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

    private UpdateableSprite ball;

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
        ball = new UpdateableSprite(widthSurfaceView, heightSurfaceView);
        /////////////////////////////////////////////////////////////////////////////////
    }

    public void init(SoundManager soundManager, GameListener gameListener) {
        this.soundManager = soundManager;
        this.gameListener = gameListener;

        // SCENES
        sceneCurrent = WorldScene.getInstance();
        ((WorldScene) sceneCurrent).init(resources, handler, soundManager,
                gameListener, gameCamera,
                widthSurfaceView, heightSurfaceView,
                widthSpriteDst, heightSpriteDst);
        sceneCurrent.enter(null, null);

        /////////////////////////////////////////////////////////////////////////////////
        Bitmap ballImage = BitmapFactory.decodeResource(resources, R.drawable.ic_coins_l);
        ball.init(ballImage);
        /////////////////////////////////////////////////////////////////////////////////
    }

    public void update(long elapsed) {
        if (sceneCurrent.isPaused()) {
            return;
        }

        sceneCurrent.update(elapsed);

        /////////////////////////////////////////////////////////////////////////////////
        ball.update(elapsed);
        /////////////////////////////////////////////////////////////////////////////////
    }

    public void draw() {
        Canvas canvas = holder.lockCanvas();

        if (canvas != null) {
            canvas.drawColor(Color.WHITE);

            sceneCurrent.draw(canvas);

            /////////////////////////////////////////////////////////////////////////////////
            ball.draw(canvas);
            /////////////////////////////////////////////////////////////////////////////////

            holder.unlockCanvasAndPost(canvas);
        }
    }

    public GameCamera getGameCamera() {
        return gameCamera;
    }

    public int getWidthSpriteDst() {
        return widthSpriteDst;
    }

    public int getHeightSpriteDst() {
        return heightSpriteDst;
    }

    public Scene getSceneCurrent() {
        return sceneCurrent;
    }
}
