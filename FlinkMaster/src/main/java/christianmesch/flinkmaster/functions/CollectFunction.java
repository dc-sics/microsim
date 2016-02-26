/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.flinkmaster.functions;

import christianmesch.simulationworker.misc.Report;
import org.apache.flink.api.common.functions.ReduceFunction;

/**
 *
 * @author Christian Mesch
 */
public class CollectFunction implements ReduceFunction<Report> {

	@Override
	public Report reduce(Report t, Report t1) throws Exception {
		return t.concat(t1);
	}
	
}
