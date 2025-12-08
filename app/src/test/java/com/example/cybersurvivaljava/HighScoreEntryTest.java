package com.example.cybersurvivaljava;

import org.junit.Test;
import static org.junit.Assert.*;

public class HighScoreEntryTest {

    @Test
    public void highScoreEntry_storesCorrectData() {
        // Make a new entry
        HighScoreEntry entry = new HighScoreEntry("TestPlayer", 85, 30, 4);

        // Verify the getters return what we put in
        assertEquals("TestPlayer", entry.getUserName());
        assertEquals(85, entry.getTotalAccuracy());
        assertEquals(30, entry.getTotalSpeed());
        assertEquals(4, entry.getTasksCompleted());
    }

    @Test
    public void highScoreEntry_handlesZeroValues() {
        // make an entry with edge case values (0)
        HighScoreEntry entry = new HighScoreEntry("Newbie", 0, 0, 0);

        // verify it handles them correctly
        assertEquals(0, entry.getTotalAccuracy());
        assertEquals(0, entry.getTasksCompleted());
    }
}