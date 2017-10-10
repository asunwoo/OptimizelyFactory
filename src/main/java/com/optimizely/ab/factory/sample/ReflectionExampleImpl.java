package com.optimizely.ab.factory.sample;

import com.optimizely.ab.factory.OptimizelyReflectionFactory;
import com.optimizely.ab.factory.samplesortclasses.ProductSort;
import com.optimizely.ab.factory.samplesortclasses.Product;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;


/**
 * Created by asunwoo on 8/16/17.
 */
public class ReflectionExampleImpl {

    public static void main (String[] args){
        Scanner scanner = new Scanner(System.in);
        String input = "";

        OptimizelyReflectionFactory<ProductSort> optimizely = new OptimizelyReflectionFactory<ProductSort>();

        String dataFileLocation = args[0];
        optimizely.initializeOptimizely(dataFileLocation);

        String experimentName = "ProductSort";

        while(!input.equals("q")){
            System.out.print("Enter a user name: ");
            input = scanner.next();

            if(input.equals("q")){
                System.exit(0);
            }

            ProductSort sort = optimizely.getExperimentImpl(experimentName, input);
            sort.sort(sampleProductList());
        }
    }

    public static List<Product> sampleProductList(){
        List<Product> productList = new ArrayList(3);
        productList.add(new Product(10000, "Tennis Shoes", "Shoes"));
        productList.add(new Product(12000, "Running Shoes", "Shoes"));
        productList.add(new Product(1000, "Running Shirt", "Shirt"));

        return productList;
    }
}
