package com.towel.cfg;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProgressiveString {
    private int charIntervalIndex;
    private List<CharInterval> charsIntervals;
    private int maxChar;
    private int minChar;
    private char[] string;

    public ProgressiveString(CharInterval... charIntervals) {
        this("", charIntervals);
    }

    public ProgressiveString(String original, CharInterval... charIntervals) {
        if (charIntervals.length == 0) {
            throw new RuntimeException("CharIntervals can't be null!");
        }
        this.string = original.toCharArray();
        this.charsIntervals = Arrays.asList(charIntervals);
        Collections.sort(this.charsIntervals);
        this.charIntervalIndex = 0;
        this.minChar = this.charsIntervals.get(0).first;
        this.maxChar = this.charsIntervals.get(this.charsIntervals.size() - 1).last;
    }

    private char[] copyChars(int start, char[] old) {
        char[] str = new char[(old.length + start)];
        for (int i = 0; i < old.length; i++) {
            str[start + i] = old[i];
        }
        return str;
    }

    public void increase() {
        char[] str = (char[]) this.string.clone();
        int i = str.length - 1;
        while (true) {
            if (i >= 0) {
                IncreaseResult result = tryIncrease(str[i]);
                str[i] = result.newChar;
                if (result.result != 0) {
                    if (result.result == 1 && i - 1 == -1) {
                        str = copyChars(1, str);
                        str[0] = (char) this.minChar;
                        break;
                    }
                    i--;
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        this.string = (char[]) str.clone();
    }

    private IncreaseResult tryIncrease(char c) {
        if (c != this.maxChar) {
            return new IncreaseResult(nextChar(c), 0);
        }
        this.charIntervalIndex = 0;
        return new IncreaseResult((char) this.minChar, 1);
    }

    private char nextChar(char c) {
        if (c != this.charsIntervals.get(this.charIntervalIndex).last) {
            return (char) (c + 1);
        }
        this.charIntervalIndex++;
        return (char) this.charsIntervals.get(this.charIntervalIndex).first;
    }

    public String toString() {
        return new String(this.string);
    }

    public static void main(String[] args) {
        ProgressiveString str = new ProgressiveString(String.valueOf('!'), new CharInterval(97, 109), new CharInterval(110, 122));
        int i = 0;
        while (i < 2500000) {
            i++;
            str.increase();
            System.out.println(str);
        }
    }

    public static class CharInterval implements Comparable<CharInterval> {
        protected int first;
        protected int last;

        public CharInterval(int first2, int last2) {
            this.first = first2;
            this.last = last2;
        }

        public boolean canIncrease(char character) {
            return character >= this.first && character <= this.last;
        }

        public int compareTo(CharInterval arg0) {
            if (this.first < arg0.first) {
                return -1;
            }
            if (this.first > arg0.first) {
                return 1;
            }
            return 0;
        }
    }

    /* access modifiers changed from: private */
    public class IncreaseResult {
        protected static final int DECREASED = 1;
        protected static final int INCREASED = 0;
        protected char newChar;
        protected int result;

        public IncreaseResult(char newC, int result2) {
            this.newChar = newC;
            this.result = result2;
        }
    }
}
