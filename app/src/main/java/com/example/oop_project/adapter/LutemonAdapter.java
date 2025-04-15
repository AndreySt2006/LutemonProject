package com.example.oop_project.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oop_project.R;
import com.example.oop_project.model.Lutemon;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale; // Import Locale

public class LutemonAdapter extends RecyclerView.Adapter<LutemonAdapter.ViewHolder> {
    private final List<Lutemon> lutemonList;
    private final Context context;
    private final SparseBooleanArray selectedItems = new SparseBooleanArray();
    private OnItemClickListener clickListener;
    private static final int MAX_SELECTIONS = 2;

    public interface OnItemClickListener {
        void onItemClick(List<Lutemon> selected);
    }

    public LutemonAdapter(Context context, List<Lutemon> lutemonList) {
        this.context = context;
        this.lutemonList = new ArrayList<>(lutemonList);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lutemon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lutemon lutemonElement = lutemonList.get(position);

        holder.tvLutemonName.setText(lutemonElement.getName());
        holder.tvLutemonColor.setText(lutemonElement.getColor());

        holder.tvLutemonHealth.setText(String.format(Locale.getDefault(), "HP: %d/%d",
                lutemonElement.getHealth(), lutemonElement.getMaxHealth()));
        holder.tvLutemonAttack.setText(String.format(Locale.getDefault(), "Atk: %d",
                lutemonElement.getAttack()));
        holder.tvLutemonDefense.setText(String.format(Locale.getDefault(), "Def: %d",
                lutemonElement.getDefense()));
        holder.tvLutemonExp.setText(String.format(Locale.getDefault(), "XP: %d",
                lutemonElement.getExperience()));

        Glide.with(context)
                .load(lutemonElement.getPicURL())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.lutemonPic);

        holder.itemView.setBackgroundColor(
                selectedItems.get(position) ? Color.LTGRAY : Color.TRANSPARENT
        );

        holder.itemView.setOnClickListener(v -> {
            toggleSelection(position);
            if (clickListener != null) {
                clickListener.onItemClick(getSelectedLutemons());
            }
        });
    }

    private void toggleSelection(int position) {
        if (selectedItems.get(position)) {
            selectedItems.delete(position);
        } else {
            if (selectedItems.size() < MAX_SELECTIONS) {
                selectedItems.put(position, true);
            } else {
                Toast.makeText(context, "Maximum " + MAX_SELECTIONS + " selections allowed", Toast.LENGTH_SHORT).show();
            }
        }
        notifyItemChanged(position);
    }

    public List<Lutemon> getSelectedLutemons() {
        List<Lutemon> selected = new ArrayList<>();
        for (int i = 0; i < selectedItems.size(); i++) {
            int pos = selectedItems.keyAt(i);
            if (pos >= 0 && pos < lutemonList.size()) {
                selected.add(lutemonList.get(pos));
            }
        }
        return selected;
    }

    public void clearSelections() {
        List<Integer> positionsToNotify = new ArrayList<>();
        for (int i = 0; i < selectedItems.size(); i++) {
            positionsToNotify.add(selectedItems.keyAt(i));
        }
        selectedItems.clear();
        for (int pos : positionsToNotify) {
            if (pos >= 0 && pos < getItemCount()) {
                notifyItemChanged(pos);
            }
        }
    }

    @Override
    public int getItemCount() {
        return lutemonList.size();
    }

    public void updateLutemon(List<Lutemon> newLutemon) {
        lutemonList.clear();
        lutemonList.addAll(newLutemon);
        clearSelections();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLutemonName, tvLutemonColor, tvLutemonAttack, tvLutemonDefense, tvLutemonHealth, tvLutemonExp;
        ImageView lutemonPic;
        ViewHolder(@NonNull View itemview) {
            super(itemview);
            tvLutemonAttack = itemview.findViewById(R.id.tv_lutemonAttack);
            tvLutemonColor = itemview.findViewById(R.id.tv_LutemonColor);
            tvLutemonDefense = itemview.findViewById(R.id.tv_lutemonDefense);
            tvLutemonName = itemview.findViewById(R.id.tv_lutemonName);
            tvLutemonExp = itemview.findViewById(R.id.tv_Lutemonexp);
            tvLutemonHealth = itemview.findViewById(R.id.tv_LutemonHealth);
            lutemonPic = itemview.findViewById(R.id.IW_lutemonPic);
        }
    }
}