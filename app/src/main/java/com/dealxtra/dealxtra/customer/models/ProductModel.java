package com.dealxtra.dealxtra.customer.models;

public class ProductModel {
    private String id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private int discount;
    private boolean isFavorite;
    private float rating;
    private String categoryId;     // Added category field
    private String categoryName;   // Added for convenience

    public ProductModel(String id, String name, String description, double price,
                        String imageUrl, int discount, String categoryId, String categoryName, float rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.discount = discount;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.isFavorite = false;
        this.rating = rating;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    // Add getters and setters for new fields
    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public int getDiscount() { return discount; }
    public void setDiscount(int discount) { this.discount = discount; }
    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
}

