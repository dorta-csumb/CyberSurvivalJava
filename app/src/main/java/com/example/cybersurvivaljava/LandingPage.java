package com.example.cybersurvivaljava;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cybersurvivaljava.database.CyberSurvivalRepository;
import com.example.cybersurvivaljava.databinding.ActivityLandingPageBinding;

public class LandingPage extends AppCompatActivity {

    private static final String LANDING_PAGE_USER_ID = "com.example.cybersurvivaljava.LANDING_PAGE_USER_ID";
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

        updateSharedPreference();

        repository.getUserById(loggedInUserId).observe(this, user -> {
            if (user != null) {
                binding.landingNameTextView.setText(user.getUsername());
                if (user.isAdmin()) {
                    binding.adminButton.setVisibility(View.VISIBLE);
                } else {
                    binding.adminButton.setVisibility(View.GONE);
                }
            }
        });

        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        binding.adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AdminPanel.adminPanelIntentFactory(getApplicationContext(), loggedInUserId));
            }
        });
    }

    private void logoutUser() {
        loggedInUserId = LOGGED_OUT;
        updateSharedPreference();
        Intent intent = MainActivity.mainActivityIntentFactory(getApplicationContext());
        startActivity(intent);
        finish();
    }

    static Intent landingPageIntentFactory(Context context, int userId) {
        Intent intent = new Intent(context, LandingPage.class);
        intent.putExtra(LANDING_PAGE_USER_ID, userId);
        return intent;
    }

    private void updateSharedPreference() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putInt(getString(R.string.preference_userId_key), loggedInUserId);
        sharedPrefEditor.apply();
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
