package com.app.user.effy.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.app.user.effy.data.GoalContract.GoalEntry;
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

        db.execSQL(CREATE_TABLE);
    }


    /**
     * This method discards the old table of data and calls onCreate to recreate a new one.
     * This only occurs when the version number for this database (DATABASE_VERSION) is incremented.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GoalEntry.TABLE_NAME);
        onCreate(db);
    }
}