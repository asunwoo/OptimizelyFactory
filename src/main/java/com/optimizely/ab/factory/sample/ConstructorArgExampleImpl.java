package com.optimizely.ab.factory.sample;

import com.optimizely.ab.factory.OptimizelyFactory;
import com.optimizely.ab.factory.sampleobjects.Shape;

import java.util.Scanner;

/**
 * Created by asunwoo on 8/16/17.
 */
public class ConstructorArgExampleImpl {

    public static void main (String[] args){
        Scanner scanner = new Scanner(System.in);
        String input = "";

        OptimizelyFactory optimizely = new OptimizelyFactory();
        optimizely.initializeOptimizely(args[0]);
        optimizely.setPackageName("com.optimizely.ab.factory.sampleobjects");

        Object[] parameters = new Object[1];
        parameters[0] = 10;

        while(!input.equals("q")){
            System.out.print("Enter a user name: ");
            input = scanner.next();

            if(input.equals("q")){
                System.exit(0);
            }

            Shape shape = (Shape)optimizely.createVariationObject(parameters, args[1], input);
            shape.print();
        }
    }
}
