package de.fh_zwickau.tm.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.sql.Date;

public class Task implements Serializable{
    private int id;
    private String title;
    private String description;
    private boolean completed;
    private String date;
    private CustomTime begin;
    private CustomTime end;
    private Enum<Priority> priority;

    /**
     *
     * @param id
     * @param title
     * @param description
     * @param begin
     * @param end
     * @param priority
     */
    public Task(int id, String title, String description,String date, CustomTime begin, CustomTime end, Enum<Priority> priority) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = false;
        this.date = date;
        this.begin = begin;
        this.end = end;
        this.priority = priority;
    }

    public Task(int id, String title, String description,String date, CustomTime begin, CustomTime end, Enum<Priority> priority, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = false;
        this.date = date;
        this.begin = begin;
        this.end = end;
        this.priority = priority;
        this.completed = completed;
    }

    public Task(String title, String description,String date, CustomTime begin, CustomTime end, Enum<Priority> priority) {
        this.title = title;
        this.description = description;
        this.completed = false;
        this.date = date;
        this.begin = begin;
        this.end = end;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public CustomTime getBegin() {
        return begin;
    }

    public void setBegin(CustomTime begin) {
        this.begin = begin;
    }

    public CustomTime getEnd() {
        return end;
    }

    public void setEnd(CustomTime end) {
        this.end = end;
    }

    public Enum<Priority> getPriority() {
        return priority;
    }

    public void setPriority(Enum<Priority> priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", completed=" + completed +
                ", date='" + date + '\'' +
                ", begin=" + begin +
                ", end=" + end +
                ", priority=" + priority +
                '}';
    }

}
