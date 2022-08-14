package com.towel.cfg;

import java.util.Locale;

public class TowelConfig {
    public static TowelConfig instance;
    private Locale locale = Locale.getDefault();

    public static TowelConfig getInstance() {
        if (instance == null) {
            instance = new TowelConfig();
        }
        return instance;
    }

    public Locale getDefaultLocale() {
        return this.locale;
    }

    public void setLocale(Locale locale2) {
        this.locale = locale2;
    }
}
