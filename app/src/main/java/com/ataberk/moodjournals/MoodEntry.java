// MoodEntry.java
package com.ataberk.moodjournals;

public class MoodEntry {
    private int day;
    private int moodScore;

    public MoodEntry(int day, int moodScore) {
        this.day = day;
        this.moodScore = moodScore;
    }

    public int getDay() {
        return day;
    }

    public int getMoodScore() {
        return moodScore;
    }
}