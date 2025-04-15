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

public class MainActivity extends AppCompatActivity implements LutemonAdapter.OnItemClickListener {
    private RecyclerView recyclerInventory;
    private TextView tvEmptyView;
    private LutemonAdapter adapter;
    private Button btnMoveToTraining, btnMoveToBattle;
    // Variable for the new button
    private Button btnCreateNewBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recyclerInventory = findViewById(R.id.recycler_main);
        recyclerInventory.setLayoutManager(new LinearLayoutManager(this));
        tvEmptyView = findViewById(R.id.tv_empty_view);
        btnMoveToTraining = findViewById(R.id.btn_MoveToTraining);
        btnMoveToBattle = findViewById(R.id.btn_moveToBattle);
        // Find the new button
        btnCreateNewBottom = findViewById(R.id.btn_create_new_bottom);

        DataProvider.getInstance().restoreAllHealth();
        List<Lutemon> lutemonList = DataProvider.getInstance().getLutemonData();

        adapter = new LutemonAdapter(this, lutemonList);
        adapter.setOnItemClickListener(this);
        recyclerInventory.setAdapter(adapter);

        updateUI(lutemonList);
        setupButtonListeners();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    private void setupButtonListeners() {
        Button btnViewHome = findViewById(R.id.btn_arenaToHome);
        btnViewHome.setOnClickListener(v -> {
            adapter.clearSelections();
            refreshData();
            Toast.makeText(this, "Viewing Home", Toast.LENGTH_SHORT).show();
        });

        btnCreateNewBottom.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, cr_new_ltmActivity.class);
            startActivity(intent);
        });

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

        Button btnStats = findViewById(R.id.btn_view_stats);
        btnStats.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
            startActivity(intent);
        });
    }


    private void updateUI(List<Lutemon> lutemonList) {
        adapter.updateLutemon(lutemonList);
        if (lutemonList.isEmpty()) {
            tvEmptyView.setVisibility(View.VISIBLE);
            recyclerInventory.setVisibility(View.GONE);
        } else {
            tvEmptyView.setVisibility(View.GONE);
            recyclerInventory.setVisibility(View.VISIBLE);
        }
        updateButtonStates(adapter.getSelectedLutemons().size());
    }

    private void refreshData() {
        DataProvider.getInstance().restoreAllHealth();
        List<Lutemon> currentData = DataProvider.getInstance().getLutemonData();
        updateUI(currentData);
    }

    @Override
    public void onItemClick(List<Lutemon> selected) {
        updateButtonStates(selected.size());
    }

    private void updateButtonStates(int selectionCount) {
        btnMoveToTraining.setEnabled(selectionCount >= 1);
        btnMoveToBattle.setEnabled(selectionCount == 2);
    }
}