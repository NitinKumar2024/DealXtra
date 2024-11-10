package com.dealxtra.dealxtra.customer.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.dealxtra.dealxtra.R;
import com.dealxtra.dealxtra.customer.models.RecentSearch;

import java.util.ArrayList;
import java.util.List;

public class RecentSearchAdapter extends RecyclerView.Adapter<RecentSearchAdapter.ViewHolder> {
    private List<RecentSearch> recentSearches;
    private OnRecentSearchClickListener listener;

    public interface OnRecentSearchClickListener {
        void onRecentSearchClicked(RecentSearch recentSearch);
    }

    public RecentSearchAdapter(List<RecentSearch> recentSearches, OnRecentSearchClickListener listener) {
        this.recentSearches = recentSearches;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recent_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecentSearch recentSearch = recentSearches.get(position);
        holder.queryText.setText(recentSearch.getQuery());
        holder.timeText.setText(recentSearch.getFormattedTime());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRecentSearchClicked(recentSearch);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recentSearches.size();
    }

//    public void updateData(List<RecentSearch> newData) {
//        DiffUtil.calculateDiff(new SearchDiffCallback(recentSearches, newData))
//                .dispatchUpdatesTo(this);
//        this.recentSearches = new ArrayList<>(newData);
//    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView queryText;
        TextView timeText;

        ViewHolder(View itemView) {
            super(itemView);
            queryText = itemView.findViewById(R.id.queryText);
            timeText = itemView.findViewById(R.id.timeText);
        }
    }
}
