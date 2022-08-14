package com.towel.collections.filter;

public interface Filter<T> {
    boolean accept(T t);
}
