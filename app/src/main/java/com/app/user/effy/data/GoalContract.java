package com.app.user.effy.data;


import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URI;

public class GoalContract {
    public static final String AUTHORITY = "com.app.user.effy";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_GOALS = "goals";
    public static final String PATH_SUB_GOALS = "sub_goals";

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
        public static Uri makeUriForStock(String goal_id) {
            return CONTENT_URI.buildUpon().appendPath(goal_id).build();
        }

    }
    public static final class SubGoalEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SUB_GOALS).build();

        // Task table and column names
        public static final String TABLE_NAME_SUB = "sub_goals";

        // Since TaskEntry implements the interface "BaseColumns", it has an automatically produced
        // "_ID" column in addition to the two below
        public static final String COLUMN_SUB_GOAL_NAME = "sub_goal_name";
        public static final String COLUMN_GOAL_ID = "goal_id";


    }
}
