package com.jackingaming.thestraylightrun.accelerometer.game.sounds;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import com.jackingaming.thestraylightrun.R;

public class SoundManager {
    public static final String TAG = SoundManager.class.getSimpleName();

    private MediaPlayer mediaPlayer;
    private boolean pausedBackgroundMusic;

    private SoundPool soundPool;
    private int indexSfx = 0;
    public int sfxBallPoof, sfxBallToss, sfxCollision,
            sfxEnterPc, sfxGetItem, sfxHorn;
    private boolean loadedSfxBallPoof, loadedSfxBallToss, loadedSfxCollision,
            loadedSfxEnterPc, loadedSfxGetItem, loadedSfxHorn;

    public SoundManager(Context context) {
//        mediaPlayer = MediaPlayer.create(context, R.raw.corporate_ukulele);
        mediaPlayer = MediaPlayer.create(context, R.raw.sounds_breathe_of_dippy);
        mediaPlayer.setLooping(true);

        soundPool = new SoundPool(
                6,
                AudioManager.STREAM_MUSIC,
                0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                Log.d(TAG, "SoundPool.OnLoadCompleteListener sampleId: " + i);

                int sampleId = i;
                if (sampleId == sfxBallPoof) {
                    loadedSfxBallPoof = true;
                } else if (sampleId == sfxBallToss) {
                    loadedSfxBallToss = true;
                } else if (sampleId == sfxCollision) {
                    loadedSfxCollision = true;
                } else if (sampleId == sfxEnterPc) {
                    loadedSfxEnterPc = true;
                } else if (sampleId == sfxGetItem) {
                    loadedSfxGetItem = true;
                } else if (sampleId == sfxHorn) {
                    loadedSfxHorn = true;
                } else {
                    Log.e(TAG, "sampleId: " + sampleId + " (not a pre-defined sound sample).");
                }
            }
        });

        // load()'s parameters: context, file_name, priority
        sfxBallPoof = soundPool.load(context, R.raw.sfx_ball_poof, 1);
        Log.d(TAG, "sfxBallPoof: " + sfxBallPoof);

        sfxBallToss = soundPool.load(context, R.raw.sfx_ball_toss, 1);
        Log.d(TAG, "sfxBallToss: " + sfxBallToss);

        sfxCollision = soundPool.load(context, R.raw.sfx_collision, 1);
        Log.d(TAG, "sfxCollision: " + sfxCollision);

        sfxEnterPc = soundPool.load(context, R.raw.sfx_enter_pc, 1);
        Log.d(TAG, "sfxEnterPc: " + sfxEnterPc);

        sfxGetItem = soundPool.load(context, R.raw.sfx_get_item_1, 1);
        Log.d(TAG, "sfxGetItem: " + sfxGetItem);

        sfxHorn = soundPool.load(context, R.raw.horn, 1);
        Log.d(TAG, "sfxHorn: " + sfxHorn);
    }

    public void sfxPlay(int id) {
        // play()'s parameters: leftVolume, rightVolume,
        // priority, loop, and rate
        soundPool.play(id, 1, 1,
                0, 0, 1);
    }

    public void sfxIterateAndPlay() {
        if (loadedSfxBallPoof && loadedSfxBallToss && loadedSfxCollision
                && loadedSfxEnterPc && loadedSfxGetItem && loadedSfxHorn) {
            // change selection of sound sample
            indexSfx++;
            if (indexSfx > 6) {
                indexSfx = 1;
            }

            // play()'s parameters: leftVolume, rightVolume,
            // priority, loop, and rate
            soundPool.play(indexSfx, 1, 1,
                    0, 0, 1);
        }
    }

    public void backgroundMusicTogglePause() {
        pausedBackgroundMusic = !pausedBackgroundMusic;

        if (pausedBackgroundMusic) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
    }

    public void onStart() {
        mediaPlayer.start();
    }

    public void onStop() {
        mediaPlayer.pause();
    }

    public void onDestroy() {
        mediaPlayer.release();
    }
}
