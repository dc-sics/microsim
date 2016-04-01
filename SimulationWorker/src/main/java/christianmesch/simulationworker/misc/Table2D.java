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
 * Map with two dimensional keys with continuous values. Will only work as
 * intended if all permutations of the key values exist.
 *
 * @author Christian Mesch
 * @param <A> First value of key
 * @param <B> Second value of key
 * @param <C> Value
 */
public class Table2D<A extends Comparable<A>, B extends Comparable<B>, C>
		extends HashMap<Pair<A, B>, C> {

	private final NavigableSet<A> setA = new TreeSet<>();
	private final NavigableSet<B> setB = new TreeSet<>();

	public Table2D() {
		super();
	}

	/**
	 * Inner class for the key.
	 *
	 * This is meant to be informative. The outer class extends the HashMap
	 * using Pair<A, B> (cf. Key<A, B>).
	 *
	 * @param <A> First value of key
	 * @param <B> Second value of key
	 */
	public static class Key<A, B> extends Pair<A, B> {

		public Key(A a, B b) {
			super(a, b);
		}
	}

	@Override
	public C put(Pair<A, B> key, C value) {
		setA.add(key.a);
		setB.add(key.b);

		return super.put(key, value);
	}

	public C put(A a, B b, C value) {
		setA.add(a);
		setB.add(b);
		Pair<A, B> newKey = new Pair<>(a, b);

		return super.put(newKey, value);
	}

	public void put(A[] as, B[] bs, C[] values) {
		// Will only add to map if the arrays are of the same length
		if (as.length != bs.length || as.length != values.length) {
			return;
		}

		for (int i = 0; i < as.length; i++) {
			put(new Pair<>(as[i], bs[i]), values[i]);
		}
	}

	public C floor(A a, B b) {
		Pair<A, B> newKey = new Pair<>(setA.floor(a), setB.floor(b));

		return super.get(newKey);
	}

	public C floor(Pair<A, B> key) {
		return floor(key.a, key.b);
	}

	private static class KeyDD extends Pair<Double, Double> {

		public KeyDD(Double a, Double b) {
			super(a, b);
		}
	}
}
