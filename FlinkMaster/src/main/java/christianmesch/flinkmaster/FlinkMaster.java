package christianmesch.flinkmaster;

import christianmesch.simulationworker.misc.Report;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import christianmesch.flinkmaster.functions.CollectFunction;
import christianmesch.flinkmaster.functions.SimulationFunction;
import christianmesch.flinkmaster.misc.Utils;
import umontreal.iro.lecuyer.rng.MRG32k3a;
import umontreal.iro.lecuyer.rng.RandomStreamBase;

public class FlinkMaster {

	public static void main(String[] args) throws Exception {
		// Constants for number of workers and replications per worker
		// NUM_WORKERS * REPLICATIONS_PER_WORKER = Full # of replications
		final int NUM_WORKERS = 10;
		final int REPLICATIONS_PER_WORKER = 100;
		
		// set up the execution environment
		final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();


		// Add Streams to a map with the name you want to use to access them as key
		Map<String, RandomStreamBase> randomStreams = new HashMap<>();
		randomStreams.put("Cancer", new MRG32k3a());
		randomStreams.put("Death", new MRG32k3a());
		randomStreams.put("CancerDeath", new MRG32k3a());

		// Create list with commands for workers
		String[][] commandList = Utils.generateCommandList(randomStreams,
				NUM_WORKERS, REPLICATIONS_PER_WORKER);
		
		// Create a data set from commands
		DataSource<String[]> dataSet = env.fromElements(commandList);
		
		// Run the simulations
		// This will be the complete data set from the simulations distributed on the cluster
		DataSet<Report> reports = dataSet.map(new SimulationFunction());
		
		// Collect and print results. The collect() call is needed to gain access to the Report object
		List<Report> collectedReport = reports.reduce(new CollectFunction()).collect();
		
		collectedReport.get(0).printEvents();
		collectedReport.get(0).printPersonTimes();
		
		
	}
}
