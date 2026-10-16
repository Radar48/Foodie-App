package com.example.moblayout;

import java.io.Serializable;

public class FoodItem implements Serializable {
    private final String name;
    private final double price;
    private final String imageUrl;
    private final String category;
    private final String description;

    public FoodItem(String name, double price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = "All";
        this.description = "Delicious " + name + " prepared fresh with quality ingredients.";
    }

    public FoodItem(String name, double price, String imageUrl, String category, String description) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.description = description;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
}
