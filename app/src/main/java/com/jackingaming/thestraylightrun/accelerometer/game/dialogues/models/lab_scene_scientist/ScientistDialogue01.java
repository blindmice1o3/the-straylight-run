package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.models.lab_scene_scientist;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;

import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.EditTextDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.outputs.TypeWriterDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.models.DialogueState;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.views.TypeWriterTextView;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.LabScene;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.Scene;

public class ScientistDialogue01
        implements DialogueState {
    public static final String TAG = ScientistDialogue01.class.getSimpleName();

    private Resources resources;
    private Game.GameListener gameListener;
    private LabScene labScene;
    private Bitmap portraitOfSpeaker;

    private TypeWriterDialogFragment dialogFragmentScientistDialogue02;

    @Override
    public void showDialogue(Resources resources, Game.GameListener gameListener,
                             Scene scene, Bitmap portraitOfSpeaker) {
        this.resources = resources;
        this.gameListener = gameListener;
        if (scene instanceof LabScene) {
            labScene = (LabScene) scene;
        }
        this.portraitOfSpeaker = portraitOfSpeaker;


        String messageScientistDialogue02 = resources.getString(R.string.scientist_dialogue02);
        dialogFragmentScientistDialogue02 =
                TypeWriterDialogFragment.newInstance(50L, portraitOfSpeaker,
                        messageScientistDialogue02,
                        new TypeWriterDialogFragment.DismissListener() {
                            @Override
                            public void onDismiss() {
                                Log.e(TAG, "onDismiss()");
                                labScene.unpause();
                            }
                        },
                        new TypeWriterTextView.TextCompletionListener() {
                            @Override
                            public void onAnimationFinish() {
                                Log.e(TAG, "onAnimationFinish()");
                                onAnimationFinishScientistDialogue02(
                                        dialogFragmentScientistDialogue02,
                                        portraitOfSpeaker
                                );
                            }
                        });

        gameListener.onShowDialogFragment(
                dialogFragmentScientistDialogue02,
                "ScientistDialogue02"
        );
    }

    private void onAnimationFinishScientistDialogue02(TypeWriterDialogFragment typeWriterDialogFragmentScientistDialogue02,
                                                      Bitmap portraitScientist) {
        EditTextDialogFragment dialogFragmentScientistInput01 = EditTextDialogFragment.newInstance(
                new EditTextDialogFragment.EnterListener() {
                    @Override
                    public void onDismiss() {
                        Log.e(TAG, "onDismiss()");
                        typeWriterDialogFragmentScientistDialogue02.dismiss();
                    }

                    @Override
                    public void onEnterKeyPressed(String name) {
                        Log.e(TAG, "onEnterKeyPressed()");
                        labScene.setNameRival(name);
                        onEnterKeyPressedScientistInput01(portraitScientist, name);
                    }
                }
        );

        gameListener.onShowDialogFragment(
                dialogFragmentScientistInput01,
                "ScientistInput00"
        );
    }

    private void onEnterKeyPressedScientistInput01(Bitmap portraitScientist, String nameRival) {
        labScene.pause();

        String messageTemplate03 = resources.getString(R.string.scientist_dialogue03_template);
        String messageScientistDialogue03 = String.format(messageTemplate03, nameRival);

        TypeWriterDialogFragment dialogFragmentScientistDialogue03 =
                TypeWriterDialogFragment.newInstance(50L, portraitScientist,
                        messageScientistDialogue03,
                        new TypeWriterDialogFragment.DismissListener() {
                            @Override
                            public void onDismiss() {
                                Log.e(TAG, "onDismiss()");
                                labScene.changeToNextDialogueWithScientist();
                                labScene.startDialogueWithScientist(portraitScientist);
                            }
                        }, new TypeWriterTextView.TextCompletionListener() {
                            @Override
                            public void onAnimationFinish() {
                                Log.e(TAG, "onAnimationFinish()");
                            }
                        });

        gameListener.onShowDialogFragment(
                dialogFragmentScientistDialogue03,
                "ScientistDialogue03"
        );
    }
}
