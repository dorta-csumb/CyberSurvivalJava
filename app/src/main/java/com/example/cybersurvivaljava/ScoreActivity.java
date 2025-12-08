package com.example.cybersurvivaljava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import com.example.cybersurvivaljava.databinding.ActivityScoreBinding;

import java.util.ArrayList;
import java.util.List;

public class ScoreActivity extends AppCompatActivity {

    // 1. Declare a variable for the auto-generated binding class.
    private ActivityScoreBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 2. Inflate the layout using the binding class. This creates all the views.
        binding = ActivityScoreBinding.inflate(getLayoutInflater());

        // 3. Set the root of the binding as the content view for this activity.
        setContentView(binding.getRoot());

        // 4. Initialize Repository (The connection to the database)
        com.example.cybersurvivaljava.database.CyberSurvivalRepository repository =
                com.example.cybersurvivaljava.database.CyberSurvivalRepository.getRepository(getApplication());

        // 5. Setup RecyclerView with an empty list initially
        // This prevents the screen from being blank/crashing while data loads
        HighScoreAdapter adapter = new HighScoreAdapter(new ArrayList<>());
        binding.highScoreRecyclerView.setAdapter(adapter);
        binding.highScoreRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Wire up Back Button
        binding.backButton.setOnClickListener(v -> {
            finish(); // Closes this activity and returns to the previous one
        });

        // 6. Observe Real Data from Database
        // This code runs automatically whenever the database changes!
        repository.getHighScores().observe(this, highScores -> {
            if (highScores != null) {
                // Create a new adapter with the REAL data and set it
                HighScoreAdapter newAdapter = new HighScoreAdapter(highScores);
                binding.highScoreRecyclerView.setAdapter(newAdapter);
            }
        });
    }
}