package com.optimizely.ab.factory.sample;

import com.optimizely.ab.factory.samplesortclasses.Product;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by asunwoo on 9/18/17.
 */
public class ProductList {
    private List<Product> productList;

    public void ProductList(){
        productList = new ArrayList<Product>(3);
        productList.add(new Product(8000, "Basketball Shoes", "Shoes"));
        productList.add(new Product(2000, "TShirt", "Clothes"));
        productList.add(new Product(7000, "Running Shoes", "Shoes"));
    }

    public List<Product> getProductList() {
        return productList;
    }

}
