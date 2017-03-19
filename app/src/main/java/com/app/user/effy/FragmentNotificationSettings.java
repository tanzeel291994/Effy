package com.app.user.effy;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.app.user.effy.App.Config;


public class FragmentNotificationSettings extends DialogFragment {

    TextView text_subject;
   Switch aSwitch;
   static Context mcontext;


    public FragmentNotificationSettings() {

    }



    public static FragmentNotificationSettings newInstance(String title, Context context) {
        FragmentNotificationSettings fragment = new FragmentNotificationSettings();
        Bundle args = new Bundle();
        args.putString("title", title);
        mcontext=context;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        int dialogWidth = (MainActivity.displayMetrics.widthPixels);
        int dialogHeight = (int) (MainActivity.displayMetrics.heightPixels * 0.30);
        if (getDialog().getWindow() != null)
            getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_notification_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle("Settings");
        // Get field from view
        text_subject = (TextView) view.findViewById(R.id.textViewSubject);
        aSwitch = (Switch) view.findViewById(R.id.switch1);
        SharedPreferences pref = mcontext.getSharedPreferences(Config.SHARED_PREF, 0);
        if(pref.contains("notification"))
            aSwitch.setChecked(pref.getBoolean("notification",true));
        else
            aSwitch.setChecked(true);
        if(aSwitch.isChecked())
            aSwitch.setText(R.string.on);
        else
            aSwitch.setText(R.string.off);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                SharedPreferences pref = mcontext.getSharedPreferences(Config.SHARED_PREF, 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("notification", isChecked);
                editor.apply();
                if(isChecked)
                {
                    aSwitch.setText(R.string.on);
                    MainActivity.startPeriodicTask();
                }
                else {
                    aSwitch.setText(R.string.off);
                    MainActivity.stopPeriodicTask();
                }

            }
        });


        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title");
       // getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        //   mEditText.requestFocus();
        // getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }


}
