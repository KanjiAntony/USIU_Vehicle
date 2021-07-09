package com.example.popinclient;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class InventoryEventViewHolder extends RecyclerView.ViewHolder {
    TextView event_name;
    ImageView event_poster;
    TextView event_start_date;
    TextView event_stop_date;
    TextView event_start_time;
    TextView event_stop_time;
    TextView event_venue;
    Button event_buy_btn;

    InventoryEventViewHolder(View itemView)
    {
        super(itemView);
        event_name = (TextView)itemView.findViewById(R.id.txt_event_name);
        event_start_date = (TextView)itemView.findViewById(R.id.txt_event_start_date);
        event_poster = (ImageView)itemView.findViewById(R.id.pic_event_poster);
        event_stop_date = (TextView)itemView.findViewById(R.id.txt_event_stop_date);
        event_start_time = (TextView)itemView.findViewById(R.id.txt_event_start_time);
        event_stop_time = (TextView)itemView.findViewById(R.id.txt_event_end_time);
        event_venue = (TextView)itemView.findViewById(R.id.txt_event_venue);
    }
}
