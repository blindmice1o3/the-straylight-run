package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.evo.hud;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.fish.FishForm;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.tiles.Tile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class HeadUpDisplay
        implements Serializable {
    transient private Game game;
    private ArrayList<ComponentHUD> timedNumericIndicators;

    public HeadUpDisplay(Game game) {
        this.game = game;
        timedNumericIndicators = new ArrayList<ComponentHUD>();
        widthScreen = game.getWidthViewport();
        xCenterOfScreen = widthScreen / 2;
    }

    public void update(long elapsed) {
        Iterator<ComponentHUD> it = timedNumericIndicators.iterator();
        while (it.hasNext()) {
            ComponentHUD componentHUD = it.next();

            ////////////////////
            componentHUD.update(elapsed);
            ////////////////////

            if (componentHUD.isTimerFinished()) {
                it.remove();
            }
        }
    }

    public void render(Canvas canvas) {
        for (int i = 0; i < timedNumericIndicators.size(); i++) {
            ComponentHUD componentHUD = timedNumericIndicators.get(i);
            ///////////////////////
            componentHUD.render(canvas);
            ///////////////////////
        }

        //hp and exp indicators.
        renderHUD(canvas);
    }

    private static final String LABEL_HP = "hp: ";
    private static final String LABEL_EXP = "experiencePoints: ";
    private static final float TEXT_SIZE = 40f;
    private static final int MARGIN_SIZE_HORIZONTAL = Tile.WIDTH;
    private static final int MARGIN_SIZE_VERTICAL = Tile.HEIGHT;
    private static final int PADDING_SIZE_HORIZONTAL = 2;
    private static final int PADDING_SIZE_VERTICAL = 2;
    private int widthScreen;
    private int xCenterOfScreen;
    transient private static Rect boundsLabelHp;
    transient private static Paint paintLabelHp;
    private static int heightLabelHp;
    private static int widthLabelHp;

    public int calculateHpLabelY1() {
        return MARGIN_SIZE_VERTICAL + heightLabelHp + heightLabelHp;
    }

    static {
        boundsLabelHp = new Rect();
        paintLabelHp = new Paint();
        paintLabelHp.setAntiAlias(true);
        paintLabelHp.setColor(Color.GREEN);
        paintLabelHp.setTextSize(TEXT_SIZE);
        paintLabelHp.getTextBounds(LABEL_HP, 0, LABEL_HP.length(), boundsLabelHp);
        heightLabelHp = boundsLabelHp.height();
        widthLabelHp = boundsLabelHp.width();

    }

    private void renderHUD(Canvas canvas) {
        Player playerBase = Player.getInstance();
        FishForm player = ((FishForm) playerBase.getForm());

        // HP LABEL & HP BAR
        canvas.drawText(LABEL_HP, MARGIN_SIZE_HORIZONTAL, MARGIN_SIZE_VERTICAL + heightLabelHp, paintLabelHp);
        canvas.drawText(Integer.toString(player.getHealth()), MARGIN_SIZE_HORIZONTAL,
                MARGIN_SIZE_VERTICAL + heightLabelHp + heightLabelHp, paintLabelHp);

        // HP BAR BACKGROUND
        int x0HpBarBackground = MARGIN_SIZE_HORIZONTAL + widthLabelHp + MARGIN_SIZE_HORIZONTAL;
        int y0HpBarBackground = heightLabelHp;
        int x1HpBarBackground = xCenterOfScreen - MARGIN_SIZE_HORIZONTAL;
        int y1HpBarBackground = heightLabelHp + MARGIN_SIZE_VERTICAL;
        Paint paintHpBarBackground = new Paint();
        paintHpBarBackground.setAntiAlias(true);
        paintHpBarBackground.setColor(Color.BLACK);
        canvas.drawRect(x0HpBarBackground, y0HpBarBackground, x1HpBarBackground, y1HpBarBackground, paintHpBarBackground);
        // The HUD's hp bar uses percent so the width won't change when healthMax changes.
        // Use floating-point-division (int-division lobs-off digits).
        float currentHealthPercent = (float) player.getHealth() / (float) player.getHealthMax();
        if (currentHealthPercent < 0) {
            currentHealthPercent = 0;
        }
        // HP BAR FOREGROUND
        int x0HPBarForeground = MARGIN_SIZE_HORIZONTAL + widthLabelHp + MARGIN_SIZE_HORIZONTAL + PADDING_SIZE_HORIZONTAL;
        int y0HPBarForeground = heightLabelHp + PADDING_SIZE_VERTICAL;
        int lengthOfHPBarForeground = xCenterOfScreen - (MARGIN_SIZE_HORIZONTAL + PADDING_SIZE_HORIZONTAL) - x0HPBarForeground;
        int x1HPBarForeground = x0HPBarForeground + (int) (lengthOfHPBarForeground * currentHealthPercent);
        int y1HPBarForeground = heightLabelHp + MARGIN_SIZE_VERTICAL - PADDING_SIZE_VERTICAL;
        Paint paintHpBarForeground = new Paint();
        paintHpBarForeground.setAntiAlias(true);
        paintHpBarForeground.setColor(Color.GREEN);
        canvas.drawRect(x0HPBarForeground, y0HPBarForeground, x1HPBarForeground, y1HPBarForeground, paintHpBarForeground);

        //XP
        Paint paintLabelExp = new Paint();
        paintLabelExp.setAntiAlias(true);
        paintLabelExp.setColor(Color.WHITE);
        paintLabelExp.setTextSize(TEXT_SIZE);
        canvas.drawText(LABEL_EXP + player.getExperiencePoints(),
                xCenterOfScreen + MARGIN_SIZE_HORIZONTAL, MARGIN_SIZE_VERTICAL + heightLabelHp, paintLabelExp);
    }

    public void addTimedNumericIndicator(ComponentHUD componentHUD) {
        timedNumericIndicators.add(componentHUD);
    }

    // GETTERS AND SETTERS

    public ArrayList<ComponentHUD> getTimedNumericIndicators() {
        return timedNumericIndicators;
    }

    public void setTimedNumericIndicators(ArrayList<ComponentHUD> timedNumericIndicators) {
        this.timedNumericIndicators = timedNumericIndicators;
    }
}