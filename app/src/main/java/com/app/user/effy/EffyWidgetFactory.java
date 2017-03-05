package com.app.user.effy;


import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.constraint.solver.Goal;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.app.user.effy.data.GoalContract;


public class EffyWidgetFactory implements RemoteViewsService.RemoteViewsFactory {
    private Cursor mCursor;
    private Context mContext;
    int mWidgetId;
    public EffyWidgetFactory(Context applicationContext, Intent intent)
    {
       // Log.i("tag","311111");
        mContext = applicationContext;
        mWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        Log.i("tag","411111");
        if (mCursor != null) {
            mCursor.close();
        }
        String[] projections={GoalContract.GoalEntry.COLUMN_GOAL_NAME,GoalContract.GoalEntry.COLUMN_URGENT,
                GoalContract.GoalEntry.COLUMN_IMORTANT};
        mCursor = mContext.getContentResolver().query(
               GoalContract.GoalEntry.CONTENT_URI,
                projections,
                null, null,null);

    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.i("tag","2");
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.effy_widget_item);
        if (mCursor.moveToPosition(position)) {
            Log.i("tag",mCursor.toString());
            rv.setTextViewText(R.id.goal_name_w, mCursor.getString(mCursor.getColumnIndex(GoalContract.GoalEntry.COLUMN_GOAL_NAME)));
            rv.setBoolean(R.id.chk_imp_w,null, Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(GoalContract.GoalEntry.COLUMN_IMORTANT))));
            rv.setBoolean(R.id.chk_urg_w,null, Boolean.parseBoolean(mCursor.getString(mCursor.getColumnIndex(GoalContract.GoalEntry.COLUMN_URGENT))));
        }
        // AppWidgetManager manager = AppWidgetManager.getInstance(mContext);
        // manager.updateAppWidget(mWidgetId, rv);
        // manager.notifyAppWidgetViewDataChanged(mWidgetId,R.id.stock_list_widget);
        //appWidgetManager.updateAppWidget(appWidgetId, rv);
        return rv;


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
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}