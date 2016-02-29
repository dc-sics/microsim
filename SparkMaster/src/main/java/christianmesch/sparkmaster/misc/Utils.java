/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.sparkmaster.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import umontreal.ssj.rng.MRG32k3a;
import umontreal.ssj.rng.RandomStreamBase;

/**
 *
 * @author Christian Mesch
 */
public class Utils {

	/**
	 * Generates commands for the workers as a list of strings.
	 * @param randomStreams				A map of random streams
	 * @param NUM_WORKERS				The number of workers
	 * @param REPLICATIONS_PER_WORKER	The number of replications per worker
	 * @return {@literal List<List<String>>} A list of commands for the workers
	 */
	public static List<List<String>> generateCommandList(Map<String, RandomStreamBase> randomStreams, final int NUM_WORKERS, final int REPLICATIONS_PER_WORKER) {
		List<List<String>> commandList = new ArrayList<>(NUM_WORKERS);

		// One list of commands for each worker
		for (int i = 0; i < NUM_WORKERS; i++) {
			List<String> commands = new ArrayList<>();
			
			commands.add(String.valueOf(REPLICATIONS_PER_WORKER));

			// Extract the name and seed from the random streams
			// then convert it to a command list
			for (Entry<String, RandomStreamBase> entry : randomStreams.entrySet()) {
				MRG32k3a tmp = (MRG32k3a) entry.getValue();
				StringBuilder builder = new StringBuilder();
				
				for(long state : tmp.getState()) {
					builder.append(state).append(" ");
				}
				
				commands.add(entry.getKey());
				commands.add(builder.toString());
				
				// Set next substream for next worker
				entry.getValue().resetNextSubstream();
			}
			
			commandList.add(commands);
		}
		
		return commandList;
	}
	
	public static List<Map<String, RandomStreamBase>> inflateStreams(Map<String, RandomStreamBase> randomStreams, final int NUM_WORKERS, final int REPLICATIONS_PER_WORKER) throws Exception {
		List<Map<String, RandomStreamBase>> streamList = new ArrayList<>(NUM_WORKERS);
		
		for(int i = 0; i < NUM_WORKERS; i++) {
			Map<String, RandomStreamBase> streams = new HashMap<>();
			
			for(Entry<String, RandomStreamBase> entry : randomStreams.entrySet()) {
				RandomStreamBase stream = (RandomStreamBase) Class.forName(entry.getValue().getClass().getName())
						.getConstructor().newInstance();
				streams.put(entry.getKey(), stream);
			}
			
			streamList.add(streams);
		}
		
		return streamList;
	}
}
