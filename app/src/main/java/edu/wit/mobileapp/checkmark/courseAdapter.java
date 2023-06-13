package edu.wit.mobileapp.checkmark;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class courseAdapter extends RecyclerView.Adapter<courseAdapter.courseViewHolder> {
    ArrayList<course_Item> courseItems;
    Context context;

    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;

    }

    public courseAdapter(Context context, ArrayList<course_Item> courseItems) {
        this.courseItems = courseItems;
        this.context = context;
    }

    public static class courseViewHolder extends RecyclerView.ViewHolder {
        TextView courseName;
        TextView courseID;

        public courseViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            courseName = itemView.findViewById(R.id.courseTextView);
            courseID = itemView.findViewById(R.id.courseIDTextView);
            itemView.setOnClickListener(v -> onItemClickListener.onClick(getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public courseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false);
        return new courseViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull courseViewHolder holder, int position) {
        holder.courseName.setText(courseItems.get(position).getCourseName());
        holder.courseID.setText(String.valueOf(courseItems.get(position).getCourseID()));
    }

    @Override
    public int getItemCount() {
        return courseItems.size();
    }
}
