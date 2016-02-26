/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.flinkmaster.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import umontreal.iro.lecuyer.rng.MRG32k3a;
import umontreal.iro.lecuyer.rng.RandomStreamBase;

/**
 *
 * @author Christian Mesch
 */
public class Utils {
	
	public static String[][] generateCommandList(Map<String, RandomStreamBase> randomStreams, final int NUM_WORKERS, final int REPLICATIONS_PER_WORKER) {
		String[][] commandList = new String[NUM_WORKERS][];

		// One list of commands for each worker
		for (int i = 0; i < NUM_WORKERS; i++) {
			List<String> commands = new ArrayList<>();
			
			commands.add(String.valueOf(REPLICATIONS_PER_WORKER));

			// Extract the name and seed from the random streams
			// then convert it to a command list
			for (Map.Entry<String, RandomStreamBase> entry : randomStreams.entrySet()) {
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
			
			commandList[i] = commands.toArray(new String[commands.size()]);
		}
		
		return commandList;
	}
}
