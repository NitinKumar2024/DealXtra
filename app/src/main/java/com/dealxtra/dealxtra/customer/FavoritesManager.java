package com.dealxtra.dealxtra.customer;

import android.content.Context;
import android.content.SharedPreferences;

import com.dealxtra.dealxtra.customer.models.ProductModel;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;

public class FavoritesManager {
    private static final String PREFS_NAME = "FavoritesPrefs";
    private static final String FAVORITES_KEY = "favorites";
    private final SharedPreferences prefs;
    private final Gson gson;

    public FavoritesManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void toggleFavorite(ProductModel product) {
        Set<String> favorites = getFavoriteIds();
        if (product.isFavorite()) {
            favorites.add(product.getId());
        } else {
            favorites.remove(product.getId());
        }
        prefs.edit().putStringSet(FAVORITES_KEY, favorites).apply();
    }

    public Set<String> getFavoriteIds() {
        return prefs.getStringSet(FAVORITES_KEY, new HashSet<>());
    }

    public boolean isFavorite(String productId) {
        return getFavoriteIds().contains(productId);
    }
}