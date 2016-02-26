/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.simulationworker.events;

import christianmesch.simulationworker.misc.Container;
import umontreal.ssj.simevents.Event;
import umontreal.ssj.simevents.Simulator;

/**
 * Abstract event class with added functionality.
 * This class should be extended by every event.
 * @author Christian Mesch
 */
public abstract class MyEvent extends Event {
	
	private double sendingTime;
	protected final Container container;
	
	public MyEvent(Simulator sim, Container container) {
		super(sim);
		
		this.container = container;
	}
	
	/**
	 * Override Event.schedule(...) to save sending times for the events as well
	 * @param delay Delay until the event fires
	 */
	@Override
	public void schedule(double delay) {
		super.schedule(delay);
		
		this.sendingTime = sim.time();
	}
	
	/**
	 * Logging for events
	 */
	public void logEvent() {
		container.getReport()
				.logEvent(container.getPerson(),
						this.getClass().getSimpleName(),
						sim.time());
	}
	
	/**
	 * Logging for person times
	 */
	public void logPersonTime() {
		container.getReport()
				.logPersonTime(container.getPerson(),
						sim.time());
	}

	/**
	 * Get sending time of event
	 * @return sendingTime
	 */
	public double sendingTime() {
		return sendingTime;
	}	
	
}
