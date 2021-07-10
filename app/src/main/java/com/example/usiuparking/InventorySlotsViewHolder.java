package com.example.usiuparking;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class InventorySlotsViewHolder extends RecyclerView.ViewHolder {
    TextView slot_name;
    ImageView slot_image;
    TextView slot_availability;

    InventorySlotsViewHolder(View itemView)
    {
        super(itemView);
        slot_name = (TextView)itemView.findViewById(R.id.txt_slot_name);
        slot_image = (ImageView)itemView.findViewById(R.id.pic_slot_image);
        slot_availability = (TextView)itemView.findViewById(R.id.txt_slot_availability);
    }
}
