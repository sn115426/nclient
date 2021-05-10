package com.ngames.nclient.module.settings;

public abstract class Setting {

    public int id;
    public String name;
    public Settings type;

    public Setting(String name, Settings type) {
        this.type = type;
        this.name = name;
    }

    public Settings getType() {
        return type;
    }

    public void onUpdate() {

    }
}
