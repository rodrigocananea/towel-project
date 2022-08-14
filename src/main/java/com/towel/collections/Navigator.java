package com.towel.collections;

public interface Navigator<T> {
    T get(int i);

    T next();

    T previous();

    int size();
}
