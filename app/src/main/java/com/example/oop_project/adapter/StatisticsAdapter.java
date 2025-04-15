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

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.ViewHolder> {

    private final Context context;
    private final List<Lutemon> lutemonList;

    public StatisticsAdapter(Context context, List<Lutemon> lutemonList) {
        this.context = context;
        this.lutemonList = new ArrayList<>(lutemonList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.statistic_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lutemon lutemon = lutemonList.get(position);

        holder.tvLutemonName.setText(lutemon.getName());
        holder.tvLutemonColor.setText(lutemon.getColor());
        holder.tvBattles.setText(String.valueOf(lutemon.getBattlesFought()));
        holder.tvWins.setText(String.valueOf(lutemon.getBattlesWon()));
        holder.tvTraining.setText(String.valueOf(lutemon.getTrainingSessions()));

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

    public void updateLutemonStats(List<Lutemon> newLutemonList) {
        lutemonList.clear();
        lutemonList.addAll(newLutemonList);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLutemonName, tvLutemonColor, tvBattles, tvWins, tvTraining;
        ImageView ivLutemonPic;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLutemonName = itemView.findViewById(R.id.tv_stat_lutemonName);
            tvLutemonColor = itemView.findViewById(R.id.tv_stat_lutemonColor);
            tvBattles = itemView.findViewById(R.id.tv_stat_battles_value);
            tvWins = itemView.findViewById(R.id.tv_stat_wins_value);
            tvTraining = itemView.findViewById(R.id.tv_stat_training_value);
            ivLutemonPic = itemView.findViewById(R.id.iw_stat_lutemonPic);
        }
    }
}