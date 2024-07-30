package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.states.menustate.evo;

import android.graphics.Canvas;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MenuItemManager
        implements Serializable {
    transient private Game game;
    private List<MenuStateImplEvo.MenuItem> menuItemStack;

    public MenuItemManager() {
        menuItemStack = new ArrayList<MenuStateImplEvo.MenuItem>();
        menuItemStack.add(MenuItemInitial.getInstance());
    }

    public void init(Game game) {
        this.game = game;

        MenuItemInitial.getInstance().init(game);
        MenuItemEvolution.getInstance().init(game);
        MenuItemConfirmation.getInstance().init(game);
        MenuItemCapability.getInstance().init(game);
        MenuItemRecordOfEvolution.getInstance().init(game);
    }

    public void update(long elapsed) {
        getCurrentMenuItem().update(elapsed);
    }

    public void render(Canvas canvas) {
        getCurrentMenuItem().render(canvas);
    }

    public void popMenuItemStack() {
        if (menuItemStack.size() > 1) {
            getCurrentMenuItem().exit();
            int indexOfTop = menuItemStack.size() - 1;
            menuItemStack.remove(indexOfTop);
            getCurrentMenuItem().enter(null);
        }
    }

    public void pushMenuItemEvolution() {
        getCurrentMenuItem().exit();
        menuItemStack.add(MenuItemEvolution.getInstance());
        getCurrentMenuItem().enter(null);
    }

    public void pushMenuItemConfirmation() {
        getCurrentMenuItem().exit();
        menuItemStack.add(MenuItemConfirmation.getInstance());
        getCurrentMenuItem().enter(null);
    }

    public void pushMenuItemCapability() {
        getCurrentMenuItem().exit();
        menuItemStack.add(MenuItemCapability.getInstance());
        getCurrentMenuItem().enter(null);
    }

    public void pushMenuItemRecordOfEvolution() {
        getCurrentMenuItem().exit();
        menuItemStack.add(MenuItemRecordOfEvolution.getInstance());
        getCurrentMenuItem().enter(null);
    }

    public MenuStateImplEvo.MenuItem getCurrentMenuItem() {
        int indexOfTop = menuItemStack.size() - 1;
        return menuItemStack.get(indexOfTop);
    }
}