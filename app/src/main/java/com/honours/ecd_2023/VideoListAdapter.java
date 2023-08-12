package com.honours.ecd_2023;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoViewHolder> {
    private Context context;
    private List<Video> videoList;
    private OnItemClickListener onItemClickListener;

    public VideoListAdapter(Context context, List<Video> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view, onItemClickListener);
    }

    public void updateVideoList(List<Video> newVideoList) {
        videoList.clear();
        videoList.addAll(newVideoList);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video videoFile = videoList.get(position);
        holder.videoTitle.setText(videoFile.getTitle());
        holder.videoTag.setText(videoFile.getTags());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(holder.getAdapterPosition());
                }
            }
        });



        // Set other video information as needed (e.g., thumbnail, duration, etc.).
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView videoTitle;
        TextView videoTag;

        VideoViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            videoTitle = itemView.findViewById(R.id.tv_item);
            videoTag = itemView.findViewById(R.id.tag_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);

                        }
                    }
                }
            });
        }
    }
}
