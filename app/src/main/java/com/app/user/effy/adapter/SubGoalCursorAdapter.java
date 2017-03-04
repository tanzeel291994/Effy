package com.app.user.effy.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.app.user.effy.R;
import com.app.user.effy.data.GoalContract;
import java.util.ArrayList;


public class SubGoalCursorAdapter extends RecyclerView.Adapter<SubGoalCursorAdapter.GoalViewHolder> {
    private Cursor mCursor;
    private Context mContext;
    ArrayList<GoalModel> goal_list;
  /*  public interface OnItemClickListener {
        void onClick(String goal_name,int goal_id);
    }*/
public interface OnReminderBtnPressed
  {
      void onClick(String sub_goal_name);
  }
    private final OnReminderBtnPressed onReminderBtnPressed;
   // private final OnItemClickListener listener;

    @Override
    public SubGoalCursorAdapter.GoalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.sub_goal_item, parent, false);
        return new GoalViewHolder(view);
    }


    public SubGoalCursorAdapter(Context mContext,OnReminderBtnPressed onReminderBtnPressed) {
        this.mContext = mContext;
        this.onReminderBtnPressed=onReminderBtnPressed;
     //   this.listener = listener;
        //goal_list=new ArrayList<GoalModel>();

    }

    public String getIdByPosition(int position)
    {
        mCursor.moveToPosition(position);
        return mCursor.getString(mCursor.getColumnIndex(GoalContract.SubGoalEntry._ID));
    }
    @Override
    public void onBindViewHolder(SubGoalCursorAdapter.GoalViewHolder holder, int position) {
        int sub_goal_name_Index = mCursor.getColumnIndex(GoalContract.SubGoalEntry.COLUMN_SUB_GOAL_NAME);
        //int sub_goal_id = mCursor.getColumnIndex(GoalContract.SubGoalEntry._ID);


        mCursor.moveToPosition(position);
        final String sub_goal_name=mCursor.getString(sub_goal_name_Index);
        holder.sub_goal_name.setText(sub_goal_name);
        holder.sub_goal_id.setText(String.valueOf(position+1));
        holder.btn_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onReminderBtnPressed.onClick(sub_goal_name);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    class GoalViewHolder extends RecyclerView.ViewHolder  {


         TextView sub_goal_name;
         TextView sub_goal_id;
         Button btn_reminder;

        public GoalViewHolder(View itemView) {
            super(itemView);
            sub_goal_name=(TextView) itemView.findViewById(R.id.sub_goal_name);
            sub_goal_id=(TextView) itemView.findViewById(R.id.sub_goal_id);
            btn_reminder=(Button) itemView.findViewById(R.id.reminder);
            //  itemView.setOnClickListener(this);

        }
/*
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            //Log.i("tag", String.valueOf(adapterPosition));
            mCursor.moveToPosition(adapterPosition);
            listener.onClick(mCursor.getString(mCursor.getColumnIndex(GoalContract.GoalEntry.COLUMN_GOAL_NAME)),
                    mCursor.getInt(mCursor.getColumnIndex(GoalContract.GoalEntry._ID)));
        }
*/
    }
}
