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
import com.example.oop_project.adapter.LutemonAdapter;
import com.example.oop_project.model.Lutemon;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for training Lutemon characters.
 * Handles stat improvements through training sessions.
 */
public class Training_lutemon extends AppCompatActivity {

    // UI Components
    private LutemonAdapter adapter;
    private TextView tvEmptyView;
    private RecyclerView recyclerTraining;
    private ArrayList<Lutemon> lutemonsInTraining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable edge-to-edge display
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_training_lutemon);

        // Initialize UI components
        initializeViews();

        // Get selected Lutemons from intent
        lutemonsInTraining = getIntent().getParcelableArrayListExtra("selectedLutemons");

        // Set up RecyclerView with adapter
        setupRecyclerView();

        // Update initial UI state
        updateUI(lutemonsInTraining);

        // Set up button listeners
        setupButtonListeners();

        // Configure window insets for edge-to-edge display
        setupWindowInsets();
    }

    /**
     * Initializes all view components
     */
    private void initializeViews() {
        recyclerTraining = findViewById(R.id.recycler_training);
        tvEmptyView = findViewById(R.id.tv_empty_view);
    }

    /**
     * Sets up the RecyclerView with adapter and layout
     */
    private void setupRecyclerView() {
        recyclerTraining.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LutemonAdapter(this, new ArrayList<>(lutemonsInTraining));
        recyclerTraining.setAdapter(adapter);
    }

    /**
     * Sets up button click listeners
     */
    private void setupButtonListeners() {
        // Train button - initiates training session
        Button btnTrain = findViewById(R.id.btn_train);
        btnTrain.setOnClickListener(v -> trainSelectedLutemons());

        // Home button - returns to previous screen
        Button btnViewHome = findViewById(R.id.btn_trainingToHome);
        btnViewHome.setOnClickListener(v -> finish());
    }

    /**
     * Configures edge-to-edge window insets
     */
    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.training_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Trains all selected Lutemons, improving their stats
     */
    private void trainSelectedLutemons() {
        // Validate there are Lutemons to train
        if (lutemonsInTraining == null || lutemonsInTraining.isEmpty()) {
            Toast.makeText(this, "No Lutemons to train.", Toast.LENGTH_SHORT).show();
            return;
        }

        DataProvider dataProvider = DataProvider.getInstance();
        List<Lutemon> updatedListForAdapter = new ArrayList<>();

        // Process each Lutemon in training
        for (Lutemon lutemonToTrain : lutemonsInTraining) {
            Lutemon originalLutemon = dataProvider.getLutemonByName(lutemonToTrain.getName());

            if (originalLutemon != null) {
                // Calculate base attack (for potential future use)
                int baseAttack = originalLutemon.getAttack() - originalLutemon.getExperience();

                // Improve Lutemon stats
                originalLutemon.setExperience(originalLutemon.getExperience() + 1);
                originalLutemon.setAttack(originalLutemon.getAttack() + 1);
                originalLutemon.incrementTrainingSessions();

                // Update in DataProvider
                dataProvider.updateLutemon(originalLutemon);
                updatedListForAdapter.add(originalLutemon);
                dataProvider.incrementTotalTrainingSessions();
            } else {
                // Handle case where Lutemon isn't found in DataProvider
                updatedListForAdapter.add(lutemonToTrain);
                System.err.println("Warning: Trained Lutemon '" +
                        lutemonToTrain.getName() + "' not found in DataProvider for update.");
            }
        }

        // Update local list and UI
        lutemonsInTraining.clear();
        lutemonsInTraining.addAll(updatedListForAdapter);
        updateUI(lutemonsInTraining);
        Toast.makeText(this, "Training complete!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Updates the UI based on current Lutemon data
     * @param lutemonList List of Lutemons to display
     */
    private void updateUI(List<Lutemon> lutemonList) {
        if (adapter != null) {
            adapter.updateLutemon(new ArrayList<>(lutemonList));
        }

        // Show empty view if no Lutemons exist
        if (lutemonList == null || lutemonList.isEmpty()) {
            tvEmptyView.setVisibility(View.VISIBLE);
            recyclerTraining.setVisibility(View.GONE);
        } else {
            tvEmptyView.setVisibility(View.GONE);
            recyclerTraining.setVisibility(View.VISIBLE);
        }
    }
}