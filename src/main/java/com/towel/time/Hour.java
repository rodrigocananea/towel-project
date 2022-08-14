package com.towel.time;

public class Hour implements Cloneable {
    private double hour;
    private double minute;

    public Hour(double hour2, double minute2) {
        this.hour = hour2;
        this.minute = minute2;
    }

    public Hour between(Hour other) {
        double hoursBetween;
        double minutesBetween;
        if (other.hour - this.hour >= 0.0d) {
            hoursBetween = other.hour - this.hour;
        } else {
            hoursBetween = (24.0d - this.hour) + other.hour;
        }
        if (other.minute - this.minute >= 0.0d) {
            minutesBetween = other.minute - this.minute;
        } else {
            minutesBetween = (60.0d - this.minute) + other.minute;
            hoursBetween -= 1.0d;
        }
        return new Hour(hoursBetween, minutesBetween);
    }

    public int minutesBetweenPlus(Hour end, Hour start) {
        if (!end.isAfter(start) || !end.isAfter(this)) {
            return 0;
        }
        return (int) ((((end.hour - ((double) ((int) (start.isAfter(this) ? start.hour : this.hour)))) * 60.0d) + end.minute) - ((double) ((int) (start.isAfter(this) ? start.minute : this.minute))));
    }

    public boolean isAfter(Hour other) {
        if (this.hour > other.hour) {
            return true;
        }
        if (this.hour == other.hour) {
            return this.minute > other.minute;
        }
        return false;
    }

    public int minutesBetween(Hour other) {
        Hour between = between(other);
        return (int) (between.minute + (between.hour * 60.0d));
    }

    public Hour between(double otherHour, double otherMinute) {
        return between(new Hour(otherHour, otherMinute));
    }

    public static Hour between(Hour start, Hour end) {
        return start.between(end);
    }

    public static Hour between(Hour start, double endH, double endM) {
        return start.between(endH, endM);
    }

    public static Hour between(double startH, double startM, double endH, double endM) {
        return new Hour(startH, startM).between(endH, endM);
    }

    @Override // java.lang.Object
    public Hour clone() {
        try {
            Hour hour2 = (Hour) super.clone();
            hour2.hour = this.hour;
            hour2.minute = this.minute;
            return hour2;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        return String.valueOf(String.valueOf((int) this.hour)) + ":" + String.valueOf((int) this.minute);
    }
}
