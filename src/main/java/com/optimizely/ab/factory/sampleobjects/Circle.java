package com.optimizely.ab.factory.sampleobjects;

/**
 * Created by asunwoo on 8/16/17.
 */
public class Circle implements Shape {
    private int instanceVar = 0;

    public Circle(Integer instanceVar){
        this.instanceVar = instanceVar;
    }

    public void print(){
        System.out.println("I am a Circle " + instanceVar);
    }
}
