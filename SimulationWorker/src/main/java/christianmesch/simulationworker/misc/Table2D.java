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
public class Table2D<A extends Comparable<A>, B extends Comparable<B>, C>
    extends HashMap<Pair<A, B>, C> {

    private final NavigableSet<A> setA = new TreeSet<>();
    private final NavigableSet<B> setB = new TreeSet<>();

    public Table2D() {
	super();
    }

    @Override
	public C put(Pair<A, B> key, C value) {
	setA.add(key.a);
	setB.add(key.b);
	return super.put(key, value);
    }

    public void put(A[] as, B[] bs, C[] values) {
	for (int i=0; i<as.length; ++i)
	    put(new Pair<A,B>(as[i],bs[i]), values[i]);
    }
    
    public C floor(Pair<A, B> key) {
	Pair<A, B> newKey = new Pair<>(setA.floor(key.a), setB.floor(key.b));
	return super.get(newKey);
    }

    private static class Key extends Pair<Double,Double> {
	public Key(Double age, Double grade) {
	    super(age,grade);
	}
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	Table2D<Double, Double, Double> lookup = new Table2D<>();
	
	Double[] ages = {50.0,60.0,50.0,60.0};
	Double[] grades = {0.0,0.0,1.0,1.0};
	Double[] values = {0.1, 0.2, 0.3, 0.4};
	lookup.put(ages,grades,values);
	
	System.out.println(lookup.floor(new Key(56.3, 2.0))); // 0.3
	System.out.println(lookup.floor(new Key(80.2, 0.1))); // 0.2
	System.out.println(lookup.floor(new Key(55.0, 0.1))); // 0.1
    }

}
