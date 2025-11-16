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
import com.example.cybersurvivaljava.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    
    private ActivityLoginBinding binding;
    private CyberSurvivalRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = CyberSurvivalRepository.getRepository(getApplication());

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyUser();
            }
        });
    }

    private void verifyUser() {
        String username = binding.usernameLoginEditText.getText().toString();
        if (username.isEmpty()) {
            toastMaker("Username cannot be empty");
            return;
        }
        LiveData<User> userObserver = repository.getUserByUsername(username);
        userObserver.observe(this, user -> {
            if (user != null) {
                String password = binding.passwordLoginEditText.getText().toString();
                if (password.equals(user.getPassword())) {
//                    startActivity();
                    toastMaker("Login successful");
                } else {
                    toastMaker("Incorrect password");
                    binding.passwordLoginEditText.setSelection(0);
                }
            } else {
                toastMaker("Not a registered user");
                binding.usernameLoginEditText.setSelection(0);
            }
        });
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    static Intent loginIntentFactory(Context context) {
        return new Intent(context, LoginActivity.class);
    }
}
