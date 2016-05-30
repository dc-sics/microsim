/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.sparkmaster.misc;

/**
 *
 * @author Christian Mesch
 */
public class StopWatch {
	
	private long startTime = 0;
	private long stopTime = 0;
	
	public void start() {
		startTime = System.nanoTime();
		stopTime = 0;
	}
	
	public void stop() {
		stopTime = System.nanoTime();
	}
	
	public long getElapsedTimeNano() {
		if(stopTime == 0) {
			return System.nanoTime() - startTime;
		}
		
		return stopTime - startTime;
	}
	
	public long getElapsedTimeMilli() {
		return getElapsedTimeNano() / 1000;
	}
	
	public double getElapsedTime() {
		Long tmp = getElapsedTimeNano();
		return tmp.doubleValue() / 1e9;
	}
}
