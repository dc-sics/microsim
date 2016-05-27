/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.simulationworker.events;

import christianmesch.simulationworker.misc.Container;
import christianmesch.simulationworker.misc.Utils;
import christianmesch.simulationworker.models.States;
import umontreal.ssj.randvar.WeibullGen;
import umontreal.ssj.simevents.Simulator;


/**
 *
 * @author Christian Mesch
 */
public class Cancer extends MyEvent {
	
	public Cancer(Simulator sim, Container container) {
		super(sim, container);
	}

	@Override
	public void execute() {
		
		container.getPerson().setHealthState(States.HealthState.LOCOREGIONAL);
		
		new CancerDeath(sim, container).schedule(WeibullGen.nextDouble(container.getRandomStreams()
				.get("CancerDeath"), 0.5, Utils.lambda(0.5, 5.0), 0.0));
	}
	
}
