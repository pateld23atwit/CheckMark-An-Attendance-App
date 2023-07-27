package edu.wit.mobileapp.checkmark;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton addButton;
    ImageView menuLogo;
    RecyclerView recyclerView;
    courseAdapter courseAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<course_Item> course_items = new ArrayList<>();
    Toolbar toolbar;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(this);

        addButton = findViewById(R.id.addButtonXML);
        addButton.setOnClickListener(v -> showDialog());

        loadData();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        courseAdapter = new courseAdapter(this, course_items);
        recyclerView.setAdapter(courseAdapter);
        courseAdapter.setOnItemClickListener(position -> gotoItemActivity(position));
        setToolBar();


    }

    private void loadData() {
        Cursor cursor = databaseHelper.getCourseTable();

        course_items.clear();

        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(databaseHelper.COURSE_ID));
            @SuppressLint("Range") String courseName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COURSE_NAME_KEY));
            @SuppressLint("Range") String courseID = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COURSEID_KEY));

            course_items.add(new course_Item(id, courseName, courseID));
        }
    }

    private void setToolBar() {
        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.toolbarTitle);
        TextView subtitle = toolbar.findViewById(R.id.subtitletoolbar);
        ImageButton backbtn = toolbar.findViewById(R.id.icon_back);
        menuLogo = findViewById(R.id.toolbarLogo);
        ImageButton savebtn = toolbar.findViewById(R.id.icon_save);

        title.setVisibility(View.GONE);
        subtitle.setVisibility(View.GONE);
        backbtn.setVisibility(View.INVISIBLE);
        savebtn.setVisibility(View.INVISIBLE);
        menuLogo.setVisibility(View.VISIBLE);
    }

    private void gotoItemActivity(int position) {
        Intent intent = new Intent(this, student.class);
        intent.putExtra("courseName", course_items.get(position).getCourseName());
        intent.putExtra("courseID", course_items.get(position).getCourseID());
        intent.putExtra("position", position);
        intent.putExtra("course_ID", course_items.get(position).getCourse_ID());
        startActivity(intent);
    }

    private void showDialog() {
        myDiag diag = new myDiag();
        diag.show(getSupportFragmentManager(), myDiag.courseAddDialog);
        diag.setListener((courseName, courseID)-> addCourse(courseName, courseID));
    }

    private void addCourse(String courseName, String courseID) {

        for (course_Item course: course_items) {
            if (course.getCourseID().equals(courseID)) {
                String error = "Cannot add a duplicate course ID! Course not added!";
//                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                Snackbar mySnackbar = Snackbar.make(recyclerView, error, Snackbar.LENGTH_LONG);
                mySnackbar.show();
                return;
            }
        }

        long course_ID = databaseHelper.addCourse(courseName, courseID);
        course_Item course_item = new course_Item(course_ID, courseName, courseID);
//        course_items.add(new course_Item(courseName, courseID));
        course_items.add(course_item);
        courseAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                showUpdateDialog(item.getGroupId());
                break;
            case 1:
                deleteCourse(item.getGroupId());
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateDialog(int position) {
        String course_Name = course_items.get(position).getCourseName();
        String course_ID = course_items.get(position).getCourseID();

        myDiag dialog = new myDiag(course_Name, course_ID);
        dialog.show(getSupportFragmentManager(), myDiag.courseUpdateDialog);
        dialog.setListener((courseName, courseID) -> updateCourse(position, courseName, courseID));
    }

    private void updateCourse(int position, String courseName, String courseID) {
        databaseHelper.updateCourse(course_items.get(position).getCourse_ID(), courseName, courseID);
        course_items.get(position).setCourseName(courseName);
        course_items.get(position).setCourseID(courseID);
        courseAdapter.notifyItemChanged(position);
    }

    private void deleteCourse(int position) {
        databaseHelper.deleteCourse(course_items.get(position).getCourse_ID());
        course_items.remove(position);
        courseAdapter.notifyItemRemoved(position);
    }

}