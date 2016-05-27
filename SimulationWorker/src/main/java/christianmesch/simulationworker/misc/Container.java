/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.simulationworker.misc;

import christianmesch.simulationworker.models.Person;
import java.util.Map;
import umontreal.ssj.rng.RandomStreamBase;

/**
 * Container class to hold all data which will be sent to each new event.
 * @author Christian Mesch
 */
public class Container {
	
	private final Report report;
	private final Map<String, RandomStreamBase> randomStreams;
	private Person person;

	public Container(Report report, Map<String, RandomStreamBase> randomStreams, Person person) {
		this.report = report;
		this.randomStreams = randomStreams;
		this.person = person;
		person.getStates().setAttribute(randomStreams.get("attributes").nextInt(1,100000));
	}

	public Report getReport() {
		return report;
	}

	public Map<String, RandomStreamBase> getRandomStreams() {
		return randomStreams;
	}

	public Person getPerson() {
		return person;
	}
	
	/**
	 * Reset the Person object to its starting state.
	 */
	public void resetPerson() {
		person = person.reset();
		person.getStates().setAttribute(randomStreams.get("attributes").nextInt(1,100000));
	}
	
	public void resetNextSubstreams() {
		for(RandomStreamBase stream : randomStreams.values()) {
			stream.resetNextSubstream();
		}
	}
	
}
