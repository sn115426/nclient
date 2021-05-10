package com.ngames.nclient.module.settings;

import com.ngames.nclient.gui.ButtonFloat;

public class SettingValue extends Setting {

    public ButtonFloat element;
    private float value;
    public final float min;
    public float max;

    public SettingValue(String name, float value, float min, float max) {
        super(name, Settings.VALUE_TYPE);
        this.value = value;
        this.min = min;
        this.max = max;
    }

    @Override
    public void onUpdate() {
        element.ds = this.name + ": " + getValue();
    }

    public void setValue(float value) {
        if (value < min) {
            this.value = min;
        } else if (value > max) {
            this.value = max;
        } else {
            this.value = value;
        }
    }

    public float getValue() {
        return this.value;
    }
}
