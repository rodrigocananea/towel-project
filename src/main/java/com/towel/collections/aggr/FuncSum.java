package com.towel.collections.aggr;

public class FuncSum implements AggregateFunc<Number> {
    private Number x;

    public void update(Number obj) {
        this.x = new Double(this.x.doubleValue() + obj.doubleValue());
    }

    @Override // com.towel.collections.aggr.AggregateFunc
    public Number getResult() {
        return this.x;
    }

    @Override // com.towel.collections.aggr.AggregateFunc
    public void init() {
        this.x = new Double(0.0d);
    }
}
