package com.example.osama.baking.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.osama.baking.fragments.MasterListFragment;
import com.example.osama.baking.R;
import com.example.osama.baking.fragments.VideoFragment;

public class DetailsActivity extends AppCompatActivity implements VideoFragment.OnButtonClickListener {
public static   boolean mTwoPane;
    MasterListFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //to get intent of main activity

        int index = getIntent().getIntExtra(Intent.EXTRA_INDEX, 1);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("index", index);
        editor.commit();
        // check if it is tablet mode

        if (findViewById(R.id.two_pane) != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }

        //start add fragment
        if (savedInstanceState == null) {
            fragment = new MasterListFragment();
            fragment.setPosition(index);
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.details_container, fragment)
                    .commit();


        } else {
           fragment= (MasterListFragment) getSupportFragmentManager().getFragment(savedInstanceState, "fragment");

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .show(fragment)
                    .commit();


        }
    }
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "fragment", fragment);
    }
    @Override
    public void onButtonSelected(int position) {

    }
}
