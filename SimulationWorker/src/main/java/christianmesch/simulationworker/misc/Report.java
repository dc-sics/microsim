/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.simulationworker.misc;

import christianmesch.simulationworker.models.Person;
import christianmesch.simulationworker.models.States;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Report class.
 * Here's where all reporting and logging takes place.
 * <br><br>
 * TODO:<br>
 * <ul>
 * <li>More sophisticated reporting. Maybe graphs and/or tables?</li>
 * </ul>
 * @author Christian Mesch
 */
public class Report implements Serializable {
	
	private final Map<EventKey, Integer> events;
	private final Map<PTKey, Double> personTimes;
	
	public Report() {
		events = new HashMap<>();
		personTimes = new HashMap<>();
	}
	
	/**
	 * Copy constructor.
	 * @param other Report to be copied
	 */
	public Report(Report other) {
		events = other.events;
		personTimes = other.personTimes;
	}

	public Map<EventKey, Integer> getEvents() {
		return events;
	}

	public Map<PTKey, Double> getPersonTimes() {
		return personTimes;
	}
	
	/**
	 * Log the event
	 * @param person Person with all its states to be logged
	 * @param event Event to log
	 * @param age Age of the person when the event fired
	 */
	public void logEvent(Person person, String event, double age) {
		EventKey key = new EventKey(person.copyStates(), event, Math.floor(age));
		
		events.put(key, events.getOrDefault(key, 0) + 1);
	}
	
	/**
	 * Private method to add an event.
	 * This will only be used by another method to add events from another report.
	 * @param key Key in the map
	 * @param value Value which should be added to the key
	 */
	private void addEvent(EventKey key, Integer value) {
		events.put(key, events.getOrDefault(key, 0) + value);
	}
	
	/**
	 * Log the person times. Should be used when a state has changed for a person. (you log the previous state).
	 * @param person Person with all its states to be logged
	 * @param age Age of the person
	 */
	public void logPersonTime(Person person, double age) {
		final double previous = Math.floor(person.getPreviousTime());
		final double now = Math.floor(age);
		final States states = person.copyStates();

		// do not update if there has been no change in time
		if (age == person.getPreviousTime()) return;
		
		// Go from last logging time to now and add person times
		for(double t = previous; t <= now; t++) {
			PTKey key = new PTKey(states, t);
			double value = personTimes.getOrDefault(key, 0.0)
					+ Math.min(t + 1.0, age) - Math.max(t, person.getPreviousTime());
			
			personTimes.put(key, value);
		}
	}
	
	/**
	 * Private method to add a person time.
	 * This will only be used by another method to add person times from another report.
	 * @param key Key in the map
	 * @param value Value which should be added to the key
	 */
	private void addPersonTime(PTKey key, double value) {
		double newValue = personTimes.getOrDefault(key, 0.0) + value;
		
		personTimes.put(key, newValue);
	}

	/**
	 * Add all events and person times from other to this report
	 * @param other Report to be added to this report
	 */
	private void addAll(Report other) {
		other.events.forEach((EventKey key, Integer i) -> addEvent(key, i));
		other.personTimes.forEach((PTKey key, Double d) -> addPersonTime(key, d));
	}
	
	/**
	 * Concatenate this report with the other report and return a new instance of it
	 * @param other Report to be concatenated with this report
	 * @return Report Concatenated report
	 */
	public Report concat(Report other) {
		Report newReport = new Report(other);
		
		newReport.addAll(this);
		
		return newReport;
	}
	
	/**
	 * Filter the person times based on a pattern (key)
	 * @param filterKey A key with the pattern you want to filter by.
	 * @return Report object with the filtered out person times.
	 */
	public Report filterPersonTimes(PTKeyFilter filterKey) {
		Report report = new Report();
		
		for(Entry<PTKey, Double> entry : personTimes.entrySet()) {
			if(filterKey.filter(entry.getKey())) {
				report.addPersonTime(entry.getKey(), entry.getValue());
			}
		}
		
		return report;
	}
	
	/**
	 * Filter the events based on a pattern (key)
	 * @param filterKey A key with the pattern you want to filter by.
	 * @return Report object with the filtered out events.
	 */
	public Report filterEvents(EventKeyFilter filterKey) {
		Report report = new Report();
		
		for(Entry<EventKey, Integer> entry : events.entrySet()) {
			if(filterKey.filter(entry.getKey())) {
				report.addEvent(entry.getKey(), entry.getValue());
			}
		}
		
		return report;
	}
	
	
	/**
	 * Print all events to std out
	 */
	public void printEvents() {
		
		SortedMap<EventKey, Integer> tmp = new TreeMap<>(events);
		tmp.forEach((EventKey key, Integer i) -> System.out.println(key + " = " + i));
		
		/*
		events.forEach((EventKey key, Integer i) -> System.out.println(key + " = " + i));
		*/
		
		// For debug purposes
		int count = 0;
		for(Integer i : events.values()) {
			count += i;
		}
		
		System.out.println(count);
	}
	
	/**
	 * Print all person times to std out
	 */
	public void printPersonTimes() {
		
		SortedMap<PTKey, Double> tmp = new TreeMap<>(personTimes);
		tmp.forEach((PTKey key, Double d) -> System.out.println(key + " = " + d));
		
		/*
		personTimes.forEach((PTKey key, Double d) -> System.out.println(key + " = " + d));
		*/
		
		// For debug purposes
		double count = 0;
		for(Double d : personTimes.values()) {
			count += d;
		}
		
		System.out.println(count);
	}
	
	/**
	 * Report everything that has been logged
	 */
	public void report() {
		printEvents();
		printPersonTimes();
	}
}
