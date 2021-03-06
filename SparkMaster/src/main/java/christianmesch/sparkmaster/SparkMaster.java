/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.sparkmaster;

import christianmesch.sparkmaster.functions.CollectFunction;
import christianmesch.sparkmaster.functions.SimulationFunction;
import christianmesch.sparkmaster.misc.Utils;
import christianmesch.simulationworker.misc.Report;
import christianmesch.sparkmaster.misc.StopWatch;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import umontreal.ssj.rng.MRG32k3a;
import umontreal.ssj.rng.RandomStreamBase;

/**
 * The driver class.<br><br>
 * 
 * How to:
 * <ul>
 *	<li>Change the constants to reflect your cluster and number of replications.</li>
 *	<li>Add the random streams you want each worker to have to the randomStreams map.</li>
 *	<li>Write your filters to get your specific data.</li>
 * </ul>
 * 
 * TODO:
 * <ul>
 *	<li>Put constants as arguments</li>
 *	<li>Maybe put randomStream seeds as argument (as in the simulation jar. Probably not)</li>
 * </ul>
 * @author Christian Mesch
 */
public class SparkMaster {

	/**
	 * @param args the command line arguments
	 * @throws java.lang.Exception
	 */
	public static void main(String[] args) throws Exception {
		// Constants for number of workers and full replications
		// NUM_WORKERS * REPLICATIONS_PER_WORKER = REPLICATIONS
		int NUM_WORKERS = 10;
		int REPLICATIONS = 1000000;
		
		StopWatch stopWatch = new StopWatch();
		
		if(args.length == 2) {
			NUM_WORKERS = Integer.valueOf(args[0]);
			REPLICATIONS = Integer.valueOf(args[1]);
		}

		int REPLICATIONS_PER_WORKER = REPLICATIONS / NUM_WORKERS;
		
		stopWatch.start();
		
		SparkConf config = new SparkConf().setAppName("SparkMaster");
		JavaSparkContext context = new JavaSparkContext(config);
		
		// Add Streams to a map with the name you want to use to access them as key
		Map<String, RandomStreamBase> randomStreams = new HashMap<>();
		randomStreams.put("Cancer", new MRG32k3a());
		randomStreams.put("Death", new MRG32k3a());
		randomStreams.put("CancerDeath", new MRG32k3a());
		randomStreams.put("Attribute", new MRG32k3a());

		// Create list containing maps of randomstreams for each worker
		List<Map<String, RandomStreamBase>> streamList = Utils.inflateStreams(randomStreams,
				NUM_WORKERS);
		
		
		JavaRDD<Map<String, RandomStreamBase>> dataSet = context.parallelize(streamList, streamList.size());
		
		// Run the simulations and cache the data. 
		// This will be the complete data set from the simulations distributed on the cluster
		JavaRDD<Report> reports = dataSet.map(new SimulationFunction(REPLICATIONS_PER_WORKER)).cache();
		
		Report allReports = reports.reduce(new CollectFunction());
		
		/* Comment everything to do with filter for now
		
		// Create a filter to filter the events
		States eventsStates = new States(States.HealthState.LOCOREGIONAL, States.Diagnosis.NONE);
		EventKeyFilter eventsKey = new EventKeyFilter(eventsStates, "cancerdeath", 56.0, 70.0);
		
		// Filter the events and collect the results
		Report filteredEvents = reports.map(new FilterEventsFunction(eventsKey)).
				reduce(new CollectFunction());
		
		// Create a filter for the person times
		States ptStates = new States(States.HealthState.HEALTHY, States.Diagnosis.NOT_DIAGNOSED);
		PTKeyFilter ptKey = new PTKeyFilter(ptStates, 80.0, 90.0);
		
		// Filter the person times and collect the results
		Report filteredPTs = reports.map(new FilterPTsFunction(ptKey)).
				reduce(new CollectFunction());
		
		// Print the filtered reports
		filteredEvents.printEvents();
		filteredPTs.printPersonTimes();
		*/

		System.out.println("Life expectancy = " + allReports.lifeExpectancy(REPLICATIONS));
		
		// ChartCreator chart = new ChartCreator(allReports)
		// 		.setTitle("Title")
		// 		.setxLabel("Age (years)")
		// 		.setyLabel("Incidence rate per 100,000")
		// 		.setLineName("Line name")
		// 		.setHeight(600)
		// 		.setWidth(800)
		// 		.setStepSize(5)
		// 		.setMultiplier(100000)
		// 		.setEvents("Cancer");
		
		// chart.createRateChart();
		
		// allReports.report();

		context.stop();
		
		stopWatch.stop();
		System.out.println("Time: " + stopWatch.getElapsedTime());
	}

}
