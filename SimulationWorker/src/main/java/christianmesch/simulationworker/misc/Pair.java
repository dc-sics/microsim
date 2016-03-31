/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.simulationworker.misc;

import java.util.Objects;

public class Pair<A, B> {
    
    public final A a;
    public final B b;
    
    public Pair(A a, B b) {
	this.a = a;
	this.b = b;
    }
    
    @Override
	public int hashCode() {
	return Objects.hash(a,b);
    }
    
    @Override
	public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (obj instanceof Pair) {
	    final Pair<?, ?> other = (Pair<?, ?>) obj;
	    if (!Objects.equals(this.a, other.a)) {
		return false;
	    }
	    if (!Objects.equals(this.b, other.b)) {
		return false;
	    }
	    return true;
	} 
	return false;
    }
    
}
