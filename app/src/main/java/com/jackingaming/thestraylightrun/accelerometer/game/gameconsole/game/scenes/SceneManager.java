package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes;

import android.graphics.Canvas;
import android.util.Log;

import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.bubblepop.SceneBubblePop;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.evo.SceneEvo;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.frogger.SceneFrogger;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.pocketcritters.SceneHome01;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.pocketcritters.SceneHome02;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.pocketcritters.SceneHomeRival;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.pocketcritters.SceneLab;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.pocketcritters.SceneWorldMapPart01;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.pong.ScenePong;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.SceneChickenCoop;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.SceneCowBarn;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.SceneFarm;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.SceneHothouse;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.SceneHouseLevel01;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.SceneSheepPen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SceneManager
        implements Serializable {
    public static final String TAG = SceneManager.class.getSimpleName();

    transient private Game game;

    private List<Scene> sceneStack;

    public SceneManager(String gameTitle) {
        sceneStack = new ArrayList<Scene>();

        switch (gameTitle) {
            case "Pocket Critters":
                sceneStack.add(SceneWorldMapPart01.getInstance());
                break;
            case "Pooh Farmer":
                sceneStack.add(SceneFarm.getInstance());
                break;
            case "Bubble Pop":
                sceneStack.add(SceneBubblePop.getInstance());
                break;
            case "Evo":
                sceneStack.add(SceneEvo.getInstance());
                break;
            case "Frogger":
                sceneStack.add(SceneFrogger.getInstance());
                break;
            case "Pong":
                sceneStack.add(ScenePong.getInstance());
                break;
            default:
                sceneStack.add(SceneWorldMapPart01.getInstance());
                break;
        }

    }

    public void init(Game game) {
        Log.e(TAG, "init()");
        this.game = game;

        for (int i = 0; i < sceneStack.size(); i++) {
            Scene scene = sceneStack.get(i);

            if (scene instanceof SceneFarm) {
                SceneFarm.setInstance(((SceneFarm) scene));
            } else if (scene instanceof SceneHome02) {
                SceneHome02.setInstance((SceneHome02) scene);
            }

            scene.init(game);
            scene.enter(null);
        }
    }

    public void reload(Game game) {
        this.game = game;

        for (int i = 0; i < sceneStack.size(); i++) {
            Scene scene = sceneStack.get(i);

            if (scene instanceof SceneFarm) {
                SceneFarm.setInstance(((SceneFarm) scene));
            } else if (scene instanceof SceneHome02) {
                SceneHome02.setInstance((SceneHome02) scene);
            }
        }

        SceneFarm.getInstance().reload(game);

        SceneHouseLevel01.getInstance().setGame(game);

        SceneHothouse.getInstance().setGame(game);

        SceneSheepPen.getInstance().setGame(game);

        SceneChickenCoop.getInstance().setGame(game);

        SceneCowBarn.getInstance().setGame(game);

        SceneBubblePop.getInstance().setGame(game);
    }

    public void update(long elapsed) {
        getCurrentScene().update(elapsed);
    }

    public void drawCurrentFrame(Canvas canvas) {
        getCurrentScene().drawCurrentFrame(canvas);
    }

    private int getIndexOfTop() {
        return sceneStack.size() - 1;
    }

    public Scene getCurrentScene() {
        return sceneStack.get(getIndexOfTop());
    }

    public void pop() {
        Scene sceneCurrent = getCurrentScene();
        List<Object> args = sceneCurrent.exit();

        sceneStack.remove(sceneCurrent);

        getCurrentScene().enter(args);
    }

    public void push(Scene newScene) {
        List<Object> args = getCurrentScene().exit();

        newScene.getEntityManager().addEntity(Player.getInstance());
        if (newScene.getTileManager().getTiles() == null) {
            newScene.init(game);
        }

        sceneStack.add(newScene);

        getCurrentScene().enter(args);
    }

    public void changeScene(String idOfCollidedTransferPoint) {
        if (getCurrentScene() instanceof SceneWorldMapPart01) {
            if (idOfCollidedTransferPoint.equals("HOME_01")) {
                push(SceneHome01.getInstance());
            } else if (idOfCollidedTransferPoint.equals("HOME_RIVAL")) {
                push(SceneHomeRival.getInstance());
            } else if (idOfCollidedTransferPoint.equals("LAB")) {
                push(SceneLab.getInstance());
            }
        } else if (getCurrentScene() instanceof SceneHome01) {
            if (idOfCollidedTransferPoint.equals("HOME_02")) {
                push(SceneHome02.getInstance());
            } else if (idOfCollidedTransferPoint.equals("PART_01")) {
                pop();
            }
        } else if (getCurrentScene() instanceof SceneHome02) {
            if (idOfCollidedTransferPoint.equals("HOME_01")) {
                pop();
            } else if (idOfCollidedTransferPoint.equals("FARM")) {
                push(SceneFarm.getInstance());
            } else if (idOfCollidedTransferPoint.equals("COMPUTER")) {
                ((SceneHome02) getCurrentScene()).showComputerDialog();
            }
        } else if (getCurrentScene() instanceof SceneHomeRival) {
            if (idOfCollidedTransferPoint.equals("PART_01")) {
                pop();
            }
        } else if (getCurrentScene() instanceof SceneLab) {
            if (idOfCollidedTransferPoint.equals("PART_01")) {
                pop();
            }
        } else if (getCurrentScene() instanceof SceneFarm) {
            if (idOfCollidedTransferPoint.equals("HOUSE_LEVEL_01")) {
                push(SceneHouseLevel01.getInstance());
            } else if (idOfCollidedTransferPoint.equals("HOTHOUSE")) {
                push(SceneHothouse.getInstance());
            } else if (idOfCollidedTransferPoint.equals("SHEEP_PEN")) {
                push(SceneSheepPen.getInstance());
            } else if (idOfCollidedTransferPoint.equals("CHICKEN_COOP")) {
                push(SceneChickenCoop.getInstance());
            } else if (idOfCollidedTransferPoint.equals("COW_BARN")) {
                push(SceneCowBarn.getInstance());
            } else if (idOfCollidedTransferPoint.equals("SEEDS_SHOP")) {
                // TODO:
                Log.d(TAG, getClass().getSimpleName() + ".changeScene() inSeedShopState: " + ((SceneFarm) getCurrentScene()).isInSeedShopState());
                if (!((SceneFarm) getCurrentScene()).isInSeedShopState()) {
                    ((SceneFarm) getCurrentScene()).showSeedShopFragment();
                }
            }
        } else if (getCurrentScene() instanceof SceneHouseLevel01) {
            if (idOfCollidedTransferPoint.equals("FARM")) {
                ((SceneHouseLevel01) getCurrentScene()).onExitToFarm();
                pop();
            }
        } else if (getCurrentScene() instanceof SceneChickenCoop) {
            if (idOfCollidedTransferPoint.equals("FARM")) {
                pop();
            }
        } else if (getCurrentScene() instanceof SceneCowBarn) {
            if (idOfCollidedTransferPoint.equals("FARM")) {
                pop();
            }
        } else if (getCurrentScene() instanceof SceneSheepPen) {
            if (idOfCollidedTransferPoint.equals("FARM")) {
                pop();
            }
        } else if (getCurrentScene() instanceof SceneHothouse) {
            if (idOfCollidedTransferPoint.equals("FARM")) {
                pop();
            }
        } else if (getCurrentScene() instanceof SceneBubblePop) {
            Log.i(TAG, "changeScene(String): case where getCurrentScene() instanceof SceneBubblePop");
        }
    }
}