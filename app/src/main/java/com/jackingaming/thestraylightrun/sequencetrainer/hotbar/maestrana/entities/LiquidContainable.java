package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.maestrana.entities;

import java.util.HashMap;

public interface LiquidContainable {
    void transferIn(HashMap<String, String> content);

    HashMap<String, String> transferOut();
}
