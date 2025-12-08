package com.example.cybersurvivaljava;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cybersurvivaljava.databinding.ActivitySatelliteBinding;

public class SatelliteActivity extends AppCompatActivity {

    private ActivitySatelliteBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySatelliteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
