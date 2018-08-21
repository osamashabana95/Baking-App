package com.example.osama.baking.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.osama.baking.R;
import com.example.osama.baking.adapters.RecipeAdapter;
import com.example.osama.baking.utility.Utility;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final int CARDS_LOADER_ID = 44;
    GridLayoutManager mLayoutManager;
    private RecipeAdapter mRecipeAdapter;
    private Parcelable mListState;
    @BindView(R.id.recipe_cards_list)RecyclerView mRecyclerView;



    //handle click of item of recyclerView

    private  RecipeAdapter.RecipeOnClickHandler recipeOnClickHandler =new RecipeAdapter.RecipeOnClickHandler() {
        @Override
        public void onListItemClck(int position) {
            Context context = MainActivity.this;
            Class dClass =  DetailsActivity.class;
            Intent startDetailsActivityIntent = new Intent(context,dClass);
            startDetailsActivityIntent.putExtra(Intent.EXTRA_INDEX,position);
            startActivity(startDetailsActivityIntent);
        }
    };

    //make http connection and load data in background thread

    private LoaderManager.LoaderCallbacks<String> loadCardsData = new LoaderManager.LoaderCallbacks<String>() {
        @Override
        public Loader<String> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<String>(MainActivity.this) {
                String mCardsData = null;
                @Override
                protected void onStartLoading() {
                    if (mCardsData != null) {
                        deliverResult(mCardsData);
                    } else {
                        forceLoad();
                    }
                }


                @Override
                public String loadInBackground() {
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

                    mCardsData = data;
                    super.deliverResult(data);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            if (data != null) {
                try {
                    String[][] nameArray =Utility.getStingArrayOfRecipesDetails(data);
                    mRecipeAdapter.setNamesAndImages(nameArray);
                    mLayoutManager.onRestoreInstanceState(mListState);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mLayoutManager.onRestoreInstanceState(mListState);
            }

        }

        @Override
        public void onLoaderReset(Loader<String> loader) {

        }
    };


    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        // Save list state
        mListState = mLayoutManager.onSaveInstanceState();
        state.putParcelable("kk", mListState);
    }

    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);

        // Retrieve list state and list/item positions
        if(state != null)
            mListState = state.getParcelable("kk");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this,1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecipeAdapter = new RecipeAdapter(this, recipeOnClickHandler);
        mRecyclerView.setAdapter(mRecipeAdapter);


        // intialize loader and start it

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> loader = loaderManager.getLoader(CARDS_LOADER_ID);
        if (loader == null) {
            loaderManager.initLoader(CARDS_LOADER_ID, null, loadCardsData);
        }
        loaderManager.restartLoader(CARDS_LOADER_ID, null, loadCardsData);

    }

}
