package com.dealxtra.dealxtra.customer.ui.home.product;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.dealxtra.dealxtra.R;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.dealxtra.dealxtra.customer.adapters.ProductImageAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetails extends AppCompatActivity {

    private ViewPager2 productImageViewPager;
    private TextView productTitle, currentPrice, originalPrice, discountPercentage;
    private RatingBar ratingBar;
    private TextView ratingCount, productDescription;
    private ChipGroup sizeChipGroup;
    private MaterialButton addToCartButton;
    private FloatingActionButton wishlistButton;
    private TextView quantityValue;
    private ImageButton decreaseQuantity, increaseQuantity;
    private TextView deliveryInfo;
    private boolean isWishlisted = false;
    private int quantity = 1;
    private String selectedSize = "";

    // Sample image resources array
    private String[] productImages = {
            "https://qikink.com/wp-content/uploads/2023/06/Unisex-classic-t-Shirt-qikink.webp",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQaJh5xB5WGrdqthDwm2vO58NOeAdrSnIexyg&s"

    };

    private String[] sizes = {"S", "M", "L", "XL", "XXL"};
    private NumberFormat currencyFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_details);

        currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        initializeViews();
        setupToolbar();
        setupImageViewPager();
        setupSizeSelector();
        setupQuantityControls();
        loadProductData();
        setupClickListeners();
    }

    private void initializeViews() {
        // Initialize Toolbar and CollapsingToolbarLayout
        Toolbar toolbar = findViewById(R.id.toolbar);
//        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsingToolbar);
//        collapsingToolbar.setTitle("Product Details");

        // Initialize ViewPager and indicators
        productImageViewPager = findViewById(R.id.productImageViewPager);
        TabLayout dotsIndicator = findViewById(R.id.dotsIndicator);

        // Initialize all TextViews
        productTitle = findViewById(R.id.productTitle);
        currentPrice = findViewById(R.id.currentPrice);
        originalPrice = findViewById(R.id.originalPrice);
        discountPercentage = findViewById(R.id.discountPercentage);
        ratingCount = findViewById(R.id.ratingCount);
        productDescription = findViewById(R.id.productDescription);
        deliveryInfo = findViewById(R.id.deliveryInfo);
        quantityValue = findViewById(R.id.quantityValue);

        // Initialize Rating elements
        ratingBar = findViewById(R.id.ratingBar);

        // Initialize Buttons
        addToCartButton = findViewById(R.id.addToCartButton);
        wishlistButton = findViewById(R.id.wishlistButton);
        decreaseQuantity = findViewById(R.id.decreaseQuantity);
        increaseQuantity = findViewById(R.id.increaseQuantity);

        // Initialize Size Selector
        sizeChipGroup = findViewById(R.id.sizeChipGroup);

        // Setup strikethrough for original price
        originalPrice.setPaintFlags(originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupImageViewPager() {
        ProductImageAdapter imageAdapter = new ProductImageAdapter(productImages);
        productImageViewPager.setAdapter(imageAdapter);

        // Add page change callback for custom animations
        productImageViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Could add custom animations here
            }
        });

        // Setup dots indicator
        TabLayout dotsIndicator = findViewById(R.id.dotsIndicator);
        new TabLayoutMediator(dotsIndicator, productImageViewPager, (tab, position) -> {}).attach();
    }

    private void setupSizeSelector() {
        sizeChipGroup.removeAllViews();
        for (String size : sizes) {
            Chip chip = new Chip(this);
            chip.setText(size);
            chip.setCheckable(true);
            chip.setClickable(true);
            sizeChipGroup.addView(chip);
        }

        sizeChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != View.NO_ID) {
                Chip chip = findViewById(checkedId);
                selectedSize = chip.getText().toString();
            }
        });
    }

    private void setupQuantityControls() {
        decreaseQuantity.setEnabled(false); // Initially disabled as quantity is 1

        decreaseQuantity.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updateQuantityUI();
            }
        });

        increaseQuantity.setOnClickListener(v -> {
            if (quantity < 10) {
                quantity++;
                updateQuantityUI();
            } else {
                showMessage("Maximum quantity limit reached");
            }
        });
    }

    private void updateQuantityUI() {
        quantityValue.setText(String.valueOf(quantity));
        decreaseQuantity.setEnabled(quantity > 1);
        increaseQuantity.setEnabled(quantity < 10);
    }

    private void loadProductData() {
        // Sample product data with formatted currency
        double price = 129.99;
        double originalPriceValue = 159.99;
        int discountPercent = calculateDiscount(price, originalPriceValue);

        productTitle.setText("Nike Air Max 270");
        currentPrice.setText(currencyFormat.format(price));
        originalPrice.setText(currencyFormat.format(originalPriceValue));
        discountPercentage.setText(String.format(Locale.getDefault(), "-%d%%", discountPercent));

        // Set rating data
        float rating = 4.5f;
        ratingBar.setRating(rating);
        ratingCount.setText(String.format(Locale.getDefault(), "(%,d Reviews)", 2547));

        // Set description
        productDescription.setText(getString(R.string.product_description));

        // Set delivery info
        deliveryInfo.setText("Free delivery for orders above $50\nEstimated delivery: 2-4 business days");

        // Initialize quantity
        quantityValue.setText(String.valueOf(quantity));
    }

    private int calculateDiscount(double currentPrice, double originalPrice) {
        return (int) ((1 - (currentPrice / originalPrice)) * 100);
    }

    private void setupClickListeners() {
        addToCartButton.setOnClickListener(v -> {
            if (selectedSize.isEmpty()) {
                showMessage("Please select a size");
                return;
            }
            addToCart();
        });

        wishlistButton.setOnClickListener(v -> toggleWishlist());

        findViewById(R.id.viewReviewsButton).setOnClickListener(v ->
                showMessage("Reviews feature coming soon"));

        findViewById(R.id.sizeGuideButton).setOnClickListener(v ->
                showMessage("Size guide coming soon"));
    }

    private void addToCart() {
        String message = String.format(Locale.getDefault(),
                "Added %d item(s) - Size %s to cart", quantity, selectedSize);
        showMessage(message);

        // Here you would typically add to cart in your database/storage
    }

    private void toggleWishlist() {
        isWishlisted = !isWishlisted;
        wishlistButton.setImageResource(isWishlisted ?
                R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border);

        showMessage(isWishlisted ? "Added to wishlist" : "Removed from wishlist");
    }

    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (productImageViewPager.getCurrentItem() > 0) {
            productImageViewPager.setCurrentItem(productImageViewPager.getCurrentItem() - 1);
        } else {
            super.onBackPressed();
        }
    }
}