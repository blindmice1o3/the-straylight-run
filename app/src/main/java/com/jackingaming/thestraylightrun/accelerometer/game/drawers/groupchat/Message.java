package com.jackingaming.thestraylightrun.accelerometer.game.drawers.groupchat;

public class Message {
    private String nameOfSender;
    private String message;
    private long delayMs;
    private boolean isFromPlayer;

    public Message(String nameOfSender, String message, long delayMs, boolean isFromPlayer) {
        this.nameOfSender = nameOfSender;
        this.message = message;
        this.delayMs = delayMs;
        this.isFromPlayer = isFromPlayer;
    }

    public String getNameOfSender() {
        return nameOfSender;
    }

    public String getMessage() {
        return message;
    }

    public long getDelayMs() {
        return delayMs;
    }

    public boolean isFromPlayer() {
        return isFromPlayer;
    }
}
