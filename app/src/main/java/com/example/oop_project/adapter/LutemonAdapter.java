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
import java.util.Locale;

/**
 * RecyclerView adapter for displaying Lutemon characters with selection capability.
 * Supports a maximum of 2 selected items at once.
 */
public class LutemonAdapter extends RecyclerView.Adapter<LutemonAdapter.ViewHolder> {
    // Data storage
    private final List<Lutemon> lutemonList;
    private final Context context;

    // Selection tracking
    private final SparseBooleanArray selectedItems = new SparseBooleanArray();
    private static final int MAX_SELECTIONS = 2;

    // Click listener
    private OnItemClickListener clickListener;

    /**
     * Interface for item click events
     */
    public interface OnItemClickListener {
        void onItemClick(List<Lutemon> selected);
    }

    /**
     * Constructor
     * @param context The application context
     * @param lutemonList Initial list of Lutemons to display
     */
    public LutemonAdapter(Context context, List<Lutemon> lutemonList) {
        this.context = context;
        this.lutemonList = new ArrayList<>(lutemonList);
    }

    /**
     * Sets the item click listener
     * @param listener The listener to notify on clicks
     */
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
        Lutemon lutemon = lutemonList.get(position);

        // Set text values with locale-aware formatting
        holder.tvLutemonName.setText(lutemon.getName());
        holder.tvLutemonColor.setText(lutemon.getColor());
        holder.tvLutemonHealth.setText(String.format(Locale.getDefault(),
                "HP: %d/%d", lutemon.getHealth(), lutemon.getMaxHealth()));
        holder.tvLutemonAttack.setText(String.format(Locale.getDefault(),
                "Atk: %d", lutemon.getAttack()));
        holder.tvLutemonDefense.setText(String.format(Locale.getDefault(),
                "Def: %d", lutemon.getDefense()));
        holder.tvLutemonExp.setText(String.format(Locale.getDefault(),
                "XP: %d", lutemon.getExperience()));

        // Load image using Glide
        Glide.with(context)
                .load(lutemon.getPicURL())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.lutemonPic);

        // Highlight selected items
        holder.itemView.setBackgroundColor(
                selectedItems.get(position) ? Color.LTGRAY : Color.TRANSPARENT
        );

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            toggleSelection(position);
            if (clickListener != null) {
                clickListener.onItemClick(getSelectedLutemons());
            }
        });
    }

    /**
     * Toggles selection state for an item
     * @param position The position of the item to toggle
     */
    private void toggleSelection(int position) {
        if (selectedItems.get(position)) {
            selectedItems.delete(position);
        } else {
            if (selectedItems.size() < MAX_SELECTIONS) {
                selectedItems.put(position, true);
            } else {
                Toast.makeText(context,
                        "Maximum " + MAX_SELECTIONS + " selections allowed",
                        Toast.LENGTH_SHORT).show();
            }
        }
        notifyItemChanged(position);
    }

    /**
     * Gets currently selected Lutemons
     * @return List of selected Lutemons
     */
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

    /**
     * Clears all selections
     */
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

    /**
     * Updates the adapter with new Lutemon data
     * @param newLutemon New list of Lutemons
     */
    public void updateLutemon(List<Lutemon> newLutemon) {
        lutemonList.clear();
        lutemonList.addAll(newLutemon);
        clearSelections();
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class for Lutemon items
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLutemonName, tvLutemonColor, tvLutemonAttack,
                tvLutemonDefense, tvLutemonHealth, tvLutemonExp;
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