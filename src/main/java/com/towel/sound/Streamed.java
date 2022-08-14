package com.towel.sound;

import java.io.InputStream;

public interface Streamed extends Formatted {
    InputStream newInputStream();
}
