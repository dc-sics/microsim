/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package christianmesch.simulationworker.misc;

import umontreal.ssj.randvar.NormalGen;
import umontreal.ssj.rng.RandomStream;

/**
 *
 * @author Christian Mesch
 */
public class NormalPosGen {

	public static double nextDouble(RandomStream s, double mu, double sigma) {
		NormalGen rand = new NormalGen(s, mu, sigma);
		double out;
		
		do {
			out = rand.nextDouble();
		} while(out < 0);
		
		return out;
	}
}
