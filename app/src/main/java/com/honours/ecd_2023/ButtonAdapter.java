package com.honours.ecd_2023;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ButtonAdapter extends RecyclerView.Adapter<ButtonAdapter.ButtonViewHolder> {

    // Interface for item click callbacks
    public interface OnItemClickListener {
        void onItemClick(String itemText);
    }

    private ArrayList<String> data; // List of data items
    private OnItemClickListener listener; // Listener for item click events

    // Constructor to initialize the adapter with data and a click listener
    public ButtonAdapter(ArrayList<String> data, OnItemClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    // Inflates the layout for each item in the RecyclerView
    @NonNull
    @Override
    public ButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_recycler_item, parent, false);
        return new ButtonViewHolder(view, listener);
    }

    // Binds data to the views in each item
    @Override
    public void onBindViewHolder(@NonNull ButtonViewHolder holder, int position) {
        holder.tv_Title.setText(data.get(position));
    }

    // Returns the total number of items in the data
    @Override
    public int getItemCount() {
        return data.size();
    }

    // ViewHolder class to hold references to the item's views
    public class ButtonViewHolder extends RecyclerView.ViewHolder {
        TextView tv_Title; // TextView for displaying item text
        String itemText; // The text of the current item

        // Constructor to initialize the ViewHolder
        public ButtonViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            tv_Title = itemView.findViewById(R.id.tv_title); // Initialize the TextView

            // Set a click listener for the item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String itemText = data.get(position); // Get the text of the clicked item
                        listener.onItemClick(itemText); // Trigger the onItemClick event
                    }
                }
            });
        }
    }
}
