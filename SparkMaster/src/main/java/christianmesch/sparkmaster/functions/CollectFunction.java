/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.sparkmaster.functions;

import christianmesch.simulationworker.misc.Report;
import org.apache.spark.api.java.function.Function2;

/**
 * Collect function. 
 * To be used when you want to collect and combine all reports to one report.
 * @author Christian Mesch
 */
public class CollectFunction implements Function2<Report, Report, Report> {

	@Override
	public Report call(Report t1, Report t2) throws Exception {
		return t1.concat(t2);
	}
	
}
