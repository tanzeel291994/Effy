package com.app.user.effy.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import static com.app.user.effy.data.GoalContract.GoalEntry.TABLE_NAME;
import static com.app.user.effy.data.GoalContract.SubGoalEntry.TABLE_NAME_SUB;

public class GoalContentProvider extends ContentProvider {

    public static final int GOALS = 100;
    public static final int SUB_GOALS = 200;
    public static final int GOALS_WITH_ID = 101;
    public static final int SUB_GOALS_WITH_ID = 201;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher()
    {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(GoalContract.AUTHORITY, GoalContract.PATH_GOALS, GOALS);
        uriMatcher.addURI(GoalContract.AUTHORITY, GoalContract.PATH_SUB_GOALS, SUB_GOALS);
        uriMatcher.addURI(GoalContract.AUTHORITY, GoalContract.PATH_GOALS + "/#", GOALS_WITH_ID);
        uriMatcher.addURI(GoalContract.AUTHORITY, GoalContract.PATH_SUB_GOALS + "/#", SUB_GOALS_WITH_ID);
        return uriMatcher;
    }
    private GoalDbHelper mGoalDbHelper;
    @Override
    public boolean onCreate() {
        Context context = getContext();
        mGoalDbHelper = new GoalDbHelper(context);
        return true;

    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mGoalDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor retCursor;
        switch (match) {
            case GOALS:
                retCursor =  db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case SUB_GOALS:
                retCursor =  db.query(TABLE_NAME_SUB,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mGoalDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case GOALS:
                long id = db.insert(TABLE_NAME, null, contentValues);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(GoalContract.GoalEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case SUB_GOALS:
                long id_sub = db.insert(TABLE_NAME_SUB, null, contentValues);
                if ( id_sub > 0 ) {
                    returnUri = ContentUris.withAppendedId(GoalContract.SubGoalEntry.CONTENT_URI, id_sub);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
