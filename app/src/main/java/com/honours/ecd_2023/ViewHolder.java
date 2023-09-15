package com.honours.ecd_2023;

import android.app.Application;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.*;
import com.google.android.exoplayer2.ui.PlayerView;

import java.io.File;
import java.util.Collections;

/**
 * ViewHolder class for displaying video items in a RecyclerView.
 */
public class ViewHolder extends RecyclerView.ViewHolder {
    public ExoPlayer player;

    ImageView downloadBtn;

    Button delete_button;

    /**
     * Constructor for the ViewHolder class.
     *
     * @param itemView The View for the item.
     */
    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        downloadBtn = itemView.findViewById(R.id.download_button_icon);
        delete_button = itemView.findViewById(R.id.delete_button);

    }


    public interface clicklistener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }
}

