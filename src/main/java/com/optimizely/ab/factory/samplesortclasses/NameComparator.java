package com.optimizely.ab.factory.samplesortclasses;

import java.util.Comparator;

/**
 * Created by asunwoo on 9/18/17.
 */
public class NameComparator implements Comparator {
    @Override
    public int compare(Product o1, Product o2) {
        return o1.getProductName().compareTo(o2.getProductName());
    }
}
