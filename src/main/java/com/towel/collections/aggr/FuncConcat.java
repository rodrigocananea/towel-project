package com.towel.collections.aggr;

public class FuncConcat implements AggregateFunc<String> {
    private String separator;
    private StringBuilder x;

    public FuncConcat(String x2) {
        this.separator = x2;
    }

    public void update(String obj) {
        this.x.append(obj).append(this.separator);
    }

    @Override // com.towel.collections.aggr.AggregateFunc
    public String getResult() {
        return this.x.delete(this.x.length() - this.separator.length(), this.x.length()).toString();
    }

    @Override // com.towel.collections.aggr.AggregateFunc
    public void init() {
        this.x = new StringBuilder();
    }
}
