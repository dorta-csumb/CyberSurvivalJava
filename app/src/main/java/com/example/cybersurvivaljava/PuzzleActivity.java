package com.example.cybersurvivaljava;

import android.media.AudioAttributes;
import android.media.SoundPool;
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

    // Timer
    private final Handler timerHandler = new Handler(Looper.getMainLooper());
    private long startMs = 0L;
    private boolean timerRunning = false;

    // UI
    private TextView tvTimer, tvChances;
    private Button btnA, btnB, btnC, btnD;

    // Sound effect
    private SoundPool soundPool;
    private int soundClick;


    private int chances = 4;
    private boolean answered = false;
    private final char correct = 'B';


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        // Find views
        tvTimer = findViewById(R.id.tvTimer);
        tvChances = findViewById(R.id.tvChances);
        TextView tvQuestion = findViewById(R.id.tvQuestion);
        btnA = findViewById(R.id.btnA);
        btnB = findViewById(R.id.btnB);
        btnC = findViewById(R.id.btnC);
        btnD = findViewById(R.id.btnD);

        // Temporary stub question (replace later with DB)
        final String prompt = "You find a locked terminal. Which command lists files in the current directory?";
        final String optA_text = "A) cd ..";
        final String optB_text = "B) ls";
        final String optC_text = "C) mkdir";
        final String optD_text = "D) rm -rf /";


        // Seed UI
        tvQuestion.setText(prompt);
        btnA.setText(optA_text);
        btnB.setText(optB_text);
        btnC.setText(optC_text);
        btnD.setText(optD_text);
        updateChances();

        // SoundPool setup
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(4)
                .setAudioAttributes(audioAttributes)
                .build();

        // res/raw/clickclack.mp3
        soundClick = soundPool.load(this, R.raw.clickclack, 1);

        // Click listeners with SFX
        btnA.setOnClickListener(v -> {
            playClick();
            handleAnswer('A', v);
        });
        btnB.setOnClickListener(v -> {
            playClick();
            handleAnswer('B', v);
        });
        btnC.setOnClickListener(v -> {
            playClick();
            handleAnswer('C', v);
        });
        btnD.setOnClickListener(v -> {
            playClick();
            handleAnswer('D', v);
        });
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

    // Stopwatch tick runnable
    private final Runnable tick = new Runnable() {
        @Override
        public void run() {
            long elapsed = System.currentTimeMillis() - startMs;
            int secs = (int) (elapsed / 1000);
            int mins = secs / 60;
            secs = secs % 60;

            tvTimer.setText(String.format(Locale.US, "Time: %02d:%02d", mins, secs));

            if (timerRunning) {
                timerHandler.postDelayed(this, 1000);
            }
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

    private void playClick() {
        if (soundPool != null) {
            soundPool.play(soundClick, 1f, 1f, 0, 0, 1f);
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
}
