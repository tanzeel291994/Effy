package com.app.user.effy;


import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.app.user.effy.data.GoalContract;

public class FragmentAddSubDialog extends DialogFragment{

    EditText editText_goal;
    Button btn_add;
    CheckBox chk_imp;
    CheckBox chk_urg;
    CustomSubDialogInterface customDI;
    public FragmentAddSubDialog() {

    }
    public interface CustomSubDialogInterface  {
        public void addGoalClicked(String goal_name);
    }
    public static FragmentAddSubDialog newInstance(String title)
    {
        FragmentAddSubDialog fragment = new FragmentAddSubDialog();
        Bundle args = new Bundle();
        args.putString("title",title);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onResume() {
        super.onResume();
        int dialogWidth = (int)(MainActivity.displayMetrics.widthPixels);
        int dialogHeight = (int)(MainActivity.displayMetrics.heightPixels*0.35);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_add_sub_goal_dialog, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        editText_goal = (EditText) view.findViewById(R.id.editText_sub_goal);
        btn_add=(Button)view.findViewById(R.id.add_sub_goal);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomSubDialogInterface customDialogInterface=(CustomSubDialogInterface)getActivity();
                customDialogInterface.addGoalClicked(editText_goal.getText().toString());
                dismiss();

            }});
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        //   mEditText.requestFocus();
        // getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }


}
