/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.simulationworker.misc;

import christianmesch.simulationworker.models.States;
import java.io.Serializable;
import java.util.Objects;

/**
 * Composite key class for the events map.
 * @author Christian Mesch
 */
public class EventKey implements Serializable, Comparable<EventKey> {

	private final States states;
	private final String event;
	private final double age;

	public EventKey(States states, String event, double age) {
		this.states = states;
		this.event = event;
		this.age = age;
	}

	public States getStates() {
		return states;
	}

	public String getEvent() {
		return event;
	}

	public double getAge() {
		return age;
	}
	
	@Override
	public int hashCode() {
		int hash = 5;
		hash = 89 * hash + Objects.hashCode(this.states);
		hash = 89 * hash + Objects.hashCode(this.event);
		hash = 89 * hash + (int) (Double.doubleToLongBits(this.age) ^ (Double.doubleToLongBits(this.age) >>> 32));
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final EventKey other = (EventKey) obj;
		if (Double.doubleToLongBits(this.age) != Double.doubleToLongBits(other.age)) {
			return false;
		}
		if (!Objects.equals(this.event, other.event)) {
			return false;
		}
		
		return Objects.equals(this.states, other.states);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("{ states = ")
				.append(states)
				.append(", event = ")
				.append(event)
				.append(", age = ")
				.append(age)
				.append(" }");
		
		return builder.toString();
	}

	/**
	 * {@literal Compare by Event -> States -> age}
	 * @param other EventKey to compare to
	 * @return int negative, 0, positive depending on the comparison
	 */
	@Override
	public int compareTo(EventKey other) {
		int eventComp = event.compareTo(other.event);
		if(eventComp != 0) return eventComp;
		
		int statesComp = states.compareTo(other.states);
		if(statesComp != 0) return statesComp;
		
		int ageComp = (int) (age - other.age);
		
		return ageComp;
	}
}
