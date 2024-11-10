package com.dealxtra.dealxtra.customer;

import android.os.Bundle;

import com.dealxtra.dealxtra.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.dealxtra.dealxtra.databinding.ActivityCustomerMainBottomBinding;

public class CustomerMainBottomActivity extends AppCompatActivity {

    private ActivityCustomerMainBottomBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCustomerMainBottomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_customer_main_bottom);

        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}