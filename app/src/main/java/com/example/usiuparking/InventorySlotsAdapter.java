package com.example.usiuparking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventorySlotsAdapter extends RecyclerView.Adapter<InventorySlotsViewHolder> implements Filterable
{

    List<slotData> list = Collections.emptyList();
    List<slotData> filteredList;
    public List<slotData> exampleListFull;
    public String filterPattern;

    Context context;

    public InventorySlotsAdapter(List<slotData> list, Context context)
    {
        this.list = list;
        this.context = context;
        this.exampleListFull = list;
    }

    @Override
    public InventorySlotsViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType)
    {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the layout

        View photoView = inflater.inflate(R.layout.main_page,
                parent, false);

        InventorySlotsViewHolder viewHolder = new InventorySlotsViewHolder(photoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final InventorySlotsViewHolder viewHolder,
                                 final int position)
    {

        viewHolder.slot_name.setText(list.get(position).slot_name);
        viewHolder.slot_availability.setText(list.get(position).slot_availability);

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
                for (slotData item : exampleListFull) {
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
            notifyDataSetChanged();
        }
    };

    public void clear() {
        list.clear();
    }


}
