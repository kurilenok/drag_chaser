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
    private Map<String, String> queenEvents;

    public Queen() {
    }

    public Queen(String name) {
        this.name = name;
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

    public Map<String, String> getQueenEvents() {
        return queenEvents;
    }

    public void setQueenEvents(Map<String, String> queenEvents) {
        this.queenEvents = queenEvents;
    }

    @Override
    public int compareTo(@NonNull Queen queen) {
        return this.getName().compareTo(queen.getName());
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Queen) {
            Queen another = (Queen) object;
            if (another.name.equalsIgnoreCase(this.name) &&
                    another.key.equals(this.key)) {
                return true;
            }
        }
        return false;
    }
}

