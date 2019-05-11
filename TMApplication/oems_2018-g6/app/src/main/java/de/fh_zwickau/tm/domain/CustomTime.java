package de.fh_zwickau.tm.domain;

import java.io.Serializable;

public class CustomTime implements Serializable{
    private int hour;
    private int minute;

    public CustomTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
