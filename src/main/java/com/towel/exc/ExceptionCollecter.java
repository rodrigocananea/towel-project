package com.towel.exc;

import java.util.ArrayList;
import java.util.List;

public class ExceptionCollecter {
    private List<Throwable> list = new ArrayList();

    public void collect(Throwable t) {
        this.list.add(t);
    }

    public boolean isEmpty() {
        return this.list.size() == 0;
    }

    public void throwException() {
        throw new CollectedExceptions(this.list);
    }
}
