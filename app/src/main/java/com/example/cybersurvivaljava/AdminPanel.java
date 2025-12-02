package com.example.cybersurvivaljava;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.cybersurvivaljava.database.CyberSurvivalRepository;
import com.example.cybersurvivaljava.databinding.ActivityAdminPanelBinding;

public class AdminPanel extends AppCompatActivity {

    private static final String ADMIN_PANEL_USER_ID = "com.example.cybersurvivaljava.ADMIN_PANEL_USER_ID";
    private static final int LOGGED_OUT = -1;

    private ActivityAdminPanelBinding binding;
    private CyberSurvivalRepository repository;

    private int loggedInUserId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminPanelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = CyberSurvivalRepository.getRepository(getApplication());

        loggedInUserId = getIntent().getIntExtra(ADMIN_PANEL_USER_ID, LOGGED_OUT);

        repository.getUserById(loggedInUserId).observe(this, user -> {
            if (user != null) {
                binding.adminPanelNameTextView.setText(user.getUsername());
            }
        });

        // Listen for changes in the back stack
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                // A fragment is visible
                binding.adminButtonContainer.setVisibility(View.GONE);
                binding.fragmentContainerView.setVisibility(View.VISIBLE);
            } else {
                // No fragments are visible
                binding.adminButtonContainer.setVisibility(View.VISIBLE);
                binding.fragmentContainerView.setVisibility(View.GONE);
            }
        });

        binding.newAdminButton.setOnClickListener(v -> {
            // Just perform the transaction. The listener will handle the UI changes.
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, new NewAdmin())
                    .addToBackStack(null)
                    .commit();
        });

        binding.newQuestionButton.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, new NewQuestionFragment())
                    .addToBackStack(null)
                    .commit();
        });

        binding.backButton.setOnClickListener(v -> {
            finish();
        });
    }

    static Intent adminPanelIntentFactory(Context context, int userId) {
        Intent intent = new Intent(context, AdminPanel.class);
        intent.putExtra(ADMIN_PANEL_USER_ID, userId);
        return intent;
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}