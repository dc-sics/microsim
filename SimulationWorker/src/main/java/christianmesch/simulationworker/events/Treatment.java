/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.simulationworker.events;

import christianmesch.simulationworker.misc.Container;
import christianmesch.simulationworker.models.Person;
import christianmesch.simulationworker.models.States;
import umontreal.ssj.simevents.Simulator;

/**
 *
 * @author Christian Mesch
 */
public class Treatment extends MyEvent {

	public Treatment(Simulator sim, Container container) {
		super(sim, container);
	}

	@Override
	public void actions() {
		logEvent();
		logPersonTime();
		
		Person person = container.getPerson();
		double age_c = person.getStates().getStage() == States.Stage.LOCALISED ?
			person.getStateVariable("ageClinicalDiagnosisLocalised") :
			person.getStateVariable("ageClinicalDiagnosisMetastatic");
		
		double lead_time = age_c - sim.time();
	}
	
}
