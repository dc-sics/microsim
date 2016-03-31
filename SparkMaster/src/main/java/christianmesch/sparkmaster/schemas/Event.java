/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.sparkmaster.schemas;

import christianmesch.simulationworker.misc.EventKey;
import java.io.Serializable;

/**
 *
 * @author Christian Mesch
 */
public class Event extends Schema implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String event;
	private Double age;
	
	private Integer value;

	public Event() {}
	
	public Event(EventKey key, Integer value) {
		super(key.getStates());
		
		this.event = key.getEvent();
		this.age = key.getAge();
		this.value = value;
	}
	
	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public Double getAge() {
		return age;
	}

	public void setAge(Double age) {
		this.age = age;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
}
