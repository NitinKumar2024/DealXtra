package com.dealxtra.dealxtra;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dealxtra.dealxtra.api_service.SecureStorage;
import com.dealxtra.dealxtra.auth.SignUpActivity;
import com.dealxtra.dealxtra.customer.CustomerMainBottomActivity;
import com.dealxtra.dealxtra.useful_classes.ShopDataStorage;

public class MainActivity extends AppCompatActivity {
    private DistanceCalculator distanceCalculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        SecureStorage secureStorage = new SecureStorage(this);

        ShopDataStorage shopDataStorage = new ShopDataStorage(this);

        // Retrieve shop data
        ShopDataStorage.ShopData shopData = shopDataStorage.getShopData();
        String shopName = shopData.getName();
        String shopLocation = shopData.getLocation();
        String shopDescription = shopData.getDescription();
        Bitmap shopImage = shopData.getImage();




        String name = secureStorage.getName();
        String email = secureStorage.getEmail();
        String token = secureStorage.getToken();


        if (token.isEmpty()){

            startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            finish();
        }
        else {
            startActivity(new Intent(MainActivity.this, CustomerMainBottomActivity.class));
            finish();
        }



    }

    private void calculateDistance() {

        //        distanceCalculator = new DistanceCalculator(this);
//
//        Button calculateButton = findViewById(R.id.calculateButton);
//        calculateButton.setOnClickListener(v -> calculateDistance());
        String address1 = "1600 Amphitheatre Parkway, Mountain View, CA";
        String address2 = "1 Infinite Loop, Cupertino, CA";

        distanceCalculator.calculateDistance(address1, address2, new DistanceCalculator.DistanceCallback() {
            @Override
            public void onDistanceCalculated(float distanceInKm) {
                runOnUiThread(() -> {
                    // Update UI with the distance
                    //    TextView resultView = findViewById(R.id.resultView);
                    //   resultView.setText(String.format("Distance: %.2f km", distanceInKm));
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    // Show error message
                    // Toast.makeText(YourActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }}