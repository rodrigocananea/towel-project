package com.towel.swing.table.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateView implements Comparable<DateView> {
    private Date date;
    private DateFormat format;

    public DateView(Date date2) {
        this(date2, DateFormat.getDateInstance(2));
    }

    public DateView(Calendar cal) {
        this(cal, DateFormat.getDateInstance(2));
    }

    public DateView(Date date2, String format2) {
        this(date2, new SimpleDateFormat(format2));
    }

    public DateView(Calendar cal, String format2) {
        this(cal.getTime(), new SimpleDateFormat(format2));
    }

    public DateView(Date date2, DateFormat format2) {
        this.date = date2;
        this.format = format2;
    }

    public DateView(Calendar cal, DateFormat format2) {
        this(cal.getTime(), format2);
    }

    public int compareTo(DateView o) {
        return this.date.compareTo(o.date);
    }

    public String toString() {
        return this.format.format(this.date);
    }

    public int hashCode() {
        if (this.date == null) {
            return 0;
        }
        return toString().hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return toString().equals(((DateView) obj).toString());
    }
}
