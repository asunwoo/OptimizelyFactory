package com.optimizely.ab.factory.samplesortclasses;

/**
 * Created by asunwoo on 9/18/17.
 */
public class Product {
    private int priceInCents;
    private String productName;
    private String categoryName;

    public Product(int priceInCents, String productName, String categoryName) {
        this.priceInCents = priceInCents;
        this.productName = productName;
        this.categoryName = categoryName;
    }

    public int getPriceInCents() {
        return priceInCents;
    }

    public void setPriceInCents(int priceInCents) {
        this.priceInCents = priceInCents;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void print(){
        System.out.println("Name: " + this.getProductName() + " Category: " + this.getCategoryName() +
                " PriceInCents: " + this.getPriceInCents());
    }
}
