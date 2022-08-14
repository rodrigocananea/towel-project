package com.towel.collections.aggr;

public interface AggregateFunc<T> {
    T getResult();

    void init();

    void update(T t);
}
