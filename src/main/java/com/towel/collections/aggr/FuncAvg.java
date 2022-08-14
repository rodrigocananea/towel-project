package com.towel.collections.aggr;

public class FuncAvg implements AggregateFunc<Number> {
    private int total;
    private Number x;

    public void update(Number obj) {
        this.x = new Double(this.x.doubleValue() + obj.doubleValue());
        this.total++;
    }

    @Override // com.towel.collections.aggr.AggregateFunc
    public Number getResult() {
        return Double.valueOf(this.x.doubleValue() / ((double) this.total));
    }

    @Override // com.towel.collections.aggr.AggregateFunc
    public void init() {
        this.x = new Double(0.0d);
        this.total = 0;
    }
}
