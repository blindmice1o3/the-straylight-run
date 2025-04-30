package com.jackingaming.thestraylightrun.accelerometer.game.drawers.groupchat;

public class Message {
    private String nameOfSender;
    private String message;
    private boolean isFromPlayer;

    public Message(String nameOfSender, String message, boolean isFromPlayer) {
        this.nameOfSender = nameOfSender;
        this.message = message;
        this.isFromPlayer = isFromPlayer;
    }

    public String getNameOfSender() {
        return nameOfSender;
    }

    public String getMessage() {
        return message;
    }

    public boolean isFromPlayer() {
        return isFromPlayer;
    }
}
