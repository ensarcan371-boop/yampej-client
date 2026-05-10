package com.yampej.module.setting;

public class SliderSetting extends Setting<Double> {
    private final double min, max, step;

    public SliderSetting(String name, double defaultValue, double min, double max, double step) {
        super(name, defaultValue);
        this.min = min;
        this.max = max;
        this.step = step;
    }

    public double getMin() { return min; }
    public double getMax() { return max; }
    public double getStep() { return step; }

    public void setValue(double val) {
        value = Math.max(min, Math.min(max, val));
    }

    public float getPercent() {
        return (float) ((value - min) / (max - min));
    }
}
