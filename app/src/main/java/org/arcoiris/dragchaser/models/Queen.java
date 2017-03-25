package org.arcoiris.dragchaser.models;

import android.support.annotation.NonNull;

import java.util.Map;

/**
 * Created by kukolka on 3/25/2017.
 */

public class Queen implements Comparable<Queen> {

    private String key;
    private String name;
    private String hometown;
    private Map<String, Boolean> queenEvents;

    public Queen() {
    }

    public Queen(String name, String hometown) {
        this.name = name;
        this.hometown = hometown;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public Map<String, Boolean> getQueenEvents() {
        return queenEvents;
    }

    public void setQueenEvents(Map<String, Boolean> queenEvents) {
        this.queenEvents = queenEvents;
    }

    @Override
    public int compareTo(@NonNull Queen queen) {
        return this.getName().compareTo(queen.getName());
    }
}

