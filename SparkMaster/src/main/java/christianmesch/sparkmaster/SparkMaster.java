/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.sparkmaster;

import christianmesch.simulationworker.misc.EventKey;
import christianmesch.sparkmaster.functions.SimulationFunction;
import christianmesch.sparkmaster.misc.Utils;
import christianmesch.sparkmaster.schemas.Event;
import christianmesch.simulationworker.misc.PTKey;
import christianmesch.simulationworker.misc.Report;
import christianmesch.sparkmaster.schemas.PersonTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
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
		// Constants for number of workers and replications per worker
		// NUM_WORKERS * REPLICATIONS_PER_WORKER = Full # of replications
		int NUM_WORKERS = 10;
		int REPLICATIONS_PER_WORKER = 100000;
		
		if(args.length == 2) {
			NUM_WORKERS = Integer.valueOf(args[0]);
			REPLICATIONS_PER_WORKER = Integer.valueOf(args[1]);
		}

		SparkConf config = new SparkConf().setAppName("Testing");
		JavaSparkContext context = new JavaSparkContext(config);
		SQLContext sqlContext = new SQLContext(context);

		// Add Streams to a map with the name you want to use to access them as key
		Map<String, RandomStreamBase> randomStreams = new HashMap<>();
		randomStreams.put("Cancer", new MRG32k3a());
		randomStreams.put("Death", new MRG32k3a());
		randomStreams.put("CancerDeath", new MRG32k3a());

		// Create list containing maps of randomstreams for each worker
		List<Map<String, RandomStreamBase>> streamList = Utils.inflateStreams(randomStreams,
				NUM_WORKERS, REPLICATIONS_PER_WORKER);
		
		
		JavaRDD<Map<String, RandomStreamBase>> dataSet = context.parallelize(streamList);
		
		// Run the simulations and cache the data. 
		// This will be the complete data set from the simulations distributed on the cluster
		JavaRDD<Report> reports = dataSet.map(new SimulationFunction(REPLICATIONS_PER_WORKER)).cache();
		
		// Create RDDs and DataFrames for the events and person times
		JavaRDD<Event> events = reports.flatMap(new FlatMapFunction<Report, Event>() {
			@Override
			public Iterable<Event> call(Report t) {
				List<Event> list = new ArrayList<>(t.getEvents().size());
				
				for(Entry<EventKey, Integer> entry : t.getEvents().entrySet()) {
					list.add(new Event(entry.getKey(), entry.getValue()));
				}
				
				return list;
			}
		});
		
		DataFrame schemaEvents = sqlContext.createDataFrame(events, Event.class).cache();
		schemaEvents.registerTempTable("events");
		
		JavaRDD<PersonTime> personTimes = reports.flatMap(new FlatMapFunction<Report, PersonTime>() {
			@Override
			public Iterable<PersonTime> call(Report t) {
				List<PersonTime> list = new ArrayList<>(t.getEvents().size());
				
				for(Entry<PTKey, Double> entry : t.getPersonTimes().entrySet()) {
					list.add(new PersonTime(entry.getKey(), entry.getValue()));
				}
				
				return list;
			}
		});
		
		DataFrame schemaPTs = sqlContext.createDataFrame(personTimes, PersonTime.class).cache();
		schemaPTs.registerTempTable("persontimes");
		
		// example of aggregating the data and writing out as json
		schemaPTs.groupBy("diagnosis", "healthState", "age")
				.agg(org.apache.spark.sql.functions.sum(schemaPTs.col("value")).alias("sum_value"))
				.orderBy("healthState", "diagnosis", "age")
				.coalesce(1) // copy everything to one worker. Can use FileUtil.copyMerge if it proves to be too large
				.write().format("json").save("/Users/christianmesch/Desktop/test5.json");		
		
	/*	
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
*/
		context.stop();
	}

}
