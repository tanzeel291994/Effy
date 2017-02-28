package com.app.user.effy;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class SubGoals extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS =1 ;
    int requestCode = 0;
    ContentResolver contentResolver;
    long newEventId;
    long prev_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_goals);
        int goal_id = getIntent().getIntExtra("goal_id", 1);
        String goal_name = getIntent().getStringExtra("goal_name");
        //Toast.makeText(SubGoals.this,String.valueOf(title_goal), Toast.LENGTH_SHORT).show();
    }

    public void setReminder(View view) {

        //readCalender(getContentResolver());
        Intent calIntent = new Intent(Intent.ACTION_INSERT);
         calIntent.setData(CalendarContract.Events.CONTENT_URI);
        //calIntent.setType("vnd.android.cursor.item/event");
        calIntent.putExtra(CalendarContract.Events.TITLE, "My House Party");
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
}
