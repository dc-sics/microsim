/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.simulationworker.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import umontreal.ssj.rng.RandomStreamBase;
import umontreal.ssj.simevents.Event;
import umontreal.ssj.simevents.Simulator;
import umontreal.ssj.util.Num;

/**
 * A static utility class.
 * @author Christian Mesch
 */
public class Utils {

	/**
	 * Generate a list of maps with strings and randomstreams.
	 * Each map will have all the randomstreams a worker will need.
	 * @param randomStreams Map of random streams
	 * @param replications Number of replications
	 * @return {@literal List<Map<String, RandomStreamBase>>} List of random stream maps
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
	 * Parse a string of longs to a long array.
	 * The string must be space separated, i.e. "12345 12345 12345"
	 * @param string Long array as a string, e.g. "12345 12345 12345"
	 * @return long[] Parsed long array
	 */
	public static long[] parseLongArray(String string) {
		String[] split = string.split(" ");
		long[] arr = new long[split.length];
		
		for(int i = 0; i < split.length; i++) {
			arr[i] = Long.parseLong(split[i]);
		}
		
		return arr;
	}
	
	/**
	 * Remove events of a specific class from the simulator.
	 * @param <T> Event type to be canceled
	 * @param simulator Simulator in which the event resides
	 * @param type Event type to be canceled
	 */
	public static <T> void removeEvents(Simulator simulator, Class<T> type) {
		for(Event event : simulator.getEventList()) {
			if(type.isInstance(event)) {
				event.cancel();
			}
		}
	}
	
	/**
	 * Calculate the lambda used in WeibullGen
	 * @param alpha Alpha
	 * @param meanX Mean of x
	 * @return double Lambda
	 */
	public static double lambda(double alpha, double meanX) {
		return Math.exp(Num.lnGamma(1.0 + 1.0 / alpha)) / meanX;
	}
}
