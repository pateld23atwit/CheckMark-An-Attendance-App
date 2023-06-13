package edu.wit.mobileapp.checkmark;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton addButton;
    RecyclerView recyclerView;
    courseAdapter courseAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<course_Item> course_items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = findViewById(R.id.addButtonXML);
        addButton.setOnClickListener(v -> showDialog());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        courseAdapter = new courseAdapter(this, course_items);
        recyclerView.setAdapter(courseAdapter);
        courseAdapter.setOnItemClickListener(position -> gotoItemActivity(position));
    }

    private void gotoItemActivity(int position) {
        Intent intent = new Intent(this, student.class);
        intent.putExtra("courseName", course_items.get(position).getCourseName());
        intent.putExtra("courseID", course_items.get(position).getCourseID());
        intent.putExtra("position", position);
        startActivity(intent);
    }

    private void showDialog() {
        myDiag diag = new myDiag();
        diag.show(getSupportFragmentManager(), myDiag.courseAddDialog);
        diag.setListener((courseName, courseID)->addCourse(courseName, courseID));
    }

    private void addCourse(String courseName, int courseID) {
        course_items.add(new course_Item(courseName, courseID));
        courseAdapter.notifyDataSetChanged();
    }
}