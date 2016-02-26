/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.sparkmaster.functions;

import christianmesch.simulationworker.SimulationWorker;
import christianmesch.simulationworker.misc.Report;
import java.util.List;
import org.apache.spark.api.java.function.Function;

/**
 * Simulation function.
 * Calls the simulation jar on each of the workers.
 * @author Christian Mesch
 */
public class SimulationFunction implements Function<List<String>, Report> {

	@Override
	public Report call(List<String> commands) throws Exception {
		return SimulationWorker.main(commands.toArray(new String[commands.size()]));
	}
	
}
