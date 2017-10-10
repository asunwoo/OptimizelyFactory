package com.optimizely.ab.factory;

import java.lang.reflect.*;

import com.optimizely.ab.Optimizely;
import com.optimizely.ab.config.Variation;
import com.optimizely.ab.config.parser.ConfigParseException;
import com.optimizely.ab.event.AsyncEventHandler;
import com.optimizely.ab.event.EventHandler;

import com.optimizely.ab.factory.samplesortclasses.ProductSort;
import com.optimizely.ab.factory.samplesortclasses.CategoryProductSort;
import com.optimizely.ab.factory.samplesortclasses.NameProductSort;
import com.optimizely.ab.factory.samplesortclasses.PriceProductSort;

import java.net.URL;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.BufferedReader;

import java.util.stream.Stream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by asunwoo on 8/16/17.
 */
public class OptimizelyFactory {
    private String dataFile = ""; //instance variable to hold the datafile
    private EventHandler eventHandler; //Optimizely object responsible for dispatching events back to Optimizely
    private Optimizely optimizelyClient; //Optimizely client for determining variations and tracking events

    /**
     * Public constructor the after construction
     * Optimizely specific components still need to be initialized
     * through initializeOptimizely
     * @param dataFile
     */
    public OptimizelyFactory(){
        this.eventHandler = new AsyncEventHandler(20000, 1);
    }

    /**
     * Initialization method that will create the necessary
     * Optimizely components based on the provided data file url.
     * Local or remote file determined by presence of "http"
     * Ex. https://cdn.optimizely.com/json/1234567890.json
     * or
     * /usr/home/1234567890.json
     * @param dataFile
     */
    public void initializeOptimizely(String dataFile){
        //Load the full data file either from a local file or from
        //Optimizely ex: https://cdn.optimizely.com/json/1234567890.json
        if(dataFile.startsWith("http")) {
            this.dataFile = OptimizelyFactory.loadRemoteDataFile(dataFile);
        } else {
            this.dataFile = OptimizelyFactory.loadLocalDataFile(dataFile);
        }
        this.optimizelyClient = createOptimizelyClient(this.dataFile);
    }

    /**
     * Used to create an optimizely client.
     * This object is needed for user routing to variations and for
     * metric tracking.
     * @param dataFile
     * @return
     */
    private Optimizely createOptimizelyClient(String dataFile){
        Optimizely optimizelyClient = null;

        try {
            // Initialize an Optimizely client
            optimizelyClient = Optimizely.builder(dataFile, eventHandler).build();
        } catch (ConfigParseException e) {
            e.printStackTrace();
        }

        return optimizelyClient;
    }

    private static String loadRemoteDataFile(String urlString){
        URL url = null;
        InputStream in = null;

        StringBuilder sb = new StringBuilder();

        try {
            url = new URL(urlString);
            in = url.openStream();
            InputStreamReader inputStream = new InputStreamReader( in );
            BufferedReader buf = new BufferedReader( inputStream );
            String line;
            while ( ( line = buf.readLine() ) != null ) {
                sb.append( line );
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        finally {
            try {
                if(in != null) {
                    in.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * Method for loading the Optimizely data file from Optimizely.
     * ex: /Users/<User>/workspace/Optimizely/<ex>.json
     * @param urlString
     * local path of datafile
     * @return
     * The Optimizely data file as a string.
     */
    private static String loadLocalDataFile(String pathString){
        StringBuilder sb = new StringBuilder();
        Path path = Paths.get(pathString);

        //read file into stream, try-with-resources
        StringBuilder data = new StringBuilder();
        try {
            Stream<String> lines = Files.lines(path);
            lines.forEach(line -> sb.append(line));
            lines.close();
        }catch(IOException e){
            e.printStackTrace();
        }

        return sb.toString();
    }

    /**
     * Method to arbitrarily determine if the user had a tracked event.
     * Using a 50/50 split for having an event
     * @param eventName
     * @param userId
     * User to determine event occurence for.
     */
    public void track(String eventName, String userId){
        this.optimizelyClient.track(eventName, userId);
    }

    /**
     *
     * @param experimentName
     * @param userId
     * @return
     */
    public ProductSort getExperimentImpl(String experimentName, String userId){
        Variation variation = this.optimizelyClient.activate(experimentName, userId);

        ProductSort retobj = null;

        if(variation.getKey().equals("Circle")){
            retobj = new CategoryProductSort();
        } else if(variation.getKey().equals("Square")){
            retobj = new NameProductSort();
        } else if(variation.getKey().equals("Triangle")){
            retobj = new PriceProductSort();
        }

        return retobj;
    }
}