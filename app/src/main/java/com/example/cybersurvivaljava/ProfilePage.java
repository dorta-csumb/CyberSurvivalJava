package com.example.cybersurvivaljava;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfilePage extends AppCompatActivity {

    private TextView profileUsernameTextView;
    private TextView profileEmailTextView;
    private TextView profileRoleTextView;
    private TextView profileJoinedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);

        // Get data from intent (sent from LandingPage)
        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");
        if (username == null) {
            username = "Unknown User";
        }

        // Bind views
        profileUsernameTextView = findViewById(R.id.profileUsernameTextView);
        profileEmailTextView = findViewById(R.id.profileEmailTextView);
        profileRoleTextView = findViewById(R.id.profileRoleTextView);
        profileJoinedTextView = findViewById(R.id.profileJoinedTextView);

        // Set values (you can replace hard-coded values with real data)
        profileUsernameTextView.setText(username);
        profileEmailTextView.setText(username.toLowerCase() + "@example.com");
        profileRoleTextView.setText("Student");
        profileJoinedTextView.setText("Member since: 2025");
    }
}
