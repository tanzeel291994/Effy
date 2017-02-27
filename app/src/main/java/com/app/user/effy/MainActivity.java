package com.app.user.effy;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.app.user.effy.data.GoalContract;

public class MainActivity extends AppCompatActivity implements FragmentAddGoalDialog.CustomDialogInterface {

    public static DisplayMetrics displayMetrics;
    Context context;
    FragmentAddGoalDialog addGoalDialogFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        displayMetrics =getResources().getDisplayMetrics();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "fab", Toast.LENGTH_SHORT).show();
                //start a dialog fragment to add goal
                FragmentManager fm = getSupportFragmentManager();
                 addGoalDialogFragment = FragmentAddGoalDialog.newInstance("Add Goal");
                addGoalDialogFragment.show(fm, "fragment_edit_name_dialog");
                /*

*/
            }
        });
    }

    @Override
    public void addGoalClicked(String goal_name,Boolean imp,Boolean urg) {
        Toast.makeText(MainActivity.this,goal_name+imp+urg, Toast.LENGTH_SHORT).show();
        makeTableRow(goal_name,imp,urg);
        //Add to content provider data
        ContentValues contentValues = new ContentValues();
        contentValues.put(GoalContract.GoalEntry.COLUMN_GOAL_NAME,goal_name);
        contentValues.put(GoalContract.GoalEntry.COLUMN_IMORTANT, imp);
        contentValues.put(GoalContract.GoalEntry.COLUMN_URGENT, urg);
        Uri uri=null;
        try {
            uri = getContentResolver().insert(GoalContract.GoalEntry.CONTENT_URI, contentValues);
        }
        catch (Exception e)
        {
            Toast.makeText(getBaseContext(),e.toString(), Toast.LENGTH_LONG).show();
        }
        if(uri != null) {
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
        }
    }

     void makeTableRow(String goal_name,Boolean imp,Boolean urg)
    {
        //Add a new row into table layout
        TableLayout table_goals = (TableLayout) findViewById(R.id.table_goals);
        TableRow tr = new TableRow(context);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        final TextView txt_goal=new TextView(context);
        txt_goal.setText(goal_name);
        txt_goal.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f));
        txt_goal.setGravity(Gravity.FILL);
        txt_goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                //Go to the sub goals activity from here
                Intent intent=new Intent(context,SubGoals.class);
                intent.putExtra("goal",txt_goal.getText());
                startActivity(intent);
            }
        });

        tr.addView(txt_goal);

        RelativeLayout rel1=new RelativeLayout(context);
        rel1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        rel1.setGravity(Gravity.CENTER);
        RelativeLayout rel2=new RelativeLayout(context);
        rel2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        rel2.setGravity(Gravity.CENTER);

        CheckBox checkbox_imp=new CheckBox(context);
        checkbox_imp.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        checkbox_imp.setChecked(imp);
        checkbox_imp.setEnabled(false);
        rel1.addView(checkbox_imp);
        tr.addView(rel1);
        CheckBox checkbox_urg=new CheckBox(context);
        checkbox_urg.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        checkbox_urg.setChecked(urg);
        checkbox_urg.setEnabled(false);
        rel2.addView(checkbox_urg);
        tr.addView(rel2);
        table_goals.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }
}
