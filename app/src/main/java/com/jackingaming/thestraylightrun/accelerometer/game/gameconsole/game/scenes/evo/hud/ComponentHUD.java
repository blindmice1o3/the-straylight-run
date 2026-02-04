package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.evo.hud;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.GameCamera;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.Entity;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

import java.io.Serializable;

public class ComponentHUD
        implements Serializable {
    public static final String TAG = ComponentHUD.class.getSimpleName();

    public enum ComponentType {HP, EXP, DAMAGE, TEXT;}

    private static final int BORDER = Tile.WIDTH;

    transient private Game game;
    transient private Rect rectStaticLayoutBackgroundPanel;
    transient private Paint paintStaticLayoutBackgroundPanel;
    transient private TextPaint textPaint;
    private int heightLine;

    private float x, y;
    private ComponentType currentComponentType;
    private int value;

    private long timeElapsed, timerTarget;
    private boolean timerFinished;

    private StaticLayout staticLayout;

    public ComponentHUD(Game game, ComponentType componentType, int value, Entity entity) {
        this.game = game;

        this.currentComponentType = componentType;
        this.value = value;
        x = entity.getX();
        y = entity.getY();

        timerFinished = false;
        timeElapsed = 0L;
        timerTarget = 5_000L;

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(16 * game.getContext().getResources().getDisplayMetrics().density);
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        heightLine = (int) (fm.bottom - fm.top + fm.leading);
    }

    private int findWidthOfLongestWord(String text) {
        String[] words = text.split("\\s+");
        int widthMax = 0;
        for (String word : words) {
            Rect boundsOfWord = new Rect();
            textPaint.getTextBounds(word, 0, word.length(), boundsOfWord);
            if (boundsOfWord.width() > widthMax) {
                widthMax = boundsOfWord.width();
            }
        }
        return widthMax + 3;    // +3 fixes period-on-last-word new line bug.
    }

    public ComponentHUD(Game game, ComponentType componentType, String text, Entity entity) {
        this(game, componentType, 0, entity);

        // STATIC_LAYOUT (size)
        int widthLine = findWidthOfLongestWord(text);
        Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;
        float spacingMultiplier = 1;
        float spacingAddition = 0;
        boolean includePadding = false;
        staticLayout = new StaticLayout(text, textPaint, widthLine,
                alignment, spacingMultiplier, spacingAddition, includePadding);
        int heightStaticLayout = staticLayout.getHeight();

        // STATIC_LAYOUT (position)
        x = GameCamera.getInstance().convertInGameXPositionToScreenXPosition(entity.getX()) -
                widthLine - BORDER;
        y = GameCamera.getInstance().convertInGameYPositionToScreenYPosition(entity.getY()) -
                heightStaticLayout - BORDER;

        // BACKGROUND PANEL
        paintStaticLayoutBackgroundPanel = new Paint();
        paintStaticLayoutBackgroundPanel.setColor(0xBB0000FF);
        paintStaticLayoutBackgroundPanel.setAlpha(175);
        rectStaticLayoutBackgroundPanel = new Rect((int) (x - BORDER), (int) (y - BORDER),
                (int) (x + widthLine + BORDER), (int) (y + heightStaticLayout + BORDER));
    }

    public void update(long elapsed) {
        timeElapsed += elapsed;

        if (timeElapsed >= timerTarget) {
            timerFinished = true;
        }
    }

    public void render(Canvas canvas) {
        GameCamera gameCamera = GameCamera.getInstance();
        switch (currentComponentType) {
            case HP:
                textPaint.setColor(Color.GREEN);
                canvas.drawText("+" + Integer.toString(value),
                        gameCamera.convertInGameXPositionToScreenXPosition(x),
                        gameCamera.convertInGameYPositionToScreenYPosition(y), textPaint);
                break;
            case EXP:
                textPaint.setColor(Color.YELLOW);
                canvas.drawText("+" + Integer.toString(value),
                        gameCamera.convertInGameXPositionToScreenXPosition(x),
                        gameCamera.convertInGameYPositionToScreenYPosition(y) + heightLine, textPaint);
                break;
            case DAMAGE:
                textPaint.setColor(Color.RED);
                canvas.drawText("-" + Integer.toString(value),
                        gameCamera.convertInGameXPositionToScreenXPosition(x) - heightLine,
                        gameCamera.convertInGameYPositionToScreenYPosition(y) - heightLine, textPaint);
                break;
            case TEXT:
                textPaint.setColor(Color.WHITE);
                // Draw StaticLayout.
                // BACKGROUND PANEL
                canvas.drawRect(rectStaticLayoutBackgroundPanel, paintStaticLayoutBackgroundPanel);

                // TEXT (confirmation message)
                canvas.save();
                canvas.translate(x, y); // ALREADY converted to screen coordinate system!
                staticLayout.draw(canvas);
                canvas.restore();
                break;
            default:
                Log.d(TAG, getClass().getSimpleName() + ".render(Canvas canvas), switch's default.");
                break;
        }
    }

    // GETTERS AND SETTERS

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public boolean isTimerFinished() {
        return timerFinished;
    }

    public void setTimerFinished(boolean timerFinished) {
        this.timerFinished = timerFinished;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}