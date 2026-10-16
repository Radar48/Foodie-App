package com.example.moblayout;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private CartAdapter cartAdapter;
    private TextView tvSubtotal, tvDiscountLabel, tvDiscount, tvDeliveryFee, tvCartTotal;
    private EditText etCustomerName, etPhone, etAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        RecyclerView rvCart = findViewById(R.id.rvCart);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvDiscountLabel = findViewById(R.id.tvDiscountLabel);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvDeliveryFee = findViewById(R.id.tvDeliveryFee);
        tvCartTotal = findViewById(R.id.tvCartTotal);

        etCustomerName = findViewById(R.id.etCustomerName);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);

        Button btnCartBack = findViewById(R.id.btnCartBack);
        Button btnClearCart = findViewById(R.id.btnClearCart);
        Button btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

        // Setup Cart Adapter
        cartAdapter = new CartAdapter(CartManager.getInstance().getCartItems(), new CartAdapter.CartActionListener() {
            @Override
            public void onQuantityChanged(CartItem item, int newQuantity) {
                CartManager.getInstance().updateQuantity(item.getFoodItem(), newQuantity);
                cartAdapter.notifyDataSetChanged();
                updateCalculations();
            }

            @Override
            public void onItemRemoved(CartItem item) {
                CartManager.getInstance().removeItem(item.getFoodItem());
                cartAdapter.notifyDataSetChanged();
                updateCalculations();
                Toast.makeText(CartActivity.this, item.getFoodItem().getName() + " removed from cart", Toast.LENGTH_SHORT).show();
            }
        });

        rvCart.setLayoutManager(new LinearLayoutManager(this));
        rvCart.setAdapter(cartAdapter);

        updateCalculations();

        btnCartBack.setOnClickListener(v -> finish());

        btnClearCart.setOnClickListener(v -> {
            CartManager.getInstance().clearCart();
            cartAdapter.notifyDataSetChanged();
            updateCalculations();
            Toast.makeText(this, "Cart cleared", Toast.LENGTH_SHORT).show();
        });

        btnPlaceOrder.setOnClickListener(v -> {
            List<CartItem> items = CartManager.getInstance().getCartItems();
            if (items.isEmpty()) {
                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = etCustomerName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String address = etAddress.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Please fill in all delivery details (Name, Phone, Address)", Toast.LENGTH_LONG).show();
                return;
            }

            double subtotal = CartManager.getInstance().getSubtotal();
            double discountRate = CartManager.getInstance().getDiscountRate();
            double discountAmount = CartManager.getInstance().getDiscountAmount();
            double deliveryFee = CartManager.getInstance().getDeliveryFee();
            double finalAmount = CartManager.getInstance().getFinalTotal();

            // Build detailed order summary matching the exercise
            StringBuilder itemsSummary = new StringBuilder();
            for (CartItem ci : items) {
                itemsSummary.append(String.format(Locale.getDefault(), "• %s x %d @ KSh %.0f = KSh %.0f\n",
                        ci.getFoodItem().getName(), ci.getQuantity(), ci.getFoodItem().getPrice(), ci.getTotalPrice()));
            }

            String orderReceipt = String.format(Locale.getDefault(),
                    "========================\n" +
                    "       FOOD ORDER SUMMARY\n" +
                    "========================\n" +
                    "%s" +
                    "------------------------\n" +
                    "Subtotal: KSh %.0f\n" +
                    "Discount (%.0f%%): -KSh %.0f\n" +
                    "Delivery Fee: KSh %.0f\n" +
                    "========================\n" +
                    "FINAL AMOUNT: KSh %.0f\n" +
                    "========================\n" +
                    "Customer: %s\n" +
                    "Phone: %s\n" +
                    "Address: %s",
                    itemsSummary.toString(), subtotal, discountRate, discountAmount, deliveryFee, finalAmount, name, phone, address);

            CartManager.getInstance().saveOrderHistory(this, orderReceipt);

            // Show complete success dialog
            new AlertDialog.Builder(this)
                    .setTitle("Order Placed Successfully!")
                    .setMessage(orderReceipt)
                    .setPositiveButton("OK", (dialog, which) -> {
                        CartManager.getInstance().clearCart();
                        finish();
                    })
                    .show();
        });
    }

    private void updateCalculations() {
        CartManager mgr = CartManager.getInstance();
        double subtotal = mgr.getSubtotal();
        double discountRate = mgr.getDiscountRate();
        double discountAmount = mgr.getDiscountAmount();
        double deliveryFee = mgr.getDeliveryFee();
        double finalTotal = mgr.getFinalTotal();

        tvSubtotal.setText(String.format(Locale.getDefault(), "KSh %.0f", subtotal));
        tvDiscountLabel.setText(String.format(Locale.getDefault(), "Discount (%.0f%%):", discountRate));
        tvDiscount.setText(String.format(Locale.getDefault(), "-KSh %.0f", discountAmount));
        tvDeliveryFee.setText(String.format(Locale.getDefault(), "KSh %.0f", deliveryFee));
        tvCartTotal.setText(String.format(Locale.getDefault(), "KSh %.0f", finalTotal));
    }
}
