/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.sparkmaster.misc;

import christianmesch.simulationworker.misc.EventKey;
import christianmesch.simulationworker.misc.PTKey;
import christianmesch.simulationworker.misc.PTKeyFilter;
import christianmesch.simulationworker.misc.Report;
import christianmesch.simulationworker.models.States;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Christian Mesch
 */
public class ChartCreator {
	
	/**
	 * Method for creating incidence charts. <br><br>
	 * 
	 * Right now it's only using one line. Can be fixed if needed.<br>
	 * The saving to working directory will change as well.
	 * 
	 * @param report Report to create the chart from
	 * @param title Title of the chart
	 * @param xLabel Label of the X axis
	 * @param yLabel Label of the Y axis
	 * @param lineName Name of the line in the chart
	 * @param width Width of the chart in px
	 * @param height Height of the chart in px
	 * @param events One or more events to create the incidence chart on
	 * @throws IOException 
	 */
	public static void incidenceChart(Report report, String title, String xLabel,
			String yLabel, String lineName, int width, int height, String... events) throws IOException {
		
		DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		
		Map<Double, Double> totalPTs = totalPersonTimes(report);
		Map<Double, Integer> totalEvents = totalEvents(report, events);
		
		// Add all points to the data set
		for(Entry<Double, Integer> entry : totalEvents.entrySet()) {
			Double value = entry.getValue() / totalPTs.get(entry.getKey());
			
			dataSet.addValue(value, lineName, entry.getKey());
		}
		
		// Create chart object
		JFreeChart chartObject = ChartFactory.createLineChart(title, xLabel,
				yLabel, dataSet, PlotOrientation.VERTICAL, true, true, false);
		
		// Save as chart.png to working directory
		File chart = new File("chart.png");
		ChartUtilities.saveChartAsPNG(chart, chartObject, width, height);
	}
	
	/**
	 * Calculate the total person times for all ages
	 * 
	 * @param report Report to calculate from
	 * @return Map with the total person times for all ages
	 */
	private static Map<Double, Double> totalPersonTimes(Report report) {
		Map<Double, Double> aggregatePTs = new HashMap<>();
		
		for(Entry<PTKey, Double> entry : report.getPersonTimes().entrySet()) {
			Double age = entry.getKey().getAge();
			
			// Add or update
			aggregatePTs.put(age, aggregatePTs.getOrDefault(age, 0.0) + entry.getValue());
		}
		
		return aggregatePTs;
	}
	
	/**
	 * Count the total events for all ages
	 * 
	 * @param report Report to calculate from
	 * @param events The events to be counted
	 * @return Map with the total count of events for all ages
	 */
	private static Map<Double, Integer> totalEvents(Report report, String... events) {
		Map<Double, Integer> totalEvents = new HashMap<>();
		
		for(Entry<EventKey, Integer> entry : report.getEvents().entrySet()) {
			// Check to see if any of the events match the event in the key
			for(String event : events) {
				if(event.equalsIgnoreCase(entry.getKey().getEvent())) {
					Double age = entry.getKey().getAge();
					
					// Add or update
					totalEvents.put(age, totalEvents.getOrDefault(age, 0) + entry.getValue());
				}
			}
			
		}
		
		return totalEvents;
	}
}
