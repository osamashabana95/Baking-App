package com.example.osama.baking;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.osama.baking.R;

/**
 * Created by osama on 4/19/2018.
 */

/*
class to adapt data to ListView
 */

public class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    String[]list;
    String []list2;
    private Context ctxt=null;



   public ListRemoteViewsFactory(Context context,Intent intent){
       ctxt=context;
      list =intent.getStringArrayExtra("extra_ee");
      list2=intent.getStringArrayExtra("ext");

   }




    @Override
    public void onCreate() {

    }



    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return (list.length);
    }




    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews views = new RemoteViews(ctxt.getPackageName(), R.layout.baking_widget);
        views.setTextViewText(R.id.appwidget_text,list[i]);
        views.setTextViewText(R.id.ingredients,list2[i]);


        Intent intent=new Intent();
        Bundle extras=new Bundle();
        extras.putInt(Intent.EXTRA_INDEX, i);
        intent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.appwidget_text, intent);

        return views;
   }




    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


}
