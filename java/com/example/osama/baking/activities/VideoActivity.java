package com.example.osama.baking.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.osama.baking.R;
import com.example.osama.baking.adapters.StepsAdapter;
import com.example.osama.baking.fragments.VideoFragment;

public class VideoActivity extends AppCompatActivity implements VideoFragment.OnButtonClickListener {

 public   static boolean mLandscape;
    VideoFragment fragment ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        //to handle landscape mode

        if(findViewById(R.id.land)!=null){mLandscape=true;}
        else{mLandscape=false;}


        //for add fragment
        if (savedInstanceState == null) {
            fragment = new VideoFragment();
            Intent intent = getIntent();
            fragment.setVideoUrl(intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT));
            fragment.setDescription(intent.getStringExtra(Intent.EXTRA_SUBJECT));
            fragment.setNextStepDescription(intent.getStringExtra(Intent.EXTRA_TEXT));
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.video_container, fragment)
                    .commit();

        }else {
            fragment= (VideoFragment) getSupportFragmentManager().getFragment(savedInstanceState, "fragment");
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .show(fragment)
                    .commit();

        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "fragment", fragment);
    }
    // to handle next_step_button click
    @Override
    public void onButtonSelected(int position) {
        VideoFragment fragment = new VideoFragment();
        fragment.setVideoUrl(StepsAdapter.mVideoUrl[position+1]);
        fragment.setDescription(StepsAdapter.mDescriptions[position+1]);
        if(StepsAdapter.mShortDescriptions.length-1<position+2){
            fragment.setNextStepDescription(StepsAdapter.mShortDescriptions[position+1]);
        }else{
            fragment.setNextStepDescription(StepsAdapter.mShortDescriptions[position+2]);}
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.video_container, fragment)
                .commit();
    }
}
