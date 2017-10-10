package com.optimizely.ab.factory.samplesortclasses;

import java.util.Collections;
import java.util.List;

/**
 * Created by asunwoo on 9/18/17.
 */
public class CategoryProductSort implements ProductSort {

    public List<Product> sort(List unSortedList){
        CategoryComparator comparator = new CategoryComparator();
        Collections.sort(unSortedList, comparator);
        return unSortedList;
    }
}
