package com.optimizely.ab.factory.sample;

import java.util.Scanner;

import com.optimizely.ab.factory.OptimizelyFactory;
import com.optimizely.ab.factory.sampleobjects.Shape;

/**
 * Created by asunwoo on 8/16/17.
 */
public class ExampleImpl {

    public static void main (String[] args){
        Scanner scanner = new Scanner(System.in);
        String input = "";

        OptimizelyFactory optimizely = new OptimizelyFactory();

        String dataFileLocation = args[0];
        optimizely.initializeOptimizely(dataFileLocation);

        String experimentName = "ProductSort";

        while(!input.equals("q")){
            System.out.print("Enter a user name: ");
            input = scanner.next();

            if(input.equals("q")){
                System.exit(0);
            }

            Shape shape = (Shape)optimizely.getExperimentImpl(experimentName, input);
            shape.print();
        }
    }
}
