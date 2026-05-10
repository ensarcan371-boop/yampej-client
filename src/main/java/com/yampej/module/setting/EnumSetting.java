package com.yampej.module.setting;

public class EnumSetting extends Setting<String> {
    private final String[] options;
    private int index;

    public EnumSetting(String name, String[] options) {
        super(name, options[0]);
        this.options = options;
        this.index = 0;
    }

    public String[] getOptions() { return options; }
    public int getIndex() { return index; }

    public void next() {
        index = (index + 1) % options.length;
        value = options[index];
    }

    public void setValue(String val) {
        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(val)) { index = i; value = val; return; }
        }
    }
}
