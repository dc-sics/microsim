/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.simulationworker.events;

import christianmesch.simulationworker.misc.Container;
import umontreal.ssj.simevents.Simulator;

/**
 *
 * @author Christian Mesch
 */
public class Screen extends MyEvent {

	public Screen(Simulator sim, Container container) {
		super(sim, container);
	}

	@Override
	public void actions() {
		logEvent();
		logPersonTime();
	}
	
}
