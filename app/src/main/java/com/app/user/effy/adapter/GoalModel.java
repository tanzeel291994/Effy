package com.app.user.effy.adapter;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class GoalModel implements Parcelable
{
    public GoalModel(String goal_name,String imp,String urg)
    {
        this.goal_name=goal_name;
        this.urg=urg;
        this.imp=imp;
    }

    public String goal_name;
    public String imp;
    public String urg;



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(goal_name);
        out.writeString(imp.toString());
        out.writeString(urg.toString());

    }
    private GoalModel(Parcel in) {
        //imp = in.readString();
        goal_name = in.readString();
       // urg = in.readString();

    }

    public static final Parcelable.Creator<GoalModel> CREATOR = new Parcelable.Creator<GoalModel>() {
        public GoalModel createFromParcel(Parcel in) {
            return new GoalModel(in);
        }

        public GoalModel[] newArray(int size) {
            return new GoalModel[size];
        }
    };
}