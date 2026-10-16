package com.example.moblayout;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private final List<CartItem> cartItems;
    private static final String PREF_NAME = "FoodieAppPrefs";
    private static final String KEY_ORDERS = "OrderHistory";

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void addItem(FoodItem item, int quantity) {
        if (quantity <= 0) quantity = 1;
        for (CartItem ci : cartItems) {
            if (ci.getFoodItem().getName().equals(item.getName())) {
                ci.setQuantity(ci.getQuantity() + quantity);
                return;
            }
        }
        cartItems.add(new CartItem(item, quantity));
    }

    public void removeItem(FoodItem item) {
        cartItems.removeIf(ci -> ci.getFoodItem().getName().equals(item.getName()));
    }

    public void updateQuantity(FoodItem item, int quantity) {
        if (quantity <= 0) {
            removeItem(item);
            return;
        }
        for (CartItem ci : cartItems) {
            if (ci.getFoodItem().getName().equals(item.getName())) {
                ci.setQuantity(quantity);
                return;
            }
        }
    }

    public void clearCart() {
        cartItems.clear();
    }

    public double getSubtotal() {
        double subtotal = 0;
        for (CartItem ci : cartItems) {
            subtotal += ci.getTotalPrice();
        }
        return subtotal;
    }

    public double getDiscountRate() {
        double subtotal = getSubtotal();
        if (subtotal >= 2000) {
            return 15.0; // 15% discount for orders KSh 2,000 or above
        } else if (subtotal > 0) {
            return 5.0;  // 5% discount for orders below KSh 2,000
        } else {
            return 0.0;
        }
    }

    public double getDiscountAmount() {
        return getSubtotal() * (getDiscountRate() / 100.0);
    }

    public double getDeliveryFee() {
        return cartItems.isEmpty() ? 0.0 : 250.0; // Flat delivery fee of KSh 250
    }

    public double getFinalTotal() {
        if (cartItems.isEmpty()) return 0.0;
        return getSubtotal() - getDiscountAmount() + getDeliveryFee();
    }

    public int getCartItemCount() {
        int count = 0;
        for (CartItem ci : cartItems) {
            count += ci.getQuantity();
        }
        return count;
    }

    public void saveOrderHistory(Context context, String orderDetails) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String historyJson = prefs.getString(KEY_ORDERS, "[]");
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        List<String> orders = gson.fromJson(historyJson, type);
        if (orders == null) {
            orders = new ArrayList<>();
        }
        orders.add(orderDetails);
        prefs.edit().putString(KEY_ORDERS, gson.toJson(orders)).apply();
    }

    public List<String> getOrderHistory(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String historyJson = prefs.getString(KEY_ORDERS, "[]");
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        List<String> orders = gson.fromJson(historyJson, type);
        return orders != null ? orders : new ArrayList<>();
    }
}
