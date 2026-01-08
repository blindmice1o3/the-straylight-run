package com.jackingaming.thestraylightrun.accelerometer.game.sounds;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import com.jackingaming.thestraylightrun.R;

public class SoundManager {
    public static final String TAG = SoundManager.class.getSimpleName();

    private Context context;
    private MediaPlayer mediaPlayer;
    private boolean pausedBackgroundMusic;

    private SoundPool soundPool;
    private int indexSfx = 0;
    public static int sfxBubbles, sfxSpin, sfxCoin,
            sfxSow, sfxScissors, sfxShovel;
    private int positionProject2Marioish = 0;
    private int positionBreatheOfDippy = 0;

    public void changeBackgroundMusicToTranceBattle() {
        positionBreatheOfDippy = mediaPlayer.getCurrentPosition();
        mediaPlayer.pause();

        mediaPlayer = MediaPlayer.create(context, R.raw.sounds_cc0_trance_boss_battle_bpm150);
        mediaPlayer.setLooping(true);

        mediaPlayer.start();
    }

    public void changeBackgroundMusicToBreatheOfDippy() {
        mediaPlayer.pause();

        mediaPlayer = MediaPlayer.create(context, R.raw.sounds_breathe_of_dippy);
        mediaPlayer.setLooping(true);

        mediaPlayer.seekTo(positionBreatheOfDippy);
        mediaPlayer.start();
    }

    public void changeBackgroundMusicToProject2Marioish() {
        mediaPlayer.pause();

        mediaPlayer = MediaPlayer.create(context, R.raw.sounds_cc0_project_2_marioish);
        mediaPlayer.setLooping(true);

        mediaPlayer.seekTo(positionProject2Marioish);
        mediaPlayer.start();
    }

    public void pauseProject2Marioish() {
        positionProject2Marioish = mediaPlayer.getCurrentPosition();
        mediaPlayer.pause();
    }

    public SoundManager(Context context) {
        this.context = context;
//        mediaPlayer = MediaPlayer.create(context, R.raw.corporate_ukulele);
        mediaPlayer = MediaPlayer.create(context, R.raw.sounds_breathe_of_dippy);
        mediaPlayer.setLooping(true);

        soundPool = new SoundPool(
                6,
                AudioManager.STREAM_MUSIC,
                0);

        // load()'s parameters: context, file_name, priority
        sfxBubbles = soundPool.load(context, R.raw.sfx_cc0_bubbles, 1);
        Log.d(TAG, "sfxBubbles: " + sfxBubbles);

        sfxSpin = soundPool.load(context, R.raw.sfx_cc0_car_spinning, 1);
        Log.d(TAG, "sfxSpin: " + sfxSpin);

        sfxCoin = soundPool.load(context, R.raw.sfx_cc0_coin, 1);
        Log.d(TAG, "sfxCoin: " + sfxCoin);

        sfxSow = soundPool.load(context, R.raw.sfx_cc0_sow, 1);
        Log.d(TAG, "sfxSow: " + sfxSow);

        sfxScissors = soundPool.load(context, R.raw.sfx_cc0_scissors, 1);
        Log.d(TAG, "sfxScissors: " + sfxScissors);

        sfxShovel = soundPool.load(context, R.raw.sfx_cc0_shovel, 1);
        Log.d(TAG, "sfxShovel: " + sfxShovel);
    }

    public void sfxPlay(int id) {
        // play()'s parameters: leftVolume, rightVolume,
        // priority, loop, and rate
        soundPool.play(id, 1, 1,
                0, 0, 1);
    }

    public void sfxIterateAndPlay() {
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
