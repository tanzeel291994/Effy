package com.app.user.effy;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.user.effy.App.Config;
import com.app.user.effy.Util.*;
import com.app.user.effy.adapter.GoalCursorAdapter;
import com.app.user.effy.adapter.GoalModel;
import com.app.user.effy.data.GoalContract.GoalEntry;
import com.app.user.effy.service.MyTaskService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FragmentAddGoalDialog.CustomDialogInterface
        , LoaderManager.LoaderCallbacks<Cursor>, GoalCursorAdapter.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    public static DisplayMetrics displayMetrics;
    Context context;
    RecyclerView recyclerview;
    GoalCursorAdapter goalCursorAdapter;
    FragmentAddGoalDialog addGoalDialogFragment;
    Cursor mCursor;
    ArrayList<GoalModel> goals_list;
    Toolbar mToolbar;
    TextView title_toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    Button btColorScheme;
    private BottomSheetBehavior mBottomSheetBehavior;
    public static final String TASK_TAG_WIFI = "wifi_task";
    private BroadcastReceiver mReceiver;
    private GcmNetworkManager mGcmNetworkManager;
    private static final String TAG = "MainActivity";
    private static final int RC_PLAY_SERVICES = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title_toolbar = (TextView) findViewById(R.id.main_toolbar_title);
        btColorScheme = (Button) findViewById(R.id.colorScheme);
        String name = getIntent().getStringExtra("name");
        Toast.makeText(MainActivity.this, getString(R.string.hey) + name + getString(R.string.greeting), Toast.LENGTH_LONG).show();
        context = this;
        int GOAL_LOADER_ID = 0;
        goals_list = new ArrayList<>();
        goalCursorAdapter = new GoalCursorAdapter(this, this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        title_toolbar.setText(R.string.effy);
        title_toolbar.setTextSize(Float.parseFloat("32"));
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "font/BEBAS___.ttf");
        title_toolbar.setTypeface(custom_font);
        //title_toolbar.setGravity(Gravity.CENTER);
//        LinearLayout r = (LinearLayout) ((ViewGroup) title_toolbar.getParent()).getParent();
        //      r.setGravity(Gravity.CENTER);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        onRefresh();

        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(goalCursorAdapter);
        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        displayMetrics = getResources().getDisplayMetrics();
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   Toast.makeText(MainActivity.this, "fab", Toast.LENGTH_SHORT).show();

                FragmentManager fm = getSupportFragmentManager();
                addGoalDialogFragment = FragmentAddGoalDialog.newInstance("Add Goal");
                addGoalDialogFragment.show(fm, "fragment_add_goal");

            }
        });
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && fab.isShown()) {
                    fab.hide();
                }

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    fab.show();
                }

                super.onScrollStateChanged(recyclerView, newState);
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
                getContentResolver().delete(GoalEntry.CONTENT_URI, GoalEntry._ID + " = " + goal_id, null);
            }
        }).attachToRecyclerView(recyclerview);

        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setPeekHeight(0);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {

                    btColorScheme.setVisibility(View.VISIBLE);
                    mBottomSheetBehavior.setPeekHeight(0);
                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {
            }
        });

        // [START get_gcm_network_manager]
        mGcmNetworkManager = GcmNetworkManager.getInstance(this);
        // [END get_gcm_network_manager]

       //startPeriodicTask();
        //stopPeriodicTask();
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(MyTaskService.ACTION_DONE)) {
                    String tag = intent.getStringExtra(MyTaskService.EXTRA_TAG);
                    int result = intent.getIntExtra(MyTaskService.EXTRA_RESULT, -1);

                    String msg = String.format("DONE: %s (%d)", tag, result);
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            }
        };

        // Check that Google Play Services is available, since we need it to use GcmNetworkManager
        // but the API does not use GoogleApiClient, which would normally perform the check
        // automatically.
        checkPlayServicesAvailable();

      getSupportLoaderManager().initLoader(GOAL_LOADER_ID, null, this);
    }

    @Override
    public void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter();
        filter.addAction(MyTaskService.ACTION_DONE);

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(mReceiver, filter);
    }


    @Override
    public void onStop() {
        super.onStop();

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.unregisterReceiver(mReceiver);

        // For the purposes of this sample, cancel all tasks when the app is stopped.
        //mGcmNetworkManager.cancelAllTasks(MyTaskService.class);
    }
    public void startPeriodicTask() {
        Log.d(TAG, "startPeriodicTask");

        // [START start_periodic_task]
        PeriodicTask task = new PeriodicTask.Builder()
                .setService(MyTaskService.class)
                .setTag(TASK_TAG_WIFI)
                .setPeriod(30L)
                .build();

        mGcmNetworkManager.schedule(task);
        // [END start_periodic_task]
    }

    public void stopPeriodicTask() {
        Log.d(TAG, "stopPeriodicTask");

        // [START stop_periodic_task]
        mGcmNetworkManager.cancelTask(TASK_TAG_WIFI, MyTaskService.class);
        // [END stop_per
    }
    private void checkPlayServicesAvailable() {
        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();
        int resultCode = availability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (availability.isUserResolvableError(resultCode)) {
                // Show dialog to resolve the error.
                availability.getErrorDialog(this, resultCode, RC_PLAY_SERVICES).show();
            } else {
                // Unresolvable error
                Toast.makeText(this, "Google Play Services error", Toast.LENGTH_SHORT).show();
            }
        }
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
            Intent intent = new Intent(this, QuoteActivity.class);
            startActivity(intent);
            //getContentResolver().delete(GoalEntry.makeUriForStock("1"),GoalEntry._ID,null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addGoalClicked(String goal_name, Boolean imp, Boolean urg) {

        if (goal_name.isEmpty()) {
            Toast.makeText(getBaseContext(), R.string.user_input_error, Toast.LENGTH_LONG).show();
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(GoalEntry.COLUMN_GOAL_NAME, goal_name);
        contentValues.put(GoalEntry.COLUMN_IMORTANT, String.valueOf(imp));
        contentValues.put(GoalEntry.COLUMN_URGENT, String.valueOf(urg));

        try {
            getContentResolver().insert(GoalEntry.CONTENT_URI, contentValues);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), R.string.error_goal, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {GoalEntry._ID, GoalEntry.COLUMN_GOAL_NAME, GoalEntry.COLUMN_IMORTANT, GoalEntry.COLUMN_URGENT};
        return new CursorLoader(this,
                GoalEntry.CONTENT_URI,
                projection,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        swipeRefreshLayout.setRefreshing(false);
        mCursor=data;
        goalCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
       swipeRefreshLayout.setRefreshing(false);
        goalCursorAdapter.swapCursor(null);
    }

    @Override
    public void onClick(String goal_name, int goal_id) {
        //Toast.makeText(MainActivity.this,String.valueOf(goal_id), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SubGoals.class);
        intent.putExtra("goal_id", goal_id);
        intent.putExtra("goal_name", goal_name);
        startActivity(intent);
    }

    public void colorScheme(View view)
    {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        btColorScheme.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        //goalCursorAdapter.swapCursor(mCursor);
    }
}
