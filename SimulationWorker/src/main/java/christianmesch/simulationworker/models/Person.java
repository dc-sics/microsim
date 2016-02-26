/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.simulationworker.models;

import umontreal.ssj.simevents.Simulator;

/**
 * Object with all relevant information about the person in the replication.
 * @author Christian Mesch
 */
public class Person {

	private final States states = new States();
	private final Simulator simulator;
	private double previousTime = 0.0;
	
	public Person(Simulator simulator) {
		this.simulator = simulator;
	}

	/**
	 * Reset the person by creating and returning a new instance with the same simulator.
	 * @return Person New instance of Person
	 */
	public Person reset() {
		return new Person(this.simulator);
	}
	
	/**
	 * Get the time when the last event occurred.
	 * Used when logging person times
	 * @return previousTime Time for previous event
	 */
	public double getPreviousTime() {
		return previousTime;
	}
	
	/**
	 * Set the time when the last event occurred.
	 * @param previousTime 
	 */
	public void setPreviousTime(double previousTime) {
		this.previousTime = previousTime;
	}
	
	/**
	 * Set health state for person and update the previous time for a state change if new state differs.
	 * @param state New health state
	 */
	public void setHealthState(States.HealthState state) {
		if(states.getHealthState() != state) {
			states.setHealthState(state);
			previousTime = simulator.time();
		}
	}
	
	/**
	 * Set diagnosis for person and update the previous time for a state change if new state differs.
	 * @param state New diagnosis state
	 */
	public void setDiagnosis(States.Diagnosis state) {
		if(states.getDiagnosis() != state) {
			states.setDiagnosis(state);
			previousTime = simulator.time();
		}
	}
	
	/**
	 * Return a copy of the persons states
	 * @return States Copy of the persons states
	 */
	public States copyStates() {
		return new States(states);
	}
}
