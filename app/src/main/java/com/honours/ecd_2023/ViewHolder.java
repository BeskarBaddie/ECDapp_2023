package com.honours.ecd_2023;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.exoplayer2.*;

import com.google.android.exoplayer2.ExoPlayer;

import com.google.android.exoplayer2.ui.PlayerView;

import java.util.Collections;
import com.google.firebase.analytics.FirebaseAnalytics;

public class ViewHolder extends RecyclerView.ViewHolder {
    private Context application;
    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(application);

    //PlayerControlView playerView;
    PlayerView playerView;
    Video video;
    public ExoPlayer player;

    public void setExoPlayer(ExoPlayer player) {
        this.player = player;
    }


    public ViewHolder(@NonNull View itemView) {

        super(itemView);

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

    public void setExoplayer(Application application, String name, String Videourl){

        TextView textView = itemView.findViewById(R.id.tv_item);
        playerView = itemView.findViewById(R.id.exoplayer_item);
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(application);
        textView.setText(name);

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

            logVideoPlayEvent(name, Videourl);
            exoPlayer.addListener((new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                boolean isVideoPlaying = false;
                long videoStartTimestamp = 0;
                if (isPlaying) {
                    videoStartTimestamp = System.currentTimeMillis();
                    isVideoPlaying = true;
                } else{
                    if (isVideoPlaying) {
                        long videoEndTimestamp = System.currentTimeMillis();
                        isVideoPlaying=false;
                        long videoDurationSeconds = (videoEndTimestamp - videoStartTimestamp) / 1000;
                        logVideoPlayEvent(name, Videourl, videoDurationSeconds);
                    }
                }
            }
            }));






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
    private void logVideoPlayEvent(String videoName, String videoUrl) {
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, "Video Played");
        params.putString("video_name", videoName);
        params.putString("video_url", videoUrl);
        FirebaseAnalytics.getInstance(itemView.getContext()).logEvent("video_played", params);
    }
    private void logVideoPlayEvent(String videoName, String videoUrl, long durationSeconds) {
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, "Video Played");
        params.putString("video_name", videoName);
        params.putString("video_url", videoUrl);
        params.putLong("video_duration_seconds", durationSeconds);
        FirebaseAnalytics.getInstance(itemView.getContext()).logEvent("video_played", params);
    }
}
