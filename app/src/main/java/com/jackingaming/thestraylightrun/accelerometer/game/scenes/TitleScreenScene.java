package com.jackingaming.thestraylightrun.accelerometer.game.scenes;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.entities.controllables.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.sounds.SoundManager;

import java.util.ArrayList;
import java.util.List;

public class TitleScreenScene extends Scene {
    public static final String TAG = TitleScreenScene.class.getSimpleName();

    private static TitleScreenScene instance;

    private Resources resources;
    private Handler handler;
    private SoundManager soundManager;
    private Game.GameListener gameListener;
    private Player player;
    private GameCamera gameCamera;
    private int widthSurfaceView, heightSurfaceView;
    private int widthSpriteDst, heightSpriteDst;

    private List<Entity> entities = new ArrayList<>();

    private Bitmap imageTitleScreen;

    private TitleScreenScene() {
    }

    public static TitleScreenScene getInstance() {
        if (instance == null) {
            instance = new TitleScreenScene();
        }
        return instance;
    }

    public void init(Player player, Resources resources, Handler handler, SoundManager soundManager,
                     Game.GameListener gameListener, GameCamera gameCamera,
                     int widthSurfaceView, int heightSurfaceView,
                     int widthSpriteDst, int heightSpriteDst) {
        this.player = player;
        this.resources = resources;
        this.handler = handler;
        this.soundManager = soundManager;
        this.gameListener = gameListener;
        this.gameCamera = gameCamera;
        this.widthSurfaceView = widthSurfaceView;
        this.heightSurfaceView = heightSurfaceView;
        this.widthSpriteDst = widthSpriteDst;
        this.heightSpriteDst = heightSpriteDst;

        imageTitleScreen = BitmapFactory.decodeResource(resources, R.drawable.title_screen_the_quiet_loop);
    }

    @Override
    public void update(long elapsed) {

    }

    @Override
    public void draw(Canvas canvas) {
        Rect rectOfImage = new Rect(0, 0, imageTitleScreen.getWidth(), imageTitleScreen.getHeight());
        Rect rectOnScreen = new Rect(0, 0, widthSurfaceView, heightSurfaceView);

        canvas.drawBitmap(imageTitleScreen, rectOfImage, rectOnScreen, null);
    }

    @Override
    public List<Object> exit() {
        return null;
    }

    @Override
    public void enter(List<Object> args) {

    }

    @Override
    public boolean checkIsWalkableTile(int x, int y) {
        return false;
    }

    @Override
    public List<Entity> getEntities() {
        return entities;
    }
}
