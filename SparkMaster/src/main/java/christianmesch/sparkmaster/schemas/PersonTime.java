/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.sparkmaster.schemas;

import christianmesch.simulationworker.misc.PTKey;
import java.io.Serializable;

/**
 *
 * @author Christian Mesch
 */
public class PersonTime extends Schema implements Serializable {
	
	private double age;
	private double value;

	public PersonTime() {}

	public PersonTime(PTKey key, double value) {
		super(key.getStates());
		
		this.age = key.getAge();
		this.value = value;
	}

	public double getAge() {
		return age;
	}

	public void setAge(double age) {
		this.age = age;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
}
