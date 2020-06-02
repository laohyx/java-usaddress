package com.github.laohyx.usaddress.feature;

public class StringFeature extends Feature {
    String value;

    public StringFeature(String val) {
        type = Type.STRING;
        this.value = val;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }
}
