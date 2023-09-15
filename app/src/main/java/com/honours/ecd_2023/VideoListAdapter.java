package com.honours.ecd_2023;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

/**
 * Adapter for the video list RecyclerView.
 */
public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoViewHolder> {
    private Context context;
    private List<Video> videoList;
    private OnItemClickListener onItemClickListener;

    private boolean showDeleteButton;

    private String currentActivityName;

    /**
     * Constructor for VideoListAdapter.
     *
     * @param context          The context in which the adapter is used.
     * @param videoList        The list of videos to display.
     * @param showDeleteButton A flag indicating whether to show the delete button.
     */
    public VideoListAdapter(Context context, List<Video> videoList, boolean showDeleteButton) {
        this.context = context;
        this.videoList = videoList;
        this.showDeleteButton = showDeleteButton;
    }

    /**
     * Interface for item click events.
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    /**
     * Updates the layout parameters for the CardView based on the current activity name.
     *
     * @param cardView The CardView to update.
     */
    public void updateCardLayoutParams(CardView cardView) {
        if ("ShowVideo".equals(currentActivityName)) {
            // Set layout parameters for YourActivityName1
            cardView.setLayoutParams(new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        } else if ("VideoListActivity".equals(currentActivityName)) {
            // Set layout parameters for YourActivityName2
            cardView.setLayoutParams(new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            // Default layout parameters if no match is found
            cardView.setLayoutParams(new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    /**
     * Sets an item click listener for the RecyclerView.
     *
     * @param listener The item click listener to set.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view, onItemClickListener);
    }

    /**
     * Updates the video list with new data.
     *
     * @param newVideoList The new list of videos to display.
     */
    public void updateVideoList(List<Video> newVideoList) {
        videoList.clear();
        videoList.addAll(newVideoList);
    }

    /**
     * Checks if a video is stored in local memory.
     *
     * @param videoFileName The name of the video file.
     * @return True if the video is in local memory, false otherwise.
     */
    private boolean isVideoInLocalMemory(String videoFileName) {
        File internalStorageDir = context.getApplicationContext().getFilesDir();
        if (videoFileName.endsWith(" (Download)")) {
            File videoFile = new File(internalStorageDir, videoFileName);
            return videoFile.exists();
        } else {
            File videoFile = new File(internalStorageDir, videoFileName + " (Download)");
            return videoFile.exists();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video videoFile = videoList.get(position);
        holder.videoTitle.setText(videoFile.getTitle());
        holder.videoTag.setText(videoFile.getTags());
        holder.videoLangauage.setText(videoFile.getLanguage());

        updateCardLayoutParams(holder.cardView);

        boolean isVideoStoredLocally = isVideoInLocalMemory(videoFile.getTitle());

        // Set the appropriate download image based on the video's storage status
        if (isVideoStoredLocally) {
            holder.downloadBtn.setImageResource(R.drawable.download_green);
            holder.delete_button.setVisibility(View.VISIBLE);
        } else {
            holder.downloadBtn.setImageResource(R.drawable.download_black);
            holder.delete_button.setVisibility(View.INVISIBLE);
        }

        if (showDeleteButton == true) {
            holder.delete_button.setVisibility(View.VISIBLE);
        } else {
            holder.delete_button.setVisibility(View.INVISIBLE);
        }

        if ("Baby Health".equals(videoFile.getTopics())) {
            holder.cardImage.setImageResource(R.drawable.baby_health_picture);
        } else if ("Baby Development".equals(videoFile.getTopics())) {
            holder.cardImage.setImageResource(R.drawable.baby_picture);
        } else if ("Child Entertainment".equals(videoFile.getTopics())) {
            holder.cardImage.setImageResource(R.drawable.baby_picture);
        } else if ("Parent Health".equals(videoFile.getTopics())) {
            holder.cardImage.setImageResource(R.drawable.parent_picture);
        } else if ("Community Content".equals(videoFile.getTopics())) {
            holder.cardImage.setImageResource(R.drawable.community_picture);
        }

        if ("Baby Health".equals(videoFile.getTopics())) {
            holder.itemView.setBackgroundResource(R.color.baby_health_green);
        }

        if ("Baby Development".equals(videoFile.getTopics())) {
            holder.itemView.setBackgroundResource(R.color.baby_development_orange);
        }

        if ("Parent Health".equals(videoFile.getTopics())) {
            holder.itemView.setBackgroundResource(R.color.parent_health_purple);
        }

        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteVideoFile(v.getContext(), videoFile.getTitle());
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

    /**
     * Deletes a video file from local storage.
     *
     * @param context   The context.
     * @param videoName The name of the video file to delete.
     */
    private void deleteVideoFile(Context context, String videoName) {
        File internalStorageDir = context.getFilesDir();
        File videoFile = new File(internalStorageDir, videoName);
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
        CardView cardView;

        VideoViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            videoTitle = itemView.findViewById(R.id.tv_item);
            videoTag = itemView.findViewById(R.id.tag_item);
            videoLangauage = itemView.findViewById(R.id.language_item);
            cardImage = itemView.findViewById(R.id.card_image);
            downloadBtn = itemView.findViewById(R.id.download_button_icon);
            delete_button = itemView.findViewById(R.id.delete_button);
            cardView = itemView.findViewById(R.id.card_view);

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
