package com.example.popinclient;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventoryEventTicketAdapter extends RecyclerView.Adapter<InventoryEventViewHolder> implements Filterable
{

    List<eventData> list = Collections.emptyList();
    List<eventData> filteredList;
    public List<eventData> exampleListFull;
    public String filterPattern;

    Context context;

    public InventoryEventTicketAdapter(List<eventData> list, Context context)
    {
        this.list = list;
        this.context = context;
        this.exampleListFull = list;
    }

    @Override
    public InventoryEventViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType)
    {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the layout

        View photoView = inflater.inflate(R.layout.main_page,
                parent, false);

        InventoryEventViewHolder viewHolder = new InventoryEventViewHolder(photoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final InventoryEventViewHolder viewHolder,
                                 final int position)
    {

        viewHolder.event_name.setText(list.get(position).event_name);
        viewHolder.event_start_date.setText(list.get(position).event_start_date);
        //viewHolder.event_poster.setText(list.get(position).event_name);
        viewHolder.event_stop_date.setText(list.get(position).event_stop_date);
        viewHolder.event_start_time.setText(list.get(position).event_start_time);
        viewHolder.event_stop_time.setText(list.get(position).event_stop_time);
        viewHolder.event_venue.setText(list.get(position).event_venue);

        //Picasso.get().load(list.get(position).event_poster).into(viewHolder.event_poster);
        Glide.with(viewHolder.itemView) //1
                .load(list.get(position).event_poster)
                .placeholder(R.drawable.bahama)
                .error(R.drawable.bahama)
                .skipMemoryCache(true) //2
                .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                .into(viewHolder.event_poster);

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                filterPattern = constraint.toString().toLowerCase().trim();
                for (eventData item : exampleListFull) {
                    if (item.getText2().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //results1 = performFiltering(constraint);
            list.clear();
            list.addAll((List) results.values);
            //Log.d("E",results.values.toString());
            notifyDataSetChanged();
        }
    };

    public void clear() {
        list.clear();
    }


}
