package com.example.cybersurvivaladminpage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class AdminLogActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView emptyView;

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

        recyclerView = findViewById(R.id.recycler_admin_log);
        emptyView = findViewById(R.id.text_empty);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Dummy data for now — replace with LiveData from DB later
        List<AdminLogEntry> dummy = createDummyLogs();
        AdminLogAdapter adapter = new AdminLogAdapter(dummy);
        recyclerView.setAdapter(adapter);

        toggleEmpty(dummy.isEmpty());
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

    // --- Simple data model for now ---
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

    // --- Minimal adapter using android.R.layout.simple_list_item_2 ---
    static class AdminLogAdapter extends RecyclerView.Adapter<AdminLogAdapter.VH> {
        private final List<AdminLogEntry> items;
        private final DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);

        AdminLogAdapter(List<AdminLogEntry> items) {
            this.items = items;
        }

        @NonNull @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_2, parent, false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            AdminLogEntry e = items.get(position);
            String when = dateFormat.format(new Date(e.timestamp));
            holder.title.setText(e.title);
            holder.subtitle.setText(e.detail + " • " + when);
        }

        @Override
        public int getItemCount() { return items.size(); }

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
