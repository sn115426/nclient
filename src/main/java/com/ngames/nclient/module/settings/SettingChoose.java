package com.ngames.nclient.module.settings;

import java.util.ArrayList;
import java.util.List;

import com.ngames.nclient.gui.ButtonChoose;

public class SettingChoose extends Setting {

    public ButtonChoose element;
    private byte value;
    public final int max;
    public List<String> mapping = new ArrayList<>();

    public SettingChoose(String name, byte value, String... names) {
        super(name, Settings.CHOOSE);
        this.value = value;
        this.max = names.length - 1;
        for (String vname : names)
            mapping.add(vname);
    }

    @Override
    public void onUpdate() {
        element.ds = this.name + ": " + mapping.get(getValue());
    }

    public void setValue() {
        byte value = this.value += 1;
        if (value <= this.max && value >= 0) {
            this.value = value;
        }
        if (value > this.max && value >= 0) {
            this.value = (byte) (value % (max + 1));
        }
    }

    public void setValue(byte value) {
        if (value <= this.max && value >= 0) {
            this.value = value;
        }
        if (value > this.max && value >= 0) {
            this.value = (byte) (value % (max + 1));
        }
    }

    public byte getValue() {
        return this.value;
    }
}
