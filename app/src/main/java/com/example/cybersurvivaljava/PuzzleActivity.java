package com.example.cybersurvivaljava;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class PuzzleActivity extends AppCompatActivity {

    // Stopwatch
    private final Handler timerHandler = new Handler(Looper.getMainLooper());
    private long startMs = 0L;
    private boolean timerRunning = false;

    private TextView tvTimer, tvChances, tvQuestion;
    private Button btnA, btnB, btnC, btnD;

    // Temporary stub (swap for Room later)
    private String prompt = "You find a locked terminal. Which command lists files in the current directory?";
    private String optA = "A) cd ..";
    private String optB = "B) ls";
    private String optC = "C) mkdir";
    private String optD = "D) rm -rf /";
    private char correct = 'B';

    private int chances = 4;
    private boolean answered = false;

    public static Intent newIntent(Context ctx) {
        return new Intent(ctx, PuzzleActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        tvTimer = findViewById(R.id.tvTimer);
        tvChances = findViewById(R.id.tvChances);
        tvQuestion = findViewById(R.id.tvQuestion);
        btnA = findViewById(R.id.btnA);
        btnB = findViewById(R.id.btnB);
        btnC = findViewById(R.id.btnC);
        btnD = findViewById(R.id.btnD);

        // Seed UI
        tvQuestion.setText(prompt);
        btnA.setText(optA);
        btnB.setText(optB);
        btnC.setText(optC);
        btnD.setText(optD);
        updateChances();

        // Click listeners
        btnA.setOnClickListener(v -> handleAnswer('A', v));
        btnB.setOnClickListener(v -> handleAnswer('B', v));
        btnC.setOnClickListener(v -> handleAnswer('C', v));
        btnD.setOnClickListener(v -> handleAnswer('D', v));
    }

    @Override
    protected void onResume() {
        super.onResume();
        startStopwatch();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopStopwatch();
    }

    // Stopwatch tick
    private final Runnable tick = new Runnable() {
        @Override public void run() {
            long elapsed = System.currentTimeMillis() - startMs;
            int secs = (int) (elapsed / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            tvTimer.setText(String.format(Locale.US, "Time: %02d:%02d", mins, secs));
            if (timerRunning) timerHandler.postDelayed(this, 1000);
        }
    };

    private void startStopwatch() {
        if (timerRunning) return;
        startMs = System.currentTimeMillis();
        timerRunning = true;
        timerHandler.post(tick);
    }

    private void stopStopwatch() {
        timerRunning = false;
        timerHandler.removeCallbacks(tick);
    }

    private void handleAnswer(char selected, View v) {
        if (answered) return;

        boolean isCorrect = (selected == correct);
        if (isCorrect) {
            answered = true;
            disableAll();
            Toast.makeText(this, "Correct! ✅", Toast.LENGTH_SHORT).show();
            // TODO: log success and navigate
        } else {
            chances--;
            updateChances();
            v.setEnabled(false);
            Toast.makeText(this, "Try again.", Toast.LENGTH_SHORT).show();

            if (chances <= 0) {
                answered = true;
                disableAll();
                Toast.makeText(this, "Out of chances. ❌", Toast.LENGTH_SHORT).show();
                // TODO: log failure and navigate
            }
        }
    }

    private void updateChances() {
        tvChances.setText("Chances Remaining: " + chances);
    }

    private void disableAll() {
        btnA.setEnabled(false);
        btnB.setEnabled(false);
        btnC.setEnabled(false);
        btnD.setEnabled(false);
        stopStopwatch();
    }
}
