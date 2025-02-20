package com.ataberk.moodjournals;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private Spinner moodSpinner;
    private EditText commentEditText;
    private Button saveButton, statsButton;
    private TextView savedMoodText;
    private String selectedDate;

    // SharedPreferences keys
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String MOOD_KEY_PREFIX = "mood_";
    private static final String COMMENT_KEY_PREFIX = "comment_";
    private static final String COMMENT_COUNT_PREFIX = "comment_count_";
    private static final String MOOD_COUNT_PREFIX = "mood_count_";

    private String currentMonthYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        calendarView = findViewById(R.id.calendar1);
        moodSpinner = findViewById(R.id.spinner);
        commentEditText = findViewById(R.id.editTextText);
        saveButton = findViewById(R.id.button);
        statsButton = findViewById(R.id.button2);
        savedMoodText = findViewById(R.id.textView);

        // Add items to the spinner
        String[] moods = {"Happy", "Sad", "Confused", "Angry", "Sick", "Lovely"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, moods);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moodSpinner.setAdapter(adapter);

        // Get the current month and year in "YYYY-MM" format
        currentMonthYear = getCurrentMonthYear();

        // Get the selected date from the calendar
        selectedDate = getCurrentDate(); // Default to today's date
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            loadMoodAndCommentForDate(selectedDate);
        });

        // Save mood and comment when button is clicked
        saveButton.setOnClickListener(v -> {
            String selectedMood = moodSpinner.getSelectedItem().toString();
            String comment = commentEditText.getText().toString();

            // Save mood and comment for the selected date
            saveMoodAndCommentForDate(selectedDate, selectedMood, comment);

            // Update monthly mood and comment statistics
            updateMonthlyStatistics(selectedMood, comment, currentMonthYear);
        });

        // Button to go to the statistics activity
        statsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StatsActivity.class);
            intent.putExtra("monthYear", currentMonthYear);  // Pass current month and year to StatsActivity
            startActivity(intent);
        });

        // Load mood and comment for today's date when app starts
        loadMoodAndCommentForDate(selectedDate);
    }

    // Method to save the selected mood and comment for the selected date
    private void saveMoodAndCommentForDate(String date, String mood, String comment) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Save the mood and comment using a key that includes the date
        editor.putString(MOOD_KEY_PREFIX + date, mood);
        editor.putString(COMMENT_KEY_PREFIX + date, comment);
        editor.apply();

        savedMoodText.setText("Mood for " + date + ": " + mood + "\nComment: " + (comment.isEmpty() ? "No comment" : comment));
    }

    // Method to load and display the saved mood and comment for the selected date
    private void loadMoodAndCommentForDate(String date) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String savedMood = sharedPreferences.getString(MOOD_KEY_PREFIX + date, "No mood selected");
        String savedComment = sharedPreferences.getString(COMMENT_KEY_PREFIX + date, "No comment");

        savedMoodText.setText("Mood for " + date + ": " + savedMood + "\nComment: " + savedComment);
    }

    // Helper method to get the current date in the format YYYY-MM-DD
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    // Helper method to get the current month and year in the format YYYY-MM
    private String getCurrentMonthYear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        return sdf.format(new Date());
    }

    // Method to update the monthly mood and comment statistics
    private void updateMonthlyStatistics(String mood, String comment, String monthYear) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Update mood count for the month
        int moodCount = sharedPreferences.getInt(MOOD_COUNT_PREFIX + monthYear, 0);
        editor.putInt(MOOD_COUNT_PREFIX + monthYear, moodCount + 1);

        // Update comment count for the month if a comment was provided
        if (!comment.isEmpty()) {
            int commentCount = sharedPreferences.getInt(COMMENT_COUNT_PREFIX + monthYear, 0);
            editor.putInt(COMMENT_COUNT_PREFIX + monthYear, commentCount + 1);
        }

        editor.apply();
    }
}
