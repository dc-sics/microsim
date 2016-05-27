/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.simulationworker.misc.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import umontreal.ssj.rng.RandomStreamBase;
import umontreal.ssj.simevents.Simulator;
import umontreal.ssj.simevents.Event;
import umontreal.ssj.simevents.eventlist.EventList;
import umontreal.ssj.util.Num;

/**
 * A static utility class.
 *
 * @author Christian Mesch
 */
public class Utils {

	/**
	 * Generate a list of maps with strings and randomstreams. Each map will
	 * have all the randomstreams a worker will need.
	 *
	 * @param randomStreams Map of random streams
	 * @param replications Number of replications
	 * @return {@literal List<Map<String, RandomStreamBase>>} List of random
	 * stream maps
	 */
	public static List<Map<String, RandomStreamBase>> generateMapList(Map<String, RandomStreamBase> randomStreams, int replications) {
		List<Map<String, RandomStreamBase>> list = new ArrayList<>(replications);

		for(int i = 0; i < replications; i++) {
			Map<String, RandomStreamBase> tmpMap = new HashMap<>();

			for(Entry<String, RandomStreamBase> entry : randomStreams.entrySet()) {
				RandomStreamBase tmpStream = entry.getValue().clone();

				tmpMap.put(entry.getKey(), tmpStream);

				entry.getValue().resetNextSubstream();
			}

			list.add(tmpMap);
		}

		return list;
	}

	/**
	 * Remove events of a specific class from the simulator.
	 *
	 * @param <T> Event type to be canceled
	 * @param simulator Simulator in which the event resides
	 * @param type Event type to be canceled
	 */
	public static <T> void removeEvents(Simulator simulator, Class<T> type) {
		EventList list = simulator.getEventList();
		
		for(Iterator<Event> it = list.iterator();  it.hasNext(); ) {
			Event event = it.next();
			
			if(type.isInstance(event)) {
				it.remove();
			}
		}
	}

	/**
	 * Calculate the lambda used in WeibullGen
	 *
	 * @param alpha Alpha
	 * @param meanX Mean of x
	 * @return double Lambda
	 */
	public static double lambda(double alpha, double meanX) {
		return Math.exp(Num.lnGamma(1.0 + 1.0 / alpha)) / meanX;
	}

	public static double[] iota(int n) {
		double[] values = new double[n];
		for(int i = 0; i < n; i++) {
			values[i] = i;
		}

		return values;
	}
	
	public static double psaMean(Map<String, Double> vars, double age) {
		double t = age < 35.0 ? 0.0 : age - 35.0;
		double t_onset = vars.get("ageCancerOnset") - 35.0;
		
		if(age < vars.get("ageCancerOnset")) {
			return Math.exp(vars.get("beta0") + vars.get("beta1") * t);
		} else {
			return Math.exp(vars.get("beta0") + vars.get("beta1") * t 
					+ vars.get("beta2") * (t - t_onset));
		}
	}
}
