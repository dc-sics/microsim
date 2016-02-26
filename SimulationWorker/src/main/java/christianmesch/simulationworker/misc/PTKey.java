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
 * Composite key class for the person times maps.
 * @author Christian Mesch
 */
public class PTKey implements Serializable, Comparable<PTKey> {
	
	private final States states;
	private final double age;

	public PTKey(States states, double age) {
		this.states = states;
		this.age = age;
	}

	public States getStates() {
		return states;
	}

	public double getAge() {
		return age;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 59 * hash + Objects.hashCode(this.states);
		hash = 59 * hash + (int) (Double.doubleToLongBits(this.age) ^ (Double.doubleToLongBits(this.age) >>> 32));
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
		final PTKey other = (PTKey) obj;
		if (Double.doubleToLongBits(this.age) != Double.doubleToLongBits(other.age)) {
			return false;
		}
		
		return Objects.equals(this.states, other.states);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("{ states = ")
				.append(states)
				.append(", age = ")
				.append(age)
				.append(" }");
		
		return builder.toString();
	}

	/**
	 * {@literal Compare by States -> age}
	 * @param other EventKey to compare to
	 * @return int negative, 0, positive depending on the comparison
	 */
	@Override
	public int compareTo(PTKey other) {
		int stateComp = states.compareTo(other.states);
		if(stateComp != 0) return stateComp;
		
		int ageComp = (int) (age - other.age);
		return ageComp;
	}
	
	
}
