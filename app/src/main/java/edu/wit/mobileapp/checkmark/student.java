package edu.wit.mobileapp.checkmark;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class student extends AppCompatActivity {
    String myApp = "DUBUGGING!";
    Toolbar toolbar;
    private String courseName;
    private String courseID;
    private int position;
    private RecyclerView recyclerView;
    private studentAdapter studentAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<student_item> studentItems = new ArrayList<>();
    private DatabaseHelper databaseHelper;
    private long course_ID;
    private myCalendar calendar;
    private TextView subtitle;
    public static String barcodeID;
    FloatingActionButton scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        calendar = new myCalendar();
        databaseHelper = new DatabaseHelper(this);
        Intent intent = getIntent();
        courseName = intent.getStringExtra("courseName");
        courseID = String.valueOf(intent.getStringExtra("courseID"));
        position = intent.getIntExtra("position", -1);
        course_ID = intent.getLongExtra("course_ID", -1);

        setToolBar();
        loadData();
        recyclerView = findViewById(R.id.studentRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        studentAdapter = new studentAdapter(this, studentItems);
        recyclerView.setAdapter(studentAdapter);
        studentAdapter.setOnItemClickListener(position->changeStatus(position));
        loadStatusData();

        scanButton = findViewById(R.id.scanButtonXML);
        scanButton.setOnClickListener(v -> {
            String myApp = "TESTING!";
            Log.v(myApp, "Status scan button clicked");

            IntentIntegrator intentIntegrator = new IntentIntegrator(this);
            intentIntegrator.setBeepEnabled(true);
            intentIntegrator.setOrientationLocked(true);
            intentIntegrator.setCaptureActivity(Capture.class);
            intentIntegrator.initiateScan();
        });

    }

    private void loadData() {
        Cursor cursor = databaseHelper.getStudentTable(course_ID);
        studentItems.clear();

        while (cursor.moveToNext()) {
            long student_ID = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.STUDENT_ID));
            String studentID = cursor.getString(cursor.getColumnIndex(DatabaseHelper.STUDENTID_KEY));
            String studentFName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.STUDENT_FNAME_KEY));
            String studentLName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.STUDENT_LNAME_KEY));

            String studentNum = String.valueOf(studentItems.size()+1);;

            studentItems.add(new student_item(student_ID, studentNum, studentFName, studentLName, studentID));
        }
        cursor.close();
    }

    private void changeStatus(int position) {
        String status = studentItems.get(position).getstudentStatus();

        if (status.equals("P")) {
            status = "A";
        }
        else {
            status = "P";
        }

        studentItems.get(position).setStatus(status);
        studentAdapter.notifyItemChanged(position);
    }

    private void setToolBar() {
        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.toolbarTitle);
        subtitle = toolbar.findViewById(R.id.subtitletoolbar);

        ImageButton backbtn = toolbar.findViewById(R.id.icon_back);
        ImageButton savebtn = toolbar.findViewById(R.id.icon_save);
        savebtn.setOnClickListener(v->saveStatus());

        title.setText(courseName + " | " + courseID);
        subtitle.setText(calendar.getDate());

        backbtn.setOnClickListener(v->onBackPressed());
        toolbar.inflateMenu(R.menu.student_menu);
        toolbar.setOnMenuItemClickListener(menuItem->onMenuItemClick(menuItem));
    }

    private void saveStatus() {
        Button cancel;
        Button confirm;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View view = LayoutInflater.from(this).inflate(R.layout.savestatus_dialog, null);
        builder.setView(view);

        cancel = view.findViewById(R.id.cancelSaveStatus);
        confirm = view.findViewById(R.id.SaveStatus);

        AlertDialog dialog = builder.create();
        dialog.show();

        cancel.setOnClickListener(v -> dialog.dismiss());

        confirm.setOnClickListener(v -> {
            for (student_item student : studentItems) {
                String status = student.getstudentStatus();

                //            Log.i("Attempt Save Status", "Status Value Before-1 = " + status);

                if (!status.equals("P")) {
                    status = "A";
                    student.setStatus(status);
                    studentAdapter.notifyItemChanged(studentAdapter.studentItems.indexOf(student));
                }
                long value = databaseHelper.addStatus(student.getStudent_ID(), course_ID, calendar.getDate(), status);

                //            Log.i("Attempt Save Status", "Status Value After-1 = " + status);

                //            Log.i("Attempt Save Status", "Long value = : " + value);
                //            Log.i("Attempt Save Status", "Status Error = : " + value);

                if (value == -1) {
                    //                Log.i("Attempt Save Status", "Status Value Before-2 = " + status);
                    databaseHelper.updateStatus(student.getStudent_ID(), calendar.getDate(), status);
                }
                //            databaseHelper.updateStatus(student.getStudent_ID(), calendar.getDate(), status);
            }

            dialog.dismiss();

            Toast toast = Toast.makeText(this, "Attendance Saved.", Toast.LENGTH_SHORT);
            toast.show();
        });


//        for (student_item student : studentItems) {
//            String status = student.getstudentStatus();
//
////            Log.i("Attempt Save Status", "Status Value Before-1 = " + status);
//
//            if (!status.equals("P")) {
//                status = "A";
//                student.setStatus(status);
//                studentAdapter.notifyItemChanged(studentAdapter.studentItems.indexOf(student));
//            }
//            long value = databaseHelper.addStatus(student.getStudent_ID(), course_ID, calendar.getDate(), status);
//
////            Log.i("Attempt Save Status", "Status Value After-1 = " + status);
//
////            Log.i("Attempt Save Status", "Long value = : " + value);
////            Log.i("Attempt Save Status", "Status Error = : " + value);
//
//            if (value == -1) {
////                Log.i("Attempt Save Status", "Status Value Before-2 = " + status);
//                databaseHelper.updateStatus(student.getStudent_ID(), calendar.getDate(), status);
//            }
////            databaseHelper.updateStatus(student.getStudent_ID(), calendar.getDate(), status);
//        }
    }

    private void loadStatusData() {
        for (student_item student : studentItems) {
            String status = databaseHelper.getStatus(student.getStudent_ID(), calendar.getDate());

            if (status != null) {
                student.setStatus((status));
            }
            else {
                student.setStatus((""));
            }
        }
        studentAdapter.notifyDataSetChanged();
    }

    private boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.add_student) {
            showAddStudentDialog();
        }
        else if (menuItem.getItemId() == R.id.view_calendar) {
            showCalender();
        }
        else if (menuItem.getItemId() == R.id.view_attendance_sheet) {
            openSheetList();
        }
        return true;
    }

    private void openSheetList() {

        long[] idArr = new long[studentItems.size()];
        String[] numArr = new String[studentItems.size()];
        String[] fNameArr = new String[studentItems.size()];
        String[] lNameArr = new String[studentItems.size()];
        String[] studentIDArr = new String[studentItems.size()];

        for (int x = 0; x < idArr.length; x++) {
            idArr[x] = studentItems.get(x).getStudent_ID();
        }
        for (int x = 0; x < numArr.length; x++) {
            numArr[x] = studentItems.get(x).getNum();
        }
        for (int x = 0; x < fNameArr.length; x++) {
            fNameArr[x] = studentItems.get(x).getFirstName();
        }
        for (int x = 0; x < lNameArr.length; x++) {
            lNameArr[x] = studentItems.get(x).getLastName();
        }
        for (int x = 0; x < studentIDArr.length; x++) {
            studentIDArr[x] = studentItems.get(x).getStudentID();
        }

        Intent intent = new Intent(this, sheetListActivity.class);
        intent.putExtra("courseName", courseName);
        intent.putExtra("courseID", courseID);
        intent.putExtra("course_ID", course_ID);
        intent.putExtra("idArr", idArr);
        intent.putExtra("numArr", numArr);
        intent.putExtra("fNameArr", fNameArr);
        intent.putExtra("lNameArr", lNameArr);
        intent.putExtra("studentIDArr", studentIDArr);
        startActivity(intent);
    }

    private void showCalender() {
        myCalendar calender = new myCalendar();
        calender.show(getSupportFragmentManager(), "View Calendar");
        calender.setOnCalendarOkClickListener(this::onCalendarOkClicked);
    }

    private void onCalendarOkClicked(int year, int month, int day) {
        calendar.setDate(year, month, day);
        subtitle.setText(calendar.getDate());
        loadStatusData();
    }

    private void showAddStudentDialog() {
        Log.v(myApp, "Student add dialog.");
        myDiag diag = new myDiag();
        diag.show(getSupportFragmentManager(), myDiag.studentAddDialog);
        diag.setListener2((studentfName, studentlName, studentID)->addStudent(studentfName, studentlName, studentID));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        String myApp = "TESTING!";
        Log.v(myApp, "Barcode onActivityResult");

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult.getContents() != null) {
            barcodeID = intentResult.getContents();

            for (student_item student : studentItems) {
                String id = student.getStudentID();
                Log.v(myApp, "TEMPORARY student ID: " + id);

                if (id.equalsIgnoreCase(barcodeID)) {
                    Log.v(myApp, "Changing student status.");
                    int position = studentItems.indexOf(student);
//                    changeStatus(studentItems.indexOf(student));
                    student.setStatus("P"); 
                    studentAdapter.notifyItemChanged(position);
                    return;
                }
            }

            Log.v(myApp, barcodeID);

        } else {
            barcodeID = "Scan Error";
        }
        Log.v(myApp, "Barcode scanned: " + barcodeID);
    }

    public static String getScannedID() {
        if (barcodeID.equals("Scan Error")) {
            return "Scan Error";
        }
        else {
            return barcodeID;
        }
    }

    private void addStudent(String studentfName, String studentlName, String studentID) {
        Log.v(myApp, "Adding Student");

        for (student_item student: studentItems) {
            if (student.getStudentID().equals(studentID)) {
                String error = "Cannot add a duplicate student ID! Student not added!";
                Snackbar mySnackbar = Snackbar.make(recyclerView, error, Snackbar.LENGTH_LONG);
                mySnackbar.show();
                return;
            }
        }

        long student_ID = databaseHelper.addStudent(course_ID, studentID, studentfName, studentlName);
        String number = String.valueOf(studentItems.size()+1);;

        student_item studentItem = new student_item(student_ID, number, studentfName, studentlName, studentID);
        studentItems.add(studentItem);
        studentAdapter.notifyDataSetChanged();

        Snackbar mySnackbar = Snackbar.make(recyclerView, studentfName + " " + studentlName + " added.", Snackbar.LENGTH_LONG);
        mySnackbar.show();
        Log.v(myApp, "Student Added");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                showUpdateStudentDialog(item.getGroupId());
                break;
            case 1:
                deleteStudent(item.getGroupId());
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateStudentDialog(int position) {
        myDiag diag = new myDiag(studentItems.get(position).getFirstName(), studentItems.get(position).getLastName(), studentItems.get(position).getStudentID());
        diag.show(getSupportFragmentManager(), myDiag.studentUpdateDialog);
        diag.setListener2((studentfName, studentlName, studentID)->updateStudent(position, studentfName, studentlName, studentID));
    }

    private void updateStudent(int position, String studentfName, String studentlName, String studentID) {
        databaseHelper.updateStudent(studentItems.get(position).getStudent_ID(), studentfName, studentlName, studentID);
        studentItems.get(position).setFirstName(studentfName);
        studentItems.get(position).setLastName(studentlName);
        studentItems.get(position).setID(studentID);
        studentAdapter.notifyItemChanged(position);
    }

    private void deleteStudent(int position) {
        databaseHelper.deleteStudent(studentItems.get(position).getStudent_ID());
        studentItems.remove(position);
        studentAdapter.notifyItemRemoved(position);
    }
}