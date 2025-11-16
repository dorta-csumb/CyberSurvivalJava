package com.example.cybersurvivaljava;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cybersurvivaljava.database.CyberSurvivalRepository;
import com.example.cybersurvivaljava.databinding.ActivityLandingPageBinding;

public class LandingPage extends AppCompatActivity {

    private static final String LANDING_PAGE_USER_ID = "com.example.cybersurvivaljava.LANDING_PAGE_USER_ID";
    private final String SAVED_INSTANCE_STATE_USERID_KEY = "com.example.cybersurvivaljava.SAVED_INSTANCE_STATE_USERID_KEY";
    private static final int LOGGED_OUT = -1;

    private ActivityLandingPageBinding binding;
    private CyberSurvivalRepository repository;

    private int loggedInUserId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLandingPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = CyberSurvivalRepository.getRepository(getApplication());

        loggedInUserId = getIntent().getIntExtra(LANDING_PAGE_USER_ID, LOGGED_OUT);

        repository.getUserById(loggedInUserId).observe(this, user -> {
            if (user != null) {
                binding.landingNameTextView.setText(user.getUsername());
            }
        });

        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    static Intent landingPageIntentFactory(Context context, int userId) {
        Intent intent = new Intent(context, LandingPage.class);
        intent.putExtra(LANDING_PAGE_USER_ID, userId);
        return intent;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_INSTANCE_STATE_USERID_KEY, loggedInUserId);
        updateSharedPreference();
    }

    private void updateSharedPreference() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key),
                                                                        Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putInt(getString(R.string.preference_userId_key), loggedInUserId);
        sharedPrefEditor.apply();
    }
}