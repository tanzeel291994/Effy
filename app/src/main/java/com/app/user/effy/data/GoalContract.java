package com.app.user.effy.data;


import android.net.Uri;
import android.provider.BaseColumns;

public class GoalContract {
    public static final String AUTHORITY = "com.app.user.effy";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_GOALS = "goals";

    public static final class GoalEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_GOALS).build();

        // Task table and column names
        public static final String TABLE_NAME = "goals";

        // Since TaskEntry implements the interface "BaseColumns", it has an automatically produced
        // "_ID" column in addition to the two below
        public static final String COLUMN_GOAL_NAME = "goal_name";
        public static final String COLUMN_IMORTANT = "important";
        public static final String COLUMN_URGENT= "urgent";


    }
}
