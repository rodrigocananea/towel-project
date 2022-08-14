package com.towel.bean;

public interface Formatter {
    Object format(Object obj);

    String getName();

    Object parse(Object obj);
}
