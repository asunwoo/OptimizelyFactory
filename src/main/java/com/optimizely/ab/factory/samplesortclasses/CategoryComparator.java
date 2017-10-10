package com.optimizely.ab.factory.samplesortclasses;

import java.util.Comparator;

/**
 * Created by asunwoo on 9/18/17.
 */
public class CategoryComparator<Product, Product> implements Comparator {
    @Override
    public int compare(Product o1, Product o2) {
        return o1.getCategoryName().compareTo(o2.getCategoryName());
    }
}
