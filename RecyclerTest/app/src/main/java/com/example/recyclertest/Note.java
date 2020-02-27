package com.example.recyclertest;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class Note implements Serializable, Comparable<Note> {

    private String title;
    private String date;
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Note(String title, String date, String description) {
        this.title = title;
        this.date = date;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                ", date=" + date +
                ", description='" + description + '\'' +
                '}';
    }


    public Note() {
        this.title = title;
        this.date = date;
        this.description = description;
    }

    @Override
    public int compareTo(Note o) {
        int result = 0;
        SimpleDateFormat df = new SimpleDateFormat("EEE MMM d, hh:mm a");

        try {
            result = df.parse(o.getDate()).compareTo(df.parse(getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (result == 0) {
            return -1;
        } else {
            return result;
        }

    }
}