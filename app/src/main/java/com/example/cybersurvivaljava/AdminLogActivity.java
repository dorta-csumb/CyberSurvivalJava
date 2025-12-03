package com.example.demoapplicationbuild;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminLogActivity extends AppCompatActivity {

    // ---- Temporary dummy player data (replace with real LiveData later) ----
    private String userName = "Agent Nova";
    private int totalAccuracy = 87;      // 0..100 (%)
    private long totalSpeed = 5234L;     // seconds
    private int tasksCompleted = 31;

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

        // Bind header
        textUserName = findViewById(R.id.text_user_name);
        textTotalAccuracy = findViewById(R.id.text_total_accuracy);
        textTotalSpeed = findViewById(R.id.text_total_speed);
        textTasksCompleted = findViewById(R.id.text_tasks_completed);
        buttonHighScores = findViewById(R.id.button_high_scores);

        // Render dummy values
        renderPlayerHeader();

        // Navigate to High Score screen (placeholder Activity)
        buttonHighScores.setOnClickListener(v ->
                startActivity(new Intent(this, HighScoreActivity.class))
        );

        // Recycler
        recyclerView = findViewById(R.id.recycler_admin_log);
        emptyView = findViewById(R.id.text_empty);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<AdminLogEntry> dummy = createDummyLogs();
        recyclerView.setAdapter(new AdminLogAdapter(dummy));
        toggleEmpty(dummy.isEmpty());
    }

    private void renderPlayerHeader() {
        textUserName.setText("Agent: " + userName);
        textTotalAccuracy.setText(String.format(Locale.US, "Accuracy: %d%%", totalAccuracy));

        // Show seconds and a friendlier duration
        String human = toHumanDuration(totalSpeed);
        textTotalSpeed.setText(String.format(Locale.US, "Speed: %,d s (%s)", totalSpeed, human));

        textTasksCompleted.setText(String.format(Locale.US, "Tasks: %d", tasksCompleted));
    }

    private String toHumanDuration(long seconds) {
        long h = seconds / 3600;
        long m = (seconds % 3600) / 60;
        long s = seconds % 60;
        if (h > 0) return String.format(Locale.US, "%dh %dm %ds", h, m, s);
        if (m > 0) return String.format(Locale.US, "%dm %ds", m, s);
        return String.format(Locale.US, "%ds", s);
    }

    private void toggleEmpty(boolean isEmpty) {
        emptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    private List<AdminLogEntry> createDummyLogs() {
        List<AdminLogEntry> list = new ArrayList<>();
        list.add(new AdminLogEntry("Player joined", "UID: 42 connected", System.currentTimeMillis() - 60_000));
        list.add(new AdminLogEntry("Item crafted", "EMP Grenade x1", System.currentTimeMillis() - 45_000));
        list.add(new AdminLogEntry("Zone entered", "Stealth Lab - Sector B", System.currentTimeMillis() - 30_000));
        list.add(new AdminLogEntry("Challenge completed", "Silent Bypass (Rank A)", System.currentTimeMillis() - 15_000));
        list.add(new AdminLogEntry("Player left", "UID: 42 disconnected", System.currentTimeMillis() - 5_000));
        return list;
    }

    // --- simple model ---
    static class AdminLogEntry {
        final String title;
        final String detail;
        final long timestamp;
        AdminLogEntry(String title, String detail, long timestamp) {
            this.title = title;
            this.detail = detail;
            this.timestamp = timestamp;
        }
    }

    // --- minimal adapter using android.R.layout.simple_list_item_2 ---
    static class AdminLogAdapter extends RecyclerView.Adapter<AdminLogAdapter.VH> {
        private final List<AdminLogEntry> items;
        private final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);
        AdminLogAdapter(List<AdminLogEntry> items) { this.items = items; }

        @NonNull @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_2, parent, false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull VH h, int pos) {
            AdminLogEntry e = items.get(pos);
            h.title.setText(e.title);
            h.subtitle.setText(e.detail + " • " + df.format(new Date(e.timestamp)));
        }

        @Override public int getItemCount() { return items.size(); }

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
