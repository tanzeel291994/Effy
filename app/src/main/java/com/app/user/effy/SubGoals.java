package com.app.user.effy;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.user.effy.adapter.GoalCursorAdapter;
import com.app.user.effy.adapter.SubGoalCursorAdapter;
import com.app.user.effy.data.GoalContract;

public class SubGoals extends AppCompatActivity implements FragmentAddSubDialog.CustomSubDialogInterface
        ,LoaderManager.LoaderCallbacks<Cursor>,SubGoalCursorAdapter.OnReminderBtnPressed{

    private int SUB_GOAL_LOADER_ID;
    int requestCode = 0;
    ContentResolver contentResolver;
    long newEventId;
    long prev_id;
    String goal_name;
    int goal_id;
    FragmentAddSubDialog addSubGoalDialogFragment;
    RecyclerView recyclerview;
    SubGoalCursorAdapter subGoalCursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_goals);
        goal_id = getIntent().getIntExtra("goal_id", 1);
        goal_name = getIntent().getStringExtra("goal_name");
        subGoalCursorAdapter = new SubGoalCursorAdapter(this,this);


        recyclerview = (RecyclerView) findViewById(R.id.recyclerview1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(subGoalCursorAdapter);
        //Toast.makeText(SubGoals.this,String.valueOf(title_goal), Toast.LENGTH_SHORT).show();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_sub_goals);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SubGoals.this, "fab", Toast.LENGTH_SHORT).show();
                //start a dialog fragment to add goal
                FragmentManager fm = getSupportFragmentManager();
                addSubGoalDialogFragment = FragmentAddSubDialog.newInstance("Add Sub Goal");
                addSubGoalDialogFragment.show(fm, "fragment_edit_name_dialog");

            }
        });
        getSupportLoaderManager().initLoader(SUB_GOAL_LOADER_ID, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         if (requestCode == 0) {
             Log.i("tag", "result code="+String.valueOf(resultCode));
             Log.i("tag", "intent="+data);
         }
     }

    @Override
    public void addGoalClicked(String sub_goal_name)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GoalContract.SubGoalEntry.COLUMN_SUB_GOAL_NAME,sub_goal_name);
        contentValues.put(GoalContract.SubGoalEntry.COLUMN_GOAL_ID,goal_id);
        Uri uri=null;
        try {
            uri = getContentResolver().insert(GoalContract.SubGoalEntry.CONTENT_URI, contentValues);
        }
        catch (Exception e)
        {
            Toast.makeText(getBaseContext(),e.toString(), Toast.LENGTH_LONG).show();
        }
        if(uri != null) {
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String [] projection={GoalContract.SubGoalEntry._ID,GoalContract.SubGoalEntry.COLUMN_SUB_GOAL_NAME};
        String[] selectionArgs={String.valueOf(goal_id)};
        String selection=GoalContract.SubGoalEntry.COLUMN_GOAL_ID+"=?";
        return new CursorLoader(this,
                GoalContract.SubGoalEntry.CONTENT_URI,
                projection,
                selection,selectionArgs,GoalContract.SubGoalEntry._ID);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        subGoalCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        subGoalCursorAdapter.swapCursor(null);
    }

    @Override
    public void onClick(String sub_goal_name) {
        Intent calIntent = new Intent(Intent.ACTION_INSERT);
        calIntent.setData(CalendarContract.Events.CONTENT_URI);
        //calIntent.setType("vnd.android.cursor.item/event");
        calIntent.putExtra(CalendarContract.Events.TITLE,sub_goal_name);
        startActivityForResult(calIntent, requestCode);
    }
}
