package com.example.cybersurvivaljava;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

        binding.newAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide the button list
                binding.adminButtonContainer.setVisibility(View.GONE);
                // Show the fragment container
                binding.fragmentContainerView.setVisibility(View.VISIBLE);

                // Open the NewAdmin fragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_view, new NewAdmin())
                        .addToBackStack(null) // This is important for the back button
                        .commit();
            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LandingPage.landingPageIntentFactory(getApplicationContext(), loggedInUserId));
            }
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