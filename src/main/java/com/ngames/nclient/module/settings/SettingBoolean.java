package com.ngames.nclient.module.settings;

import com.ngames.nclient.gui.ButtonBoolean;

public class SettingBoolean extends Setting {

    public ButtonBoolean element;
    private boolean value;

    public SettingBoolean(String name, boolean value) {
        super(name, Settings.BOOLEAN);
        this.value = value;
    }

    @Override
    public void onUpdate() {

    }

    public void setValue() {
        this.value = !value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return this.value;
    }
}
