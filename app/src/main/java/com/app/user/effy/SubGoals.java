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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.user.effy.data.GoalContract;

public class SubGoals extends AppCompatActivity implements FragmentAddSubDialog.CustomSubDialogInterface {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS =1 ;
    int requestCode = 0;
    ContentResolver contentResolver;
    long newEventId;
    long prev_id;
    String goal_name;
    int goal_id;
    FragmentAddSubDialog addSubGoalDialogFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_goals);
        goal_id = getIntent().getIntExtra("goal_id", 1);
         goal_name = getIntent().getStringExtra("goal_name");
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
    }

    public void setReminder(View view) {

        //readCalender(getContentResolver());
        Intent calIntent = new Intent(Intent.ACTION_INSERT);
         calIntent.setData(CalendarContract.Events.CONTENT_URI);
        //calIntent.setType("vnd.android.cursor.item/event");
        calIntent.putExtra(CalendarContract.Events.TITLE,goal_name);
        startActivityForResult(calIntent, requestCode);
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
}
