package com.example.oop_project;

import android.content.Intent;
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
import com.example.oop_project.adapter.LutemonAdapter;
import com.example.oop_project.model.Lutemon;

import java.util.ArrayList;
import java.util.List;

/**
 * Main activity serving as the home screen for the Lutemon application.
 * Displays the list of Lutemons and provides navigation to other features.
 */
public class MainActivity extends AppCompatActivity implements LutemonAdapter.OnItemClickListener {

    // UI Components
    private RecyclerView recyclerInventory;
    private TextView tvEmptyView;
    private LutemonAdapter adapter;
    private Button btnMoveToTraining, btnMoveToBattle, btnCreateNewBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable edge-to-edge display
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        initializeViews();

        // Restore health for all Lutemons
        DataProvider.getInstance().restoreAllHealth();

        // Set up RecyclerView with Lutemon data
        setupRecyclerView();

        // Set up button listeners
        setupButtonListeners();

        // Configure window insets for edge-to-edge display
        setupWindowInsets();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data when returning to the activity
        refreshData();
    }

    /**
     * Initializes all view components
     */
    private void initializeViews() {
        recyclerInventory = findViewById(R.id.recycler_main);
        tvEmptyView = findViewById(R.id.tv_empty_view);
        btnMoveToTraining = findViewById(R.id.btn_MoveToTraining);
        btnMoveToBattle = findViewById(R.id.btn_moveToBattle);
        btnCreateNewBottom = findViewById(R.id.btn_create_new_bottom);
    }

    /**
     * Sets up the RecyclerView with Lutemon data
     */
    private void setupRecyclerView() {
        // Configure layout manager
        recyclerInventory.setLayoutManager(new LinearLayoutManager(this));

        // Get Lutemon data from DataProvider
        List<Lutemon> lutemonList = DataProvider.getInstance().getLutemonData();

        // Initialize adapter
        adapter = new LutemonAdapter(this, lutemonList);
        adapter.setOnItemClickListener(this);
        recyclerInventory.setAdapter(adapter);

        // Update UI with initial data
        updateUI(lutemonList);
    }

    /**
     * Sets up all button click listeners
     */
    private void setupButtonListeners() {
        // Home button - refreshes the view
        Button btnViewHome = findViewById(R.id.btn_arenaToHome);
        btnViewHome.setOnClickListener(v -> {
            adapter.clearSelections();
            refreshData();
            Toast.makeText(this, "Viewing Home", Toast.LENGTH_SHORT).show();
        });

        // Create New Lutemon button
        btnCreateNewBottom.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, cr_new_ltmActivity.class);
            startActivity(intent);
        });

        // Training button - requires at least 1 selected Lutemon
        btnMoveToTraining.setOnClickListener(v -> {
            List<Lutemon> selected = adapter.getSelectedLutemons();
            if (selected.size() >= 1) {
                Intent intent = new Intent(this, Training_lutemon.class);
                intent.putParcelableArrayListExtra("selectedLutemons", new ArrayList<>(selected));
                startActivity(intent);
            } else {
                Toast.makeText(this, "Select at least one Lutemon to train", Toast.LENGTH_SHORT).show();
            }
        });

        // Battle button - requires exactly 2 selected Lutemons
        btnMoveToBattle.setOnClickListener(v -> {
            List<Lutemon> selected = adapter.getSelectedLutemons();
            if (selected.size() == 2) {
                Intent intent = new Intent(this, Arena.class);
                intent.putParcelableArrayListExtra("selectedLutemons", new ArrayList<>(selected));
                startActivity(intent);
            } else {
                Toast.makeText(this, "Select exactly two Lutemons to battle", Toast.LENGTH_SHORT).show();
            }
        });

        // Statistics button
        Button btnStats = findViewById(R.id.btn_view_stats);
        btnStats.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Configures edge-to-edge window insets
     */
    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Updates the UI based on current Lutemon data
     * @param lutemonList List of Lutemons to display
     */
    private void updateUI(List<Lutemon> lutemonList) {
        // Update adapter data
        adapter.updateLutemon(lutemonList);

        // Show empty view if no Lutemons exist
        if (lutemonList.isEmpty()) {
            tvEmptyView.setVisibility(View.VISIBLE);
            recyclerInventory.setVisibility(View.GONE);
        } else {
            tvEmptyView.setVisibility(View.GONE);
            recyclerInventory.setVisibility(View.VISIBLE);
        }

        // Update button states based on selection count
        updateButtonStates(adapter.getSelectedLutemons().size());
    }

    /**
     * Refreshes data from DataProvider and updates UI
     */
    private void refreshData() {
        DataProvider.getInstance().restoreAllHealth();
        List<Lutemon> currentData = DataProvider.getInstance().getLutemonData();
        updateUI(currentData);
    }

    @Override
    public void onItemClick(List<Lutemon> selected) {
        // Update button states when selection changes
        updateButtonStates(selected.size());
    }

    /**
     * Updates button enabled states based on selection count
     * @param selectionCount Number of currently selected Lutemons
     */
    private void updateButtonStates(int selectionCount) {
        btnMoveToTraining.setEnabled(selectionCount >= 1);
        btnMoveToBattle.setEnabled(selectionCount == 2);
    }
}