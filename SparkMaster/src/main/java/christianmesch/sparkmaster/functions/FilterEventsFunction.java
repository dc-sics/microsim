/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.sparkmaster.functions;

import christianmesch.simulationworker.misc.EventKeyFilter;
import christianmesch.simulationworker.misc.Report;
import org.apache.spark.api.java.function.Function;

/**
 * Filter events.
 * To be used with an EventKeyFilter when you want to filter specific events.
 * @author Christian Mesch
 */
public class FilterEventsFunction implements Function<Report, Report> {

	private final EventKeyFilter filterKey;
	
	public FilterEventsFunction(EventKeyFilter filterKey) {
		this.filterKey = filterKey;
	}
	
	@Override
	public Report call(Report report) throws Exception {
		return report.filterEvents(filterKey);
	}
	
}
