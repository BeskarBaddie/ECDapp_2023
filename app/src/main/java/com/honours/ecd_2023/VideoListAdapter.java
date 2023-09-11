package com.honours.ecd_2023;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoViewHolder> {
    private Context context;
    private List<Video> videoList;
    private OnItemClickListener onItemClickListener;

    private boolean showDeleteButton;



    public VideoListAdapter(Context context, List<Video> videoList, boolean showDeleteButton) {
        this.context = context;
        this.videoList = videoList;
        this.showDeleteButton = showDeleteButton;
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

    private boolean isVideoInLocalMemory(String videoFileName) {
        File internalStorageDir = context.getApplicationContext().getFilesDir();
        if(videoFileName.endsWith(" (Download)")){
            File videoFile = new File(internalStorageDir, (videoFileName));
            return videoFile.exists();

        }else{
        File videoFile = new File(internalStorageDir, (videoFileName + " (Download)"));
        return videoFile.exists();} // Returns true if the video file exists in local memory
    }


    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video videoFile = videoList.get(position);
        holder.videoTitle.setText(videoFile.getTitle());
        holder.videoTag.setText(videoFile.getTags());
        holder.videoLangauage.setText(videoFile.getLanguage());



        boolean isVideoStoredLocally = isVideoInLocalMemory(videoFile.getTitle());


        // Set the appropriate download image based on the video's storage status
        if (isVideoStoredLocally) {
            holder.downloadBtn.setImageResource(R.drawable.download_green);
            holder.delete_button.setVisibility(View.VISIBLE);
        } else {
            holder.downloadBtn.setImageResource(R.drawable.download_black);
            holder.delete_button.setVisibility(View.INVISIBLE);
        }

        if (showDeleteButton==true) {
            holder.delete_button.setVisibility(View.VISIBLE);
        } else {
            holder.delete_button.setVisibility(View.INVISIBLE);
        }


        if ("Baby Health".equals(videoFile.getTopics())) {
            holder.cardImage.setImageResource(R.drawable.baby_health_picture); // Change this to the appropriate play icon for topic1
        } else if ("Baby Development".equals(videoFile.getTopics())) {
            holder.cardImage.setImageResource(R.drawable.baby_picture); // Change this to the appropriate play icon for topic2
        }
        else if ("Child Entertainment".equals(videoFile.getTopics())) {
            holder.cardImage.setImageResource(R.drawable.baby_picture);
        }
        else if ("Parent Health".equals(videoFile.getTopics())) {
            holder.cardImage.setImageResource(R.drawable.parent_picture);
        }
        else if ("Community Content".equals(videoFile.getTopics())) {
            holder.cardImage.setImageResource(R.drawable.community_picture);
        }

        if ("Baby Health".equals(videoFile.getTopics())) {
            holder.itemView.setBackgroundResource(R.color.baby_health_green);}

        if ("Baby Development".equals(videoFile.getTopics())) {
            holder.itemView.setBackgroundResource(R.color.baby_development_orange);}

        if ("Parent Health".equals(videoFile.getTopics())) {
            holder.itemView.setBackgroundResource(R.color.parent_health_purple);}


        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteVideoFile(v.getContext(),videoFile.getTitle());
            }
        });














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

    private void deleteVideoFile(Context context, String videoName) {
        File internalStorageDir = context.getFilesDir();
        File videoFile = new File(internalStorageDir, videoName );
        if (videoFile.exists()) {
            if (videoFile.delete()) {
                Toast.makeText(context, "Video deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Unable to delete video", Toast.LENGTH_SHORT).show();
            }
        }

    }




    static class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView videoTitle;
        TextView videoTag;

        TextView videoLangauage;

        ImageView cardImage;

        ImageView downloadBtn;

        Button delete_button;



        VideoViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            videoTitle = itemView.findViewById(R.id.tv_item);
            videoTag = itemView.findViewById(R.id.tag_item);
            videoLangauage = itemView.findViewById(R.id.language_item);
            cardImage = itemView.findViewById(R.id.card_image);
            downloadBtn = itemView.findViewById(R.id.download_button_icon);
            delete_button = itemView.findViewById(R.id.delete_button);



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
