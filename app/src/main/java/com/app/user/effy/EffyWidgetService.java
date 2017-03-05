package com.app.user.effy;


import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;
public class EffyWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        //Log.i("tag","211111");
        return new EffyWidgetFactory(getApplicationContext(), intent);
    }
}