/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.flinkmaster.functions;

import christianmesch.simulationworker.SimulationWorker;
import christianmesch.simulationworker.misc.Report;
import org.apache.flink.api.common.functions.MapFunction;

/**
 *
 * @author Christian Mesch
 */
public class SimulationFunction implements MapFunction<String[], Report> {

	@Override
	public Report map(String[] commands) throws Exception {
		return SimulationWorker.main(commands);
	}
	
}
