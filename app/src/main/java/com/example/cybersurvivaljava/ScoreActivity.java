package com.example.cybersurvivaljava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import com.example.cybersurvivaljava.databinding.ActivityScoreBinding;

import java.util.ArrayList;
import java.util.List;

public class ScoreActivity extends AppCompatActivity {

    // 1. Declare a variable for the auto-generated binding class.
    private ActivityScoreBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 2. Inflate the layout using the binding class. This creates all the views.
        binding = ActivityScoreBinding.inflate(getLayoutInflater());

        // 3. Set the root of the binding as the content view for this activity.
        setContentView(binding.getRoot());

        // 4. Create the Mock Data list
        // This is our temporary, hardcoded data for testing. Later, this will be replaced
        // with real data from Aiden's activity.
        List<HighScoreEntry> mockScores = new ArrayList<>();
        mockScores.add(new HighScoreEntry("David", 100, 120, 4));
        mockScores.add(new HighScoreEntry("Aiden", 75, 155, 3));
        mockScores.add(new HighScoreEntry("Jesus", 100, 95, 4));
        mockScores.add(new HighScoreEntry("Adam", 50, 210, 2));

        // 5. Create an instance of our Adapter
        // We pass our list of MOCK DATA to the adapter's constructor. Finally!
        HighScoreAdapter adapter = new HighScoreAdapter(mockScores);

        // 6. Connect the Adapter to the RecyclerView
        // We use our 'binding' variable to get a direct, safe reference to our RecyclerView.
        binding.highScoreRecyclerView.setAdapter(adapter);

        // 7. Set the Layout Manager
        // Concept: The LayoutManager is essential. It tells the RecyclerView HOW to arrange
        // the items. LinearLayoutManager arranges them in a simple vertical list.
        // Applicability: For a photo gallery, you might use a GridLayoutManager here instead.
        binding.highScoreRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}