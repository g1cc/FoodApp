package com.example.foodapp.Domain;

public class CartDomain
{
    private String title;
    private String image;
    private String ingredients;
    private int price;
    private int energy;
    private double fats;
    private double carbon;
    private double protein;
    private int weight;
    private int foodQTY;
    private int ID;

    public CartDomain(String title, String image, String ingredients, int price, int foodQTY, int ID) {
        this.title = title;
        this.image = image;
        this.ingredients = ingredients;
        this.price = price;
        this.foodQTY = foodQTY;
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getFoodQTY() {
        return foodQTY;
    }

    public void setFoodQTY(int foodQTY)
    {
        this.foodQTY = foodQTY;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID)
    {
        this.ID = ID;
    }
}
