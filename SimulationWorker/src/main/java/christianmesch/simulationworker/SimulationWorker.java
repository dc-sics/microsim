/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.simulationworker;

import christianmesch.simulationworker.misc.Report;
import java.util.Map;
import umontreal.ssj.rng.RandomStreamBase;


/**
 * Main class for the simulation jar.
 * @author Christian Mesch
 */
public class SimulationWorker {

	public static Report main(Map<String, RandomStreamBase> randomStreams, final int REPLICATIONS) {
		MySimulator simulator = new MySimulator(randomStreams);
		
		// Run the simulations
		return simulator.run(REPLICATIONS);

	}
}
