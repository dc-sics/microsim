/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.sparkmaster;

import christianmesch.simulationworker.misc.EventKey;
import christianmesch.sparkmaster.functions.CollectFunction;
import christianmesch.sparkmaster.functions.FilterEventsFunction;
import christianmesch.sparkmaster.functions.FilterPTsFunction;
import christianmesch.sparkmaster.functions.SimulationFunction;
import christianmesch.sparkmaster.misc.Utils;
import christianmesch.sparkmaster.schemas.Event;
import christianmesch.simulationworker.misc.EventKeyFilter;
import christianmesch.simulationworker.misc.PTKeyFilter;
import christianmesch.simulationworker.misc.Report;
import christianmesch.sparkmaster.functions.PairSimulationFunction;
import christianmesch.sparkmaster.misc.ChartCreator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import scala.Tuple2;
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
		
		JavaRDD<Event> events = reports.flatMap(new FlatMapFunction<Report, Event>() {
			@Override
			public Iterable<Event> call(Report t) throws Exception {
				List<Event> list = new ArrayList<>(t.getEvents().size());
				for(Entry<EventKey, Integer> entry : t.getEvents().entrySet()) {
					Event event = new Event();
					
					event.setHealthState(entry.getKey().getStates().getHealthState().toString());
					event.setDiagnosis(entry.getKey().getStates().getDiagnosis().toString());
					event.setEvent(entry.getKey().getEvent());
					event.setAge(entry.getKey().getAge());
					event.setValue(entry.getValue());
					
					list.add(event);
				}
				
				return list;
			}
		}).cache();
		
		DataFrame schemaEvents = sqlContext.createDataFrame(events, Event.class);
		schemaEvents.registerTempTable("events");
		
		
		DataFrame cancer = sqlContext.sql("SELECT * FROM events WHERE age >= 30");
		cancer.printSchema();
		cancer.show();
		
		/*
		JavaPairRDD<EventKey, Integer> events = dataSet.flatMapToPair(new PairSimulationFunction(REPLICATIONS_PER_WORKER));
		
		JavaPairRDD<EventKey, Integer> reducedEvents = events.reduceByKey((Integer v1, Integer v2) -> {
			return v1 + v2;
		});
		
		for(Tuple2<EventKey, Integer> tuple : reducedEvents.sortByKey().collect()) {
			System.out.println(tuple._1 + " = " + tuple._2);
		}
		*/
		
		//Report allReports = reports.reduce(new CollectFunction());
		
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
		
		allReports.report();
*/
		context.stop();
	}

}
