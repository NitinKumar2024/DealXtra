package com.dealxtra.dealxtra;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DistanceCalculator {

    private final Context context;
    private final Geocoder geocoder;

    public DistanceCalculator(Context context) {
        this.context = context;
        this.geocoder = new Geocoder(context, Locale.getDefault());
    }

    public interface DistanceCallback {
        void onDistanceCalculated(float distanceInKm);
        void onError(String errorMessage);
    }

    public void calculateDistance(String address1, String address2, DistanceCallback callback) {
        new Thread(() -> {
            try {
                Address location1 = getLocationFromAddress(address1);
                Address location2 = getLocationFromAddress(address2);

                if (location1 == null || location2 == null) {
                    callback.onError("Unable to find one or both addresses");
                    return;
                }

                float[] results = new float[1];
                Location.distanceBetween(
                        location1.getLatitude(), location1.getLongitude(),
                        location2.getLatitude(), location2.getLongitude(),
                        results);

                float distanceInMeters = results[0];
                float distanceInKm = distanceInMeters / 1000;

                callback.onDistanceCalculated(distanceInKm);

            } catch (IOException e) {
                e.printStackTrace();
                callback.onError("Error calculating distance: " + e.getMessage());
            }
        }).start();
    }

    private Address getLocationFromAddress(String strAddress) throws IOException {
        List<Address> addresses = geocoder.getFromLocationName(strAddress, 1);
        if (addresses != null && !addresses.isEmpty()) {
            return addresses.get(0);
        }
        return null;
    }
}