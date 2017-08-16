package com.optimizely.ab.factory.sampleobjects;

/**
 * Created by asunwoo on 8/16/17.
 */
public class Square implements Shape {
    private int instanceVar = 0;

    public Square(Integer instanceVar){
        this.instanceVar = instanceVar;
    }

    public void print(){
        System.out.println("I am a Square " + instanceVar);
    }
}
