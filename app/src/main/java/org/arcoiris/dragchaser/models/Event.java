package org.arcoiris.dragchaser.models;

import android.support.annotation.NonNull;

import java.util.Map;

/**
 * Created by kukolka on 3/26/2017.
 */

public class Event implements Comparable<Event>{

    private String key;
    private String title;
    private String date;
    private Map<String, String> eventQueens;

    public Event() {
    }

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

    public Map<String, String> getEventQueens() {
        return eventQueens;
    }

    public void setEventQueens(Map<String, String> eventQueens) {
        this.eventQueens = eventQueens;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int compareTo(@NonNull Event event) {
        return this.getDate().compareTo(event.getDate());
    }

}
