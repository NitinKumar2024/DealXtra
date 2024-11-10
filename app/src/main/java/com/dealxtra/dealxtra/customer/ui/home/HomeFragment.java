package com.dealxtra.dealxtra.customer.ui.home;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.dealxtra.dealxtra.customer.FavoritesManager;
import com.dealxtra.dealxtra.customer.adapters.CategoryAdapter;
import com.dealxtra.dealxtra.customer.adapters.ProductAdapter;
import com.dealxtra.dealxtra.customer.adapters.PromotionalBannerAdapter;
import com.dealxtra.dealxtra.customer.models.CategoryModel;
import com.dealxtra.dealxtra.customer.models.ProductModel;
import com.dealxtra.dealxtra.customer.models.PromotionalBanner;
import com.dealxtra.dealxtra.databinding.FragmentHomeBinding;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private RecyclerView categoriesRecyclerView, featuredProductsRecyclerView, popularItemsRecyclerView;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter featuredProductAdapter, popularProductAdapter;
    private List<CategoryModel> categories;
    private List<ProductModel> allProducts, featuredProducts, popularProducts;
  //  private SearchView searchView;
//    private ChipGroup filterChipGroup;
    private FavoritesManager favoritesManager;

    private ViewPager2 promotionalBanner;
    private TabLayout promotionalBannerIndicator;
    private PromotionalBannerAdapter bannerAdapter;
    private Handler autoScrollHandler;
    private Runnable autoScrollRunnable;
    private static final long AUTO_SCROLL_DELAY = 3000;
    private MaterialCardView searchCardView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        favoritesManager = new FavoritesManager(requireContext());

        initializeViews();
        setupRecyclerViews();
        loadSampleData();
      //  setupSearch();
        setupPromotionalBanner();
       // setupFilterChips();

        searchCardView = binding.searchCardView;

        // Set click listener for the entire search card
        searchCardView.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SearchActivity.class);
            // Add transition animation
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    getActivity(), searchCardView, "searchTransition");
            startActivity(intent, options.toBundle());
        });


        return root;
    }

    private void initializeViews() {
        categoriesRecyclerView = binding.categoriesRecyclerView;
        featuredProductsRecyclerView = binding.featuredProductsRecyclerView;
        popularItemsRecyclerView = binding.popularItemsRecyclerView;
      //  searchView = binding.searchView;
       // filterChipGroup = binding.filterChipGroup;

        // Set layout managers
        categoriesRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        featuredProductsRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        popularItemsRecyclerView.setLayoutManager(
                new GridLayoutManager(getContext(), 2));
    }

    private void setupRecyclerViews() {
        categories = new ArrayList<>();
        allProducts = new ArrayList<>();
        featuredProducts = new ArrayList<>();
        popularProducts = new ArrayList<>();

        // Category Adapter
        categoryAdapter = new CategoryAdapter(getContext(), categories,
                (category, position) -> {
                    filterProductsByCategory(category.getId());
                });
        categoriesRecyclerView.setAdapter(categoryAdapter);

        // Featured Products Adapter
        featuredProductAdapter = new ProductAdapter(getContext(), featuredProducts,
                new ProductAdapter.OnProductClickListener() {
                    @Override
                    public void onProductClick(ProductModel product, int position) {
                        navigateToProductDetail(product);
                    }

                    @Override
                    public void onFavoriteClick(ProductModel product, int position) {
                        toggleFavorite(product, position, featuredProductAdapter);
                    }

                    @Override
                    public void onAddToCartClick(ProductModel product, int position) {
                        addToCart(product);
                    }
                });
        featuredProductsRecyclerView.setAdapter(featuredProductAdapter);

        // Popular Products Adapter
        popularProductAdapter = new ProductAdapter(getContext(), popularProducts,
                new ProductAdapter.OnProductClickListener() {
                    @Override
                    public void onProductClick(ProductModel product, int position) {
                        navigateToProductDetail(product);
                    }

                    @Override
                    public void onFavoriteClick(ProductModel product, int position) {
                        toggleFavorite(product, position, popularProductAdapter);
                    }

                    @Override
                    public void onAddToCartClick(ProductModel product, int position) {
                        addToCart(product);
                    }
                });
        popularItemsRecyclerView.setAdapter(popularProductAdapter);
    }


    private void loadSampleData() {
        // Sample Categories
        categories.addAll(Arrays.asList(
                new CategoryModel("1", "All", "url_all"),
                new CategoryModel("2", "Clothes", "https://encrypted-tbn3.gstatic.com/shopping?q=tbn:ANd9GcQyaOrVO2VbIay-pD4IlMbE2_CA_y157VLtY5Ow0xH2IKKaGiV1Wl-Wh9h1bCSQWB53fEcNoI2uYwhHn_9SROEG8Oa2FC2p8HTRL5br4fNB"),
                new CategoryModel("3", "Grocery", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQy6F0MkxmUtK3kQRy_dRfYMYHnlhNzBzWDQA&s"),
                new CategoryModel("4", "Restaurant", "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/1d/30/54/b2/bidri-ambience.jpg?w=600&h=-1&s=1"),
                new CategoryModel("5", "Electronics", "https://alloy.ai/wp-content/uploads/2023/04/industries-consumer-electronics.jpeg"),
                new CategoryModel("6", "Books", "https://thumbs.dreamstime.com/b/old-book-flying-letters-magic-light-background-bookshelf-library-ancient-books-as-symbol-knowledge-history-218640948.jpg"),
                new CategoryModel("7", "Sports", "url_sports")
        ));
        categoryAdapter.notifyDataSetChanged();

        // Extended Sample Products
        List<ProductModel> sampleProducts = Arrays.asList(
                // Clothes
                new ProductModel("1", "Cotton T-Shirt", "Comfortable casual t-shirt", 29.99,
                        "https://encrypted-tbn2.gstatic.com/shopping?q=tbn:ANd9GcQNIyo1h3dz147Ia6svUrN4waEuimUOJL5b8xq19zklIZb5IRX2XYb9OApixVK61hMvPcALT4YE-c1GKBOyA4gLFhwblCoakhsiHiLWXHyXo40vFu57dLpSJw", 10, "2", "Clothes", 5),
                new ProductModel("2", "Denim Jeans", "Classic blue jeans", 59.99,
                        "https://encrypted-tbn0.gstatic.com/shopping?q=tbn:ANd9GcSdFuItPVo8wbkk1kI3H_Zse224po0Kai8O7kXdr8IsPzRdAxRLr_HYSe_9XP6jzDW4kuFvHhzDuZcjiTsFLOXqEenq3KsKQesbpS7bLoxA8SovjE4DxCMn", 15, "2", "Clothes", 4.5F),
                new ProductModel("3", "Summer Dress", "Floral pattern dress", 49.99,
                        "https://encrypted-tbn3.gstatic.com/shopping?q=tbn:ANd9GcQyaOrVO2VbIay-pD4IlMbE2_CA_y157VLtY5Ow0xH2IKKaGiV1Wl-Wh9h1bCSQWB53fEcNoI2uYwhHn_9SROEG8Oa2FC2p8HTRL5br4fNB", 20, "2", "Clothes", 4),

                // Grocery
                new ProductModel("4", "Fresh Apples", "Organic red apples 1kg", 4.99,
                        "url_apples", 0, "3", "Grocery", 2),
                new ProductModel("5", "Whole Milk", "Farm fresh milk 1L", 3.99,
                        "url_milk", 0, "3", "Grocery", 3),
                new ProductModel("6", "Whole Wheat Bread", "Freshly baked bread", 2.99,
                        "url_bread", 5, "3", "Grocery", 2),

                // Restaurant
                new ProductModel("7", "Margherita Pizza", "Classic Italian pizza", 15.99,
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRckDwKsSN5uj3qbhnPMyW3Aso3U6k9zX3CVHxv79chugH2K6dnHw&s", 20, "4", "Restaurant", 1),
                new ProductModel("8", "Chicken Burger", "Grilled chicken burger", 12.99,
                        "url_burger", 10, "4", "Restaurant", 4),
                new ProductModel("9", "Caesar Salad", "Fresh garden salad", 8.99,
                        "url_salad", 0, "4", "Restaurant", 8)





        );

        // Restore favorite status from SharedPreferences
        Set<String> favoriteIds = favoritesManager.getFavoriteIds();
        sampleProducts.forEach(product ->
                product.setFavorite(favoriteIds.contains(product.getId())));

        allProducts.addAll(sampleProducts);

        // Set featured products (products with discounts)
        featuredProducts.addAll(sampleProducts.stream()
                .filter(p -> p.getDiscount() > 0)
                .limit(6)
                .collect(Collectors.toList()));

        // Set popular products (all products for now)
        popularProducts.addAll(sampleProducts);

        featuredProductAdapter.notifyDataSetChanged();
        popularProductAdapter.notifyDataSetChanged();
    }
//    private void setupSearch() {
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                filterProducts(query);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                filterProducts(newText);
//                return true;
//            }
//        });
//    }

//    private void setupFilterChips() {
//        filterChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
//            if (checkedIds.isEmpty()) {
//                // Show all products if no chip is selected
//                resetToAllProducts();
//            } else {
//                // Get selected chip and filter
//                Chip selectedChip = group.findViewById(checkedIds.get(0));
//                if (selectedChip != null) {
//                    String category = selectedChip.getText().toString();
//                    filterProductsByCategory(category);
//                }
//            }
//        });
//    }

    private void filterProducts(String query) {
        if (query.isEmpty()) {
            resetToAllProducts();
            return;
        }

        String lowercaseQuery = query.toLowerCase();
        List<ProductModel> filteredProducts = allProducts.stream()
                .filter(product ->
                        product.getName().toLowerCase().contains(lowercaseQuery) ||
                                product.getDescription().toLowerCase().contains(lowercaseQuery))
                .collect(Collectors.toList());

        updateProductLists(filteredProducts);
    }


    private void resetToAllProducts() {
        updateProductLists(allProducts);
    }

    private void updateProductLists(List<ProductModel> products) {
        // Update featured products (products with discounts)
        featuredProducts.clear();
        featuredProducts.addAll(products.stream()
                .filter(p -> p.getDiscount() > 0)
                .limit(3)
                .collect(Collectors.toList()));
        featuredProductAdapter.notifyDataSetChanged();

        // Update popular products
        popularProducts.clear();
        popularProducts.addAll(products);
        popularProductAdapter.notifyDataSetChanged();
    }



    private void addToCart(ProductModel product) {
        // Here you would add the product to the shopping cart
        // This is just a placeholder implementation
        Toast.makeText(getContext(),
                "Added " + product.getName() + " to cart",
                Toast.LENGTH_SHORT).show();
    }

    private void navigateToProductDetail(ProductModel product) {
        // Navigate to product detail screen
        // Example using Navigation component:
        Bundle bundle = new Bundle();
        bundle.putString("productId", product.getId());
//        Navigation.findNavController(requireView())
//                .navigate(R.id.action_homeFragment_to_productDetailFragment, bundle);
    }

    private void toggleFavorite(ProductModel product, int position, ProductAdapter adapter) {
        product.setFavorite(!product.isFavorite());
        favoritesManager.toggleFavorite(product);
        adapter.notifyItemChanged(position);

        // Update the product in other adapters if it exists there
        updateProductInOtherAdapters(product);

        Toast.makeText(getContext(),
                product.isFavorite() ? "Added to favorites" : "Removed from favorites",
                Toast.LENGTH_SHORT).show();
    }

    private void updateProductInOtherAdapters(ProductModel product) {
        // Update in featured products
        int featuredIndex = featuredProducts.indexOf(product);
        if (featuredIndex != -1) {
            featuredProducts.get(featuredIndex).setFavorite(product.isFavorite());
            featuredProductAdapter.notifyItemChanged(featuredIndex);
        }

        // Update in popular products
        int popularIndex = popularProducts.indexOf(product);
        if (popularIndex != -1) {
            popularProducts.get(popularIndex).setFavorite(product.isFavorite());
            popularProductAdapter.notifyItemChanged(popularIndex);
        }
    }

    private void filterProductsByCategory(String categoryId) {
        if (categoryId.equals("1")) { // "All" category
            resetToAllProducts();
            return;
        }

        List<ProductModel> filteredProducts = allProducts.stream()
                .filter(product -> product.getCategoryId().equals(categoryId))
                .collect(Collectors.toList());

        updateProductLists(filteredProducts);
    }

    // Add method to get favorite products
    public List<ProductModel> getFavoriteProducts() {
        return allProducts.stream()
                .filter(ProductModel::isFavorite)
                .collect(Collectors.toList());
    }

    // Add method to filter by price range
    private void filterByPriceRange(double minPrice, double maxPrice) {
        List<ProductModel> filteredProducts = allProducts.stream()
                .filter(product -> product.getPrice() >= minPrice && product.getPrice() <= maxPrice)
                .collect(Collectors.toList());

        updateProductLists(filteredProducts);
    }

    // Add method to filter by discount
    private void filterByDiscount(int minDiscount) {
        List<ProductModel> filteredProducts = allProducts.stream()
                .filter(product -> product.getDiscount() >= minDiscount)
                .collect(Collectors.toList());

        updateProductLists(filteredProducts);
    }


    private void setupPromotionalBanner() {
        promotionalBanner = binding.promotionalBanner;
        promotionalBannerIndicator = binding.promotionalBannerIndicator;
        // Create sample banner data
        List<PromotionalBanner> banners = new ArrayList<>();
        banners.add(new PromotionalBanner(
                "https://buffer.com/cdn-cgi/image/w=1000,fit=contain,q=90,f=auto/library/content/images/size/w1200/2023/10/free-images.jpg",
                "Summer Sale",
                "Get up to 50% off on summer collection",
                "Shop Now",
                () -> handleBannerClick(0)
        ));
        banners.add(new PromotionalBanner(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRmCy16nhIbV3pI1qLYHMJKwbH2458oiC9EmA&s",
                "New Arrivals",
                "Check out our latest collection",
                "Explore",
                () -> handleBannerClick(1)
        ));

        // Setup adapter
        bannerAdapter = new PromotionalBannerAdapter(getContext(), banners);
        promotionalBanner.setAdapter(bannerAdapter);
        promotionalBanner.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        // Setup page transformer for animation
        promotionalBanner.setPageTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });

        // Setup indicator
        new TabLayoutMediator(promotionalBannerIndicator, promotionalBanner,
                (tab, position) -> {
                    // No title needed for indicators
                }).attach();

        // Setup auto-scroll
        setupAutoScroll();
    }

    private void setupAutoScroll() {
        autoScrollHandler = new Handler(Looper.getMainLooper());
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                int nextItem = (promotionalBanner.getCurrentItem() + 1) % bannerAdapter.getItemCount();
                promotionalBanner.setCurrentItem(nextItem, true);
                autoScrollHandler.postDelayed(this, AUTO_SCROLL_DELAY);
            }
        };
    }

    private void startAutoScroll() {
        autoScrollHandler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);
    }

    private void stopAutoScroll() {
        autoScrollHandler.removeCallbacks(autoScrollRunnable);

    }

    private void handleBannerClick(int position) {
        // Handle banner click based on position
        Toast.makeText(getContext(), "Banner " + position + " clicked", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onPause() {
        stopAutoScroll();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        startAutoScroll();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
