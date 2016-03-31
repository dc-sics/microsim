/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.simulationworker.misc;

import java.util.Objects;

/**
 *
 * @author Mark Clements
 */
public class Triple<A, B, C> {
    
    public final A a;
    public final B b;
    public final C c;
    
    public Triple(A a, B b, C c) {
	this.a = a;
	this.b = b;
	this.c = c;
    }
    
    @Override
	public int hashCode() {
	return Objects.hash(a, b, c);
    }
    
    @Override
	public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (obj instanceof Triple) {
	    final Triple<?, ?, ?> other = (Triple<?, ?, ?>) obj;
	    if (!Objects.equals(this.a, other.a)) {
		return false;
	    }
	    if (!Objects.equals(this.b, other.b)) {
		return false;
	    }
	    if (!Objects.equals(this.c, other.c)) {
		return false;
	    }
	    return true;
	} 
	return false;
    }
    
}
