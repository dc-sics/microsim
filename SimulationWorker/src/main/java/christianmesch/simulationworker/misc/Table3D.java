/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.simulationworker.misc;

import java.util.HashMap;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.TreeSet;
import christianmesch.simulationworker.misc.Pair;

/**
 *
 * @author Christian Mesch
 */
public class Table3D<A extends Comparable<A>, B extends Comparable<B>, C extends Comparable<C>, D>
    extends HashMap<Triple<A, B, C>, D> {

    private final NavigableSet<A> setA = new TreeSet<>();
    private final NavigableSet<B> setB = new TreeSet<>();
    private final NavigableSet<C> setC = new TreeSet<>();

    public Table3D() {
	super();
    }

    @Override
	public D put(Triple<A, B, C> key, D value) {
	setA.add(key.a);
	setB.add(key.b);
	setC.add(key.c);
	
	return super.put(key, value);
    }
    
    public D floor(Triple<A, B, C> key) {
	Triple<A, B, C> newKey = new Triple<>(setA.floor(key.a), 
					      setB.floor(key.b), 
					      setC.floor(key.c));
	
	return super.get(newKey);
    }

    private static class Key extends Triple<Double,Double,Integer> {
	public Key(Double age, Double grade, Integer sex) {
	    super(age, grade, sex);
	}
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	Table3D<Double, Double, Integer, Double> lookup = new Table3D<>();
	
	lookup.put(new Key(50.0, 0.0, 1), 0.1);
	lookup.put(new Key(50.0, 1.0, 1), 0.2);
	lookup.put(new Key(60.0, 0.0, 1), 0.3);
	lookup.put(new Key(60.0, 1.0, 1), 0.4);

	System.out.println(lookup.floor(new Key(56.3, 0.0, 1))); // 0.1
	System.out.println(lookup.floor(new Key(80.2, 0.1, 1))); // 0.3
	System.out.println(lookup.floor(new Key(61.0, 1.1, 1))); // 0.4
    }

}
