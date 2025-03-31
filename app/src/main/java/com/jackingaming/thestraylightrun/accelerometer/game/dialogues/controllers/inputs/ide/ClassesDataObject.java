package com.jackingaming.thestraylightrun.accelerometer.game.dialogues.controllers.inputs.ide;

import java.io.Serializable;
import java.util.List;

public class ClassesDataObject
        implements Serializable {

    private List<Class> classes;

    public ClassesDataObject(List<Class> classes) {
        this.classes = classes;
    }

    public List<Class> getClasses() {
        return classes;
    }
}
