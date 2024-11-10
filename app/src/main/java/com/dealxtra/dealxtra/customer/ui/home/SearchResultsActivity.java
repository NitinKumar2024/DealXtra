package com.dealxtra.dealxtra.customer.ui.home;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dealxtra.dealxtra.R;import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dealxtra.dealxtra.customer.adapters.ProductAdapter;
import com.dealxtra.dealxtra.customer.models.ProductModel;
import com.dealxtra.dealxtra.customer.ui.home.product.ProductDetails;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchResultsActivity extends AppCompatActivity {
    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private List<ProductModel> allProducts;
    private List<ProductModel> filteredProducts;
    private EditText searchEditText;
    private TextView txtResultCount;
    private MaterialCardView loadingCard;
    private View noResultsLayout;
    private MaterialButton btnFilter, btnSort, btnViewType, btnClearFilters;
    private ImageButton btnBack, btnVoiceSearch;
    private boolean isGridView = true;
    private String currentQuery = "";
    private String currentSortOption = "relevance";
    private final Handler handler = new Handler();
    private static final long SEARCH_DELAY_MS = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_results);


        initializeViews();
        setupListeners();
        loadSampleData();

        // Get search query from intent
        String query = getIntent().getStringExtra("query");
        if (query != null && !query.isEmpty()) {
            searchEditText.setText(query);
            currentQuery = query;
            performSearch(query);
        }
    }

    private void initializeViews() {
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        searchEditText = findViewById(R.id.searchEditText);
        txtResultCount = findViewById(R.id.txtResultCount);
        loadingCard = findViewById(R.id.loadingCard);
        noResultsLayout = findViewById(R.id.noResultsLayout);
        btnFilter = findViewById(R.id.btnFilter);
        btnSort = findViewById(R.id.btnSort);
        btnViewType = findViewById(R.id.btnViewType);
        btnBack = findViewById(R.id.btnBack);
        btnVoiceSearch = findViewById(R.id.btnVoiceSearch);
        btnClearFilters = findViewById(R.id.btnClearFilters);

        // Initialize RecyclerView with Grid Layout
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerViewProducts.setLayoutManager(gridLayoutManager);
        productAdapter = new ProductAdapter(this, new ArrayList<>(), new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(ProductModel product, int position) {
                navigateToProductDetail(product);
            }

            @Override
            public void onFavoriteClick(ProductModel product, int position) {
              //  toggleFavorite(product, position, featuredProductAdapter);
            }

            @Override
            public void onAddToCartClick(ProductModel product, int position) {
              //  addToCart(product);
            }
        });
        recyclerViewProducts.setAdapter(productAdapter);
    }

    private void navigateToProductDetail(ProductModel product) {
        Intent intent = new Intent(SearchResultsActivity.this, ProductDetails.class);
        intent.putExtra("product", product.getId());
        startActivity(intent);

    }

    private void setupListeners() {
        // Search text change listener with debounce
        searchEditText.setOnClickListener(v -> {
            Intent intent = new Intent(SearchResultsActivity.this, SearchActivity.class);
            intent.putExtra("query", searchEditText.getText().toString());
            startActivity(intent);
            finish();
        });


        btnBack.setOnClickListener(v -> finish());

        btnVoiceSearch.setOnClickListener(v -> {
            // Implement voice search functionality if needed
        });

        btnViewType.setOnClickListener(v -> toggleViewType());

        btnSort.setOnClickListener(v -> showSortOptions());

        btnFilter.setOnClickListener(v -> showFilterOptions());

        btnClearFilters.setOnClickListener(v -> clearFilters());
    }

    private void loadSampleData() {
        allProducts = new ArrayList<>();

        // Add sample products using your ProductModel
        allProducts.add(new ProductModel(
                "1",
                "iPhone 13 Pro",
                "Apple's flagship smartphone with A15 Bionic chip",
                999.99,
                "https://m.media-amazon.com/images/I/61sdo1TfSHL.jpg",
                10,
                "cat_electronics",
                "Electronics",
                4.5f
        ));

        allProducts.add(new ProductModel(
                "2",
                "Samsung 4K TV",
                "65-inch QLED Smart TV",
                1299.99,
                "https://m.media-amazon.com/images/I/71G3w6wIhZL.jpg",
                15,
                "cat_electronics",
                "Electronics",
                4.3f
        ));

        allProducts.add(new ProductModel(
                "3",
                "Nike Air Max",
                "Comfortable running shoes",
                129.99,
                "https://qikink.com/wp-content/uploads/2023/06/Unisex-classic-t-Shirt-qikink.webp",
                20,
                "cat_fashion",
                "Fashion",
                4.7f
        ));

        // Add more sample products as needed
    }

    private void performSearch(String query) {
        currentQuery = query.toLowerCase().trim();
        showLoading();

        // Simulate network delay
        handler.postDelayed(() -> {
            filteredProducts = allProducts.stream()
                    .filter(product ->
                            product.getName().toLowerCase().contains(currentQuery) ||
                                    product.getDescription().toLowerCase().contains(currentQuery) ||
                                    product.getCategoryName().toLowerCase().contains(currentQuery))
                    .collect(Collectors.toList());

            applySorting();
            updateUI();
        }, 500); // Simulated delay of 500ms
    }

    private void showLoading() {
        loadingCard.setVisibility(View.VISIBLE);
        noResultsLayout.setVisibility(View.GONE);
        recyclerViewProducts.setVisibility(View.GONE);
    }

    private void updateUI() {
        loadingCard.setVisibility(View.GONE);

        if (filteredProducts.isEmpty()) {
            noResultsLayout.setVisibility(View.VISIBLE);
            recyclerViewProducts.setVisibility(View.GONE);
            txtResultCount.setText("No results found");
        } else {
            noResultsLayout.setVisibility(View.GONE);
            recyclerViewProducts.setVisibility(View.VISIBLE);
            txtResultCount.setText(filteredProducts.size() + " results found");

            productAdapter.updateProducts(filteredProducts);
            recyclerViewProducts.scheduleLayoutAnimation();
        }
    }

    private void toggleViewType() {
        isGridView = !isGridView;
        btnViewType.setText(isGridView ? "Grid" : "List");

        RecyclerView.LayoutManager layoutManager = isGridView ?
                new GridLayoutManager(this, 2) :
                new LinearLayoutManager(this);

        recyclerViewProducts.setLayoutManager(layoutManager);
        recyclerViewProducts.scheduleLayoutAnimation();
    }

    private void showSortOptions() {
        String[] options = {"Relevance", "Price: Low to High", "Price: High to Low", "Rating", "Discount"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Sort by")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0: currentSortOption = "relevance"; break;
                        case 1: currentSortOption = "price_asc"; break;
                        case 2: currentSortOption = "price_desc"; break;
                        case 3: currentSortOption = "rating"; break;
                        case 4: currentSortOption = "discount"; break;
                    }
                    applySorting();
                    updateUI();
                })
                .show();
    }

    private void applySorting() {
        switch (currentSortOption) {
            case "price_asc":
                filteredProducts.sort((p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()));
                break;
            case "price_desc":
                filteredProducts.sort((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
                break;
            case "rating":
                filteredProducts.sort((p1, p2) -> Float.compare(p2.getRating(), p1.getRating()));
                break;
            case "discount":
                filteredProducts.sort((p1, p2) -> Integer.compare(p2.getDiscount(), p1.getDiscount()));
                break;
            default: // relevance - keep original order
                break;
        }
    }

    private void showFilterOptions() {
        // Get unique categories from products
        List<String> categories = allProducts.stream()
                .map(ProductModel::getCategoryName)
                .distinct()
                .collect(Collectors.toList());

        categories.add(0, "All"); // Add "All" option at the beginning

        String[] categoryArray = categories.toArray(new String[0]);
        boolean[] checkedItems = new boolean[categoryArray.length];

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Filter by Category")
                .setMultiChoiceItems(categoryArray, checkedItems, (dialog, which, isChecked) -> {
                    checkedItems[which] = isChecked;
                })
                .setPositiveButton("Apply", (dialog, which) -> {
                    applyFilters(categoryArray, checkedItems);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void applyFilters(String[] categories, boolean[] checkedItems) {
        List<String> selectedCategories = new ArrayList<>();
        for (int i = 0; i < checkedItems.length; i++) {
            if (checkedItems[i]) {
                selectedCategories.add(categories[i]);
            }
        }

        if (!selectedCategories.isEmpty() && !selectedCategories.contains("All")) {
            filteredProducts = filteredProducts.stream()
                    .filter(product -> selectedCategories.contains(product.getCategoryName()))
                    .collect(Collectors.toList());
            updateUI();
        }
    }

    private void clearFilters() {
        currentSortOption = "relevance";
        performSearch(currentQuery);
    }
}