package com.example.osama.baking;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by osama on 4/19/2018.
 */

/*
class for load data to adapt it to list and make http connection
 */


public class WidgetIntentService extends IntentService {

    public static final String ACTION_WIDGETS = "com";

    public WidgetIntentService() {
        super("WidgetIntentService");
    }



  public static void loadWidgets(Context context){


      Intent intent = new Intent(context, WidgetIntentService.class);
      intent.setAction(ACTION_WIDGETS);
      context.startService(intent);

  }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
             if (ACTION_WIDGETS.equals(action)) {
                handleLoadWidgets();
            }
        }
    }




    private void handleLoadWidgets() {
        URL requestUrl = Utility.buildUrl();
        String[]ingredientsex=null;
        String[][] nameArray=null;
        String[] name = new String[1];
        String[] ingredients = new String[1];;
        String jsonResponse;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        int index=sharedPref.getInt("index",1);

        try {

           jsonResponse = Utility
                    .getResponseFromHttpUrl(requestUrl);
            nameArray =Utility.getStingArrayOfRecipesDetails(jsonResponse);
          ingredientsex = Utility.getIngredientsDetails(jsonResponse,index);
        } catch (Exception e) {
            e.printStackTrace();

        }
        name[0]=nameArray[index][0];
        String s="INGREDIENTS:";
        for(int j =0;j<ingredientsex.length;j++) {
            s=s+"\n\n"+(j+1)+"-"+ingredientsex[j];
        }
        ingredients[0]=s;





        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidget.class));


        //Now update all widgets
        BakingWidget.updatePlantWidgets(this, appWidgetManager, name,ingredients,appWidgetIds);

    }

}
