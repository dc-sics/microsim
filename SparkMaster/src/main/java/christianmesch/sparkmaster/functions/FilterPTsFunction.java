/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.sparkmaster.functions;

import christianmesch.simulationworker.misc.PTKeyFilter;
import christianmesch.simulationworker.misc.Report;
import org.apache.spark.api.java.function.Function;

/**
 * Filter person times.
 * To be used with a PTKeyFilter when you want to filter specific person times.
 * @author Christian Mesch
 */
public class FilterPTsFunction implements Function<Report, Report> {

	private final PTKeyFilter filterKey;

	public FilterPTsFunction(PTKeyFilter filterKey) {
		this.filterKey = filterKey;
	}
	
	@Override
	public Report call(Report report) throws Exception {
		return report.filterPersonTimes(filterKey);
	}
	
}
