/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.simulationworker.events;

import christianmesch.simulationworker.misc.Container;
import christianmesch.simulationworker.misc.utils.Utils;
import christianmesch.simulationworker.models.States;
import umontreal.ssj.simevents.Simulator;

/**
 *
 * @author Christian Mesch
 */
public class ScreenDiagnosis extends MyEvent {

	public ScreenDiagnosis(Simulator sim, Container container) {
		super(sim, container);
	}

	@Override
	public void actions() {
		logEvent();
		logPersonTime();
		
		container.getPerson().setDiagnosis(States.Diagnosis.SCREEN_DIAGNOSIS);
		
		Utils.removeEvents(sim, Metastatic.class);
		Utils.removeEvents(sim, Screen.class);
		Utils.removeEvents(sim, ClinicalDiagnosis.class);
		
		new Treatment(sim, container).schedule(sim.time());
	}
	
}
