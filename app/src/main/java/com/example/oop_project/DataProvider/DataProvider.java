package com.example.oop_project.DataProvider;

import com.example.oop_project.model.Lutemon;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Singleton class managing all Lutemon data and global statistics.
 * Provides thread-safe access to application data.
 */
public class DataProvider {
    // Singleton instance
    private static DataProvider instance;

    // Data storage
    private final List<Lutemon> lutemons = new ArrayList<>();
    private int totalBattles = 0;
    private int totalTrainingSessions = 0;

    /**
     * Private constructor for singleton pattern
     */
    private DataProvider() {
        initializeDefaultData();
    }

    /**
     * Gets the singleton instance (thread-safe)
     * @return The DataProvider instance
     */
    public static synchronized DataProvider getInstance() {
        if (instance == null) {
            instance = new DataProvider();
        }
        return instance;
    }

    /**
     * Initializes default Lutemon data
     */
    private void initializeDefaultData() {
        // Example initialization (commented out):
        // lutemons.add(new Lutemon("Sparky", "White", 5, 4, 0, 20, "url_white"));
        // lutemons.add(new Lutemon("Leafy", "Green", 6, 3, 0, 19, "url_green"));
    }

    /**
     * Gets an unmodifiable view of all Lutemons
     * @return List of Lutemons (immutable)
     */
    public List<Lutemon> getLutemonData() {
        return Collections.unmodifiableList(lutemons);
    }

    /**
     * Adds a new Lutemon if it doesn't already exist
     * @param newLutemon The Lutemon to add
     */
    public void addNewLutemon(Lutemon newLutemon) {
        if (newLutemon != null && !lutemonExists(newLutemon.getName())) {
            lutemons.add(newLutemon);
        } else if (lutemonExists(newLutemon.getName())) {
            System.out.println("Lutemon with name " + newLutemon.getName() + " already exists.");
        }
    }

    /**
     * Finds a Lutemon by name
     * @param name The name to search for
     * @return The matching Lutemon or null if not found
     */
    public Lutemon getLutemonByName(String name) {
        for (Lutemon l : lutemons) {
            if (l.getName().equals(name)) {
                return l;
            }
        }
        return null;
    }

    /**
     * Checks if a Lutemon exists with the given name
     * @param name The name to check
     * @return true if exists, false otherwise
     */
    private boolean lutemonExists(String name) {
        return getLutemonByName(name) != null;
    }

    /**
     * Updates an existing Lutemon
     * @param updatedLutemon The Lutemon with updated values
     */
    public void updateLutemon(Lutemon updatedLutemon) {
        if (updatedLutemon == null) return;

        ListIterator<Lutemon> iterator = lutemons.listIterator();
        while (iterator.hasNext()) {
            Lutemon current = iterator.next();
            if (current.getName().equals(updatedLutemon.getName())) {
                iterator.set(updatedLutemon);
                return;
            }
        }
        System.out.println("Attempted to update non-existent Lutemon: " + updatedLutemon.getName());
    }

    /**
     * Removes a Lutemon by name
     * @param name The name of the Lutemon to remove
     */
    public void removeLutemon(String name) {
        lutemons.removeIf(lutemon -> lutemon.getName().equals(name));
    }

    /**
     * Restores health for all Lutemons
     */
    public void restoreAllHealth() {
        for (Lutemon lutemon : lutemons) {
            lutemon.restoreHealth();
        }
    }

    // Statistics management methods

    /**
     * Increments the total battles counter
     */
    public void incrementTotalBattles() {
        this.totalBattles++;
    }

    /**
     * Increments the total training sessions counter
     */
    public void incrementTotalTrainingSessions() {
        this.totalTrainingSessions++;
    }

    /**
     * Gets the total number of created Lutemons
     * @return Count of Lutemons
     */
    public int getTotalLutemonsCreated() {
        return lutemons.size();
    }

    /**
     * Gets the total number of battles fought
     * @return Total battle count
     */
    public int getTotalBattles() {
        return totalBattles;
    }

    /**
     * Gets the total number of training sessions
     * @return Total training session count
     */
    public int getTotalTrainingSessions() {
        return totalTrainingSessions;
    }

    /**
     * Gets a copy of the Lutemon list
     * @return New ArrayList containing all Lutemons
     */
    public List<Lutemon> getLutemonCopy() {
        return new ArrayList<>(lutemons);
    }
}