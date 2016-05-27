/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.simulationworker.misc.utils;

import java.util.Arrays;
import umontreal.ssj.rng.RandomStream;

/**
 *
 * @author Mark Clements
 */
public class Rpexp {

	private double[] h, H, t;
	private int n;

	public Rpexp() {
	} // blank default constructor

	public Rpexp(double[] hin, double[] tin) {
		n = hin.length;
		t = tin.clone();
		h = hin.clone();
		H = new double[n];
		H[0] = 0.0;

		if(n > 1) {
			for(int i = 1; i < n; i++) {
				H[i] = H[i - 1] + (t[i] - t[i - 1]) * h[i - 1];
			}
		}
	}

	public double nextDouble(RandomStream s, double from) {
		double u = s.nextDouble();
		double H0 = 0.0;
		int i0;

		if(from > 0.0) {
			i0 = findInterval(t, from);
			H0 = H[i0] + (from - t[i0]) * h[i0];
		}

		double v = -Math.log(u) + H0;
		int i = findInterval(H, v);
		double tstar = t[i] + (v - H[i]) / h[i];

		return tstar;
	}

	public double nextDouble(RandomStream s) {
		return nextDouble(s, 0.0);
	}
	
	private int findInterval(double[] x, double key) {
		int out = Arrays.binarySearch(x, key);
		if (out < -1) out = -out - 2;
		
		return out;
    }
}
