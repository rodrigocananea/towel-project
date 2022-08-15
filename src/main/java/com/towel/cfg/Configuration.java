package com.towel.cfg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {

    private File file;
    private Properties props;

    public Configuration(File f) throws IOException {
        if (!f.exists()) {
            f.createNewFile();
        }
        
        this.props = new Properties();
        this.file = f;
        
        try (InputStream fileInput = new FileInputStream(this.file)) {
            this.props.load(fileInput);
        }
    }

    public StringConfiguration getCfg(String key) {
        return new StringConfiguration(get(key));
    }

    public String get(String key) {
        return (String) this.props.get(key);
    }

    public void put(String key, String value) {
        this.props.put(key, value);
    }

    public void put(String key, StringConfiguration cfg) {
        put(key, cfg.toString());
    }

    public void write() throws IOException {
        this.props.store(new FileOutputStream(this.file), "");
    }
}
