/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.sparkmaster.functions;

import christianmesch.simulationworker.SimulationWorker;
import christianmesch.simulationworker.misc.Report;
import java.util.Map;
import org.apache.spark.api.java.function.Function;
import umontreal.ssj.rng.RandomStreamBase;



/**
 * Simulation function.
 * Calls the simulation jar on each of the workers.
 * @author Christian Mesch
 */
public class SimulationFunction implements Function<Map<String, RandomStreamBase>, Report> {

	private final int REPLICATIONS_PER_WORKER;

	public SimulationFunction(int REPLICATIONS_PER_WORKER) {
		this.REPLICATIONS_PER_WORKER = REPLICATIONS_PER_WORKER;
	}
	
	@Override
	public Report call(Map<String, RandomStreamBase> commands) throws Exception {
		return SimulationWorker.main(commands, REPLICATIONS_PER_WORKER);
	}
	
}
