package com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.time;

import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

import com.jackingaming.thestraylightrun.MainActivity;
import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.Game;
import com.jackingaming.thestraylightrun.accelerometer.game.gameconsole.game.scenes.poohfarmer.SceneFarm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TimeManager
        implements Serializable {
    public static final String TAG = TimeManager.class.getSimpleName();

    public interface TimeManagerListener extends Serializable {
        void executeTimedEvent();
    }

    class EventTime implements Serializable {
        private int hour;
        private int minute;
        private boolean isPM;
        private boolean isActive;

        public EventTime(int hour, int minute, boolean isPM) {
            this.hour = hour;
            this.minute = minute;
            this.isPM = isPM;
            this.isActive = true;
        }

        public int getHour() {
            return hour;
        }

        public int getMinute() {
            return minute;
        }

        public boolean getIsPM() {
            return isPM;
        }

        public boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(boolean isActive) {
            this.isActive = isActive;
        }
    }

    transient private Map<EventTime, TimeManagerListener> timeManagerListeners;

    public void registerTimeManagerListener(TimeManagerListener timeManagerListener,
                                            int hour, int minute, boolean isPM) {
        if (!timeManagerListeners.containsValue(timeManagerListener)) {
            EventTime eventTime = new EventTime(hour, minute, isPM);
            timeManagerListeners.put(eventTime, timeManagerListener);
        }
    }

    public enum Season {SPRING, SUMMER, FALL, WINTER;}

    private String textSpring, textSummer, textFall, textWinter;

    public enum ModeOfDay {DAYLIGHT, TWILIGHT, NIGHT;}

    private static final float MILLISECOND_TO_MINUTE_RATIO = 20000f / 60;

    private static long timePlayed = 0L;

    transient private Game game;
    transient private Game.StatsChangeListener statsChangeListener;
    private short year;     //4-season years (SPRING, SUMMER, FALL, WINTER)
    private Season season;  //30-day seasons
    private short day;      //18-hour days (6am-12am)

    //DAYLIGHT==(6am-3pm), TWILIGHT==(3pm-6pm), NIGHT==(6pm-12am)
    private long ticker;
    private short hour;     //20-real-time-second hours (average day lasts ~4 minutes)
    private short minute;
    private boolean isPM;
    private ModeOfDay modeOfDay;

    //TIME STOPS WHEN INDOORS
    private boolean isPaused;

    public TimeManager() {
        year = 1;
        season = Season.SPRING;
        day = 1;

        resetInGameClock();

        isPaused = false;
    }

    public void init(Game game, Game.StatsChangeListener statsChangeListener) {
        this.game = game;
        this.statsChangeListener = statsChangeListener;
        timeManagerListeners = new HashMap<EventTime, TimeManagerListener>();

        textSpring = game.getContext().getResources().getString(R.string.text_spring);
        textSummer = game.getContext().getResources().getString(R.string.text_summer);
        textFall = game.getContext().getResources().getString(R.string.text_fall);
        textWinter = game.getContext().getResources().getString(R.string.text_winter);
    }

    public void startNewDay() {
        callRemainingActiveEventTimeObjects();
        setAllEventTimeObjectsToActive();
        resetInGameClock();
        incrementDay();
    }

    public void update(long elapsed) {
        //////////////////////
        timePlayed += elapsed;
        //////////////////////

        /////////////////////////////////////////////
        if (!isPaused) {
            ticker += elapsed;

            if (ticker >= MILLISECOND_TO_MINUTE_RATIO) {
                minute++;
                ticker = 0L;

                if (minute >= 60) {
                    hour++;
                    minute = 0;

                    //6am
                    if ((hour == 6) && (!isPM)) {
                        modeOfDay = ModeOfDay.DAYLIGHT;

                        SceneFarm.getInstance().updatePaintLightingColorFilter(modeOfDay);
                    }
                    //noon
                    else if ((hour == 12) && (!isPM)) {
                        isPM = true;
                    }
                    //1 o'clock (for both am and pm)
                    else if (hour == 13) {
                        hour = 1;
                    }
                    //3pm
                    else if ((hour == 3) && (isPM)) {
                        modeOfDay = ModeOfDay.TWILIGHT;

                        SceneFarm.getInstance().updatePaintLightingColorFilter(modeOfDay);
                    }
                    //6pm
                    else if ((hour == 6) && (isPM)) {
                        modeOfDay = ModeOfDay.NIGHT;

                        SceneFarm.getInstance().updatePaintLightingColorFilter(modeOfDay);
                    }
                    //midnight => TimeManager stops in-game clock.
                    else if ((hour == 12) && (isPM)) {
                        isPM = false;
                        ////////////////
                        isPaused = true;
                        ////////////////
                    }

                    //ALERT LISTENER DURING THEIR REGISTERED IN-GAME TIME (limited to hoursly checks)!
                    for (EventTime eventTime : timeManagerListeners.keySet()) {
                        if ((eventTime.getIsPM() == isPM) && (hour == eventTime.getHour()) && (minute == eventTime.getMinute())) {
                            timeManagerListeners.get(eventTime).executeTimedEvent();
                            eventTime.setIsActive(false);
                        }
                    }
                }
            }

        }
        /////////////////////////////////////////////

        //CALENDAR ((SEASON) DAY)
        String dayFormatted = String.format("%02d", day);
        String textSeason = null;
        switch (season) {
            case SPRING:
                textSeason = textSpring;
                break;
            case SUMMER:
                textSeason = textSummer;
                break;
            case FALL:
                textSeason = textFall;
                break;
            case WINTER:
                textSeason = textWinter;
                break;
        }
        String calendarText = "(" + textSeason + ") " + dayFormatted;

        //GAME-CLOCK (HOUR:MINUTE)
        String hourCurrent = String.format("%02d", hour);
        String minuteCurrent = String.format("%02d", minute);
        String amOrPM = (isPM) ? ("pm") : ("am");
        String inGameClockTime = hourCurrent + ":" + minuteCurrent + amOrPM;

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                statsChangeListener.onTimeChange(inGameClockTime, calendarText);
            }
        });
    }

    private void callRemainingActiveEventTimeObjects() {
        for (EventTime eventTime : timeManagerListeners.keySet()) {
            if (eventTime.getIsActive()) {
                timeManagerListeners.get(eventTime).executeTimedEvent();
                eventTime.setIsActive(false);
            }
        }
    }

    private void setAllEventTimeObjectsToActive() {
        for (EventTime eventTime : timeManagerListeners.keySet()) {
            eventTime.setIsActive(true);
        }
    }

    private void incrementDay() {
        day++;
        if (day > 30) {
            incrementSeason();
            day = 1;
        }
    }

    private void incrementSeason() {
        int indexNextSeason = season.ordinal() + 1;
        if (indexNextSeason >= Season.values().length) {
            incrementYear();
            indexNextSeason = 0;
        }
        season = Season.values()[indexNextSeason];

        SceneFarm.getInstance().updateTilesBySeason(season);
    }

    private void incrementYear() {
        year++;

        /////////////////////////////////////////////////////////////////////////////////
        ((MainActivity) game.getContext()).runOnUiThread(new Runnable() {
            public void run() {
                final Toast toast = Toast.makeText(game.getContext(), "year: " + year, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        });
        /////////////////////////////////////////////////////////////////////////////////
    }

    public Season getSeason() {
        return season;
    }

    public ModeOfDay getModeOfDay() {
        return modeOfDay;
    }

    public void setIsPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

    private void resetInGameClock() {
        ticker = 0L;
        hour = 5;
        minute = 59;
        isPM = false;
        modeOfDay = ModeOfDay.DAYLIGHT;
    }

}