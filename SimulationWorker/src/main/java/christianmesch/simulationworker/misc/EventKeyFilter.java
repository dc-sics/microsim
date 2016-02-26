/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.simulationworker.misc;

import christianmesch.simulationworker.models.States;
import java.io.Serializable;

/**
 * Filter class used when filtering events
 * @author Christian Mesch
 */
public class EventKeyFilter implements Serializable {
	
	private final States states;
	private final String event;
	private final double minAge;
	private final double maxAge;

	/**
	 * Constructor for the filter with all arguments.
	 * The minAge and maxAge are inclusive, i.e. [minAge, maxAge].
	 * @param states States you want to filter by
	 * @param event Event type
	 * @param minAge Lower bound of age (inclusive)
	 * @param maxAge Upper bound of age (inclusive)
	 */
	public EventKeyFilter(States states, String event, double minAge, double maxAge) {
		this.states = states;
		this.event = event;
		this.minAge = minAge;
		this.maxAge = maxAge;
	}
	
	/**
	 * Constructor without event type.
	 * Is to be used if you want to have all event types and not a specific one.
	 * The minAge and maxAge are inclusive, i.e. [minAge, maxAge].
	 * @param states States you want to filter by
	 * @param minAge Lower bound of age (inclusive)
	 * @param maxAge Upper bound of age (inclusive)
	 */
	public EventKeyFilter(States states, double minAge, double maxAge) {
		this.states = states;
		this.minAge = minAge;
		this.maxAge = maxAge;
		
		this.event = "";
	}
	
	/**
	 * Use this filter to filter events from the data.
	 * 
	 * The minAge and maxAge are inclusive, i.e. [minAge, maxAge].
	 * @param other EventKey from the map to filter with
	 * @return boolean True if the other key pass the test
	 */
	public boolean filter(EventKey other) {
		return this.minAge <= other.getAge() && 
				this.maxAge >= other.getAge() &&
				this.states.filter(other.getStates()) &&
				(this.event.isEmpty() || this.event.equalsIgnoreCase(other.getEvent()));
	}
	
}
