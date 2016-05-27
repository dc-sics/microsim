/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.simulationworker.models;

import java.io.Serializable;
import java.util.Objects;

/**
 * State class for a person
 * @author Christian Mesch
 */
public class States implements Serializable, Comparable<States> {
	
	/**
	 * The different health states which can be used.
	 * Please note that NONE is only to be used when creating a filter.
	 */
	public static enum HealthState {
		HEALTHY,
		LOCOREGIONAL,
		METASTATIC,
		NONE
	}
	
	/**
	 * The different diagnosis states which can be used.
	 * Please note that NONE is only to be used when creating a filter.
	 */
	public static enum Diagnosis {
		NOT_DIAGNOSED,
		CLINICAL_DIAGNOSIS,
		SCREEN_DIAGNOSIS,
		NONE
	}
	
	private HealthState healthState;
	private Diagnosis diagnosis;
	private int attribute;
	
	/**
	 * Standard constructor.
	 * Initializes the object with starting values.
	 */
	public States() {
		healthState = HealthState.HEALTHY;
		diagnosis = Diagnosis.NOT_DIAGNOSED;
		attribute = 0;
	}
	
	/**
	 * Copy constructor.
	 * Copies the internals of the original object
	 * @param original States to be copied
	 */
	public States(States original) {
		healthState = original.healthState;
		diagnosis = original.diagnosis;
		attribute = original.attribute;
	}

	/**
	 * Constructor which takes all states as parameters
	 * @param healthState Health state
	 * @param diagnosis  Diagnosis
	 */
	public States(HealthState healthState, Diagnosis diagnosis, int attribute) {
		this.healthState = healthState;
		this.diagnosis = diagnosis;
		this.attribute = attribute;
	}
	
	public HealthState getHealthState() {
		return healthState;
	}

	public void setHealthState(HealthState healthState) {
		this.healthState = healthState;
	}

	public Diagnosis getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(Diagnosis diagnosis) {
		this.diagnosis = diagnosis;
	}

	public int getAttribute() {
		return attribute;
	}

	public void setAttribute(int attribute) {
		this.attribute = attribute;
	}

	/**
	 * Filter out states with the possibility to have "wildcards".
	 * Compare this to multiple other States.
	 * @param other
	 * @return 
	 */
	public boolean filter(States other) {
		// Is states irrelevant? I.e. we want to have all combinations of states
		if(this.diagnosis.equals(Diagnosis.NONE) && 
				this.healthState.equals(HealthState.NONE))
			return true;
		
		// Is diagnosis a wildcard?
		if(this.diagnosis.equals(Diagnosis.NONE))
			return this.healthState.equals(other.healthState);
		
		// Is healthstate a wildcard?
		if(this.healthState.equals(HealthState.NONE))
			return this.diagnosis.equals(other.diagnosis);
		
		// No wildcards, compare everything
		return this.diagnosis.equals(other.diagnosis) &&
				this.healthState.equals(other.healthState);
	}
	
	@Override
	public int hashCode() {
		int hash = 3;
		hash = 97 * hash + Objects.hashCode(this.healthState);
		hash = 97 * hash + Objects.hashCode(this.diagnosis);
		hash = 97 * hash + Objects.hashCode(this.attribute);
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
		
		final States other = (States) obj;
		
		if (this.healthState != other.healthState) {
			return false;
		}
		
		if (this.diagnosis != other.diagnosis) {
			return false;
		}
		
		return this.attribute == other.attribute;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("{ healthState = ")
				.append(healthState)
				.append(", diagnosis = ")
				.append(diagnosis)
				.append(", attribute = ")
				.append(attribute)
				.append(" }");
		
		return builder.toString();
	}
	
	@Override
	public int compareTo(States o) {
		int health = healthState.compareTo(o.healthState);
		if(health != 0) return health;
		
		int diag = diagnosis.compareTo(o.diagnosis);
		if(diag != 0) return diag;
		
		int attr = Integer.compare(attribute, o.attribute);
		return attr;
	}

	
}
