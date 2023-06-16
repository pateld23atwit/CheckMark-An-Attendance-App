package edu.wit.mobileapp.checkmark;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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
    public static final String courseUpdateDialog = "UpdateCourse";
    public static final String studentAddDialog = "addStudent";
    public static final String studentUpdateDialog = "updateStudent";

    private onClickListener listener;
    private onClickListener2 listener2;
    private String studentfName;
    private String studentlName;
    private String studentID;

    public myDiag() {
    }

    public myDiag(String firstName, String lastName, String studentID) {
        this.studentfName = firstName;
        this.studentlName = lastName;
        this.studentID = studentID;
    }
//    private onClickListener3 listener3;

    public interface onClickListener {
        void onClick(String t1, String t2);
    }
    public interface onClickListener2 {
        void onClick(String fName, String lName, String studentID);
    }
    public void setListener(onClickListener listener) {
        this.listener = listener;
    }
    public void setListener2(onClickListener2 listener) {
        this.listener2 = listener;
    }
//    public void setListener3(onClickListener3 listener) {
//        this.listener3 = listener;
//    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = null;

        if (getTag().equals(courseAddDialog))dialog=getAddCourseDialog();
        if (getTag().equals(studentAddDialog))dialog=getAddStudentDialog();
        if (getTag().equals(courseUpdateDialog))dialog=getUpdateCourseDialog();
        if (getTag().equals(studentUpdateDialog))dialog=getUpdateStudentDialog();

        return dialog;
    }

    private Dialog getUpdateStudentDialog() {
        Button cancelBtn;
        Button updateBtn;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.student_dialog, null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDiag);
        title.setText("Update Student");

        EditText fNameEditText = view.findViewById(R.id.studentfNameEditDiag);
        EditText lNameEditText = view.findViewById(R.id.studentlNameEditDiag);
        EditText IDEditText = view.findViewById(R.id.studentIDEditDiag);

        fNameEditText.setText(studentfName);
        lNameEditText.setText(studentlName);
        IDEditText.setText(studentID);

        cancelBtn = view.findViewById(R.id.cancelAddStudentButton);
        updateBtn = view.findViewById(R.id.addStudentButton);
        updateBtn.setText("Update");

        cancelBtn.setOnClickListener(v -> dismiss());

        updateBtn.setOnClickListener(v -> {
            String myApp = "TESTING!";
            Log.v(myApp, "Student update button clicked");

            String fName = fNameEditText.getText().toString();
            String lName = lNameEditText.getText().toString();
            String studentID = IDEditText.getText().toString();
            listener2.onClick(fName, lName, studentID);
            dismiss();
        });
        return builder.create();
    }

    private Dialog getUpdateCourseDialog() {
        Button cancelBtn;
        Button updateBtn;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.course_dialog, null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.coursetitleDiag);
        title.setText("Update Course");

        cancelBtn = view.findViewById(R.id.cancelAddCourseButton);
        updateBtn = view.findViewById(R.id.addCourseButton);
        updateBtn.setText("Update");

        EditText course_edit = view.findViewById(R.id.courseNameEditDiag);
        EditText courseID_edit = view.findViewById(R.id.courseIDEditDiag);

        course_edit.setHint("Course Name");
        courseID_edit.setHint("Course ID#");

        cancelBtn.setOnClickListener(v -> dismiss());
        updateBtn.setOnClickListener(v -> {
            String courseName = course_edit.getText().toString();
            String courseID = courseID_edit.getText().toString();
            listener.onClick(courseName, courseID);
            dismiss();
        });
        return builder.create();
    }

    private Dialog getAddCourseDialog() {
        Button cancel;
        Button add;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.course_dialog, null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.coursetitleDiag);
        title.setText("Add New Course");

        cancel = view.findViewById(R.id.cancelAddCourseButton);
        add = view.findViewById(R.id.addCourseButton);

        EditText course_edit = view.findViewById(R.id.courseNameEditDiag);
        EditText courseID_edit = view.findViewById(R.id.courseIDEditDiag);

        course_edit.setHint("Course Name");
        courseID_edit.setHint("Course ID#");

        cancel.setOnClickListener(v -> dismiss());
        add.setOnClickListener(v -> {
            String courseName = course_edit.getText().toString();
            String courseID = courseID_edit.getText().toString();

            listener.onClick(courseName, courseID);
//            listener.onClick(courseName, courseID);
            dismiss();
        });
        return builder.create();
    }

    private Dialog getAddStudentDialog() {
        Button cancel;
        Button add;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.student_dialog, null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDiag);
        title.setText("Add New Student");

        EditText fNameEditText = view.findViewById(R.id.studentfNameEditDiag);
        EditText lNameEditText = view.findViewById(R.id.studentlNameEditDiag);
        EditText IDEditText = view.findViewById(R.id.studentIDEditDiag);

        fNameEditText.setHint("First Name");
        lNameEditText.setHint("Last Name");
        IDEditText.setHint("W0000000");
        cancel = view.findViewById(R.id.cancelAddStudentButton);
        add = view.findViewById(R.id.addStudentButton);

        cancel.setOnClickListener(v -> dismiss());

        add.setOnClickListener(v -> {
            String myApp = "TESTING!";
            Log.v(myApp, "Student add button clicked");

            String fName = fNameEditText.getText().toString();
            String lName = lNameEditText.getText().toString();
            String studentID = IDEditText.getText().toString();
            listener2.onClick(fName, lName, studentID);
//            dismiss();
        });
        return builder.create();
    }
}
