package com.towel.cfg;

import java.util.HashMap;
import java.util.Map;

public class StringConfiguration {
    private Map<String, String> attrs;
    private String string;

    public StringConfiguration() {
        this("");
    }

    public StringConfiguration(String s) {
        this.attrs = new HashMap();
        this.string = s;
        map();
    }

    private void map() {
        String[] split = this.string.split("[\\[\\]]");
        for (String s : split) {
            if (!s.isEmpty()) {
                int index = s.indexOf(":");
                this.attrs.put(s.substring(0, index), s.substring(index + 1));
            }
        }
    }

    public String getAttribute(String name) {
        String result = this.attrs.get(name);
        return result == null ? "" : result;
    }

    public StringConfiguration setAttribute(String name, String value) {
        this.attrs.put(name, value);
        return this;
    }

    public boolean hasAttribute(String string2) {
        return this.attrs.containsKey(string2);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> ent : this.attrs.entrySet()) {
            builder.append("[").append(ent.getKey()).append(":").append(ent.getValue()).append("]");
        }
        return builder.toString();
    }
}
