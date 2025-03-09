package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.models.lab_scene_scientist;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.outputs.TypeWriterDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.models.DialogueState;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.views.TypeWriterTextView;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.LabScene;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.Scene;

public class ScientistDialogue03
        implements DialogueState {
    public static final String TAG = ScientistDialogue03.class.getSimpleName();

    private Resources resources;
    private Game.GameListener gameListener;
    private LabScene labScene;
    private Bitmap portraitOfSpeaker;

    private TypeWriterDialogFragment dialogFragmentScientistDialogue04;

    @Override
    public void showDialogue(Resources resources, Game.GameListener gameListener,
                             Scene scene, Bitmap portraitOfSpeaker) {
        this.resources = resources;
        this.gameListener = gameListener;
        if (scene instanceof LabScene) {
            labScene = (LabScene) scene;
        }
        this.portraitOfSpeaker = portraitOfSpeaker;

        String nameUser = labScene.getNameUser();
        String messageTemplate04 = resources.getString(R.string.scientist_dialogue04_template);
        String messageScientistDialogue04 = String.format(messageTemplate04, nameUser);

        dialogFragmentScientistDialogue04 =
                TypeWriterDialogFragment.newInstance(50L, portraitOfSpeaker,
                        messageScientistDialogue04,
                        new TypeWriterDialogFragment.DismissListener() {
                            @Override
                            public void onDismiss() {
                                Log.e(TAG, "onDismiss()");
                                onDismissScientistDialogue04(portraitOfSpeaker);
                            }
                        },
                        new TypeWriterTextView.TextCompletionListener() {
                            @Override
                            public void onAnimationFinish() {
                                Log.e(TAG, "onAnimationFinish()");
                            }
                        });

        gameListener.onShowDialogFragment(
                dialogFragmentScientistDialogue04,
                "ScientistDialogue04"
        );
    }

    public void onDismissScientistDialogue04(Bitmap portraitScientist) {
        String messageScientistDialogue05 = resources.getString(R.string.scientist_dialogue05);

        TypeWriterDialogFragment dialogFragmentScientistDialogue05 =
                TypeWriterDialogFragment.newInstance(50L, portraitScientist,
                        messageScientistDialogue05,
                        new TypeWriterDialogFragment.DismissListener() {
                            @Override
                            public void onDismiss() {
                                Log.e(TAG, "onDismiss()");

                                if (labScene.getCurrentDialogueState() == null) {
                                    // Used in exit() (determines: xBeforeTransfer or X_SPAWN_HOMETOWN)
                                    labScene.setDismissCompletedDialog04(true);
                                }

                                labScene.unpause();
                            }
                        },
                        new TypeWriterTextView.TextCompletionListener() {
                            @Override
                            public void onAnimationFinish() {
                                Log.e(TAG, "onAnimationFinish()");
                                labScene.changeToNextDialogueWithScientist();
                            }
                        }
                );

        gameListener.onShowDialogFragment(
                dialogFragmentScientistDialogue05,
                "ScientistDialogue05"
        );
    }
}
