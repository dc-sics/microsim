/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.simulationworker.misc;

import christianmesch.simulationworker.models.States;
import java.io.Serializable;

/**
 * Filter class used when filtering person times.
 * @author Christian Mesch
 */
public class PTKeyFilter implements Serializable {
	
	private final States states;
	private final double minAge;
	private final double maxAge;

	/**
	 * Constructor for the filter with all arguments.
	 * The minAge and maxAge are inclusive, i.e. [minAge, maxAge].
	 * @param states States you want to filter by
	 * @param minAge Lower bound of age (inclusive)
	 * @param maxAge Upper bound of age (inclusive)
	 */
	public PTKeyFilter(States states, double minAge, double maxAge) {
		this.states = states;
		this.minAge = minAge;
		this.maxAge = maxAge;
	}
	
	/**
	 * Use this filter to filter person times from the data.
	 * The minAge and maxAge are inclusive, i.e. [minAge, maxAge].
	 * @param other PTKey from the map to filter with
	 * @return boolean True if the other key pass the test
	 */
	public boolean filter(PTKey other) {
		return this.minAge <= other.getAge() &&
				this.maxAge >= other.getAge() &&
				this.states.filter(other.getStates());
	}
}
