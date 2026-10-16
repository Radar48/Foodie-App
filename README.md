 

##Features

- **Clean Homepage Catalog**: Displays food images, names, and prices in **KSh** in a grid layout without visual clutter.
- **Real-Time Search & Category Filters**: Search food items by name and filter by categories: `All`, `Burgers`, `Pizza`, `Sides`, `Drinks`, and `Desserts`.
- **Detailed Food View (`FoodDetailsActivity`)**: Tapping any food card opens its details screen showing full descriptions, pricing, high-contrast quantity controls (`-` / `+`), and direct **Add to Cart** or **Order Now** actions.
- **Interactive Shopping Cart (`CartActivity`)**:
  - Live quantity adjustments and item removal.
  - Delivery information form (**Full Name**, **Phone Number**, **Delivery Address**).
  - Clear Cart functionality.
-  **Food Price Calculator**:
  - **Subtotal**: $\sum (\text{Price} \times \text{Quantity})$
  - **Conditional Discount**: **15% discount** for orders $\ge$ KSh 2,000; **5% discount** for orders $<$ KSh 2,000.
  - **Delivery Fee**: Flat fee of **KSh 250**.
  - **Final Amount**: $\text{Subtotal} - \text{Discount} + \text{Delivery Fee}$.
- **Itemized Order Receipt**: Generates a complete order confirmation receipt and saves order records locally using **SharedPreferences** and **Gson**.
- **High-Contrast UX/UI**: Features `NestedScrollView` for smooth full-page scrolling and navy blue / slate black typography for maximum legibility.

---

## 🛠 Tech Stack & Dependencies

- **Language**: Java 11
- **Min SDK**: 24 (Android 7.0 Nougat) | **Target SDK**: 36
- **UI Components**: `RecyclerView`, `CardView`, `NestedScrollView`, `LinearLayout`, `HorizontalScrollView`
- **Image Loading**: [Glide 4.x](https://github.com/bumptech/glide)
- **JSON Serialization**: [Gson 2.10.1](https://github.com/google/gson)
- **Build System**: Gradle (Kotlin DSL)

---

## Project Architecture

```text
app/src/main/java/com/example/moblayout/
├── MainActivity.java           # Homepage catalog, search bar, & category filters
├── FoodDetailsActivity.java    # Detailed food view, quantity selector, order actions
├── CartActivity.java           # Cart management, delivery input form, receipt dialog
├── CartManager.java            # Singleton cart state manager & calculation engine
├── FoodItem.java               # Food item data model (Name, Price, Image, Category, Description)
├── CartItem.java               # Cart item model wrapping FoodItem & Quantity
├── FoodAdapter.java            # RecyclerView adapter for homepage grid
└── CartAdapter.java            # RecyclerView adapter for cart item rows
```

---

## Sample Food Items & Categories

| Food Item | Category | Price (KSh) |
| :--- | :--- | :--- |
| **Cheese Burger** | Burgers | KSh 450 |
| **Double Cheese Burger** | Burgers | KSh 650 |
| **Chicken Pizza** | Pizza | KSh 800 |
| **Pepperoni Pizza** | Pizza | KSh 950 |
| **French Fries** | Sides | KSh 250 |
| **Cold Cola** | Drinks | KSh 150 |
| **Fresh Orange Juice** | Drinks | KSh 250 |
| **Chocolate Sundae** | Desserts | KSh 350 |
| **Ice Cream Cone** | Desserts | KSh 200 |

---

