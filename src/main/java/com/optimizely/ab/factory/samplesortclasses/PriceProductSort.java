package com.optimizely.ab.factory.samplesortclasses;

import java.util.Collections;
import java.util.List;

/**
 * Created by asunwoo on 9/18/17.
 */
public class PriceProductSort implements ProductSort {
    public List sort(List unSortedList){
        PriceComparator comparator = new PriceComparator();
        Collections.sort(unSortedList, comparator);
        return unSortedList;
    }
}
