package com.example.oop_project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oop_project.DataProvider.DataProvider;
import com.example.oop_project.adapter.StatisticsAdapter;
import com.example.oop_project.model.Lutemon;

import java.util.List;
import java.util.Locale;

/**
 * Activity for displaying Lutemon statistics and aggregate data
 */
public class StatisticsActivity extends AppCompatActivity {

    // UI Components
    private RecyclerView recyclerStatistics;
    private TextView tvEmptyStatsView;
    private StatisticsAdapter adapter;
    private TextView tvTotalLutemons, tvTotalBattles, tvTotalTraining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize the layout component for the statistic view.
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_statistics);

        // Initialize UI components
        initializeViews();

        // Set up RecyclerView
        recyclerStatistics.setLayoutManager(new LinearLayoutManager(this));
        updateStatisticsDisplay();

        // Set up button listeners
        setupButtonListeners();

        // Handle edge-to-edge display
        setupWindowInsets();
    }

    /**
     * Initializes all view components
     */
    private void initializeViews() {
        recyclerStatistics = findViewById(R.id.recycler_statistics);
        tvEmptyStatsView = findViewById(R.id.tv_empty_stats_view);
        tvTotalLutemons = findViewById(R.id.tv_total_lutemons_value);
        tvTotalBattles = findViewById(R.id.tv_total_battles_value);
        tvTotalTraining = findViewById(R.id.tv_total_training_value);
    }

    /**
     * Sets up button click listeners
     */
    private void setupButtonListeners() {
        Button btnBackToHome = findViewById(R.id.btn_statsToHome);
        Button btnViewCharts = findViewById(R.id.btn_view_charts);

        btnBackToHome.setOnClickListener(v -> finish());
        btnViewCharts.setOnClickListener(v -> {
            Toast.makeText(this, "Chart View - Not Implemented", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Configures edge-to-edge window insets
     */
    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.statistics_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatisticsDisplay();
    }

    /**
     * Updates all statistics displays with current data
     */
    private void updateStatisticsDisplay() {
        DataProvider dataProvider = DataProvider.getInstance();
        List<Lutemon> lutemonList = dataProvider.getLutemonData();

        // Update aggregate statistics
        tvTotalLutemons.setText(String.format(Locale.getDefault(), "%d",
                dataProvider.getTotalLutemonsCreated()));
        tvTotalBattles.setText(String.format(Locale.getDefault(), "%d",
                dataProvider.getTotalBattles()));
        tvTotalTraining.setText(String.format(Locale.getDefault(), "%d",
                dataProvider.getTotalTrainingSessions()));

        // Update RecyclerView with Lutemon data
        updateRecyclerView(lutemonList);

        // Show empty view if no Lutemons exist
        toggleEmptyView(lutemonList.isEmpty());
    }

    /**
     * Updates the RecyclerView with current Lutemon data
     * @param lutemonList List of Lutemons to display
     */
    private void updateRecyclerView(List<Lutemon> lutemonList) {
        if (adapter == null) {
            adapter = new StatisticsAdapter(this, lutemonList);
            recyclerStatistics.setAdapter(adapter);
        } else {
            adapter.updateLutemonStats(lutemonList);
        }
    }

    /**
     * Toggles visibility of empty state view
     * @param isEmpty Whether the Lutemon list is empty
     */
    private void toggleEmptyView(boolean isEmpty) {
        if (isEmpty) {
            tvEmptyStatsView.setVisibility(View.VISIBLE);
            recyclerStatistics.setVisibility(View.GONE);
        } else {
            tvEmptyStatsView.setVisibility(View.GONE);
            recyclerStatistics.setVisibility(View.VISIBLE);
        }
    }
}