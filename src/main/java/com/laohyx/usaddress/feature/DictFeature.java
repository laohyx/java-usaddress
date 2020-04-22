package com.laohyx.usaddress.feature;

import third_party.org.chokkan.crfsuite.Attribute;
import third_party.org.chokkan.crfsuite.Item;

import java.util.HashMap;
import java.util.Map;

public class DictFeature extends Feature {
    private Map<String, Feature> dict;

    public DictFeature() {
        type = Type.DICT;
        dict = new HashMap<>();
    }

    public void put(String key, Feature val) {
        dict.put(key, val);
    }

    public DictFeature getDictByKey(String key) {
        Feature f = dict.get(key);
        assert f != null && f.getType() == Type.DICT;
        return (DictFeature) f;
    }

    public void put(String key, String val) {
        dict.put(key, new StringFeature(val));
    }

    public void put(String key, boolean val) {
        dict.put(key, new BoolFeature(val));
    }

    public static final String _SEP = ":";

    public DictFeature copy() {
        DictFeature copied = new DictFeature();
        for (Map.Entry<String, Feature> entry : dict.entrySet()) {
            assert entry.getValue().getType() != Type.DICT;
            copied.put(entry.getKey(), entry.getValue());
        }
        return copied;
    }

    public Item toItem() {
        Item cItem = new Item();
        String cKey;
        double cValue;
        boolean isDict = this.getType() == Type.DICT;

        for (String key : this.dict.keySet()) {
            cKey = key;
            if (!isDict) {
                cValue = 1.0;
                cItem.add(new Attribute(cKey, cValue));
            } else {
                Feature value = this.dict.get(key);
                if (value.getType() == Type.DICT) {
                    Item subItem = ((DictFeature) value).toItem();
                    for (int i = 0; i < subItem.size(); i++) {
                        Attribute attr = subItem.get(i);
                        cItem.add(new Attribute(cKey + _SEP + attr.getAttr(), attr.getValue()));
                    }
                } else {
                    if (value.getType() == Type.STRING) {
                        cKey += _SEP;
                        cKey += ((StringFeature) value).getValue();
                        cValue = 1.0;
                    } else {
                        assert value.getType() == Type.BOOL;
                        cValue = ((BoolFeature) value).getValue() ? 1.0 : 0.0;
                    }
                    cItem.add(new Attribute(cKey, cValue));
                }
            }
        }
        return cItem;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (String key : dict.keySet()) {
            sb.append("\"" + key + "\": ");
            sb.append(dict.get(key).toString());
            sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
    }
}
