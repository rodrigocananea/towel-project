package com.towel.cfg;

public class StringUtil {
    public static String removeCharacters(String x, char... cs) {
        int length = cs.length;
        for (int i = 0; i < length; i++) {
            x = x.replaceAll("[" + String.valueOf(cs[i]) + "]", "");
        }
        return x;
    }
}
