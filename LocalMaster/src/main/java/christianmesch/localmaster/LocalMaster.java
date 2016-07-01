/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.localmaster;

import christianmesch.localmaster.misc.StopWatch;
import christianmesch.simulationworker.SimulationWorkerRunnable;
import christianmesch.simulationworker.misc.Report;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
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
		int NUM_THREADS = 1;

		StopWatch stopWatch = new StopWatch();

		if(args.length == 2) {
			NUM_THREADS = Integer.valueOf(args[0]);
			REPLICATIONS = Integer.valueOf(args[1]);
		}

		stopWatch.start();

		// simulation workers
		SimulationWorkerRunnable[] simulations = new SimulationWorkerRunnable[NUM_THREADS];

		for(int i = 0; i < NUM_THREADS; i++) {
			// Random streams
			Map<String, RandomStreamBase> randomStreams = new HashMap<>();
			randomStreams.put("Cancer", new MRG32k3a());
			randomStreams.put("Death", new MRG32k3a());
			randomStreams.put("CancerDeath", new MRG32k3a());
			randomStreams.put("Attribute", new MRG32k3a());
			
			simulations[i] = new SimulationWorkerRunnable(randomStreams, REPLICATIONS);
		}

		ExecutorService es = Executors.newFixedThreadPool(NUM_THREADS);

		final Report report = new Report();

		for(int i = 0; i < NUM_THREADS; i++) {
			final int j = i;
			final int repl = REPLICATIONS;

			es.execute(new Runnable(){
				@Override
				public void run() {
					simulations[j].run();
					
					synchronized(report) {
						report.addAll(simulations[j].report);
					}
				}
			});
		}
		
		es.shutdown();
		try {
			es.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch(InterruptedException ex) {
			System.err.println(ex);
		}
		
		
		System.out.println("Number of events: " + report.getEvents().size());
		
		System.out.println("Life expectancy: " + report.lifeExpectancy(REPLICATIONS * NUM_THREADS));

		stopWatch.stop();
		System.out.println("Time: " + stopWatch.getElapsedTime());
	}

}
