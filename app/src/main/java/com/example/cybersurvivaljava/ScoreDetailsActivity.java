package com.example.cybersurvivaljava;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cybersurvivaljava.databinding.ActivityScoreDetailsBinding;

public class ScoreDetailsActivity extends AppCompatActivity {

    private ActivityScoreDetailsBinding binding;


    private static final String EXTRA_NAME = "com.example.cybersurvival.EXTRA_NAME";
    private static final String EXTRA_ACCURACY = "com.example.cybersurvival.EXTRA_ACCURACY";
    private static final String EXTRA_SPEED = "com.example.cybersurvival.EXTRA_SPEED";
    private static final String EXTRA_TASKS = "com.example.cybersurvival.EXTRA_TASKS";

    // Intent factory
    // allows other activities to start this one cleanly
    public static Intent getIntent(Context context, String name, int accuracy, long speed, int tasks) {
        Intent intent = new Intent(context, ScoreDetailsActivity.class);
        intent.putExtra(EXTRA_NAME, name);
        intent.putExtra(EXTRA_ACCURACY, accuracy);
        intent.putExtra(EXTRA_SPEED, speed);
        intent.putExtra(EXTRA_TASKS, tasks);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScoreDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Wire up back button
        binding.detailsBackButton.setOnClickListener(v -> finish());

        // unpack Data
        String name = getIntent().getStringExtra(EXTRA_NAME);
        int accuracy = getIntent().getIntExtra(EXTRA_ACCURACY, 0);
        long speed = getIntent().getLongExtra(EXTRA_SPEED, 0);
        int tasks = getIntent().getIntExtra(EXTRA_TASKS, 0);

        // Set basic texts
        binding.detailName.setText(name);
        binding.detailStats.setText(
                "ACCURACY: " + accuracy + "%\n" +
                        "AVG SPEED: " + speed + "s\n" +
                        "TASKS SOLVED: " + tasks
        );

        // Determine archetype and description
        String archetype = "Cadet";
        String description = "Keep training to improve your skills.";

        if (accuracy >= 80) {
            if (speed < 30 && speed > 0) { // Fast and accurate
                archetype = "Elite Netrunner";
                description = "Lethal speed and precision. You are a ghost in the machine.";
            } else { // Accurate but methodical
                archetype = "System Architect";
                description = "Methodical and error-free. You build the world.";
            }
        } else if (speed < 20 && speed > 0) { // Fast but sloppy
            archetype = "Brute Force Specialist";
            description = "You break things fast. Effective, but noisy.";
        } else if (tasks > 5) {
            archetype = "Veteran Survivor";
            description = "You have seen it all and survived.";
        }

        binding.detailArchetype.setText(archetype);
        binding.detailDescription.setText(description);
    }
}