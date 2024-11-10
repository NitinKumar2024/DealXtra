package com.dealxtra.dealxtra.customer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dealxtra.dealxtra.R;
import com.dealxtra.dealxtra.customer.models.ProductModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.util.List;
import java.util.Locale;

// Product Adapter
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context context;
    private List<ProductModel> products;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(ProductModel product, int position);
        void onFavoriteClick(ProductModel product, int position);
        void onAddToCartClick(ProductModel product, int position);
    }

    public ProductAdapter(Context context, List<ProductModel> products, OnProductClickListener listener) {
        this.context = context;
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductModel product = products.get(position);
        holder.bind(product, position);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productName;
        private TextView productDescription;
        private TextView productPrice;
        private Chip discountChip;
        private ImageView favoriteButton;
        private MaterialButton addToCartButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productDescription = itemView.findViewById(R.id.productDescription);
            productPrice = itemView.findViewById(R.id.productPrice);
            discountChip = itemView.findViewById(R.id.discountChip);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);

            // Click listeners
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onProductClick(products.get(position), position);
                }
            });

            favoriteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onFavoriteClick(products.get(position), position);
                }
            });

            addToCartButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onAddToCartClick(products.get(position), position);
                }
            });
        }

        public void bind(ProductModel product, int position) {
            productName.setText(product.getName());
            productDescription.setText(product.getDescription());
            productPrice.setText(String.format(Locale.getDefault(), "$%.2f", product.getPrice()));

            // Handle discount chip
            if (product.getDiscount() > 0) {
                discountChip.setVisibility(View.VISIBLE);
                discountChip.setText(String.format(Locale.getDefault(), "-%d%%", product.getDiscount()));
            } else {
                discountChip.setVisibility(View.GONE);
            }

            // Handle favorite button
            favoriteButton.setImageResource(product.isFavorite() ?
                    R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border);

            // Load product image using Glide
            Glide.with(context)
                    .load(product.getImageUrl())
                    .centerCrop()
                    .placeholder(R.drawable.ic_cart)
                    .into(productImage);
        }
    }

    // Method to update products list
    public void updateProducts(List<ProductModel> newProducts) {
        this.products = newProducts;
        notifyDataSetChanged();
    }

    // Method to update a single product
    public void updateProduct(ProductModel product, int position) {
        if (position >= 0 && position < products.size()) {
            products.set(position, product);
            notifyItemChanged(position);
        }
    }
}