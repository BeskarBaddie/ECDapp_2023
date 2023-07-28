package com.honours.ecd_2023;

import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
    public ExoPlayer player;

    ImageButton downloadBtn;



    public void setExoPlayer(ExoPlayer player) {
        this.player = player;
    }

    public void setButton(){
        downloadBtn = itemView.findViewById(R.id.download_button_viewholder);
    }


    public ViewHolder(@NonNull View itemView) {

        super(itemView);

        downloadBtn = itemView.findViewById(R.id.download_button_viewholder);

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

    public void setExoplayer(Application application, String name, String Videourl, String tag, String Topics){


        TextView textView = itemView.findViewById(R.id.tv_item);
        TextView tagView = itemView.findViewById(R.id.tag_item);
        playerView = itemView.findViewById(R.id.exoplayer_item);

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
