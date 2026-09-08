package com.example.consultaproductosrest;

public class Producto {

    private int id;
    private String title;
    private double price;
    private String category;

    public Producto() {
    }

    public Producto(String title, double price, String category) {
        this.title = title;
        this.price = price;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }
}
