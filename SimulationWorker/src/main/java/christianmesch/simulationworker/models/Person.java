/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.simulationworker.models;

import java.util.HashMap;
import java.util.Map;
import umontreal.ssj.simevents.Simulator;

/**
 * Object with all relevant information about the person in the replication.
 *
 * @author Christian Mesch
 */
public class Person {

	private final States states = new States();
	private final Simulator simulator;

	private double previousTime = 0.0;

	// Use a map to have less variables
	private final Map<String, Double> parameters = new HashMap<>();
	private final Map<String, Double> stateVariables = new HashMap<>();

	// Will these always be the same?
	private double[] mubeta2 = new double[]{0.051, 0.129, 0.1678};
	private double[] sebeta2 = new double[]{0.064, 0.087, 0.3968};
	private double[] mu0 = new double[]{
		0.00219, 0.000304, 5.2e-05, 0.000139, 0.000141, 3.6e-05, 7.3e-05,
		0.000129, 3.8e-05, 0.000137, 6e-05, 8.1e-05, 6.1e-05, 0.00012,
		0.000117, 0.000183, 0.000185, 0.000397, 0.000394, 0.000585, 0.000448,
		0.000696, 0.000611, 0.000708, 0.000659, 0.000643, 0.000654, 0.000651,
		0.000687, 0.000637, 0.00063, 0.000892, 0.000543, 0.00058, 0.00077,
		0.000702, 0.000768, 0.000664, 0.000787, 0.00081, 0.000991, 9e-04,
		0.000933, 0.001229, 0.001633, 0.001396, 0.001673, 0.001926, 0.002217,
		0.002562, 0.002648, 0.002949, 0.002729, 0.003415, 0.003694, 0.004491,
		0.00506, 0.004568, 0.006163, 0.006988, 0.006744, 0.00765, 0.007914,
		0.009153, 0.010231, 0.011971, 0.013092, 0.013839, 0.015995, 0.017693,
		0.018548, 0.020708, 0.022404, 0.02572, 0.028039, 0.031564, 0.038182,
		0.042057, 0.047361, 0.05315, 0.058238, 0.062619, 0.074934, 0.089776,
		0.099887, 0.112347, 0.125351, 0.143077, 0.153189, 0.179702, 0.198436,
		0.240339, 0.256215, 0.275103, 0.314157, 0.345252, 0.359275, 0.41768,
		0.430279, 0.463636, 0.491275, 0.549738, 0.354545, 0.553846, 0.461538,
		0.782609};

	public Person(Simulator simulator) {
		this.simulator = simulator;

		// Set default parameters. Guessing these will change for each run
		parameters.put("g_onset", 0.0005);
		parameters.put("gc", 0.0015);
		parameters.put("gm", 0.0004);
		parameters.put("thetac", 19.1334);
		parameters.put("alpha7", 0.13);
		parameters.put("beta7", 0.006);
		parameters.put("alpha8", -4.61);
		parameters.put("beta8", 0.06);
		parameters.put("tau2", 0.0829);
		parameters.put("mubeta0", -1.609);
		parameters.put("sebeta0", 0.2384);
		parameters.put("mubeta1", 0.04463);
		parameters.put("sebeta1", 0.0430);
		
		stateVariables.put("ageClinicalDiagnosisLocalised", -1.0);
		stateVariables.put("ageClinicalDiagnosisMetastatic", -1.0);
		stateVariables.put("ageCancerOnset", -1.0);
		stateVariables.put("beta0", -1.0);
		stateVariables.put("beta1", -1.0);
		stateVariables.put("beta2", -1.0);
	}

	/**
	 * Reset the person by creating and returning a new instance with the same
	 * simulator.
	 *
	 * @return Person New instance of Person
	 */
	public Person reset() {
		return new Person(this.simulator);
	}

	public States getStates() {
		return states;
	}
	
	/**
	 * Get the time when the last event occurred. Used when logging person times
	 *
	 * @return previousTime Time for previous event
	 */
	public double getPreviousTime() {
		return previousTime;
	}

	/**
	 * Set the time when the last event occurred.
	 *
	 * @param previousTime
	 */
	public void setPreviousTime(double previousTime) {
		this.previousTime = previousTime;
	}

	/**
	 * Set stage for person and update the previous time for a state
	 * change if new state differs.
	 *
	 * @param stage New stage
	 */
	public void setStage(States.Stage stage) {
		if(states.getStage() != stage) {
			states.setStage(stage);
			previousTime = simulator.time();
		}
	}

	/**
	 * Set diagnosis for person and update the previous time for a state change
	 * if new state differs.
	 *
	 * @param diagnosis New diagnosis state
	 */
	public void setDiagnosis(States.Diagnosis diagnosis) {
		if(states.getDiagnosis() != diagnosis) {
			states.setDiagnosis(diagnosis);
			previousTime = simulator.time();
		}
	}

	public void setExtGrade(States.ExtGrade extGrade) {
		if(states.getExtGrade() != extGrade) {
			states.setExtGrade(extGrade);
			previousTime = simulator.time();
		}
	}

	public void setScreenType(States.ScreenType screenType) {
		if(states.getScreenType() != screenType) {
			states.setScreenType(screenType);
			previousTime = simulator.time();
		}
	}

	public void setTreatmentType(States.TreatmentType treatmentType) {
		if(states.getTreatmentType() != treatmentType) {
			states.setTreatmentType(treatmentType);
			previousTime = simulator.time();
		}
	}

	public void setSurvival(States.Survival survival) {
		if(states.getSurvival() != survival) {
			states.setSurvival(survival);
			previousTime = simulator.time();
		}
	}

	public void setBiomarkerModel(States.BiomarkerModel biomarkerModel) {
		if(states.getBiomarkerModel() != biomarkerModel) {
			states.setBiomarkerModel(biomarkerModel);
			previousTime = simulator.time();
		}
	}

	public Map<String, Double> getParameters() {
		return parameters;
	}

	public Map<String, Double> getStateVariables() {
		return stateVariables;
	}
	
	public Double getStateVariable(String key) {
		return stateVariables.get(key);
	}
	
	public void setStateVariable(String key, Double value) {
		stateVariables.put(key, value);
	}
	
	public Double getParameter(String key) {
		return parameters.get(key);
	}
	
	public void setParameter(String key, Double value) {
		parameters.put(key, value);
	}

	public double[] getMubeta2() {
		return mubeta2;
	}

	public double[] getSebeta2() {
		return sebeta2;
	}

	public double[] getMu0() {
		return mu0;
	}
	
	/**
	 * Return a copy of the persons states
	 *
	 * @return States Copy of the persons states
	 */
	public States copyStates() {
		return new States(states);
	}
}
