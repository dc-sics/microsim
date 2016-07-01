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
 *
 * @author Christian Mesch
 */
public class SimulationWorkerRunnable implements Runnable {

	private final Map<String, RandomStreamBase> randomStreams;
	private final int replications;
	
	public Report report;

	public SimulationWorkerRunnable(Map<String, RandomStreamBase> randomStreams, int replications) {
		this.randomStreams = randomStreams;
		this.replications = replications;
	}
	
	@Override
	public void run() {
		MySimulator simulator = new MySimulator(randomStreams);

		// Run the simulations
		report = simulator.run(replications);
	}

}
