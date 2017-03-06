package com.app.user.effy.adapter;

import android.os.Parcel;
import android.os.Parcelable;

public class GoalModel implements Parcelable {
    public GoalModel(String goal_name, String imp, String urg, Integer goal_id) {
        this.goal_name = goal_name;
        this.urg = urg;
        this.imp = imp;
        this.goal_id = goal_id;
    }

    public String goal_name;
    private String imp;
    private String urg;
    private Integer goal_id;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(goal_name);
        out.writeString(imp);
        out.writeString(urg);

    }

    private GoalModel(Parcel in) {
        imp = in.readString();
        goal_name = in.readString();
        urg = in.readString();

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