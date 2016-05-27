/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.simulationworker.events;

import christianmesch.simulationworker.misc.Container;
import christianmesch.simulationworker.misc.utils.Utils;
import christianmesch.simulationworker.models.States;
import java.util.Map;
import umontreal.ssj.randvar.ExponentialGen;
import umontreal.ssj.rng.RandomStreamBase;
import umontreal.ssj.simevents.Simulator;

/**
 *
 * @author Christian Mesch
 */
public class Metastatic extends MyEvent {

	public Metastatic(Simulator sim, Container container) {
		super(sim, container);
	}

	@Override
	public void actions() {
		logEvent();
		logPersonTime();
		
		Map<String, Double> stateVars = container.getPerson().getStateVariables();
		Map<String, Double> params = container.getPerson().getParameters();
		Map<String, RandomStreamBase> streams = container.getRandomStreams();

		container.getPerson().setStage(States.Stage.METASTATIC);

		double t_onset = stateVars.get("ageCancerOnset") - 35.0;
		double psa_onset = Utils.psaMean(stateVars, sim.time());
		double u = ExponentialGen.nextDouble(streams.get("cancer"), 1.0);

		double acdm = (Math.log((stateVars.get("beta1") + stateVars.get("beta2")) * u
				/ (params.get("gc") * params.get("thetac")) + psa_onset)
				- stateVars.get("beta0") + stateVars.get("beta2") * t_onset)
				/ (stateVars.get("beta1") + stateVars.get("beta2")) + 35.0;

		stateVars.put("ageClinicalDiagnosisMetastatic", acdm);

		new ClinicalDiagnosis(sim, container).schedule(acdm);
	}

}
