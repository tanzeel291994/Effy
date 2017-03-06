package com.app.user.effy;


import android.content.Intent;
import android.widget.RemoteViewsService;

public class EffyWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new EffyWidgetFactory(getApplicationContext(), intent);
    }
}