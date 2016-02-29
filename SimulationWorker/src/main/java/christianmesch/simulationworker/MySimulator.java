/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.simulationworker;

import christianmesch.simulationworker.events.Cancer;
import christianmesch.simulationworker.events.Death;
import christianmesch.simulationworker.misc.Container;
import christianmesch.simulationworker.misc.Report;
import christianmesch.simulationworker.misc.Utils;
import christianmesch.simulationworker.models.Person;
import java.util.Map;
import umontreal.ssj.randvar.WeibullGen;
import umontreal.ssj.rng.RandomStreamBase;
import umontreal.ssj.simevents.Simulator;

/**
 * Simulator class. 
 * @author Christian Mesch
 */
public class MySimulator {

	public Simulator simulator = new Simulator();
	
	private final Container container;
	
	public MySimulator(Map<String, RandomStreamBase> randomStreams) {
		container = new Container(new Report(),
				randomStreams, new Person(simulator));
	}

	/**
	 * Getter for container
	 * @return Container The Container object
	 */
	public Container getContainer() {
		return container;
	}
	
	/**
	 * Initialization of the simulator and events
	 * 
	 * <ul>
	 *	<li>Add initial events in this method</li>
	 * </ul>
	 */
	private void init() {
		simulator.init();
		
		// New person for a new replication
		container.resetPerson();
		
		new Death(simulator, container).schedule(WeibullGen.nextDouble(
				container.getRandomStreams().get("Death"),
				4.0, Utils.lambda(4.0, 70.0), 0.0));
		
		if(container.getRandomStreams().get("Cancer").nextDouble() < 0.2) {
			new Cancer(simulator, container).schedule(WeibullGen.nextDouble(
					container.getRandomStreams().get("Cancer"),
					4.0, Utils.lambda(4.0, 70.0), 0.0));
		}
	}
	
	/**
	 * Run the simulation with defined number of replications
	 * @param REPLICATIONS Number of replications
	 * @return Report The report for REPLICATIONS of microsimulations
	 */
	public Report run(final int REPLICATIONS) {
		for (int i = 0; i < REPLICATIONS; i++) {
			init();
			simulator.start();
			
			container.resetNextSubstreams();
		}
		
		return container.getReport();
		
	}
}
