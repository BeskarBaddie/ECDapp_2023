package com.honours.ecd_2023;

import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.exoplayer2.*;

import com.google.android.exoplayer2.ExoPlayer;

import com.google.android.exoplayer2.ui.PlayerView;

import java.io.File;
import java.util.Collections;

public class ViewHolder extends RecyclerView.ViewHolder {

    //PlayerControlView playerView;
    PlayerView playerView;
    Video video;
    public ExoPlayer player;

    ImageView downloadBtn;



    public void setExoPlayer(ExoPlayer player) {
        this.player = player;
    }



    public ViewHolder(@NonNull View itemView) {

        super(itemView);

        downloadBtn = itemView.findViewById(R.id.download_button_icon);

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mClickListener.onItemClick(v, getAdapterPosition());

            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v,getAdapterPosition());
                return false;
            }
        });



    }

    public void setDownloadButtonIcon(Application application, String name) {
        ImageView downloadBtn = itemView.findViewById(R.id.download_button_icon);
        File internalStorageDir = application.getApplicationContext().getFilesDir();
        File videoFile = new File(internalStorageDir, name + ".mp4");
        if (videoFile.exists()) {
            // If the video file exists in internal storage, show a different icon
            downloadBtn.setImageResource(R.drawable.download_green);
        } else {
            // If the video file doesn't exist in internal storage, show the default download icon
            downloadBtn.setImageResource(R.drawable.download_black);
        }
    }

    public void setContentIcon(Application application, String topic) {
        ImageView cardImage = itemView.findViewById(R.id.card_image);

        if ("Baby Health".equals(topic)) {
            cardImage.setImageResource(R.drawable.baby_health_picture); // Change this to the appropriate play icon for topic1
        } else if ("Baby Development".equals(topic)) {
            cardImage.setImageResource(R.drawable.baby_picture); // Change this to the appropriate play icon for topic2
        }
        else if ("Child Entertainment".equals(topic)) {
            cardImage.setImageResource(R.drawable.baby_picture);
        }
        else if ("Parent Health".equals(topic)) {
            cardImage.setImageResource(R.drawable.parent_picture);
        }
        else if ("Community Content".equals(topic)) {
            cardImage.setImageResource(R.drawable.community_picture);
        }
    }




    public void setExoplayer(Application application, String name, String Videourl, String tag, String Topics){


        TextView textView = itemView.findViewById(R.id.tv_item);
        TextView tagView = itemView.findViewById(R.id.tag_item);
        //playerView = itemView.findViewById(R.id.exoplayer_item);

        textView.setText(name);
        tagView.setText(tag);

        try {
           // BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(application).build();
            //TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            //exoPlayer = (SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(application);
            //Uri video = Uri.parse(Videourl);
            //DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("video");
            //ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            //MediaSource mediaSource = new ExtractorMediaSource(video,dataSourceFactory,extractorsFactory,null,null);
            //playerView.setPlayer(exoPlayer);
            //exoPlayer.prepare(mediaSource);
            //exoPlayer.setPlayWhenReady(false);

            ExoPlayer exoPlayer = new ExoPlayer.Builder(application).build();
            playerView.setPlayer(exoPlayer);
            MediaItem mediaItem = MediaItem.fromUri(Videourl);
            exoPlayer.addMediaItems(Collections.singletonList(mediaItem));
            exoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            exoPlayer.prepare();
            exoPlayer.setPlayWhenReady(false);









        }catch (Exception e ){
            Log.e("Viewholder ","exoplayer error" + e.toString());
        }

    }

    private ViewHolder.clicklistener mClickListener;
    public interface clicklistener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(ViewHolder.clicklistener clicklistener){
        mClickListener = clicklistener;
    }

}
