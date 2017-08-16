package com.optimizely.ab.factory.sampleobjects;

/**
 * Created by asunwoo on 8/16/17.
 */
public class Triangle implements Shape{
    private int instanceVar = 0;

    public Triangle(Integer instanceVar){
        this.instanceVar = instanceVar;
    }
    public void print(){
        System.out.println("I am a Triangle " + instanceVar);
    }
}
