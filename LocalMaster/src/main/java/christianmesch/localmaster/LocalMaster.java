/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.localmaster;

import christianmesch.localmaster.misc.StopWatch;
import christianmesch.simulationworker.SimulationWorker;
import christianmesch.simulationworker.misc.Report;
import java.util.HashMap;
import java.util.Map;
import umontreal.ssj.rng.MRG32k3a;
import umontreal.ssj.rng.RandomStreamBase;

/**
 *
 * @author Christian Mesch
 */
public class LocalMaster {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		
		int REPLICATIONS = 1000000;
		
		StopWatch stopWatch = new StopWatch();
		
		if(args.length == 1) {
			REPLICATIONS = Integer.valueOf(args[0]);
		}

		stopWatch.start();
		
		// Random streams
		Map<String, RandomStreamBase> randomStreams = new HashMap<>();
		randomStreams.put("Cancer", new MRG32k3a());
		randomStreams.put("Death", new MRG32k3a());
		randomStreams.put("CancerDeath", new MRG32k3a());
		randomStreams.put("Attribute", new MRG32k3a());
		
		// Run simulations and get report
		Report report = SimulationWorker.main(randomStreams, REPLICATIONS);
		
		System.out.println("Life expectancy: " + report.lifeExpectancy(REPLICATIONS));
		
		stopWatch.stop();
		System.out.println("Time: " + stopWatch.getElapsedTime());
	}
	
}
