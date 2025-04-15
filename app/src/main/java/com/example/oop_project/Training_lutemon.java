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

public class Training_lutemon extends AppCompatActivity {
    private LutemonAdapter adapter;
    private TextView tvEmptyView;
    private RecyclerView recyclerTraining;
    private ArrayList<Lutemon> lutemonsInTraining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_training_lutemon);

        recyclerTraining = findViewById(R.id.recycler_training);
        recyclerTraining.setLayoutManager(new LinearLayoutManager(this));
        tvEmptyView = findViewById(R.id.tv_empty_view);

        lutemonsInTraining = getIntent().getParcelableArrayListExtra("selectedLutemons");

        adapter = new LutemonAdapter(this, new ArrayList<>(lutemonsInTraining));
        recyclerTraining.setAdapter(adapter);

        updateUI(lutemonsInTraining);
        setupButtonListeners();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.training_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupButtonListeners() {
        Button btnTrain = findViewById(R.id.btn_train);
        btnTrain.setOnClickListener(v -> trainSelectedLutemons());

        Button btnViewHome = findViewById(R.id.btn_trainingToHome);
        btnViewHome.setOnClickListener(v -> finish());
    }

    private void trainSelectedLutemons() {
        if (lutemonsInTraining == null || lutemonsInTraining.isEmpty()) {
            Toast.makeText(this, "No Lutemons to train.", Toast.LENGTH_SHORT).show();
            return;
        }

        DataProvider dataProvider = DataProvider.getInstance();
        List<Lutemon> updatedListForAdapter = new ArrayList<>();

        for (Lutemon lutemonToTrain : lutemonsInTraining) {
            Lutemon originalLutemon = dataProvider.getLutemonByName(lutemonToTrain.getName());

            if (originalLutemon != null) {
                int baseAttack = originalLutemon.getAttack() - originalLutemon.getExperience(); // Estimate base attack if needed

                originalLutemon.setExperience(originalLutemon.getExperience() + 1);
                originalLutemon.setAttack(originalLutemon.getAttack() + 1); // Simple +1 attack for simplicity here
                originalLutemon.incrementTrainingSessions();

                // Update the DataProvider
                dataProvider.updateLutemon(originalLutemon);
                updatedListForAdapter.add(originalLutemon); // Add updated original to list
                dataProvider.incrementTotalTrainingSessions();
            } else {
                updatedListForAdapter.add(lutemonToTrain); // Add potentially outdated copy
                System.err.println("Warning: Trained Lutemon '" + lutemonToTrain.getName() + "' not found in DataProvider for update.");
            }
        }

        lutemonsInTraining.clear();
        lutemonsInTraining.addAll(updatedListForAdapter);
        updateUI(lutemonsInTraining);
        Toast.makeText(this, "Training complete!", Toast.LENGTH_SHORT).show();
    }

    private void updateUI(List<Lutemon> lutemonList) {
        if (adapter != null) {
            adapter.updateLutemon(new ArrayList<>(lutemonList));
        }
        if (lutemonList == null || lutemonList.isEmpty()) {
            tvEmptyView.setVisibility(View.VISIBLE);
            recyclerTraining.setVisibility(View.GONE);
        } else {
            tvEmptyView.setVisibility(View.GONE);
            recyclerTraining.setVisibility(View.VISIBLE);
        }
    }
}