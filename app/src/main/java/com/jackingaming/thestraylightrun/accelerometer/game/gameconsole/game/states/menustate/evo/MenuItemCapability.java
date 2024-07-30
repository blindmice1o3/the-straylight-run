package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.states.menustate.evo;

import android.graphics.Canvas;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.InputManager;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;

public class MenuItemCapability
        implements MenuStateImplEvo.MenuItem {
    private static MenuItemCapability uniqueInstance;
    transient private Game game;
    private String name;

    private MenuItemCapability() {
        name = "Capability";
    }

    public static MenuItemCapability getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new MenuItemCapability();
        }
        return uniqueInstance;
    }

    @Override
    public void init(Game game) {
        this.game = game;
    }

    @Override
    public void enter(Object[] args) {

    }

    @Override
    public void exit() {

    }

    @Override
    public void update(long elapsed) {
        if (game.getInputManager().isJustPressed(InputManager.Button.B)) {
            MenuStateImplEvo.getInstance().getMenuItemManager().popMenuItemStack();
        }
    }

    @Override
    public void render(Canvas canvas) {
        MenuItemInitial.getInstance().render(canvas);
        MenuItemEvolution.getInstance().renderCapability(canvas);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Game getGame() {
        return game;
    }
}