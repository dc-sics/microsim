/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.sparkmaster.schemas;

import christianmesch.simulationworker.models.States;
import java.io.Serializable;

/**
 *
 * @author Christian Mesch
 */
public abstract class Schema implements Serializable {
	
	private String healthState;
	private String diagnosis;

	public Schema() {
	}
	
	public Schema(States states) {
		this.healthState = states.getHealthState().toString();
		this.diagnosis = states.getDiagnosis().toString();
	}

	public String getHealthState() {
		return healthState;
	}

	public void setHealthState(String healthState) {
		this.healthState = healthState;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}	
}
