package com.app.user.effy;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class FragmentAddGoalDialog extends DialogFragment {

    EditText editText_goal;
    public FragmentAddGoalDialog() {
        // Required empty public constructor
    }
    public static FragmentAddGoalDialog newInstance(String title)
    {
        FragmentAddGoalDialog fragment = new FragmentAddGoalDialog();
        Bundle args = new Bundle();
        args.putString("title",title);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onResume() {
        super.onResume();
        int dialogWidth = (int)(MainActivity.displayMetrics.widthPixels);
        int dialogHeight = (int)(MainActivity.displayMetrics.heightPixels*0.45);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_add_goal_dialog, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        editText_goal = (EditText) view.findViewById(R.id.editText_goal);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        //   mEditText.requestFocus();
        // getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }


}
