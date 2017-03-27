package org.arcoiris.dragchaser.models;

import android.support.annotation.NonNull;

import java.util.Map;

/**
 * Created by kukolka on 3/26/2017.
 */

public class Event implements Comparable<Event> {

    private String key;
    private String title;
    private String date;
    private String venue;
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

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    @Override
    public int compareTo(@NonNull Event event) {
        return this.getDate().compareTo(event.getDate());
    }

    public static Event decode(Map.Entry<String, String> entry) {
        Event event = new Event();

        String[] data = entry.getValue().split("!@");
        event.setKey(entry.getKey());
        event.setDate(data[0]);
        event.setTitle(data[1]);
        event.setVenue(data[2]);

        return event;
    }

}
