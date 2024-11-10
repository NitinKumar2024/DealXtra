package com.dealxtra.dealxtra.useful_classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import java.io.ByteArrayOutputStream;

public class ShopDataStorage {

    private static final String PREFS_NAME = "ShopDataPrefs";
    private static final String SHOP_NAME_KEY = "shop_name";
    private static final String SHOP_LOCATION_KEY = "shop_location";
    private static final String SHOP_DESCRIPTION_KEY = "shop_description";
    private static final String SHOP_IMAGE_KEY = "shop_image";
    private static final String SHOP_ID_KEY = "shop_id";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Constructor
    public ShopDataStorage(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Method to store shop data
    public void storeShopData(String name, String location, String description, Bitmap image, String id) {
        editor.putString(SHOP_NAME_KEY, name);
        editor.putString(SHOP_LOCATION_KEY, location);
        editor.putString(SHOP_ID_KEY, id);

        // If description exists, store it
        if (description != null && !description.isEmpty()) {
            editor.putString(SHOP_DESCRIPTION_KEY, description);
        }

        // Convert image (Bitmap) to Base64 string and store it
        if (image != null) {
            String imageBase64 = encodeImageToBase64(image);
            editor.putString(SHOP_IMAGE_KEY, imageBase64);
        }

        editor.apply(); // Save the data
    }

    // Method to retrieve shop data
    public ShopData getShopData() {
        String name = sharedPreferences.getString(SHOP_NAME_KEY, null);
        String shop_id = sharedPreferences.getString(SHOP_ID_KEY, null);
        String location = sharedPreferences.getString(SHOP_LOCATION_KEY, null);
        String description = sharedPreferences.getString(SHOP_DESCRIPTION_KEY, null);
        String imageBase64 = sharedPreferences.getString(SHOP_IMAGE_KEY, null);
        Bitmap image = decodeBase64ToImage(imageBase64);

        return new ShopData(name, location, description, image, shop_id);
    }

    // Encode Bitmap image to Base64 string
    private String encodeImageToBase64(Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    // Decode Base64 string back to Bitmap image
    private Bitmap decodeBase64ToImage(String imageBase64) {
        if (imageBase64 == null || imageBase64.isEmpty()) {
            return null;
        }
        byte[] imageBytes = Base64.decode(imageBase64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    // Method to clear shop data
    public void clearShopData() {
        editor.clear();
        editor.apply();
    }

    // Inner class to hold shop data
    public static class ShopData {
        private String name;
        private String location;
        private String description, shop_id;
        private Bitmap image;

        public ShopData(String name, String location, String description, Bitmap image, String shop_id) {
            this.name = name;
            this.location = location;
            this.description = description;
            this.image = image;
            this.shop_id = shop_id;
        }

        // Getters for shop data
        public String getName() {
            return name;
        }

        public String getLocation() {
            return location;
        }

        public String getDescription() {
            return description;
        }
        public String getShop_id() {
            return shop_id;
        }

        public Bitmap getImage() {
            return image;
        }
    }
}

