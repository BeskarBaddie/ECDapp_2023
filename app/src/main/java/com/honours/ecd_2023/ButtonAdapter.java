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

    public interface OnItemClickListener {
        void onItemClick(String itemText);
    }

    private ArrayList<String> data;
    private OnItemClickListener listener;

    public ButtonAdapter(ArrayList<String> data, OnItemClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_recycler_item, parent, false);
        return new ButtonViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ButtonViewHolder holder, int position) {
        holder.tv_Title.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ButtonViewHolder extends RecyclerView.ViewHolder {
        TextView tv_Title;
        String itemText;

        public ButtonViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            tv_Title = itemView.findViewById(R.id.tv_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String itemText = data.get(position);
                        listener.onItemClick(itemText);
                    }
                }
            });
        }
    }
}
