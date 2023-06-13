package edu.wit.mobileapp.checkmark;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class myDiag extends DialogFragment {
    public static final String courseAddDialog = "addCourse";

    private onClickListener listener;

    public interface onClickListener {
        void onClick(String t1, int t2);
    }

    public void setListener(onClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = null;

        if (getTag().equals(courseAddDialog))dialog=getAddCourseDialog();
        return dialog;
    }

    private Dialog getAddCourseDialog() {
        Button cancel;
        Button add;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.course_dialog, null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDiag);
        title.setText("Add New Course");

        EditText course_edit = view.findViewById(R.id.courseNameEditDiag);
        EditText courseID_edit = view.findViewById(R.id.courseIDEditDiag);

        course_edit.setHint("Course Name");
        courseID_edit.setHint("Course ID#");

        cancel = view.findViewById(R.id.cancelAddCourseButton);
        add = view.findViewById(R.id.addCourseButton);

        cancel.setOnClickListener(v -> dismiss());
        add.setOnClickListener(v -> {
            String courseName = course_edit.getText().toString();
            int courseID = Integer.valueOf(courseID_edit.getText().toString());
            listener.onClick(courseName, courseID);
            dismiss();
        });
        return builder.create();
    }
}
