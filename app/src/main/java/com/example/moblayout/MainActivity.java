package com.example.moblayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<FoodItem> foodList;
    private FoodAdapter adapter;
    private String currentCategory = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Initialize UI
        EditText etSearch = findViewById(R.id.etSearch);
        Button btnSearch = findViewById(R.id.btnSearch);
        Button btnViewCart = findViewById(R.id.btnViewCart);
        RecyclerView recyclerView = findViewById(R.id.rvFood);

        // Category buttons
        Button btnCatAll = findViewById(R.id.btnCatAll);
        Button btnCatBurgers = findViewById(R.id.btnCatBurgers);
        Button btnCatPizza = findViewById(R.id.btnCatPizza);
        Button btnCatSides = findViewById(R.id.btnCatSides);
        Button btnCatDrinks = findViewById(R.id.btnCatDrinks);
        Button btnCatDesserts = findViewById(R.id.btnCatDesserts);

        // 2. Initialize FoodList with suggested items in KSh and Categories
        foodList = new ArrayList<>();
        foodList.add(new FoodItem("Cheese Burger", 450, "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=500&auto=format&fit=crop&q=60", "Burgers", "Juicy beef patty with melted cheese, crisp lettuce, and signature sauce."));
        foodList.add(new FoodItem("Chicken Pizza", 800, "https://images.unsplash.com/photo-1628840042765-356cda07504e?w=500&auto=format&fit=crop&q=60", "Pizza", "Loaded with tender chicken chunks, mozzarella cheese, and rich tomato sauce."));
        foodList.add(new FoodItem("French Fries", 250, "https://images.unsplash.com/photo-1573080496219-bb080dd4f877?w=500&auto=format&fit=crop&q=60", "Sides", "Golden crispy potato fries seasoned with savory spices."));
        foodList.add(new FoodItem("Double Cheese Burger", 650, "https://images.unsplash.com/photo-1586190848861-99aa4a171e90?w=500&auto=format&fit=crop&q=60", "Burgers", "Double beef patties, double melted cheese, pickles, and signature sauce."));
        foodList.add(new FoodItem("Pepperoni Pizza", 950, "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=500&auto=format&fit=crop&q=60", "Pizza", "Classic pepperoni slices with extra melted mozzarella cheese."));
        foodList.add(new FoodItem("Cold Cola", 150, "https://images.unsplash.com/photo-1622483767028-3f66f32aef97?w=500&auto=format&fit=crop&q=60", "Drinks", "Refreshing ice-cold carbonated cola beverage."));
        foodList.add(new FoodItem("Fresh Orange Juice", 250, "https://images.unsplash.com/photo-1613478223719-2ab802602423?w=500&auto=format&fit=crop&q=60", "Drinks", "Freshly squeezed sweet and tangy orange juice."));
        foodList.add(new FoodItem("Chocolate Sundae", 350, "https://images.unsplash.com/photo-1563805042-7684c019e1cb?w=500&auto=format&fit=crop&q=60", "Desserts", "Rich vanilla ice cream topped with hot fudge and chocolate sprinkles."));
        foodList.add(new FoodItem("Ice Cream Cone", 200, "https://images.unsplash.com/photo-1557142046-c704a3adf3ea?w=500&auto=format&fit=crop&q=60", "Desserts", "Creamy scoop of vanilla ice cream in a crispy waffle cone."));

        // 3. Setup RecyclerView Adapter
        adapter = new FoodAdapter(foodList, item -> {
            Intent intent = new Intent(MainActivity.this, FoodDetailsActivity.class);
            intent.putExtra("food_item", item);
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        // 4. Search Logic
        btnSearch.setOnClickListener(v -> {
            String query = etSearch.getText().toString().trim();
            filter(query, currentCategory);
        });

        // 5. View Cart Button
        btnViewCart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });

        // 6. Category Filter Listeners
        View.OnClickListener categoryListener = v -> {
            Button clickedBtn = (Button) v;
            currentCategory = clickedBtn.getText().toString();

            // Highlight active button style
            resetCategoryButtons(btnCatAll, btnCatBurgers, btnCatPizza, btnCatSides, btnCatDrinks, btnCatDesserts);
            clickedBtn.setBackgroundResource(R.drawable.bg_button);
            clickedBtn.setTextColor(getResources().getColor(android.R.color.white));

            filter(etSearch.getText().toString().trim(), currentCategory);
        };

        btnCatAll.setOnClickListener(categoryListener);
        btnCatBurgers.setOnClickListener(categoryListener);
        btnCatPizza.setOnClickListener(categoryListener);
        btnCatSides.setOnClickListener(categoryListener);
        btnCatDrinks.setOnClickListener(categoryListener);
        btnCatDesserts.setOnClickListener(categoryListener);
    }

    private void resetCategoryButtons(Button... buttons) {
        for (Button btn : buttons) {
            btn.setBackgroundResource(R.drawable.bg_search);
            btn.setTextColor(getResources().getColor(R.color.text_navy));
        }
    }

    private void filter(String query, String category) {
        List<FoodItem> filteredList = new ArrayList<>();
        for (FoodItem item : foodList) {
            boolean matchesCategory = category.equals("All") || item.getCategory().equalsIgnoreCase(category);
            boolean matchesQuery = query.isEmpty() || item.getName().toLowerCase().contains(query.toLowerCase());

            if (matchesCategory && matchesQuery) {
                filteredList.add(item);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No food items found matching your criteria", Toast.LENGTH_SHORT).show();
        }
        adapter.setFilteredList(filteredList);
    }
}
