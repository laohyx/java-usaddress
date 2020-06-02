package com.github.laohyx.usaddress.feature;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Feature {
    enum Type {
        BOOL,
        STRING,
        DICT
    }
    Type type;

    public Type getType() {
        return type;
    }

//    public abstract Item toItem();


    public static Feature createFromEndsinpunc(String token) {
        String pattern = ".+[^.\\w]";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(token);
        if (m.find()) {
            return new StringFeature(token.substring(token.length() - 1));
        } else {
            return new BoolFeature(false);
        }
    }
}
