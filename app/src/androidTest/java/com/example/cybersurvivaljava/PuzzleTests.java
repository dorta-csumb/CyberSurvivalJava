package com.example.cybersurvivaljava;

import android.content.Context;
import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.cybersurvivaljava.database.entities.Problems;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class PuzzleTests {

    @Test
    public void testPuzzleIntentFactory() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        int catId = Problems.CATEGORY_NETWORKING;
        int userId = 99;

        Intent intent = PuzzleActivity.puzzleIntentFactory(context, catId, userId);

        assertEquals(catId, intent.getIntExtra("com.example.cybersurvivaljava.CATEGORY_ID", -1));
        assertEquals(userId, intent.getIntExtra("com.example.cybersurvivaljava.USER_ID", -1));
        assertEquals(PuzzleActivity.class.getName(), intent.getComponent().getClassName());
    }

    @Test
    public void testCategoryConstants() {
        assertEquals(1, Problems.CATEGORY_PROGRAMMING);
        assertEquals(2, Problems.CATEGORY_CYBERSECURITY);
        assertEquals(3, Problems.CATEGORY_CIRCUITRY);
        assertEquals(4, Problems.CATEGORY_NETWORKING);
    }
}
