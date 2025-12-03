package com.example.cybersurvivaljava;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cybersurvivaljava.database.CyberSurvivalRepository;
import com.example.cybersurvivaljava.database.entities.Problems;
import com.example.cybersurvivaljava.databinding.ActivityPuzzleBinding;

import java.util.Locale;

public class PuzzleActivity extends AppCompatActivity {
    // Data
    private CyberSurvivalRepository repository;
    private Problems currentProblem;

    // View Binding
    private ActivityPuzzleBinding binding;

    private static final String EXTRA_CATEGORY_ID = "com.example.cybersurvivaljava.CATEGORY_ID";

    // Timer
    private final Handler timerHandler = new Handler(Looper.getMainLooper());
    private long startMs = 0L;
    private boolean timerRunning = false;

    // Sound effect
    private SoundPool soundPool;
    private int soundClick;

    private int chances = 4;
    private boolean answered = false;
    private final char correct = 'B';

    public static Intent puzzleIntentFactory(Context context, int categoryId) {
        Intent intent = new Intent(context, PuzzleActivity.class);
        intent.putExtra(EXTRA_CATEGORY_ID, categoryId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPuzzleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 🚫 No repository / DB usage for now
        // repository = CyberSurvivalRepository.getInstance(getApplication());
        // int category = 1;
        // currentProblem = repository.getRandomProblemByCategory(category);

        // ✅ Hard-coded stub question
        final String prompt = "You find a locked terminal. Which command lists files in the current directory?";
        final String optA_text = "A) cd ..";
        final String optB_text = "B) ls";
        final String optC_text = "C) mkdir";
        final String optD_text = "D) rm -rf /";

        binding.tvQuestion.setText(prompt);
        binding.btnA.setText(optA_text);
        binding.btnB.setText(optB_text);
        binding.btnC.setText(optC_text);
        binding.btnD.setText(optD_text);

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
        binding.btnA.setOnClickListener(v -> {
            playClick();
            handleAnswer('A', v);
        });
        binding.btnB.setOnClickListener(v -> {
            playClick();
            handleAnswer('B', v);
        });
        binding.btnC.setOnClickListener(v -> {
            playClick();
            handleAnswer('C', v);
        });
        binding.btnD.setOnClickListener(v -> {
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

            binding.tvTimer.setText(String.format(Locale.US, "Time: %02d:%02d", mins, secs));

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
        binding.tvChances.setText("Chances Remaining: " + chances);
    }

    private void disableAll() {
        binding.btnA.setEnabled(false);
        binding.btnB.setEnabled(false);
        binding.btnC.setEnabled(false);
        binding.btnD.setEnabled(false);
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
