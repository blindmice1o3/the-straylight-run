package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.states;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.InputManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

public class TextboxStateImpl
        implements State {
    public static final String TAG = TextboxStateImpl.class.getSimpleName();

    private static final String TEXT_DEFAULT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    transient private Game game;

    transient private StaticLayout staticLayout;
    private int widthStaticLayout;
    private float heightStaticLayout;

    private float xTextbox;
    private float yTextbox;

    transient private TextPaint textPaint;
    private String text;

    public TextboxStateImpl() {

    }

    @Override
    public void init(Game game) {
        this.game = game;
        text = TEXT_DEFAULT;
    }

    @Override
    public void enter(Object[] args) {
        if (args != null) {
            if (args[0] instanceof String) {
                text = (String) args[0];
                Log.d(TAG, getClass().getSimpleName() + " .enter(Object[] args) text: " + text);
            }
            if (args.length >= 3) {
                xTextbox = (float) args[1];
                yTextbox = (float) args[2];
            }
        }

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(16 * game.getContext().getResources().getDisplayMetrics().density);
        textPaint.setColor(0xFF000000);

        widthStaticLayout = game.getWidthViewport();
        Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;
        float spacingMultiplier = 1;
        float spacingAddition = 0;
        boolean includePadding = false;

        staticLayout = new StaticLayout(text, textPaint, widthStaticLayout,
                alignment, spacingMultiplier, spacingAddition, includePadding);

        heightStaticLayout = staticLayout.getHeight();
        Log.d(TAG, getClass().getSimpleName() + " .enter(Object[] args) heightStaticLayout: " + heightStaticLayout);
    }

    @Override
    public void exit() {

    }

    @Override
    public void update(long elapsed) {
        interpretInput();
    }

    private void interpretInput() {
        if (game.getInputManager().isJustPressed(InputManager.Button.A)) {
            // Intentionally blank.
        } else if (game.getInputManager().isJustPressed(InputManager.Button.B)) {
            game.getStateManager().popTextboxState();
        } else if (game.getInputManager().isJustPressed(InputManager.Button.MENU)) {
            // Intentionally blank.
        }

        if (game.getInputManager().isJustPressed(InputManager.Button.UP)) {
            // Intentionally blank.
        } else if (game.getInputManager().isJustPressed(InputManager.Button.DOWN)) {
            // Intentionally blank.
        } else if (game.getInputManager().isJustPressed(InputManager.Button.LEFT)) {
            // Intentionally blank.
        } else if (game.getInputManager().isJustPressed(InputManager.Button.RIGHT)) {
            // Intentionally blank.
        }
    }

    @Override
    public void render(Canvas canvas) {
        Rect bounds = canvas.getClipBounds();

        Paint paintCenter = new Paint();
        paintCenter.setColor(Color.YELLOW);
        float x0Center = bounds.exactCenterX();
        float y0Center = bounds.exactCenterY();
        float x1Center = x0Center + (Tile.WIDTH * GameCamera.getInstance().getWidthPixelToViewportRatio());
        float y1Center = y0Center + (Tile.HEIGHT * GameCamera.getInstance().getHeightPixelToViewportRatio());
        canvas.drawRect(x0Center, y0Center, x1Center, y1Center, paintCenter);

        float lineHeight = getLineHeight(text, textPaint);
        int numberOfTextLines = staticLayout.getLineCount();
        // VERTICAL CENTER
//        yTextbox = bounds.exactCenterY() - ((numberOfTextLines * textHeight) / 2);
        // VERTICAL BOTTOM
//        yTextbox = bounds.bottom - (numberOfTextLines * lineHeight);
//        xTextbox = 0;

        canvas.save();
        canvas.translate(xTextbox, yTextbox);
        Paint paintBackground = new Paint();
        paintBackground.setColor(Color.RED);
        paintBackground.setAlpha(185);
        Rect rectBackground = new Rect(0, 0, widthStaticLayout, (int) heightStaticLayout);
        canvas.drawRect(rectBackground, paintBackground);
        staticLayout.draw(canvas);

        canvas.restore();
    }

    private float getLineHeight(String text, Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        float height = fm.bottom - fm.top + fm.leading;
        return height;

//        Rect rect = new Rect();
//        paint.getTextBounds(text, 0, text.length(), rect);
//        return rect.height();
    }
}