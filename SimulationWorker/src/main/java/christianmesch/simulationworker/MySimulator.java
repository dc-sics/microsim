/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.simulationworker;

import christianmesch.simulationworker.events.Localised;
import christianmesch.simulationworker.events.OtherDeath;
import christianmesch.simulationworker.misc.Container;
import christianmesch.simulationworker.misc.NormalPosGen;
import christianmesch.simulationworker.misc.Report;
import christianmesch.simulationworker.misc.utils.Rpexp;
import christianmesch.simulationworker.misc.utils.Utils;
import christianmesch.simulationworker.models.Person;
import christianmesch.simulationworker.models.States;
import java.util.Map;
import umontreal.ssj.randvar.ExponentialGen;
import umontreal.ssj.randvar.NormalGen;
import umontreal.ssj.rng.RandomStreamBase;
import umontreal.ssj.simevents.Simulator;

/**
 * Simulator class.
 *
 * @author Christian Mesch
 */
public class MySimulator {

	public Simulator simulator = new Simulator();

	private final Container container;
	private final Rpexp randOtherDeath;

	public MySimulator(Map<String, RandomStreamBase> randomStreams) {
		container = new Container(new Report(),
				randomStreams, new Person(simulator));

		randOtherDeath = new Rpexp(container.getPerson().getMu0(),
				Utils.iota(container.getPerson().getMu0().length));
	}

	/**
	 * Getter for container
	 *
	 * @return Container The Container object
	 */
	public Container getContainer() {
		return container;
	}

	/**
	 * Initialization of the simulator and events
	 *
	 * <ul>
	 * <li>Add initial events in this method</li>
	 * </ul>
	 */
	private void init() {
		simulator.init();

		// New person for a new replication
		container.resetPerson();

		// Variables to be used
		Person person = container.getPerson();
		Map<String, RandomStreamBase> streams = container.getRandomStreams();

		// Age of cancer onset
		double aco = 35.0 + Math.sqrt(2.0
				* ExponentialGen.nextDouble(container.getRandomStreams().get("cancer"), 1.0)
				/ person.getParameter("g_onset"));

		person.setStateVariable("ageCancerOnset", aco);
		person.setParameter("t_onset", aco - 35.0);

		// Gleason score
		double u = streams.get("cancer").nextDouble();

		if(u < Math.exp(person.getParameter("alpha8")
				+ person.getParameter("beta8") * person.getParameter("t_onset"))) {

			person.setExtGrade(States.ExtGrade.GLEASON_GE_8);
		} else if(u > 1 - (person.getParameter("alpha7")
				+ person.getParameter("beta7") * person.getParameter("t_onset"))) {

			person.setExtGrade(States.ExtGrade.GLEASON_7);
		} else {
			person.setExtGrade(States.ExtGrade.GLEASON_LE_6);
		}

		// Is using ordinals the best way to do this?
		int extGradeOrdinal = person.getStates().getExtGrade().ordinal();
		
		// PSA random effects
		double beta0 = NormalGen.nextDouble(streams.get("cancer"),
				person.getParameter("mubeta0"),
				person.getParameter("sebeta0"));
		double beta1 = NormalPosGen.nextDouble(streams.get("cancer"),
				person.getParameter("mubeta1"),
				person.getParameter("sebeta1"));
		double beta2 = NormalPosGen.nextDouble(streams.get("cancer"),
				person.getMubeta2()[extGradeOrdinal],
				person.getSebeta2()[extGradeOrdinal]);

		person.setStateVariable("beta0", beta0);
		person.setStateVariable("beta1", beta1);
		person.setStateVariable("beta2", beta2);

		// Schedule events
		new OtherDeath(simulator, container).schedule(
				randOtherDeath.nextDouble(streams.get("otherDeath")));

		new Localised(simulator, container).schedule(aco);
	}

	/**
	 * Run the simulation with defined number of replications
	 *
	 * @param REPLICATIONS Number of replications
	 * @return Report The report for REPLICATIONS of microsimulations
	 */
	public Report run(final int REPLICATIONS) {
		for(int i = 0; i < REPLICATIONS; i++) {
			init();
			simulator.start();

			container.resetNextSubstreams();
		}

		return container.getReport();

	}
}
