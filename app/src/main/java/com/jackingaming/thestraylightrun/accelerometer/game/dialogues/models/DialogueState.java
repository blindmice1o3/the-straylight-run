package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.models;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.jackingaming.thestraylightrun.accelerometer.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.Scene;

public interface DialogueState {
    void showDialogue(Resources resources, Game.GameListener gameListener,
                      Scene scene, Bitmap portraitOfSpeaker);
}
