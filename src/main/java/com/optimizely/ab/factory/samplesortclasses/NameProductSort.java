package com.optimizely.ab.factory.samplesortclasses;

import java.util.Collections;
import java.util.List;

/**
 * Created by asunwoo on 9/18/17.
 */
public class NameProductSort implements ProductSort {
    public List sort(List unSortedList){
        NameComparator comparator = new NameComparator();
        Collections.sort(unSortedList, comparator);
        return unSortedList;
    }
}
