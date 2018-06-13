package com.example.osama.baking;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.osama.baking.DetailsActivity;
import com.example.osama.baking.R;
import com.example.osama.baking.StepsAdapter;
import com.example.osama.baking.VideoActivity;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by osama on 4/18/2018.
 */

public class VideoFragment extends Fragment {


    String mVideoUrl;
    String mDescription;
    String mNextStepInstruction;
    OnButtonClickListener mCallback;
    long playbackPosition;
    int currentWindow;
    boolean playWhenReady;
    private SimpleExoPlayer mExoPlayer;


    @BindView(R.id.videoView)SimpleExoPlayerView mPlayerView;
    @BindView(R.id.instruction_text)TextView textView;
    @BindView(R.id.next_step_button) Button button;

    public VideoFragment(){}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnButtonClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnButtonClickListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        playbackPosition = mExoPlayer.getCurrentPosition();
        currentWindow = mExoPlayer.getCurrentWindowIndex();
        playWhenReady = mExoPlayer.getPlayWhenReady();

        outState.putLong("position", playbackPosition);
        outState.putInt("window",currentWindow);
        outState.putBoolean("state",playWhenReady);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null){
            playbackPosition=savedInstanceState.getLong("position",0);
            currentWindow=savedInstanceState.getInt("window",0);
            playWhenReady=savedInstanceState.getBoolean("state",false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_video, container, false);
       setRetainInstance(true);

        ButterKnife.bind(this,rootView);

        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_foreground));


        // to handle if it is last step or it is tablet mode ,to remove next step button
       if(Arrays.asList(StepsAdapter.mDescriptions).indexOf(mDescription)==Arrays.asList(StepsAdapter.mShortDescriptions).indexOf(mNextStepInstruction)|| DetailsActivity.mTwoPane){
           button.setVisibility(View.GONE);
       }

       // to handle if landscape mode ,to make player full screen

       if(VideoActivity.mLandscape){
           button.setVisibility(View.GONE);
           textView.setVisibility(View.GONE);
       }


       //bind data to views
           textView.setMovementMethod(new ScrollingMovementMethod());
           textView.setText(mDescription);
           button.setText(mNextStepInstruction + "   >>");
           button.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   mCallback.onButtonSelected(Arrays.asList(StepsAdapter.mDescriptions).indexOf(mDescription));
               }
           });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }


    //to release player and resources

    private void releasePlayer() {
        if (mExoPlayer != null) {
            playbackPosition = mExoPlayer.getCurrentPosition();
            currentWindow = mExoPlayer.getCurrentWindowIndex();
            playWhenReady = mExoPlayer.getPlayWhenReady();

            mExoPlayer.release();
            mExoPlayer = null;

        }
    }

    // to intialize player
    private void initializePlayer() {

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getActivity()),
                    new DefaultTrackSelector(), new DefaultLoadControl());

            mPlayerView.setPlayer(mExoPlayer);



            mExoPlayer.setPlayWhenReady(playWhenReady);
            mExoPlayer.seekTo(currentWindow, playbackPosition);
            Uri uri = Uri.parse(mVideoUrl);
            MediaSource mediaSource = buildMediaSource(uri);
            mExoPlayer.prepare(mediaSource);
        }


        // to build media source for player
    private MediaSource buildMediaSource(Uri uri) {

       return new ExtractorMediaSource(uri, new DefaultDataSourceFactory(
                getActivity(), "exoplayer-baking"), new DefaultExtractorsFactory(), null, null);
    }


    // to intialize the data of each array before binding it

    public void setVideoUrl (String videoUrl){
    mVideoUrl=videoUrl;
    }

    public void  setDescription (String description){
        mDescription=description;
    }

    public  void setNextStepDescription(String nextStepDescription){
        mNextStepInstruction=nextStepDescription;
    }


    public interface OnButtonClickListener {
        void onButtonSelected(int position);
    }


}
