package com.optimizely.ab.factory;

import com.optimizely.ab.Optimizely;
import com.optimizely.ab.config.Variation;
import com.optimizely.ab.config.parser.ConfigParseException;
import com.optimizely.ab.event.AsyncEventHandler;
import com.optimizely.ab.event.EventHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Created by asunwoo on 8/16/17.
 */
public class OptimizelyReflectionFactory<T> {
    private String dataFile = ""; //instance variable to hold the datafile
    private String packageName = ""; //instance variable to hold the package name of classes
    private EventHandler eventHandler; //Optimizely object responsible for dispatching events back to Optimizely
    private Optimizely optimizelyClient; //Optimizely client for determining variations and tracking events

    /**
     * Public constructor.  After construction
     * Optimizely specific components still need to be initialized.
     * Parent of objects to be constructed
     * are meant to be specified through generics.
     * ex: OptimizelyReflectionFactory<ProductSort> optimizelyFactory =
     * new OptimizelyReflectionFactory<ProductSort>();
     * through initializeOptimizely
     */
    public OptimizelyReflectionFactory(){
        this.eventHandler = new AsyncEventHandler(20000, 1);
    }

    /**
     * Public constructor that includes datafile
     * initilization.
     * ex: OptimizelyReflectionFactory<ProductSort> optimizelyFactory =
     * new OptimizelyReflectionFactory<ProductSort>(dataFilePath);
     * @param dataFilePath
     */
    public OptimizelyReflectionFactory(String dataFilePath){
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
            this.dataFile = OptimizelyReflectionFactory.loadRemoteDataFile(dataFilePath);
        } else {
            this.dataFile = OptimizelyReflectionFactory.loadLocalDataFile(dataFilePath);
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

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * Method for loading the Optimizely data file from Optimizely.
     * ex: https://cdn.optimizely.com/json/8410977336.json
     * @param urlString
     * URL of datafile
     * @return
     * The Optimizely data file as a string.
     */
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
     * given the variation that the given userId falls into.
     * Method uses reflection to create a given class from the name of a variation.
     * Note that this method requires packageName is set.
     * @param experimentName
     * @param userId
     * @return
     */
    public T getExperimentImpl(String experimentName, String userId){
        Variation variation = this.optimizelyClient.activate(experimentName, userId);

        String className = this.packageName + "." + variation.getKey();

        T retobj = null;

        try {
            Class cls = Class.forName(className);

            Class partypes[] = new Class[0];
            Constructor ct = cls.getConstructor(partypes);
            retobj = (T)ct.newInstance();
        }
        catch (Throwable e) {
            System.err.println(e);
            e.printStackTrace();
            return null;
        }
        return retobj;
    }

    /**
     * Factory method for retrieving the appropriate implementation
     * given the variation that the given userId falls into.
     * Method uses reflection to create a given class from the value of the given
     * Feature Variable.
     * @param featureFlag
     * @param featureVariable
     * @param userId
     * @return
     */
//    public T getVariableImpl(String featureFlag, String featureVariable, String userId){
//        String className = this.optimizelyClient.getFeatureVariableString(featureFlag, featureVariable, userId);
//
//        T retobj = null;
//
//        try {
//            Class cls = Class.forName(className);
//
//            Class partypes[] = new Class[0];
//            Constructor ct = cls.getConstructor(partypes);
//            retobj = (T)ct.newInstance();
//        }
//        catch (Throwable e) {
//            System.err.println(e);
//            e.printStackTrace();
//            return null;
//        }
//        return retobj;
//    }

    /**
     * Factory method for retrieving the appropriate implementation
     * given the variation that the given userId falls into.  This version
     * accounts for constructors that may require a set of arguments passed in.
     * This factory method only supports Object constructor arguments.
     * @param experimentName
     * @param userId
     * @return
     */
    private T getExperimentImpl(Object[] constructorParameters, String experimentName, String userId){
        Variation variation = this.optimizelyClient.activate(experimentName, userId);

        String className = this.packageName + "." + variation.getKey();

        T retobj = null;
        try {
            Class cls = Class.forName(className);

            int size = constructorParameters.length;
            Class parameterTypes[] = new Class[size];

            for(int i=0; i<size; i++){
                parameterTypes[i] = constructorParameters[i].getClass();
            }

            Constructor ct = cls.getConstructor(parameterTypes);
            retobj = (T)ct.newInstance(constructorParameters);
        }
        catch (Throwable e) {
            System.err.println(e);
            e.printStackTrace();
            return null;
        }
        return retobj;
    }

}