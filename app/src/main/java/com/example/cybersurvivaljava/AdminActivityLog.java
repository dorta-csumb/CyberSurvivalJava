package com.example.cybersurvivaljava;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cybersurvivaljava.database.CyberSurvivalDatabase;
import com.example.cybersurvivaljava.database.ProblemsDAO;
import com.example.cybersurvivaljava.database.UserDAO;
import com.example.cybersurvivaljava.database.entities.Problems;
import com.example.cybersurvivaljava.database.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdminLogActivity extends AppCompatActivity {

    // Database / DAO references
    private CyberSurvivalDatabase database;
    private UserDAO userDao;
    private ProblemsDAO problemsDao;

    private RecyclerView recyclerView;
    private TextView emptyView;

    // Header views
    private TextView textUserName;
    private TextView textTotalAccuracy;
    private TextView textTotalSpeed;
    private TextView textTasksCompleted;
    private Button buttonHighScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_log);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Build Room database and DAO references
        database = Room.databaseBuilder(
                getApplicationContext(),
                CyberSurvivalDatabase.class,
                "CyberSurvivalDatabase"
        ).allowMainThreadQueries().build();
        userDao = database.userDAO();
        problemsDao = database.problemsDAO();

        // Bind header views
        textUserName = findViewById(R.id.text_user_name);
        textTotalAccuracy = findViewById(R.id.text_total_accuracy);
        textTotalSpeed = findViewById(R.id.text_total_speed);
        textTasksCompleted = findViewById(R.id.text_tasks_completed);
        buttonHighScores = findViewById(R.id.button_high_scores);

        // Navigate to High Score screen (placeholder Activity)
        buttonHighScores.setOnClickListener(v ->
                startActivity(new Intent(this, HighScoreAdapter.class))
        );

        // Recycler
        recyclerView = findViewById(R.id.recycler_admin_log);
        emptyView = findViewById(R.id.text_empty);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final AdminLogAdapter adapter = new AdminLogAdapter();
        recyclerView.setAdapter(adapter);
        toggleEmpty(true);

        // Determine which user to show in the header. Default to userId = 1
        int userId = getIntent().getIntExtra("userId", 1);
        LiveData<User> userLiveData = userDao.getUserById(userId);
        userLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    renderPlayerHeader(user);
                }
            }
        });

        // Populate the admin log list from the problems table
        problemsDao.getAllProblems().observe(this, new Observer<List<Problems>>() {
            @Override
            public void onChanged(List<Problems> problems) {
                List<AdminLogEntry> entries = new ArrayList<>();
                if (problems != null) {
                    for (Problems p : problems) {
                        String title = "Problem: " + p.getProblemName();
                        String detail = p.getProblemDescription();
                        entries.add(new AdminLogEntry(title, detail));
                    }
                }
                adapter.submitList(entries);
                toggleEmpty(entries.isEmpty());
            }
        });
    }

    private void renderPlayerHeader(@NonNull User user) {
        textUserName.setText("Agent: " + user.getUsername());

        int attempted = user.getUserProblemsAttempted();
        int solved = user.getUserProblemsSolved();

        int accuracy = 0;
        if (attempted > 0) {
            accuracy = (int) Math.round((solved * 100.0) / attempted);
        }
        textTotalAccuracy.setText(String.format(Locale.US, "Accuracy: %d%%", accuracy));

        // Show high-level progress instead of a fake "speed" metric
        textTotalSpeed.setText(String.format(Locale.US,
                "Problems: %d solved / %d attempted",
                solved,
                attempted));

        textTasksCompleted.setText(String.format(Locale.US, "Tasks: %d", solved));
    }

    private void toggleEmpty(boolean isEmpty) {
        emptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    // --- simple model ---
    static class AdminLogEntry {
        final String title;
        final String detail;

        AdminLogEntry(String title, String detail) {
            this.title = title;
            this.detail = detail;
        }
    }

    // --- minimal adapter using android.R.layout.simple_list_item_2 ---
    static class AdminLogAdapter extends RecyclerView.Adapter<AdminLogAdapter.VH> {
        private final List<AdminLogEntry> items = new ArrayList<>();

        AdminLogAdapter() {
        }

        void submitList(@NonNull List<AdminLogEntry> newItems) {
            items.clear();
            items.addAll(newItems);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_2, parent, false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull VH h, int pos) {
            AdminLogEntry e = items.get(pos);
            h.title.setText(e.title);
            h.subtitle.setText(e.detail);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class VH extends RecyclerView.ViewHolder {
            TextView title, subtitle;

            VH(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(android.R.id.text1);
                subtitle = itemView.findViewById(android.R.id.text2);
            }
        }
    }
}
