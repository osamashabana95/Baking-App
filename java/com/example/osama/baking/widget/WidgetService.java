package com.example.osama.baking.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by osama on 4/19/2018.
 */

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {


        return new ListRemoteViewsFactory(this.getApplicationContext(),intent);
    }

}
