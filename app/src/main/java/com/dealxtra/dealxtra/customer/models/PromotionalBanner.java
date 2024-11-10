package com.dealxtra.dealxtra.customer.models;

public class PromotionalBanner {
    private String imageUrl;
    private String title;
    private String description;
    private String buttonText;
    private OnBannerClickListener clickListener;

    public PromotionalBanner(String imageUrl, String title, String description, String buttonText, OnBannerClickListener clickListener) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
        this.buttonText = buttonText;
        this.clickListener = clickListener;
    }

    // Getters
    public String getImageUrl() { return imageUrl; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getButtonText() { return buttonText; }
    public OnBannerClickListener getClickListener() { return clickListener; }

    public interface OnBannerClickListener {
        void onClick();
    }
}
