package com.example.moblayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface CartActionListener {
        void onQuantityChanged(CartItem item, int newQuantity);
        void onItemRemoved(CartItem item);
    }

    private final List<CartItem> cartItems;
    private final CartActionListener listener;

    public CartAdapter(List<CartItem> cartItems, CartActionListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        FoodItem foodItem = cartItem.getFoodItem();

        holder.tvName.setText(foodItem.getName());
        holder.tvPrice.setText(String.format("KSh %.0f", cartItem.getTotalPrice()));
        holder.tvQuantity.setText(String.valueOf(cartItem.getQuantity()));

        Glide.with(holder.itemView.getContext())
                .load(foodItem.getImageUrl())
                .placeholder(android.R.drawable.ic_menu_report_image)
                .into(holder.ivImage);

        holder.btnPlus.setOnClickListener(v -> {
            int newQty = cartItem.getQuantity() + 1;
            if (listener != null) {
                listener.onQuantityChanged(cartItem, newQty);
            }
        });

        holder.btnMinus.setOnClickListener(v -> {
            int newQty = cartItem.getQuantity() - 1;
            if (listener != null) {
                listener.onQuantityChanged(cartItem, newQty);
            }
        });

        holder.btnRemove.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemRemoved(cartItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName, tvPrice, tvQuantity;
        Button btnMinus, btnPlus, btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivCartImage);
            tvName = itemView.findViewById(R.id.tvCartName);
            tvPrice = itemView.findViewById(R.id.tvCartPrice);
            tvQuantity = itemView.findViewById(R.id.tvCartQuantity);
            btnMinus = itemView.findViewById(R.id.btnCartMinus);
            btnPlus = itemView.findViewById(R.id.btnCartPlus);
            btnRemove = itemView.findViewById(R.id.btnRemoveItem);
        }
    }
}
