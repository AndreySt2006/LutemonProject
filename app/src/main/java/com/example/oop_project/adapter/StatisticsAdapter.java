package com.example.oop_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oop_project.R;
import com.example.oop_project.model.Lutemon;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView adapter for displaying Lutemon statistics including:
 * - Battle history (total battles and wins)
 * - Training sessions completed
 * - Basic Lutemon information (name, color, image)
 */
public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.ViewHolder> {

    // Application context for resource access
    private final Context context;

    // Data to display
    private final List<Lutemon> lutemonList;

    /*
     * Constructor initializing the adapter with context and data
     * context: The application context
     *lutemonList: List of Lutemons to display
     */
    public StatisticsAdapter(Context context, List<Lutemon> lutemonList) {
        this.context = context;
        this.lutemonList = new ArrayList<>(lutemonList); // Defensive copy
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        View view = LayoutInflater.from(context)
                .inflate(R.layout.statistic_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get current Lutemon
        Lutemon lutemon = lutemonList.get(position);

        // Set text values
        holder.tvLutemonName.setText(lutemon.getName());
        holder.tvLutemonColor.setText(lutemon.getColor());
        holder.tvBattles.setText(String.valueOf(lutemon.getBattlesFought()));
        holder.tvWins.setText(String.valueOf(lutemon.getBattlesWon()));
        holder.tvTraining.setText(String.valueOf(lutemon.getTrainingSessions()));

        // Load image using Glide with placeholders
        Glide.with(context)
                .load(lutemon.getPicURL())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.ivLutemonPic);
    }

    @Override
    public int getItemCount() {
        return lutemonList.size();
    }

    /**
     * Updates the adapter with new Lutemon statistics data
     * @param newLutemonList Updated list of Lutemons
     */
    public void updateLutemonStats(List<Lutemon> newLutemonList) {
        lutemonList.clear();
        lutemonList.addAll(newLutemonList);
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class that holds references to all views in a statistics item
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        // Text views displaying Lutemon stats
        final TextView tvLutemonName;
        final TextView tvLutemonColor;
        final TextView tvBattles;
        final TextView tvWins;
        final TextView tvTraining;

        // Image view for Lutemon picture
        final ImageView ivLutemonPic;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize all view references
            tvLutemonName = itemView.findViewById(R.id.tv_stat_lutemonName);
            tvLutemonColor = itemView.findViewById(R.id.tv_stat_lutemonColor);
            tvBattles = itemView.findViewById(R.id.tv_stat_battles_value);
            tvWins = itemView.findViewById(R.id.tv_stat_wins_value);
            tvTraining = itemView.findViewById(R.id.tv_stat_training_value);
            ivLutemonPic = itemView.findViewById(R.id.iw_stat_lutemonPic);
        }
    }
}