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

public class StatisticsActivity extends AppCompatActivity {

    private RecyclerView recyclerStatistics;
    private TextView tvEmptyStatsView;
    private StatisticsAdapter adapter;
    private TextView tvTotalLutemons, tvTotalBattles, tvTotalTraining;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_statistics);

        recyclerStatistics = findViewById(R.id.recycler_statistics);
        tvEmptyStatsView = findViewById(R.id.tv_empty_stats_view);
        Button btnBackToHome = findViewById(R.id.btn_statsToHome);
        Button btnViewCharts = findViewById(R.id.btn_view_charts);
        tvTotalLutemons = findViewById(R.id.tv_total_lutemons_value);
        tvTotalBattles = findViewById(R.id.tv_total_battles_value);
        tvTotalTraining = findViewById(R.id.tv_total_training_value);

        recyclerStatistics.setLayoutManager(new LinearLayoutManager(this));
        updateStatisticsDisplay();

        btnBackToHome.setOnClickListener(v -> finish());
        btnViewCharts.setOnClickListener(v -> {
            Toast.makeText(this, "Chart View - Not Implemented", Toast.LENGTH_SHORT).show();
        });

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

    private void updateStatisticsDisplay() {
        DataProvider dataProvider = DataProvider.getInstance();
        List<Lutemon> lutemonList = dataProvider.getLutemonData();

        tvTotalLutemons.setText(String.format(Locale.getDefault(), "%d", dataProvider.getTotalLutemonsCreated()));
        tvTotalBattles.setText(String.format(Locale.getDefault(), "%d", dataProvider.getTotalBattles()));
        tvTotalTraining.setText(String.format(Locale.getDefault(), "%d", dataProvider.getTotalTrainingSessions()));

        if (adapter == null) {
            adapter = new StatisticsAdapter(this, lutemonList);
            recyclerStatistics.setAdapter(adapter);
        } else {
            adapter.updateLutemonStats(lutemonList);
        }

        if (lutemonList.isEmpty()) {
            tvEmptyStatsView.setVisibility(View.VISIBLE);
            recyclerStatistics.setVisibility(View.GONE);
        } else {
            tvEmptyStatsView.setVisibility(View.GONE);
            recyclerStatistics.setVisibility(View.VISIBLE);
        }
    }
}