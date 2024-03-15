package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.vessels;

import java.util.HashMap;

public interface LiquidContainable {
    void transferIn(HashMap<String, Object> content);

    HashMap<String, Object> transferOut();

    void empty();
}
