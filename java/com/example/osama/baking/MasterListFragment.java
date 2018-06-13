package com.example.osama.baking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by osama on 4/14/2018.
 */

public class MasterListFragment extends Fragment {
    private static final int INGREDIENTS_STEPS_LOADER_ID = 55;
    StepsAdapter sAdapter;
    IngredientsAdapter iAdapter;
    LinearLayoutManager sLayoutManager;
    LinearLayoutManager iLayoutManager;
    int mPosition;
    int i;
    private Parcelable mListState1;
    private Parcelable mListState2;

    @BindView(R.id.ingredients_list)RecyclerView ingredientRecyclerView;
    @BindView(R.id.steps_list)RecyclerView stepsRecyclerView;


    //handle click in items of recyclerView

    private StepsAdapter.StepsOnClickHandler stepsOnClickHandler =new StepsAdapter.StepsOnClickHandler() {


        @Override
        public void onListItemClck( int position,String videoUrl, String descripton, String shortDescription) {
           if(DetailsActivity.mTwoPane)
           {

               VideoFragment fragment1 = new VideoFragment();
               fragment1.setVideoUrl(StepsAdapter.mVideoUrl[position]);
               fragment1.setDescription(StepsAdapter.mDescriptions[position]);
               FragmentManager fragmentManager1 = getActivity().getSupportFragmentManager();

               fragmentManager1.beginTransaction()
                       .replace(R.id.video_container, fragment1)
                       .commit();

           }else {

               Context context = getActivity();
               Class vClass = VideoActivity.class;
               Intent startVideoActivity = new Intent(context, vClass);
               startVideoActivity.putExtra(Intent.EXTRA_PROCESS_TEXT, videoUrl);
               startVideoActivity.putExtra(Intent.EXTRA_SUBJECT, descripton);
               startVideoActivity.putExtra(Intent.EXTRA_TEXT, shortDescription);
               startActivity(startVideoActivity);
           }
        }
    };

    //make http connection and load data

    private LoaderManager.LoaderCallbacks<String> loadIngredientsStepsData = new LoaderManager.LoaderCallbacks<String>() {
        @Override
        public Loader<String> onCreateLoader(int id, final Bundle args) {
            return new AsyncTaskLoader<String>(getContext()) {
                String mStepsData = null;
                @Override
                protected void onStartLoading() {
                    if (mStepsData != null) {
                        deliverResult(mStepsData);
                    } else {
                        forceLoad();
                    }
                }


                @Override
                public String loadInBackground() {
                 if(args!=null){
                     i = args.getInt("id");
                 }

                    URL requestUrl = Utility.buildUrl();


                    try {

                        String jsonResponse = Utility
                                .getResponseFromHttpUrl(requestUrl);



                        return jsonResponse;


                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
                @Override
                public void deliverResult(String data) {

                    mStepsData = data;
                    super.deliverResult(data);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            if (data != null) {
                try {
                    String[] ingredients = Utility.getIngredientsDetails(data,i);
                    String[][] steps = Utility.getStepsDetails(data,i);
                    iAdapter.setIngredients(ingredients);
                    sAdapter.setSteps(steps);
                    iLayoutManager.onRestoreInstanceState(mListState1);
                    sLayoutManager.onRestoreInstanceState(mListState2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<String> loader) {

        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save list state
        mListState1 = iLayoutManager.onSaveInstanceState();
        outState.putParcelable("ii", mListState1);
        mListState2 = sLayoutManager.onSaveInstanceState();
        outState.putParcelable("ss", mListState2);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            mListState1 = savedInstanceState.getParcelable("ii");
            mListState2 = savedInstanceState.getParcelable("ss");

        }
    }

    public MasterListFragment() {
    }
// to set position of object of json array needed to load data

    public void setPosition(int position){
       mPosition=position;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);


        // Get a reference to the RecyclerView in the fragment_master_list xml layout file
        ButterKnife.bind(this,rootView);
        ingredientRecyclerView.setHasFixedSize(true);
        iLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        ingredientRecyclerView.setLayoutManager(iLayoutManager);
        stepsRecyclerView.setHasFixedSize(true);
        sLayoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        stepsRecyclerView.setLayoutManager(sLayoutManager);
        // Create the adapter
        // This adapter takes in the context and an ArrayList of ALL the image resources to display
        sAdapter = new StepsAdapter(getContext(),stepsOnClickHandler);
        iAdapter =new IngredientsAdapter();
        // Set the adapter on the GridView
        ingredientRecyclerView.setAdapter(iAdapter);
        stepsRecyclerView.setAdapter(sAdapter);
        Bundle queryBundle = new Bundle();
        queryBundle.putInt("id", mPosition);
        LoaderManager loaderManager = getLoaderManager();
        Loader<String> loader = loaderManager.getLoader(INGREDIENTS_STEPS_LOADER_ID);
        if (loader == null) {
            loaderManager.initLoader(INGREDIENTS_STEPS_LOADER_ID, queryBundle, loadIngredientsStepsData);
        }
        loaderManager.restartLoader(INGREDIENTS_STEPS_LOADER_ID, queryBundle, loadIngredientsStepsData);
        // Return the root view
        return rootView;
    }


}
