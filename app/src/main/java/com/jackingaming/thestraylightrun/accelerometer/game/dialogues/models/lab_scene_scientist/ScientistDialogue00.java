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

public class ScientistDialogue00
        implements DialogueState {
    public static final String TAG = ScientistDialogue00.class.getSimpleName();

    private Resources resources;
    private Game.GameListener gameListener;
    private LabScene labScene;
    private Bitmap portraitOfSpeaker;

    private TypeWriterDialogFragment dialogFragmentScientistDialogue00;

    @Override
    public void showDialogue(Resources resources, Game.GameListener gameListener,
                             Scene scene, Bitmap portraitOfSpeaker) {
        this.resources = resources;
        this.gameListener = gameListener;
        if (scene instanceof LabScene) {
            labScene = (LabScene) scene;
        }
        this.portraitOfSpeaker = portraitOfSpeaker;

        String messageScientistDialogue00 = resources.getString(R.string.scientist_dialogue00);
        dialogFragmentScientistDialogue00 =
                TypeWriterDialogFragment.newInstance(50L, portraitOfSpeaker,
                        messageScientistDialogue00,
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
                                onAnimationFinishScientistDialogue00(
                                        dialogFragmentScientistDialogue00,
                                        portraitOfSpeaker
                                );
                            }
                        }
                );

        gameListener.onShowDialogFragment(
                dialogFragmentScientistDialogue00,
                "ScientistDialogue00"
        );
    }

    private void onAnimationFinishScientistDialogue00(TypeWriterDialogFragment typeWriterDialogFragmentScientistDialogue00,
                                                      Bitmap portraitScientist) {
        EditTextDialogFragment dialogFragmentScientistInput00 = EditTextDialogFragment.newInstance(
                new EditTextDialogFragment.EnterListener() {
                    @Override
                    public void onDismiss() {
                        Log.e(TAG, "onDismiss()");
                        typeWriterDialogFragmentScientistDialogue00.dismiss();
                    }

                    @Override
                    public void onEnterKeyPressed(String name) {
                        Log.e(TAG, "onEnterKeyPressed()");
                        labScene.setNameUser(name);
                        onEnterKeyPressedScientistInput00(portraitScientist, name);
                    }
                }
        );

        gameListener.onShowDialogFragment(
                dialogFragmentScientistInput00,
                "ScientistInput00"
        );
    }

    private void onEnterKeyPressedScientistInput00(Bitmap portraitScientist, String nameUser) {
        labScene.pause();

        String messageTemplate01 = resources.getString(R.string.scientist_dialogue01_template);
        String messageScientistDialogue01 = String.format(messageTemplate01, nameUser);

        TypeWriterDialogFragment dialogFragmentScientistDialogue01 =
                TypeWriterDialogFragment.newInstance(50L, portraitScientist,
                        messageScientistDialogue01,
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
                dialogFragmentScientistDialogue01,
                "ScientistDialogue01"
        );
    }
}
