package com.example.usiuparking;

public class slotData {
    String slot_name;
    String slot_availability;
    String slot_id;
    slotData(String slot_name, String slot_availability, String slot_id)
    {
        this.slot_name = slot_name;
        this.slot_availability = slot_availability;
        this.slot_id = slot_id;
    }

    public String getText2() {
        return slot_name;
    }
}

