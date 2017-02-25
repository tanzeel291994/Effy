package com.app.user.effy;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "fab", Toast.LENGTH_SHORT).show();
                //Add a new row into tab layout
                TableLayout table_goals = (TableLayout) findViewById(R.id.table_goals);
                TableRow tr = new TableRow(context);
                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                TextView txt_goal=new TextView(context);
                txt_goal.setText("Loose weigth");
                txt_goal.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f));
                txt_goal.setGravity(Gravity.FILL);
                tr.addView(txt_goal);

                RelativeLayout rel1=new RelativeLayout(context);
                rel1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                rel1.setGravity(Gravity.CENTER);
                RelativeLayout rel2=new RelativeLayout(context);
                rel2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                rel2.setGravity(Gravity.CENTER);

                CheckBox checkbox_imp=new CheckBox(context);
                checkbox_imp.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                rel1.addView(checkbox_imp);
                tr.addView(rel1);
                CheckBox checkbox_urg=new CheckBox(context);
                checkbox_urg.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                rel2.addView(checkbox_urg);
                tr.addView(rel2);
                table_goals.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

            }
        });
    }
}
