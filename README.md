# OptimizelyFactory

Reference implementation of standalone factory classes that can be used to instantiate and provide experimental variants of feature objects. e.g. sorting algorithms

## Getting Started

The OptimizelyFactory was built using Maven.  

### Prerequisites

This example was built with Java 1.8 and Maven 3.5.0.

### Installing

To build the requisite jar, git clone the repository and build with Maven.

```
//cd to the desired directory
git clone https://github.com/asunwoo/OptimizelyFactory.git
mvn clean package
```

This will result in a jar in the target directory.

## Running 

To run the command line example:

java -jar target/optimizely-1.0-jar-with-dependencies.jar <location of the datafile>
