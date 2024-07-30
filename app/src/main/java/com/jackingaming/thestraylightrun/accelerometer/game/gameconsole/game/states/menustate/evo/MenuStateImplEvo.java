package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.states.menustate.evo;

import android.graphics.Canvas;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.states.menustate.MenuStateImpl;

import java.io.Serializable;

public class MenuStateImplEvo extends MenuStateImpl {
    interface MenuItem extends Serializable {
        void init(Game game);

        void enter(Object[] args);

        void exit();

        void update(long elapsed);

        void render(Canvas canvas);

        String getName();

        Game getGame();
    }

    private static MenuStateImplEvo uniqueInstance;
    transient private Game game;

    private MenuItemManager menuItemManager;

    private MenuStateImplEvo() {
        menuItemManager = new MenuItemManager();
    }

    public static MenuStateImplEvo getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new MenuStateImplEvo();
        }
        return uniqueInstance;
    }

    @Override
    public void init(Game game) {
        this.game = game;
        menuItemManager.init(game);
    }

    @Override
    public void enter(Object[] args) {

    }

    @Override
    public void exit() {

    }

    @Override
    public void update(long elapsed) {
        menuItemManager.update(elapsed);
    }

    @Override
    public void render(Canvas canvas) {
        game.getSceneManager().getCurrentScene().drawCurrentFrame(canvas);
        menuItemManager.render(canvas);
    }

    public MenuItemManager getMenuItemManager() {
        return menuItemManager;
    }
}