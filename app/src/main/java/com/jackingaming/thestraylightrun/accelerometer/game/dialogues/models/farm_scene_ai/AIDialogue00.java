package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.models.farm_scene_ai;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import com.jackingaming.thestraylightrun.MainActivity;
import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ChoiceDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.outputs.TypeWriterDialogFragment;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.models.DialogueState;
import com.jackingaming.thestraylightrun.accelerometer.game.dialogues.views.TypeWriterTextView;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.SceneFarm;
import com.jackingaming.thestraylightrun.accelerometer.game.quests.Quest;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.LabScene;
import com.jackingaming.thestraylightrun.accelerometer.game.scenes.Scene;

public class AIDialogue00
        implements DialogueState {
    public static final String TAG = AIDialogue00.class.getSimpleName();

    private Resources resources;
    private Game.GameListener gameListener;
    private LabScene labScene;
    private Bitmap portraitOfSpeaker;

    transient private com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game game;
    private Quest aIQuest00;

    public AIDialogue00(com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game game, Quest aIQuest00) {
        this.game = game;
        this.aIQuest00 = aIQuest00;
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
        String message = messages[0];
        TypeWriterDialogFragment.DismissListener dismissListener = new TypeWriterDialogFragment.DismissListener() {
            @Override
            public void onDismiss() {
                Log.e(TAG, "onDismiss()");
            }
        };
        TypeWriterTextView.TextCompletionListener textCompletionListener = new TypeWriterTextView.TextCompletionListener() {
            @Override
            public void onAnimationFinish() {
                Log.e(TAG, "onAnimationFinish()");

                game.setPaused(true);

                ChoiceDialogFragment choiceDialogFragmentYesOrNo = ChoiceDialogFragment.newInstance(
                        new ChoiceDialogFragment.ChoiceListener() {
                            @Override
                            public void onChoiceYesSelected(View view, ChoiceDialogFragment choiceDialogFragment) {
                                Log.e(TAG, "YES selected");

                                choiceDialogFragment.dismiss();

                                // TODO:

                                if (game.getSceneManager().getCurrentScene() instanceof SceneFarm) {
                                    ((SceneFarm) game.getSceneManager().getCurrentScene()).changeToNextDialogueWithAI();
                                    ((SceneFarm) game.getSceneManager().getCurrentScene()).startDialogueWithAI(portraitOfSpeaker);
                                }
                            }

                            @Override
                            public void onChoiceNoSelected(View view, ChoiceDialogFragment choiceDialogFragment) {
                                Log.e(TAG, "NO selected");

                                // TODO:

                                choiceDialogFragment.dismiss();
                            }

                            @Override
                            public void onDismiss(ChoiceDialogFragment choiceDialogFragment) {
                                Log.e(TAG, "onDismiss(ChoiceDialogFragment)");

                                game.getStateManager().getTextboxState().setTextboxAnimationFinished(true);

                                if (game.getSceneManager().getCurrentScene() instanceof SceneFarm) {
                                    ((SceneFarm) game.getSceneManager().getCurrentScene()).setInDialogueWithClippitState(false);
                                }
                                game.getTextboxListener().showStatsDisplayer();

                                game.setPaused(false);
                            }

                            @Override
                            public void onCancel(ChoiceDialogFragment choiceDialogFragment) {
                                Log.e(TAG, "onCancel(ChoiceDialogFragment)");
                            }
                        });

                choiceDialogFragmentYesOrNo.show(
                        ((MainActivity) game.getContext()).getSupportFragmentManager(),
                        ChoiceDialogFragment.TAG
                );
            }
        };

        game.getStateManager().pushTextboxState(portraitOfSpeaker,
                message,
                dismissListener,
                textCompletionListener);
    }
}
