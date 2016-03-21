/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.sparkmaster.misc;

import christianmesch.simulationworker.misc.EventKey;
import christianmesch.simulationworker.misc.PTKey;
import christianmesch.simulationworker.misc.Report;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Class for creating charts. It's built using a patten which resembles builder pattern. <br>
 * You use it by creating an instance of the class and then call setters one after another, <br>
 * i.e. chart.setTitle("Title").setxLabel("X-label");<br>
 * and then call the method for the chart you want <br>
 * i.e. chart.createRateChart();
 * 
 * @author Christian Mesch
 */
public class ChartCreator {

	private final Report report;
	private String filePath;
	private String title;
	private String xLabel;
	private String yLabel;
	private String lineName;
	private int width;
	private int height;
	private int stepSize;
	private int multiplier;
	private String[] events;
	
	public ChartCreator(Report report) {
		this.report = report;
		
		this.filePath = "chart.png";
		this.width = 800;
		this.height = 600;
		this.stepSize = 1;
		this.multiplier = 1;
	}
	
	/**
	 * Set file path for chart image. Should end in ".png"
	 * @param filePath Path to save image to
	 * @return this
	 */
	public ChartCreator setFilePath(String filePath) {
		this.filePath = filePath;
		return this;
	}

	/**
	 * Set title for the chart
	 * @param title Title for the chart
	 * @return this
	 */
	public ChartCreator setTitle(String title) {
		this.title = title;
		return this;
	}

	/**
	 * Set label for x-axis
	 * @param xLabel Label for x-axis
	 * @return this
	 */
	public ChartCreator setxLabel(String xLabel) {
		this.xLabel = xLabel;
		return this;
	}

	/**
	 * Set label for y-axis
	 * @param yLabel Label for y-axis
	 * @return this
	 */
	public ChartCreator setyLabel(String yLabel) {
		this.yLabel = yLabel;
		return this;
	}

	/**
	 * Set name of line for legend
	 * @param lineName Line name
	 * @return this
	 */
	public ChartCreator setLineName(String lineName) {
		this.lineName = lineName;
		return this;
	}

	/**
	 * Set the width of the image.<br>
	 * Default: 800 px
	 * @param width
	 * @return this
	 */
	public ChartCreator setWidth(int width) {
		this.width = width;
		return this;
	}

	/**
	 * Set the height of the image.<br>
	 * Default: 600 px
	 * @param height
	 * @return this
	 */
	public ChartCreator setHeight(int height) {
		this.height = height;
		return this;
	}

	/**
	 * Set the step size of the x-axis.<br>
	 * Default: 1
	 * @param stepSize
	 * @return this
	 */
	public ChartCreator setStepSize(int stepSize) {
		this.stepSize = stepSize;
		return this;
	}

	/**
	 * Set the integer to multiply the y-values with.
	 * For example: setMultiplier(100000) if you want to have rate by 100000 persons
	 * @param multiple
	 * @return 
	 */
	public ChartCreator setMultiplier(int multiple) {
		this.multiplier = multiple;
		return this;
	}
	
	/**
	 * Name of the events you want to create the chart from
	 * @param events
	 * @return this
	 */
	public ChartCreator setEvents(String... events) {
		this.events = events;
		return this;
	}
	
	
	/**
	 * Method for creating rate charts. <br><br>
	 * 
	 * Right now it's only using one line. Can be fixed if needed.
	 * 
	 * @throws IOException 
	 */
	public void createRateChart() throws IOException {
		Map<Double, Double> totalPTs = totalPersonTimes();
		Map<Double, Integer> totalEvents = totalEvents();
		
		XYSeries series = new XYSeries(lineName);
		
		// Add all points to the data set
		for(Entry<Double, Integer> entry : totalEvents.entrySet()) {
			Double value = entry.getValue() * multiplier / totalPTs.get(entry.getKey());
			
			series.add(entry.getKey(), value);
		}
		
		XYSeriesCollection dataSet = new XYSeriesCollection(series);
		
		// Create chart object
		JFreeChart chartObject = ChartFactory.createXYLineChart(title, xLabel,
				yLabel, dataSet, PlotOrientation.VERTICAL, true, true, false);
		
		NumberAxis xAxis = new NumberAxis(xLabel);
		xAxis.setTickUnit(new NumberTickUnit(stepSize));
		xAxis.setAutoRange(true);
		
		XYPlot plot = chartObject.getXYPlot();
		plot.setDomainAxis(xAxis);
		
		// Save as chart to disk
		File chart = new File(filePath);
		ChartUtilities.saveChartAsPNG(chart, chartObject, width, height);
	}
	
	/**
	 * Calculate the total person times for all ages
	 * 
	 * @param report Report to calculate from
	 * @return Map with the total person times for all ages
	 */
	private Map<Double, Double> totalPersonTimes() {
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
	private Map<Double, Integer> totalEvents() {
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
			
		} // select age, sum(events) as events, sum(pt) as pt from events where eventType in ("cancer") group by 1;
		
		return totalEvents;
	}
}
