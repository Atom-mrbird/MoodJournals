package com.ataberk.moodjournals;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



import github.mikephil.charting.charts.BarChart;
import github.mikephil.charting.components.XAxis;
import github.mikephil.charting.components.YAxis;
import github.mikephil.charting.data.BarData;
import github.mikephil.charting.data.BarDataSet;
import github.mikephil.charting.data.BarEntry;
import github.mikephil.charting.data.Entry;
import github.mikephil.charting.formatter.IndexAxisValueFormatter;
import github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

public class StatsActivity extends AppCompatActivity {

    private List<MoodEntry> moodEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // Initialize the BarChart
        BarChart barChart = findViewById(R.id.barChart);

        // TextViews for statistics
        TextView avgMoodTextView = findViewById(R.id.avgMoodTextView);
        TextView highestMoodTextView = findViewById(R.id.highestMoodTextView);
        TextView lowestMoodTextView = findViewById(R.id.lowestMoodTextView);

        // Sample mood data (you can replace this with actual data)
        moodEntries = new ArrayList<>();
        moodEntries.add(new MoodEntry(1, 6));  // Day 1, Mood 6
        moodEntries.add(new MoodEntry(2, 8));  // Day 2, Mood 8
        moodEntries.add(new MoodEntry(3, 5));  // Day 3, Mood 5
        moodEntries.add(new MoodEntry(4, 7));  // Day 4, Mood 7
        moodEntries.add(new MoodEntry(5, 4));  // Day 5, Mood 4

        // Calculate statistics
        int highestMood = Integer.MIN_VALUE;
        int lowestMood = Integer.MAX_VALUE;
        int totalMood = 0;

        for (MoodEntry entry : moodEntries) {
            int mood = entry.getMoodScore();
            totalMood += mood;
            if (mood > highestMood) highestMood = mood;
            if (mood < lowestMood) lowestMood = mood;
        }

        float averageMood = totalMood / (float) moodEntries.size();

        // Display statistics in TextViews
        avgMoodTextView.setText(String.format("Average Mood: %.2f", averageMood));
        highestMoodTextView.setText(String.format("Highest Mood: %d", highestMood));
        lowestMoodTextView.setText(String.format("Lowest Mood: %d", lowestMood));

        // Create BarChart data
        ArrayList<BarEntry> chartEntries = new ArrayList<>();
        for (MoodEntry entry : moodEntries) {
            chartEntries.add(new BarEntry(entry.getDay(), entry.getMoodScore()));
        }

        BarDataSet barDataSet = new BarDataSet(chartEntries, "Mood Over Time");
        barDataSet.setColor(getResources().getColor(R.color.holo_green_dark));

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.9f);

        // X-axis configuration
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(moodEntries.size());
        xAxis.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return "Day " + ((int) value + 1);
            }
        });

        // Y-axis configuration
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(10f);

        // Disable right Y-axis
        barChart.getAxisRight().setEnabled(false);

        // Set the data and refresh the chart
        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.invalidate();
    }
}
