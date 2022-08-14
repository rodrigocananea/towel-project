package com.towel.util;

public class Pair<T, K> {
    private T first;
    private K second;

    public Pair(T f, K s) {
        this.first = f;
        this.second = s;
    }

    public T getFirst() {
        return this.first;
    }

    public K getSecond() {
        return this.second;
    }
}
