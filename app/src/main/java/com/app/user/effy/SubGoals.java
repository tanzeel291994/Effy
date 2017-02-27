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
        String title_goal=getIntent().getStringExtra("goal");
        Toast.makeText(SubGoals.this,title_goal, Toast.LENGTH_SHORT).show();
    }
}
