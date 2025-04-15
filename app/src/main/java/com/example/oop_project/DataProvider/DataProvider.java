package com.example.oop_project.DataProvider;

import com.example.oop_project.model.Lutemon;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class DataProvider {
    private static DataProvider instance;
    private final List<Lutemon> lutemons = new ArrayList<>();
    private int totalBattles = 0; // General stat
    private int totalTrainingSessions = 0; // General stat

    private DataProvider() {
        initializeDefaultData();
    }

    public static synchronized DataProvider getInstance() {
        if (instance == null) {
            instance = new DataProvider();
        }
        return instance;
    }

    private void initializeDefaultData() {
        // Example using new constructor:
        // lutemons.add(new Lutemon("Sparky", "White", 5, 4, 0, 20, "url_white"));
        // lutemons.add(new Lutemon("Leafy", "Green", 6, 3, 0, 19, "url_green"));
    }

    public List<Lutemon> getLutemonData() {
        return Collections.unmodifiableList(lutemons);
    }

    public void addNewLutemon(Lutemon newLutemon) {
        if (newLutemon != null && !lutemonExists(newLutemon.getName())) {
            lutemons.add(newLutemon);
        } else if (lutemonExists(newLutemon.getName())){
            System.out.println("Lutemon with name " + newLutemon.getName() + " already exists.");
        }
    }

    public Lutemon getLutemonByName(String name) {
        for (Lutemon l : lutemons) {
            if (l.getName().equals(name)) {
                return l;
            }
        }
        return null;
    }

    private boolean lutemonExists(String name) {
        return getLutemonByName(name) != null;
    }

    public void updateLutemon(Lutemon updatedLutemon) {
        if (updatedLutemon == null) return;

        ListIterator<Lutemon> iterator = lutemons.listIterator();
        while (iterator.hasNext()) {
            Lutemon current = iterator.next();
            if (current.getName().equals(updatedLutemon.getName())) {
                iterator.set(updatedLutemon); // Replace with the updated object
                return;
            }
        }
        System.out.println("Attempted to update non-existent Lutemon: " + updatedLutemon.getName());
    }

    public void removeLutemon(String name) {
        lutemons.removeIf(lutemon -> lutemon.getName().equals(name));
    }

    public void restoreAllHealth() {
        for (Lutemon lutemon : lutemons) {
            lutemon.restoreHealth();
        }
    }

    public void incrementTotalBattles() { this.totalBattles++; }
    public void incrementTotalTrainingSessions() { this.totalTrainingSessions++; }

    public int getTotalLutemonsCreated() { return lutemons.size(); }
    public int getTotalBattles() { return totalBattles; }
    public int getTotalTrainingSessions() { return totalTrainingSessions; }

    public List<Lutemon> getLutemonCopy() {
        return new ArrayList<>(lutemons);
    }
}