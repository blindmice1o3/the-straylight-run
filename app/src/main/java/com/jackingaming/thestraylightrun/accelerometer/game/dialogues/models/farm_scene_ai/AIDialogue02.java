package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.models.farm_scene_ai;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.outputs.TypeWriterDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.models.DialogueState;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.views.TypeWriterTextView;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.entities.player.Player;
import com.jackingaming.thestraylightrun.accelerometer.game.quests.Quest;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.LabScene;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.Scene;

public class AIDialogue02
        implements DialogueState {
    public static final String TAG = AIDialogue02.class.getSimpleName();

    private Resources resources;
    private Game.GameListener gameListener;
    private LabScene labScene;
    private Bitmap portraitOfSpeaker;

    private TypeWriterDialogFragment dialogFragmentAIDialogue02;

    private com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game game;
    private Quest robotDialogQuest00;

    public AIDialogue02(com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game game, Quest robotDialogQuest00) {
        this.game = game;
        this.robotDialogQuest00 = robotDialogQuest00;
    }

    @Override
    public void showDialogue(Resources resources, Game.GameListener gameListener, Scene scene, Bitmap portraitOfSpeaker) {
        this.resources = resources;
        this.gameListener = gameListener;
        if (scene instanceof LabScene) {
            labScene = (LabScene) scene;
        }
        this.portraitOfSpeaker = portraitOfSpeaker;

        String[] messages = resources.getStringArray(R.array.clippit_dialogue_array);
        String message = messages[2];

        dialogFragmentAIDialogue02 =
                TypeWriterDialogFragment.newInstance(100L, portraitOfSpeaker,
                        message,
                        new TypeWriterDialogFragment.DismissListener() {
                            @Override
                            public void onDismiss() {
                                Log.e(TAG, "onDismiss()");
                            }
                        }, new TypeWriterTextView.TextCompletionListener() {
                            @Override
                            public void onAnimationFinish() {
                                Log.e(TAG, "onAnimationFinish()");

                                // TODO: start/give third quest.
                                boolean wasQuestAccepted =
                                        Player.getInstance().getQuestManager().addQuest(
                                                robotDialogQuest00
                                        );

                                if (wasQuestAccepted) {
                                    Log.e(TAG, "wasQuestAccepted");
                                    robotDialogQuest00.dispenseStartingItems();
                                } else {
                                    Log.e(TAG, "!wasQuestAccepted");
                                }
                            }
                        });

        game.getTextboxListener().showTextbox(
                dialogFragmentAIDialogue02
        );
    }
}
