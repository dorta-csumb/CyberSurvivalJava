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
import androidx.lifecycle.LiveData;

import com.example.cybersurvivaljava.database.CyberSurvivalRepository;
import com.example.cybersurvivaljava.database.entities.User;
import com.example.cybersurvivaljava.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private CyberSurvivalRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = CyberSurvivalRepository.getRepository(getApplication());

        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        String username = binding.usernameRegisterEditText.getText().toString();
        if (username.isEmpty()) {
            toastMaker("Username cannot be empty");
            return;
        }
        LiveData<User> userObserver = repository.getUserByUsername(username);
        userObserver.observe(this, user -> {
            if (user != null) {
                toastMaker("Username not available");
            } else {
                String password = binding.passwordRegisterEditText.getText().toString();
                String passwordCheck = binding.passwordCheckRegisterEditText.getText().toString();
                if (password.isEmpty() || passwordCheck.isEmpty()) {
                    toastMaker("Password fields cannot be empty");
                } else if (!password.equals(passwordCheck)) {
                    toastMaker("Passwords do not match");
                } else {
                    User newUser = new User(username, password);
                    repository.insertUser(newUser);
                    startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
                    toastMaker("Register successful");
                }
            }
        });
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    static Intent registerIntentFactory(Context context) {
        return new Intent(context, RegisterActivity.class);
    }
}