package com.example.cybersurvivaljava;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TEMP: Jump straight to PuzzleActivity to verify it runs
        startActivity(new Intent(this, PuzzleActivity.class));
    }
}