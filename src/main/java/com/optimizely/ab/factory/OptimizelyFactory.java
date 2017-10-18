package com.optimizely.ab.factory;

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
     * Public constructor.  After construction
     * Optimizely specific components still need to be initialized
     * through initializeOptimizely
     */
    public OptimizelyFactory(){
        this.eventHandler = new AsyncEventHandler(20000, 1);
    }

    /**
     * Public constructor that includes datafile
     * initilization.
     * @param dataFilePath
     */
    public OptimizelyFactory(String dataFilePath){
        this.eventHandler = new AsyncEventHandler(20000, 1);
        this.initializeOptimizely(dataFilePath);
    }

    /**
     * Initialization method that will create the necessary
     * Optimizely components based on the provided data file url.
     * Local or remote file determined by presence of "http"
     * Ex. https://cdn.optimizely.com/json/1234567890.json
     * or
     * /usr/home/1234567890.json
     * @param dataFilePath
     */
    public void initializeOptimizely(String dataFilePath){
        //Load the full data file either from a local file or from
        //Optimizely ex: https://cdn.optimizely.com/json/1234567890.json
        if(dataFilePath.startsWith("http")) {
            this.dataFile = OptimizelyFactory.loadRemoteDataFile(dataFilePath);
        } else {
            this.dataFile = OptimizelyFactory.loadLocalDataFile(dataFilePath);
        }
        this.optimizelyClient = createOptimizelyClient();
    }

    /**
     * Used to create an optimizely client.
     * This object is needed for user routing to variations and for
     * metric tracking.
     * @return
     */
    private Optimizely createOptimizelyClient(){
        Optimizely optimizelyClient = null;

        try {
            // Initialize an Optimizely client
            optimizelyClient = Optimizely.builder(this.dataFile, eventHandler).build();
        } catch (ConfigParseException e) {
            e.printStackTrace();
        }

        return optimizelyClient;
    }

    /**
     * Method for loading the Optimizely data file from Optimizely.
     * ex: https://cdn.optimizely.com/json/8410977336.json
     * @param pathString
     * URL of datafile
     * @return
     * The Optimizely data file as a string.
     */
    private static String loadRemoteDataFile(String pathString){
        URL url = null;
        InputStream in = null;

        StringBuilder sb = new StringBuilder();

        try {
            url = new URL(pathString);
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
     * Method for loading the Optimizely data file from a local file.
     * ex: /Users/<User>/workspace/Optimizely/<ex>.json
     * @param pathString
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
     * Convenience method for tracking success metrics for experiments
     * @param eventName
     * @param userId
     * User to determine event occurence for.
     */
    public void track(String eventName, String userId){
        this.optimizelyClient.track(eventName, userId);
    }

    /**
     * Factory method for retrieving the appropriate implementation
     * givent the variation that the given userId falls into.
     * Conditional will need to be updated as new variations are introduced.
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