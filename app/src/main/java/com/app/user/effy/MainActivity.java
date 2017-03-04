package com.app.user.effy;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.constraint.solver.Goal;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.app.user.effy.adapter.GoalCursorAdapter;
import com.app.user.effy.adapter.GoalModel;
import com.app.user.effy.data.GoalContract;
import com.app.user.effy.data.GoalContract.GoalEntry;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FragmentAddGoalDialog.CustomDialogInterface
        ,LoaderManager.LoaderCallbacks<Cursor> ,GoalCursorAdapter.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener{

    private int GOAL_LOADER_ID;
    public static DisplayMetrics displayMetrics;
    Context context;
    RecyclerView recyclerview;
    GoalCursorAdapter goalCursorAdapter;
    FragmentAddGoalDialog addGoalDialogFragment;
    ArrayList<GoalModel> goals_list;
    Toolbar mToolbar;
    TextView title_toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title_toolbar=(TextView)findViewById(R.id.main_toolbar_title);
        String name=getIntent().getStringExtra("name");
        Toast.makeText(MainActivity.this,"Hey "+name+" lets up your Effy quoitient:)", Toast.LENGTH_SHORT).show();
        context=this;
        GOAL_LOADER_ID =0 ;
        goals_list=new ArrayList<GoalModel>();
        goalCursorAdapter = new GoalCursorAdapter(this,this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        title_toolbar.setText("Effy");
        //title_toolbar.setGravity(Gravity.CENTER);
//        LinearLayout r = (LinearLayout) ((ViewGroup) title_toolbar.getParent()).getParent();
  //      r.setGravity(Gravity.CENTER);
        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        onRefresh();

        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(goalCursorAdapter);

        displayMetrics =getResources().getDisplayMetrics();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "fab", Toast.LENGTH_SHORT).show();
                //start a dialog fragment to add goal
                //String[] selectionArgs={"1"};


                FragmentManager fm = getSupportFragmentManager();
                 addGoalDialogFragment = FragmentAddGoalDialog.newInstance("Add Goal");
                 addGoalDialogFragment.show(fm, "fragment_edit_name_dialog");

            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                String goal_id = goalCursorAdapter.getIdByPosition(viewHolder.getAdapterPosition());
               // Log.i("tag",GoalEntry.makeUriForStock(goal_id).toString());
                getContentResolver().delete(GoalEntry.CONTENT_URI,GoalEntry._ID+" = "+goal_id,null);
            }
        }).attachToRecyclerView(recyclerview);

        getSupportLoaderManager().initLoader(GOAL_LOADER_ID, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_get_quote) {
            Intent intent=new Intent(this,QuoteActivity.class);
            startActivity(intent);
            //getContentResolver().delete(GoalEntry.makeUriForStock("1"),GoalEntry._ID,null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addGoalClicked(String goal_name,Boolean imp,Boolean urg) {
        Toast.makeText(MainActivity.this,goal_name+imp+urg, Toast.LENGTH_SHORT).show();
//      makeTableRow(goal_name,imp,urg);
        //Add to content provider data
        ContentValues contentValues = new ContentValues();
        contentValues.put(GoalEntry.COLUMN_GOAL_NAME,goal_name);
        contentValues.put(GoalEntry.COLUMN_IMORTANT, String.valueOf(imp));
        contentValues.put(GoalEntry.COLUMN_URGENT,String.valueOf(urg));
        Uri uri=null;
        try {
            uri = getContentResolver().insert(GoalEntry.CONTENT_URI, contentValues);
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
        //Add a new row into linear layout
        LinearLayout table_goals = (LinearLayout) findViewById(R.id.table_goals);
        LinearLayout linearLayoutChild = new LinearLayout(context);
        linearLayoutChild.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        final TextView txt_goal=new TextView(context);
        txt_goal.setText(goal_name);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 2.0f;
        params.gravity = Gravity.FILL;
        txt_goal.setLayoutParams(params);
        /*txt_goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                //Go to the sub goals activity from here
                Intent intent=new Intent(context,SubGoals.class);
                intent.putExtra("goal",txt_goal.getText());
                startActivity(intent);
            }
        });
        */
        linearLayoutChild.addView(txt_goal);

        RelativeLayout rel1=new RelativeLayout(context);
        rel1.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        rel1.setGravity(Gravity.CENTER);
        RelativeLayout rel2=new RelativeLayout(context);
        rel2.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        rel2.setGravity(Gravity.CENTER);

        CheckBox checkbox_imp=new CheckBox(context);
        checkbox_imp.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        checkbox_imp.setChecked(imp);
        checkbox_imp.setEnabled(false);
        rel1.addView(checkbox_imp);
        linearLayoutChild.addView(rel1);

        CheckBox checkbox_urg=new CheckBox(context);
        checkbox_urg.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        checkbox_urg.setChecked(urg);
        checkbox_urg.setEnabled(false);
        rel2.addView(checkbox_urg);
        linearLayoutChild.addView(rel2);
        Log.i("Tag","inn");
        table_goals.addView(linearLayoutChild, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String [] projection={GoalEntry._ID,GoalEntry.COLUMN_GOAL_NAME,GoalEntry.COLUMN_IMORTANT,GoalEntry.COLUMN_URGENT};
        return new CursorLoader(this,
                GoalEntry.CONTENT_URI,
                projection,
                null, null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        swipeRefreshLayout.setRefreshing(false);
        goalCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        swipeRefreshLayout.setRefreshing(false);

        goalCursorAdapter.swapCursor(null);
    }

    @Override
    public void onClick(String goal_name,int goal_id) {
        Toast.makeText(MainActivity.this,String.valueOf(goal_id), Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this,SubGoals.class);
        intent.putExtra("goal_id",goal_id);
        intent.putExtra("goal_name",goal_name);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {

    }
}
