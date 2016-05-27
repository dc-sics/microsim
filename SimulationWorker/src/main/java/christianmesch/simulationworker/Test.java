/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.simulationworker;

import christianmesch.simulationworker.misc.Report;
import java.util.HashMap;
import java.util.Map;
import umontreal.ssj.rng.MRG32k3a;
import umontreal.ssj.rng.RandomStreamBase;

/**
 *
 * @author Christian Mesch
 */
public class Test {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		Map<String, RandomStreamBase> randomStreams = new HashMap<>();
		randomStreams.put("cancer", new MRG32k3a());
		randomStreams.put("otherDeath", new MRG32k3a());
		
		Report report = SimulationWorker.main(randomStreams, 100000);
		
		report.report();
	}
	
}
