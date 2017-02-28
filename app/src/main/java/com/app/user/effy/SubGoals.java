 package com.app.user.effy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

 public class SubGoals extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_goals);
        int goal_id=getIntent().getIntExtra("goal_id",1);
        String goal_name=getIntent().getStringExtra("goal_name");
        //Toast.makeText(SubGoals.this,String.valueOf(title_goal), Toast.LENGTH_SHORT).show();
    }
}
