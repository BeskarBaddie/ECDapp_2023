package com.honours.ecd_2023;

import android.app.Application;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.exoplayer2.*;

import com.google.android.exoplayer2.ExoPlayer;

import com.google.android.exoplayer2.ui.PlayerView;

import java.util.Collections;

public class ViewHolder extends RecyclerView.ViewHolder {

    //PlayerControlView playerView;
    PlayerView playerView;
    Video video;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void setExoplayer(Application application, String name, String Videourl){

        TextView textView = itemView.findViewById(R.id.tv_item);
        playerView = itemView.findViewById(R.id.exoplayer_item);

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
            exoPlayer.prepare();
            exoPlayer.setPlayWhenReady(false);









        }catch (Exception e ){
            Log.e("Viewholder ","exoplayer error" + e.toString());
        }

    }
}
