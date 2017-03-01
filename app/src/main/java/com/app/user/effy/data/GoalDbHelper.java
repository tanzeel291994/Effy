package com.app.user.effy.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.app.user.effy.data.GoalContract.GoalEntry;
import com.app.user.effy.data.GoalContract.SubGoalEntry;
public class GoalDbHelper extends SQLiteOpenHelper
{
    // The name of the database
    private static final String DATABASE_NAME = "goalsDb.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 1;


    // Constructor
    GoalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    /**
     * Called when the tasks database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create tasks table (careful to follow SQL formatting rules)
        final String CREATE_TABLE = "CREATE TABLE "  + GoalEntry.TABLE_NAME + " (" +
                GoalEntry._ID                + " INTEGER PRIMARY KEY, " +
                GoalEntry.COLUMN_GOAL_NAME + " TEXT NOT NULL, " +
                GoalEntry.COLUMN_IMORTANT + " TEXT NOT NULL, " +
                GoalEntry.COLUMN_URGENT+ " TEXT NOT NULL);";
        // Create tasks table (careful to follow SQL formatting rules)
        final String CREATE_TABLE_SUB_GOALS = "CREATE TABLE "  + SubGoalEntry.TABLE_NAME_SUB + "(" +
                SubGoalEntry._ID                + " INTEGER PRIMARY KEY, " +
                SubGoalEntry.COLUMN_SUB_GOAL_NAME + " TEXT NOT NULL, " +
                SubGoalEntry.COLUMN_GOAL_ID + " INTEGER NOT NULL, "+
                "FOREIGN KEY("+SubGoalEntry.COLUMN_GOAL_ID+") REFERENCES " +GoalEntry.TABLE_NAME+"("+GoalEntry._ID+"));";

        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE_SUB_GOALS);
    }


    /**
     * This method discards the old table of data and calls onCreate to recreate a new one.
     * This only occurs when the version number for this database (DATABASE_VERSION) is incremented.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GoalEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SubGoalEntry.TABLE_NAME_SUB);
        onCreate(db);
    }
}