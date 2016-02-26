/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.simulationworker;

import christianmesch.simulationworker.misc.Report;
import christianmesch.simulationworker.misc.Utils;
import java.util.HashMap;
import java.util.Map;
import umontreal.ssj.rng.MRG32k3a;
import umontreal.ssj.rng.RandomStreamBase;

/**
 * Main class for the simulation jar.
 * @author Christian Mesch
 */
public class SimulationWorker {

	public static Report main(String[] args) {
		
		final int REPLICATIONS = Integer.parseInt(args[0]);
		
		// Populate map with random streams from seeds in args
		Map<String, RandomStreamBase> randomStreams = new HashMap<>();
		for(int i = 1; i < args.length; i += 2) {
			MRG32k3a tmp = new MRG32k3a();
			tmp.setSeed(Utils.parseLongArray(args[i + 1]));
			
			randomStreams.put(args[i], tmp);
		}
		
		MySimulator simulator = new MySimulator(randomStreams);
		
		// Run the simulations
		return simulator.run(REPLICATIONS);

	}
}
