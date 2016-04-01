/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.simulationworker.misc;

import java.util.HashMap;
import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * Map with three dimensional keys with continuous values. Will only work as
 * intended if all permutations of the key values exist.
 *
 * @author Christian Mesch
 * @param <A> First key value
 * @param <B> Second key value
 * @param <C> Third key value
 * @param <D> Value
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

	public void put(A[] as, B[] bs, C[] cs, D[] values) {
		// Will only add to map if the arrays are of the same length
		if(as.length != bs.length || as.length != cs.length 
				|| as.length != values.length) {
			return;
		}
		
		for (int i = 0; i < as.length; i++) {
			put(new Triple<>(as[i], bs[i], cs[i]), values[i]);
		}
	}

	public D floor(Triple<A, B, C> key) {
		Triple<A, B, C> newKey = new Triple<>(setA.floor(key.a),
				setB.floor(key.b),
				setC.floor(key.c));

		return super.get(newKey);
	}

	private static class Key extends Triple<Double, Double, Integer> {

		public Key(Double age, Double grade, Integer sex) {
			super(age, grade, sex);
		}
	}
}
