package com.example.cybersurvivaljava;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HighScoreAdapter extends RecyclerView.Adapter<HighScoreAdapter.ViewHolder> {
    private List<HighScoreEntry> scoreList;
    public HighScoreAdapter(List<HighScoreEntry> scoreList) {
        this.scoreList = scoreList;
    }


    @NonNull
    @Override
    public HighScoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View scoreView = inflater.inflate(R.layout.list_item_highscore, parent, false);
        return new ViewHolder(scoreView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HighScoreEntry currentEntry = scoreList.get(position);

        holder.userNameTextView.setText(currentEntry.getUserName());
        holder.accuracyTextView.setText(currentEntry.getTotalAccuracy() + "%");
        holder.speedTextView.setText(currentEntry.getTotalSpeed() + "s");
        holder.tasksCompletedTextView.setText(currentEntry.getTasksCompleted() + "/4");
    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView userNameTextView;
        public final TextView accuracyTextView;
        public final TextView speedTextView;
        public final TextView tasksCompletedTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            accuracyTextView = itemView.findViewById(R.id.accuracyTextView);
            speedTextView = itemView.findViewById(R.id.speedTextView);
            tasksCompletedTextView = itemView.findViewById(R.id.tasksCompletedTextView);
        }

    }
}