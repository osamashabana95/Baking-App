package com.example.osama.baking;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,String []s,String[] s2,
                                int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);


        // Set the GridWidgetService intent to act as the adapter for the ListView
        Intent intent = new Intent(context, WidgetService.class);
        intent.putExtra("extra_ee",s);
        intent.putExtra("ext",s2);
        views.setRemoteAdapter(R.id.widget_list_view, intent);
        Intent appIntent = new Intent(context, DetailsActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_list_view, appPendingIntent);
       

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }



    public static void updatePlantWidgets(Context context, AppWidgetManager appWidgetManager, String[] s, String[] s2,int[] appWidgetIds) {


        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, s,s2, appWidgetId);
        }


    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        WidgetIntentService.loadWidgets(context);

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

