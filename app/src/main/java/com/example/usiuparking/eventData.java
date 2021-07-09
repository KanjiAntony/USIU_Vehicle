package com.example.usiuparking;

public class eventData {
    String event_name;
    String event_start_date;
    String event_stop_date;
    String event_start_time;
    String event_stop_time;
    String event_venue;
    String event_poster;
    String event_id;
    eventData(String name, String event_start_date, String event_stop_date,
              String event_start_time, String event_stop_time, String event_venue,String event_poster,String event_id)
    {
        this.event_stop_date = event_stop_date;
        this.event_name = name;
        this.event_start_date = event_start_date;
        this.event_start_time = event_start_time;
        this.event_stop_time = event_stop_time;
        this.event_venue = event_venue;
        this.event_poster = event_poster;
        this.event_id = event_id;
    }

    public String getText2() {
        return event_name;
    }
}

