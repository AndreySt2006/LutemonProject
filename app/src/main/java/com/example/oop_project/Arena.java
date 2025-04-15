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
import java.util.Locale;
import java.util.Random;

public class Arena extends AppCompatActivity {
    private RecyclerView recyclerArena;
    private TextView tvEmptyView;
    private TextView tvBattleLog;
    private LutemonAdapter adapter;
    private ArrayList<Lutemon> combatants = new ArrayList<>();

    private Lutemon lutemon1;
    private Lutemon lutemon2;
    private boolean isLutemon1Turn = true;
    private boolean battleOver = false;
    private StringBuilder battleLog = new StringBuilder();
    private Button btnNextAttack; // Class member for enabling/disabling

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_arena);

        recyclerArena = findViewById(R.id.recycler_arena);
        recyclerArena.setLayoutManager(new LinearLayoutManager(this));
        tvEmptyView = findViewById(R.id.tv_empty_view_arena);
        tvBattleLog = findViewById(R.id.tv_battle_log);
        btnNextAttack = findViewById(R.id.btn_nextAttack); // Assign button

        ArrayList<Lutemon> selectedLutemons = getIntent().getParcelableArrayListExtra("selectedLutemons");

        if (selectedLutemons == null || selectedLutemons.size() != 2) {
            Toast.makeText(this, "Error: Need exactly 2 Lutemons for arena.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        DataProvider dataProvider = DataProvider.getInstance();
        lutemon1 = dataProvider.getLutemonByName(selectedLutemons.get(0).getName());
        lutemon2 = dataProvider.getLutemonByName(selectedLutemons.get(1).getName());

        if (lutemon1 == null || lutemon2 == null) {
            Toast.makeText(this, "Error: One or more selected Lutemons not found.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        // For now, assume they start battle at current health from DataProvider.
        // lutemon1.restoreHealth();
        // lutemon2.restoreHealth();


        combatants.add(lutemon1);
        combatants.add(lutemon2);

        adapter = new LutemonAdapter(this, combatants);
        recyclerArena.setAdapter(adapter);

        updateUI();
        setupButtonListeners();
        logBattleEvent("Battle Start: " + lutemon1.getName() + " vs " + lutemon2.getName());
        logBattleEvent(String.format(Locale.getDefault(), "%s: %d/%d HP", lutemon1.getName(), lutemon1.getHealth(), lutemon1.getMaxHealth()));
        logBattleEvent(String.format(Locale.getDefault(), "%s: %d/%d HP", lutemon2.getName(), lutemon2.getHealth(), lutemon2.getMaxHealth()));


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.arena_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupButtonListeners() {
        // btnNextAttack is already assigned in onCreate
        btnNextAttack.setOnClickListener(v -> {
            if (!battleOver) {
                performAttackTurn();
            } else {
                Toast.makeText(this, "Battle is already over.", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnMoveHome = findViewById(R.id.btn_arenaToHome);
        btnMoveHome.setOnClickListener(v -> {
            if (!battleOver) {
                lutemon1.restoreHealth();
                lutemon2.restoreHealth();
                DataProvider.getInstance().updateLutemon(lutemon1);
                DataProvider.getInstance().updateLutemon(lutemon2);
                Toast.makeText(this,"Fled from battle. Health restored.", Toast.LENGTH_SHORT).show();
            }
            finish();
        });
    }

    private void performAttackTurn() {
        if (battleOver) return;

        Lutemon attacker, defender;
        int defenderCurrentHealth; // Temporary variable for calculation

        if (isLutemon1Turn) {
            attacker = lutemon1;
            defender = lutemon2;
        } else {
            attacker = lutemon2;
            defender = lutemon1;
        }

        defenderCurrentHealth = defender.getHealth();
        int attackerEffectiveAttack = attacker.getAttack();
        int defenderDefense = defender.getDefense();
        logBattleEvent(attacker.getName() + " attacks " + defender.getName() + "!");


        int damage = Math.max(0, attackerEffectiveAttack - defenderDefense);
        defenderCurrentHealth -= damage;
        defenderCurrentHealth = Math.max(0, defenderCurrentHealth); // Ensure health doesn't go below 0

        logBattleEvent(defender.getName() + " takes " + damage + " damage.");

        // Update health directly on the defender object
        defender.setHealth(defenderCurrentHealth);


        // Check if defender died
        if (defender.getHealth() <= 0) {
            logBattleEvent(defender.getName() + " fainted!");
            battleOver = true;
            endBattle(attacker, defender); // Pass winner and loser
            btnNextAttack.setEnabled(false); // Disable attack button
        } else {
            logBattleEvent(String.format(Locale.getDefault(), "%s has %d/%d HP remaining.", defender.getName(), defender.getHealth(), defender.getMaxHealth()));
            // logBattleEvent(defender.getName() + " survived the attack.");
            isLutemon1Turn = !isLutemon1Turn;
        }

        updateUI();
    }

    private void endBattle(Lutemon winner, Lutemon loser) {
        logBattleEvent("Battle Over! " + winner.getName() + " wins!");
        Toast.makeText(this, winner.getName() + " wins!", Toast.LENGTH_LONG).show();

        DataProvider dataProvider = DataProvider.getInstance();
        dataProvider.incrementTotalBattles();
        winner.setExperience(winner.getExperience() + 1);
        // Assuming +1 attack per XP gained (update attack directly)
        winner.setAttack(winner.getAttack() + 1);
        winner.incrementBattlesFought();
        winner.incrementBattlesWon();
        loser.incrementBattlesFought();

        winner.restoreHealth();
        loser.restoreHealth();

        dataProvider.updateLutemon(winner);
        dataProvider.updateLutemon(loser);
    }


    private void updateUI() {
        if (adapter != null) {
            adapter.updateLutemon(new ArrayList<>(combatants));
        }
        tvBattleLog.setText(battleLog.toString());

        if (combatants == null || combatants.isEmpty()) {
            tvEmptyView.setVisibility(View.VISIBLE);
            recyclerArena.setVisibility(View.GONE);
        } else {
            tvEmptyView.setVisibility(View.GONE);
            recyclerArena.setVisibility(View.VISIBLE);
        }
    }

    private void logBattleEvent(String event) {
        battleLog.append(event).append("\n");
        tvBattleLog.setText(battleLog.toString());
    }
}