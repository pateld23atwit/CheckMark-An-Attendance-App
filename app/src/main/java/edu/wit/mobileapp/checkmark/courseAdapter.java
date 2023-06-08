package edu.wit.mobileapp.checkmark;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class courseAdapter extends RecyclerView.Adapter<courseAdapter.courseViewHolder> {
    public static class courseViewHolder extends RecyclerView.ViewHolder {

        public courseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public courseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false);
        return new courseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull courseViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
