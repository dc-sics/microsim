/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.sparkmaster;

import christianmesch.sparkmaster.functions.CollectFunction;
import christianmesch.sparkmaster.functions.FilterEventsFunction;
import christianmesch.sparkmaster.functions.FilterPTsFunction;
import christianmesch.sparkmaster.functions.SimulationFunction;
import christianmesch.sparkmaster.misc.Utils;
import christianmesch.simulationworker.misc.EventKeyFilter;
import christianmesch.simulationworker.misc.PTKeyFilter;
import christianmesch.simulationworker.misc.Report;
import christianmesch.simulationworker.models.States;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import umontreal.iro.lecuyer.rng.MRG32k3a;
import umontreal.iro.lecuyer.rng.RandomStreamBase;

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
	 */
	public static void main(String[] args) {
		// Constants for number of workers and replications per worker
		// NUM_WORKERS * REPLICATIONS_PER_WORKER = Full # of replications
		final int NUM_WORKERS = 10;
		final int REPLICATIONS_PER_WORKER = 100;

		SparkConf config = new SparkConf().setAppName("Test");
		JavaSparkContext context = new JavaSparkContext(config);

		// Add Streams to a map with the name you want to use to access them as key
		Map<String, RandomStreamBase> randomStreams = new HashMap<>();
		randomStreams.put("Cancer", new MRG32k3a());
		randomStreams.put("Death", new MRG32k3a());
		randomStreams.put("CancerDeath", new MRG32k3a());

		// Create list with commands for workers
		List<List<String>> commandList = Utils.generateCommandList(randomStreams,
				NUM_WORKERS, REPLICATIONS_PER_WORKER);

		JavaRDD<List<String>> dataSet = context.parallelize(commandList);
		
		// Run the simulations and cache the data. 
		// This will be the complete data set from the simulations distributed on the cluster
		JavaRDD<Report> reports = dataSet.map(new SimulationFunction()).cache();
		
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
		//report.report();

		context.stop();
	}

}
