package com.towel.swing.splash;

public class ProgressCalculator {
    private int current;
    private int proccesses;

    public ProgressCalculator(int processes) {
        this.proccesses = processes;
    }

    public int getNextPercent() {
        int i = this.current + 1;
        this.current = i;
        return getPercent(i);
    }

    public int getPercent(int currentProccess) {
        this.current = currentProccess;
        return (currentProccess * 100) / this.proccesses;
    }
}
