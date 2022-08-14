package com.towel.exc;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;

public class CollectedExceptions extends RuntimeException {
    private List<Throwable> list;

    public CollectedExceptions(List<Throwable> excps) {
        this.list = excps;
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintWriter stream) {
        printStackTraces(stream);
    }

    public void printStackTraces(PrintWriter stream) {
        for (Throwable t : this.list) {
            t.printStackTrace(stream);
        }
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintStream stream) {
        printStackTraces(stream);
    }

    public void printStackTraces(PrintStream stream) {
        for (Throwable t : this.list) {
            t.printStackTrace(stream);
        }
    }

    public void printStackTrace() {
        printStackTraces();
    }

    public void printStackTraces() {
        for (Throwable t : this.list) {
            t.printStackTrace();
        }
    }
}
