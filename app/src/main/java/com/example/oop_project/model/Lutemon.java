package com.example.oop_project.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Lutemon implements Parcelable {
    private String name;
    private String color;
    private int attack;
    private int defense;
    private int experience;
    private int health;
    private String picURL;

    // Added fields
    private int maxHealth;
    private int battlesFought;
    private int battlesWon;
    private int trainingSessions;

    public Lutemon(String name, String color, int attack, int defense, int experience, int maxHealth, String picURL) {
        this.name = name;
        this.color = color;
        this.attack = attack;
        this.defense = defense;
        this.experience = experience;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.picURL = picURL;
        this.battlesFought = 0;
        this.battlesWon = 0;
        this.trainingSessions = 0;
    }

    public String getPicURL() {
        return this.picURL;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getExperience() {
        return experience;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getBattlesFought() {
        return battlesFought;
    }

    public int getBattlesWon() {
        return battlesWon;
    }

    public int getTrainingSessions() {
        return trainingSessions;
    }

    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setHealth(int health) {
        // Ensure health doesn't go below 0 or above maxHealth
        this.health = Math.max(0, Math.min(health, this.maxHealth));
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void restoreHealth() {
        this.health = this.maxHealth;
    }

    public void incrementBattlesFought() {
        this.battlesFought++;
    }

    public void incrementBattlesWon() {
        this.battlesWon++;
    }

    public void incrementTrainingSessions() {
        this.trainingSessions++;
    }


    @Override
    public String toString() {
        return "Lutemon{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", attack=" + attack +
                ", defense=" + defense +
                ", experience=" + experience +
                ", health=" + health + "/" + maxHealth +
                ", battles=" + battlesFought +
                ", wins=" + battlesWon +
                ", training=" + trainingSessions +
                '}';
    }

    protected Lutemon(Parcel in) {
        name = in.readString();
        color = in.readString();
        attack = in.readInt();
        defense = in.readInt();
        experience = in.readInt();
        health = in.readInt();
        picURL = in.readString();
        maxHealth = in.readInt();
        battlesFought = in.readInt();
        battlesWon = in.readInt();
        trainingSessions = in.readInt();
    }

    public static final Creator<Lutemon> CREATOR = new Creator<Lutemon>() {
        @Override
        public Lutemon createFromParcel(Parcel in) {
            return new Lutemon(in);
        }

        @Override
        public Lutemon[] newArray(int size) {
            return new Lutemon[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) { // Added NonNull
        dest.writeString(name);
        dest.writeString(color);
        dest.writeInt(attack);
        dest.writeInt(defense);
        dest.writeInt(experience);
        dest.writeInt(health);
        dest.writeString(picURL);
        dest.writeInt(maxHealth);
        dest.writeInt(battlesFought);
        dest.writeInt(battlesWon);
        dest.writeInt(trainingSessions);
    }
    public Lutemon() {
        // Initialize default values if needed
        this.name = "";
        this.color = "";
        this.picURL = "";
    }
}