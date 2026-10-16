package com.example.moblayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class FoodDetailsActivity extends AppCompatActivity {

    private FoodItem foodItem;
    private EditText etQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        foodItem = (FoodItem) getIntent().getSerializableExtra("food_item");
        if (foodItem == null) {
            finish();
            return;
        }

        ImageView ivImage = findViewById(R.id.ivDetailImage);
        TextView tvName = findViewById(R.id.tvDetailName);
        TextView tvPrice = findViewById(R.id.tvDetailPrice);
        TextView tvCategory = findViewById(R.id.tvDetailCategory);
        TextView tvDescription = findViewById(R.id.tvDetailDescription);
        etQuantity = findViewById(R.id.etDetailQuantity);

        Button btnMinus = findViewById(R.id.btnDetailMinus);
        Button btnPlus = findViewById(R.id.btnDetailPlus);
        Button btnAddToCart = findViewById(R.id.btnDetailAddToCart);
        Button btnOrderNow = findViewById(R.id.btnDetailOrderNow);
        Button btnBack = findViewById(R.id.btnBack);

        tvName.setText(foodItem.getName());
        tvPrice.setText(String.format("KSh %.0f", foodItem.getPrice()));
        tvCategory.setText("Category: " + foodItem.getCategory());
        tvDescription.setText(foodItem.getDescription());

        Glide.with(this)
                .load(foodItem.getImageUrl())
                .placeholder(android.R.drawable.ic_menu_report_image)
                .into(ivImage);

        btnPlus.setOnClickListener(v -> {
            try {
                int qty = Integer.parseInt(etQuantity.getText().toString());
                qty++;
                etQuantity.setText(String.valueOf(qty));
            } catch (NumberFormatException e) {
                etQuantity.setText("1");
            }
        });

        btnMinus.setOnClickListener(v -> {
            try {
                int qty = Integer.parseInt(etQuantity.getText().toString());
                if (qty > 1) {
                    qty--;
                    etQuantity.setText(String.valueOf(qty));
                }
            } catch (NumberFormatException e) {
                etQuantity.setText("1");
            }
        });

        btnAddToCart.setOnClickListener(v -> {
            int qty = 1;
            try {
                qty = Integer.parseInt(etQuantity.getText().toString());
                if (qty <= 0) qty = 1;
            } catch (NumberFormatException ignored) {}

            CartManager.getInstance().addItem(foodItem, qty);
            Toast.makeText(this, qty + "x " + foodItem.getName() + " added to cart", Toast.LENGTH_SHORT).show();
        });

        btnOrderNow.setOnClickListener(v -> {
            int qty = 1;
            try {
                qty = Integer.parseInt(etQuantity.getText().toString());
                if (qty <= 0) qty = 1;
            } catch (NumberFormatException ignored) {}

            CartManager.getInstance().addItem(foodItem, qty);
            Intent intent = new Intent(FoodDetailsActivity.this, CartActivity.class);
            startActivity(intent);
        });

        btnBack.setOnClickListener(v -> finish());
    }
}
