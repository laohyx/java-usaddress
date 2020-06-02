package com.github.laohyx.usaddress.feature;

public class BoolFeature extends Feature {
    boolean value;

    public BoolFeature(boolean val) {
        type = Type.BOOL;
        this.value = val;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value ? "True" : "False";
    }
}
