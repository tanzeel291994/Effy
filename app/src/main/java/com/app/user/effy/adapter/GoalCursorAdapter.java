package com.app.user.effy.adapter;



import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.app.user.effy.R;
import com.app.user.effy.data.GoalContract;
import com.app.user.effy.data.GoalContract.GoalEntry;

import java.util.ArrayList;

public class GoalCursorAdapter extends RecyclerView.Adapter<GoalCursorAdapter.GoalViewHolder> {
    private Cursor mCursor;
    private Context mContext;
    ArrayList<GoalModel> goal_list;
    public interface OnItemClickListener {
        void onClick(String goal_name,int goal_id);
    }

    private final OnItemClickListener listener;

    @Override
    public GoalCursorAdapter.GoalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.goal_item, parent, false);
        return new GoalViewHolder(view);
    }


    public GoalCursorAdapter(Context mContext,OnItemClickListener listener) {
        this.mContext = mContext;
        this.listener = listener;
        //goal_list=new ArrayList<GoalModel>();

    }

    @Override
    public void onBindViewHolder(GoalCursorAdapter.GoalViewHolder holder, int position) {
        int goal_name_Index = mCursor.getColumnIndex(GoalEntry.COLUMN_GOAL_NAME);
        int imp_Index = mCursor.getColumnIndex(GoalEntry.COLUMN_IMORTANT);
        int urg_Index = mCursor.getColumnIndex(GoalEntry.COLUMN_URGENT);
        int goal_id_Index = mCursor.getColumnIndex(GoalEntry._ID);
        Log.i("tag", String.valueOf(goal_id_Index));



        mCursor.moveToPosition(position);
        Log.i("tAG", String.valueOf((mCursor.getInt(goal_id_Index))));
        /*goal_list.add(new GoalModel(mCursor.getString(goal_name_Index)
                , mCursor.getString(imp_Index),
                mCursor.getString(urg_Index)));*/
        //holder.bind(goal_list.get(position), listener);
        holder.goal_name.setText(mCursor.getString(goal_name_Index));
//        if(holder.goal_id==null)
       holder.goal_id.setText(String.valueOf(mCursor.getInt(goal_id_Index)));
        //holder.goal_id.setVisibility(View.INVISIBLE);
        holder.chk_imp.setChecked(Boolean.parseBoolean(mCursor.getString(imp_Index)));
        holder.chk_urg.setChecked(Boolean.parseBoolean(mCursor.getString(urg_Index)));

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public String getIdByPosition(int position)
    {
        mCursor.moveToPosition(position);
        return mCursor.getString(mCursor.getColumnIndex(GoalEntry._ID));
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

    class GoalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        CheckBox chk_imp;
        CheckBox chk_urg;
        TextView goal_name;
        TextView goal_id;

        public GoalViewHolder(View itemView) {
            super(itemView);
            chk_imp=(CheckBox)itemView.findViewById(R.id.chk_imp1);
            chk_urg=(CheckBox)itemView.findViewById(R.id.chk_urg1);
            goal_name=(TextView) itemView.findViewById(R.id.goal_name);
            goal_id=(TextView) itemView.findViewById(R.id.goal_id);
//            goal_id.setVisibility(View.INVISIBLE);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            //Log.i("tag", String.valueOf(adapterPosition));
            mCursor.moveToPosition(adapterPosition);
            listener.onClick(mCursor.getString(mCursor.getColumnIndex(GoalEntry.COLUMN_GOAL_NAME)),
                    mCursor.getInt(mCursor.getColumnIndex(GoalEntry._ID)));
        }
/*
        void bind(final GoalModel goal_item, final OnItemClickListener listener) {

            goal_name.setText(goal_item.goal_name);
            Log.i("tag",goal_item.goal_name);
            chk_imp.setChecked(Boolean.parseBoolean(goal_item.imp));
            //chk_imp.setChecked(true);
            chk_urg.setChecked(Boolean.parseBoolean(goal_item.urg));

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.onItemClick(goal_item);

                }

            });
        }*/
    }
}
