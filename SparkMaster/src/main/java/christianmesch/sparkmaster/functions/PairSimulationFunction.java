/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.sparkmaster.functions;

import christianmesch.simulationworker.SimulationWorker;
import christianmesch.simulationworker.misc.EventKey;
import christianmesch.simulationworker.misc.Report;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import scala.Tuple2;
import umontreal.ssj.rng.RandomStreamBase;

/**
 *
 * @author Christian Mesch
 */
public class PairSimulationFunction implements PairFlatMapFunction<Map<String, RandomStreamBase>, EventKey, Integer> {

	private final int REPLICATIONS_PER_WORKER;
	
	public PairSimulationFunction(int REPLICATIONS_PER_WORKER) {
		this.REPLICATIONS_PER_WORKER = REPLICATIONS_PER_WORKER;
	}
	
	@Override
	public Iterable<Tuple2<EventKey, Integer>> call(Map<String, RandomStreamBase> commands) throws Exception {
		Report report = SimulationWorker.main(commands, REPLICATIONS_PER_WORKER);
		
		List<Tuple2<EventKey, Integer>> tuples = new ArrayList<>();
		for(Entry<EventKey, Integer> entry : report.getEvents().entrySet()) {
			tuples.add(new Tuple2<>(entry.getKey(), entry.getValue()));
		}
		
		return tuples;
	}
	
}
