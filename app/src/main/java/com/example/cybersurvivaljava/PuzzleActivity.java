package com.example.cybersurvivaljava;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cybersurvivaljava.database.CyberSurvivalRepository;
import com.example.cybersurvivaljava.database.entities.Problems;
import com.example.cybersurvivaljava.database.entities.UserProblems;
import com.example.cybersurvivaljava.databinding.ActivityPuzzleBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class PuzzleActivity extends AppCompatActivity {
    // Data
    private CyberSurvivalRepository repository;
    private Problems currentProblem;
    private int currentUserId;

    // View Binding
    private ActivityPuzzleBinding binding;

    private static final String EXTRA_CATEGORY_ID = "com.example.cybersurvivaljava.CATEGORY_ID";
    private static final String EXTRA_USER_ID = "com.example.cybersurvivaljava.USER_ID";

    // Timer
    private final Handler timerHandler = new Handler(Looper.getMainLooper());
    private long startMs = 0L;
    private boolean timerRunning = false;

    // Sound effect
    private SoundPool soundPool;
    private int soundClick;

    private int chances = 4;
    private boolean answered = false;

    public static Intent puzzleIntentFactory(Context context, int categoryId, int userId) {
        Intent intent = new Intent(context, PuzzleActivity.class);
        intent.putExtra(EXTRA_CATEGORY_ID, categoryId);
        intent.putExtra(EXTRA_USER_ID, userId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPuzzleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = CyberSurvivalRepository.getRepository(getApplication());
        int categoryId = getIntent().getIntExtra(EXTRA_CATEGORY_ID, 1);
        currentUserId = getIntent().getIntExtra(EXTRA_USER_ID, -1);

        repository.getProblemsByCategory(categoryId).observe(this, problems -> {
            if (problems != null && !problems.isEmpty()) {
                currentProblem = problems.get(new Random().nextInt(problems.size()));
                loadProblemUI(currentProblem);
            } else {
                Toast.makeText(this, "No problems found for this category.", Toast.LENGTH_LONG).show();
                finish();
            }
        });

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
            handleAnswer(v);
        });
        binding.btnB.setOnClickListener(v -> {
            playClick();
            handleAnswer(v);
        });
        binding.btnC.setOnClickListener(v -> {
            playClick();
            handleAnswer(v);
        });
        binding.btnD.setOnClickListener(v -> {
            playClick();
            handleAnswer(v);
        });
    }

    private void loadProblemUI(Problems p) {
        binding.tvQuestion.setText(p.getProblemDescription());
        List<String> answers = new ArrayList<>();
        answers.add(p.getProblemSolution());
        answers.add(p.getWrongSolution1());
        answers.add(p.getWrongSolution2());
        answers.add(p.getWrongSolution3());
        Collections.shuffle(answers);
        binding.btnA.setText(answers.get(0));
        binding.btnB.setText(answers.get(1));
        binding.btnC.setText(answers.get(2));
        binding.btnD.setText(answers.get(3));
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

    private void handleAnswer(View v) {
        if (answered || currentProblem == null) return;

        boolean isCorrect = ((Button)v).getText().toString().equals(currentProblem.getProblemSolution());
        if (isCorrect) {
            answered = true;
            disableAll();
            UserProblems userProblem = new UserProblems(currentUserId, currentProblem.getProblemId(), true);
            repository.insertUserProblem(userProblem);
            Toast.makeText(this, "Correct! ✅", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            UserProblems userProblem = new UserProblems(currentUserId, currentProblem.getProblemId(), false);
            repository.insertUserProblem(userProblem);
            chances--;
            updateChances();
            v.setEnabled(false);
            Toast.makeText(this, "Try again.", Toast.LENGTH_SHORT).show();

            if (chances <= 0) {
                answered = true;
                disableAll();
                Toast.makeText(this, "Out of chances. ❌", Toast.LENGTH_SHORT).show();
                finish();
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
