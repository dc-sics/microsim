# Microsimulation

## Prerequisites

Before starting, you'll need to do/have these:
- A working installation of Spark/Flink on your local computer and/or cluster
- Maven
- Java

## How to

### Create new events

Create a Java class file and add it to the `christianmesch.simulationworker.events` package.
Make sure that it extends the abstract class `MyEvent`.

In the `actions()` method you can choose to call `logEvent()` and/or `logPersonTime()` depending on what, or if, you want to log anything from this event.

To get a random stream, call `container.getRandomStreams().get(nameOfStream)`.

### Initialization

Add your starting events to the method `christianmesch.simulationworker.MySimulator.init()`.
These event(s) can for example be when a person gets cancer or dies from non-cancer circumstances.

In the class `christianmesch.sparkmaster.SparkMaster` you have a `Map` called *randomStreams*. Here's where you add all the streams you will use for each simulation of a person.
The key in the map will be used later on when using the streams.
These streams will later on be multiplied so that each worker gets its own set of random streams.

### Run the simulations

There are two ways to run the simulations on local. With and without arguments. 
If you choose to run without arguments, the set values of *NUM_WORKERS* and *REPLICATIONS_PER_WORKER* in `christianmesch.sparkmaster.SparkMaster` will be used.

`/path/to/spark/bin/spark-submit --master local[10] /path/to/jar/SparkMaster-1.0-SNAPSHOT.jar [NUM_WORKERS REPLICATIONS_PER_WORKER]`

### Filtering

To be written.

### Create charts

A class for creating charts exist, `christianmesch.sparkmaster.misc.ChartCreator`.
It's implemented using a builder-type pattern and can as of now only create rate charts.

Example code of how to use the class

```java
ChartCreator chart = new ChartCreator(allReports)
		.setTitle("Title")
		.setxLabel("Age (years)")
		.setyLabel("Incidence rate per 100,000")
		.setLineName("Line name")
		.setHeight(600)
		.setWidth(800)
		.setStepSize(5)
		.setMultiplier(100000)
		.setEvents("Cancer");
		
chart.createRateChart();
```

Where *allReports* is the Report object containing the data you want to plot.