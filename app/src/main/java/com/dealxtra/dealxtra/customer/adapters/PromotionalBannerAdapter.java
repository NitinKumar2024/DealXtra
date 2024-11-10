package com.dealxtra.dealxtra.customer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.dealxtra.dealxtra.R;
import com.dealxtra.dealxtra.customer.models.PromotionalBanner;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class PromotionalBannerAdapter extends RecyclerView.Adapter<PromotionalBannerAdapter.BannerViewHolder> {
    private List<PromotionalBanner> banners;
    private Context context;

    public PromotionalBannerAdapter(Context context, List<PromotionalBanner> banners) {
        this.context = context;
        this.banners = banners;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_promotional_banner, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        holder.bind(banners.get(position));
    }

    @Override
    public int getItemCount() {
        return banners.size();
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        private ImageView bannerImage;
        private TextView bannerTitle;
        private TextView bannerDescription;
        private MaterialButton bannerButton;

        BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerImage = itemView.findViewById(R.id.bannerImage);
            bannerTitle = itemView.findViewById(R.id.bannerTitle);
            bannerDescription = itemView.findViewById(R.id.bannerDescription);
            bannerButton = itemView.findViewById(R.id.bannerButton);
        }

        void bind(final PromotionalBanner banner) {
            // Load image using Glide
            Glide.with(context)
                    .load(banner.getImageUrl())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(bannerImage);

            bannerTitle.setText(banner.getTitle());
            bannerDescription.setText(banner.getDescription());
            bannerButton.setText(banner.getButtonText());

            bannerButton.setOnClickListener(v -> {
                if (banner.getClickListener() != null) {
                    banner.getClickListener().onClick();
                }
            });
        }
    }
}
