package com.jackingaming.thestraylightrun.accelerometer.game.drawers.groupchat;

public class Message {
    private String nameOfSender;
    private String message;

    public Message(String nameOfSender, String message) {
        this.nameOfSender = nameOfSender;
        this.message = message;
    }

    public String getNameOfSender() {
        return nameOfSender;
    }

    public String getMessage() {
        return message;
    }
}
