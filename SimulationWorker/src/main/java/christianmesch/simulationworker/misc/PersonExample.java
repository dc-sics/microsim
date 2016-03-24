package christianmesch.simulationworker.misc;

import umontreal.iro.lecuyer.simevents.*;
import umontreal.iro.lecuyer.simevents.eventlist.*;
import umontreal.iro.lecuyer.rng.*;
import umontreal.iro.lecuyer.randvar.*;
import umontreal.iro.lecuyer.util.*;

import java.util.Arrays;
import java.util.ListIterator;
import java.util.HashMap;

public class PersonExample {

    cSimulator csim = new cSimulator();
    EventReport eventReport = new EventReport();
    Boolean noisy = false;
    MRG32k3a rngCancer = new MRG32k3a();
    MRG32k3a rngOtherDeath = new MRG32k3a();
    MRG32k3a rngCancerDeath = new MRG32k3a();

    public enum ext_grade_t {Gleason_le_6,Gleason_7,Gleason_ge_8}

    public enum Stage {Healthy,Localised,Metastatic}

    public enum Diagnosis {NotDiagnosed,ClinicalDiagnosis,ScreenDiagnosis}

    public enum screen_t {noScreening, randomScreen50to70, twoYearlyScreen50to70, fourYearlyScreen50to70, screen50, screen60, screen70, screenUptake, stockholm3_goteborg, stockholm3_risk_stratified,goteborg, risk_stratified, mixed_screening,regular_screen, single_screen}

    public enum treatment_t {no_treatment, CM, RP, RT}
    
    public enum survival_t {StageShiftBased, LeadTimeBased}
    
    public enum biomarker_model_t {random_correction, psa_informed_correction}

    /* Parameters */
    double 
	g_onset = 0.0005,
	gc = 0.0015,
	gm = 0.0004,
	thetac = 19.1334,
	alpha7 = 0.13,
	beta7  = 0.006,
	alpha8 = -4.61,
	beta8  = 0.06,
	tau2 = 0.0829,
	mubeta0= -1.609,
	sebeta0= 0.2384,
	mubeta1= 0.04463,
	sebeta1= 0.0430;
    double[] mubeta2 = new double[]{0.051, 0.129, 0.1678};
    double[] sebeta2 = new double[]{0.064, 0.087, 0.3968};
    double[] mu0 = new double[]{
        0.00219, 0.000304, 5.2e-05, 0.000139, 0.000141, 3.6e-05, 7.3e-05,
        0.000129, 3.8e-05, 0.000137, 6e-05, 8.1e-05, 6.1e-05, 0.00012,
        0.000117, 0.000183, 0.000185, 0.000397, 0.000394, 0.000585, 0.000448,
        0.000696, 0.000611, 0.000708, 0.000659, 0.000643, 0.000654, 0.000651,
        0.000687, 0.000637, 0.00063, 0.000892, 0.000543, 0.00058, 0.00077,
        0.000702, 0.000768, 0.000664, 0.000787, 0.00081, 0.000991, 9e-04,
        0.000933, 0.001229, 0.001633, 0.001396, 0.001673, 0.001926, 0.002217,
        0.002562, 0.002648, 0.002949, 0.002729, 0.003415, 0.003694, 0.004491,
        0.00506, 0.004568, 0.006163, 0.006988, 0.006744, 0.00765, 0.007914,
        0.009153, 0.010231, 0.011971, 0.013092, 0.013839, 0.015995, 0.017693,
        0.018548, 0.020708, 0.022404, 0.02572, 0.028039, 0.031564, 0.038182,
        0.042057, 0.047361, 0.05315, 0.058238, 0.062619, 0.074934, 0.089776,
        0.099887, 0.112347, 0.125351, 0.143077, 0.153189, 0.179702, 0.198436,
        0.240339, 0.256215, 0.275103, 0.314157, 0.345252, 0.359275, 0.41768,
        0.430279, 0.463636, 0.491275, 0.549738, 0.354545, 0.553846, 0.461538,
        0.782609};

   
    /** 
	@brief filterRemove remove events of a particular class from
	a Simulator's eventList.

	Note: Trying to use element.cancel() gives a
	ConcurrentModificationException.

	Possible TODO: remove events of a particular class a specific
	individual within a group.
     **/
    public <T> void filterRemove(cSimulator sim, Class<T> type) {
    	ListIterator<Event> iterator = sim.getEventList().listIterator();
    	while (iterator.hasNext()) {
    	    Event element = iterator.next();
    	    if (type.isInstance(element)) { iterator.remove(); }
    	}
    }

    /**
       @brief Return the index for the lower bound of a sorted x for a given key. 
       Based on C++'s lower_bound behaviour. Returns -1 if the key is less than x[0].
     **/
    int lower_bound_index(double[] x, double key) {
	int out = Arrays.binarySearch(x, key);
	if (out < -1) out = -out - 2;
	return out;
    }

    /**
       @brief Rpexp is a random number generator class for piecewise constant hazards.
       Given piecewise constant hazards h and time lower bounds t, nextDouble() returns a random time.
       The random number is calculated using the inversion formula.
       Constructors provided for arrays.
    */
    class Rpexp {
    	public Rpexp() {} // blank default constructor
    	public Rpexp(double[] hin, double[] tin) {
    	    n = hin.length;
    	    int i;
	    t = tin.clone();
	    h = hin.clone();
	    H = new double[n];
    	    H[0] = 0.0; 
    	    if (n>1) {
    		for(i=1;i<n;i++) {
    		    H[i] = H[i-1]+(t[i]-t[i-1])*h[i-1];
    		}
    	    }
    	}
    	double nextDouble(RandomStream s, double from) {
    	    double u = s.nextDouble();
    	    double v = 0.0, H0 = 0.0, tstar = 0.0;
    	    int i = 0, i0 = 0;
    	    if (from > 0.0) {
    		i0 = lower_bound_index(t, from);
    		H0 = H[i0] + (from - t[i0])*h[i0];
    	    }
    	    v = - Math.log(u) + H0;
    	    i = lower_bound_index(H, v);
    	    tstar = t[i]+(v-H[i])/h[i];
    	    return tstar;
    	}
    	double nextDouble(RandomStream s) {
	    return nextDouble(s, 0.0);
	}
    	private double[] h, H, t;
    	private int n;
    }
    public void testRpexp() {
	double[] h = new double[]{0.1, 0.2, 0.3};
	double[] t = new double[]{0,   1,   2};
	Rpexp rand = new Rpexp(h, t);
	int count0 = 0, count1 = 0;
	for (int i=0; i<100000; i++) {
	    double x = rand.nextDouble(rngCancer);
	    if (x < 1.0) count0++;
	    else if (x < 2.0) count1++;
	}
	System.out.println(count0/100000.0);
	System.out.println(1-Math.exp(-0.1));
	System.out.println(count1/100000.0);
	System.out.println(Math.exp(-0.1)-Math.exp(-0.3));
    }
    double[] iota(int n) {
	double[] values = new double[n];
	for (int i=0; i<n; ++i) values[i] = i;
	return values;
    }
    Rpexp randOtherDeath = new Rpexp(mu0, iota(mu0.length));

    /**
       @brief lambda calculates the lambda parameter for a Weibull
       distribution given alpha and the mean.
    **/
    private double lambda(double alpha, double meanX) {
	return Math.exp(Num.lnGamma(1.0+1.0/alpha))/meanX;
    }

    /**
       @brief class with a nextDouble static method for a random value for a positive Normal distribution,
       that is, a truncated Normal distribution which only takes positive values. This does not extend NormalGen.
     **/
    private static class NormalPosGen {
	public static double nextDouble(RandomStream s, double mu, double sigma) {
	    NormalGen rand = new NormalGen(s, mu, sigma);
	    double out;
	    do {
		out = rand.nextDouble();
	    } while (out<0);
	    return out;
	}
    }

    /**
       @brief cSimulator adds in a lastEventTime field to the
       Simulator class. 
       The lastEventTime field is used by the cMessage class for event reporting.
    **/
    class cSimulator extends Simulator {
	double lastEventTime = 0.0;
	public void init() {
	    super.init();
	    lastEventTime = 0.0;
	}
    }

    /** 
	@brief cMessage extends Event and includes the sendingTime. 
	Calls an abstract method execute() cf actions(), which avoids calling super.actions() in a sub-class.
	Current implementation depends on csim.
     **/
    abstract class cMessage extends Event {
	cSimulator sim; // shadows and extends Event::sim
	double sendingTime = 0.0;
	public cMessage(cSimulator csim) { super(csim); sim=csim; }
	public cMessage() { this(csim); } // convenient default constructor (depends on csim)
	@Override
	public void schedule(double t) {
	    super.schedule(t);
	    sendingTime = sim.time();
	}
	@Override
	public void actions() {
	    // specific pre-processing
	    eventReport.put(sim.lastEventTime, sim.time());
	    if (noisy) System.out.println("Event: " + this.getClass() + " at " + 
					  sim.time());
	    // general
	    sim.lastEventTime = sim.time();                 
	    execute();
	}
	public abstract void execute();
    }

    /**
       @brief Event report by single year of age.
     **/
    class EventReport {
	HashMap<Double,Double> pt = new HashMap<Double,Double>();
 	HashMap<Double,Integer> events = new HashMap<Double,Integer>();
	public void put(double lhs, double rhs) {
	    double lower = Math.floor(lhs);
	    double upper = Math.floor(rhs);
	    events.put(upper, events.containsKey(upper) ? events.get(upper)+1 : 1);
	    for (double a=lower; a<=upper; a++) {
		pt.put(a, (pt.containsKey(a) ? pt.get(a) : 0.0) + 
		       Math.min(a+1.0,rhs)-Math.max(a,lhs));
	    }
	}
    }
    
    /* State variables */
    double 
	ageClinicalDiagnosisLocalised = -1.0,
	ageClinicalDiagnosisMetastatic = -1.0,
	age_onset = -1.0,
	beta0 = -1.0,
	beta1 = -1.0,
	beta2 = -1.0;
    ext_grade_t ext_grade;
    Stage stage;
    Diagnosis dx;
    public void init () {
	csim.init(); // initialise
	// attributes
	stage = Stage.Healthy;
	dx = Diagnosis.NotDiagnosed;
	// Age of cancer onset
	age_onset = 35.0 + Math.sqrt(2.0*ExponentialGen.nextDouble(rngCancer,1.0)/g_onset);
	double t_onset = age_onset - 35.0;
	// Gleason score
	double u = rngCancer.nextDouble();
	if (u < Math.exp(alpha8 + beta8 * t_onset))
	    ext_grade = ext_grade_t.Gleason_ge_8;
	else if (u > 1 - (alpha7 + beta7 * t_onset))
	    ext_grade = ext_grade_t.Gleason_7;
	else ext_grade = ext_grade_t.Gleason_le_6;
	// PSA random effects
	beta0 = NormalGen.nextDouble(rngCancer,mubeta0,sebeta0);
	beta1 = NormalPosGen.nextDouble(rngCancer,mubeta1,sebeta1);
	beta2 = NormalPosGen.nextDouble(rngCancer,mubeta2[ext_grade.ordinal()],sebeta2[ext_grade.ordinal()]);
	// schedule events
	new OtherDeath().schedule(randOtherDeath.nextDouble(rngOtherDeath));
	new Localised().schedule(age_onset);
	// new OtherDeath().schedule (WeibullGen.nextDouble(rngOtherDeath,4.0,lambda(4.0,70.0), 0.0));
	// if (rngCancer.nextDouble() < 0.2) {
	//     new Cancer().schedule (WeibullGen.nextDouble(rngCancer,4.0,lambda(4.0,70.0), 0.0));
	// }
    }
    class OtherDeath extends cMessage {
	public void execute() { sim.stop(); }
    }
    class CancerDeath extends cMessage {
	public void execute() { sim.stop(); }
    }
    class Localised extends cMessage {
	public void execute() {
	    stage = Stage.Localised;
	    double t_onset = age_onset - 35.0;
	    double psa_onset = psa_mean(sim.time());
	    double u = ExponentialGen.nextDouble(rngCancer,1.0);
	    double age_m = (Math.log((beta1+beta2)*u/gm + psa_onset) - beta0 + beta2*t_onset) / (beta1+beta2) + 35.0;
	    new Metastatic().schedule(age_m);
	    u = ExponentialGen.nextDouble(rngCancer,1.0);
	    ageClinicalDiagnosisLocalised = (Math.log((beta1+beta2)*u/gc + psa_onset) - beta0 + beta2*t_onset) / (beta1+beta2) + 35.0;
	    new ClinicalDiagnosis().schedule(ageClinicalDiagnosisLocalised);
	}
    }
    class Metastatic extends cMessage {
	public void execute() {
	    stage = Stage.Metastatic;
	    double u = ExponentialGen.nextDouble(rngCancer,1.0);
	    double psa = psa_mean(sim.time());
	    double t_onset = age_onset - 35.0;
	    ageClinicalDiagnosisMetastatic = (Math.log((beta1+beta2)*u/(gc*thetac) + psa) - beta0 + beta2*t_onset) / (beta1+beta2) + 35.0;
	    new ClinicalDiagnosis().schedule(ageClinicalDiagnosisMetastatic);
	}
    }
    class Screen extends cMessage {
	public void execute() { }
    }
    class ClinicalDiagnosticBiopsy extends cMessage {
	public void execute() { 
	    // costs
	}
    }
    class ScreenInitiatedBiopsy extends cMessage {
	public void execute() { 
	    // costs
	}
    }
    class ClinicalDiagnosis extends cMessage {
	public void execute() { 
	    dx = Diagnosis.ClinicalDiagnosis;
	    filterRemove(sim, Metastatic.class);
	    filterRemove(sim, Screen.class);
	    new ClinicalDiagnosticBiopsy().schedule(sim.time());
	    new ClinicalDiagnosticBiopsy().schedule(sim.time());
	    new ClinicalDiagnosticBiopsy().schedule(sim.time());
	    new Treatment().schedule(sim.time());
	}
    }
    class ScreenDiagnosis extends cMessage {
	public void execute() { 
	    dx = Diagnosis.ScreenDiagnosis;
	    filterRemove(sim, Metastatic.class);
	    filterRemove(sim, Screen.class);
	    filterRemove(sim, ClinicalDiagnosis.class);
	    new Treatment().schedule(sim.time());
	}
    }
    class Treatment extends cMessage {
	public void execute() { 
	    double age_c = (stage == Stage.Localised) ? ageClinicalDiagnosisLocalised : ageClinicalDiagnosisMetastatic;
	    double lead_time = age_c - sim.time();
	}
    }

    private double psa_mean(double age) {
	double t = age < 35.0 ? 0.0 : age - 35.0;
	double t_onset = age_onset - 35.0;
	double yt = age<age_onset ? Math.exp(beta0+beta1*t) : Math.exp(beta0+beta1*t+beta2*(t-t_onset));
	return yt;
    }
    private double psa_measured(double age) {
	return psa_mean(age)*Math.exp(NormalGen.nextDouble(rngCancer, 0.0, Math.sqrt(tau2)));
    }
    
    public void simulateOneRun () {
	noisy = true;
	init();
	System.out.println(csim.getEventList());
	csim.start();
	System.out.println(psa_mean(35));
	System.out.println(psa_measured(35));
	System.out.println(psa_mean(105));
	System.out.println(psa_measured(105));
	noisy = false;
    }
    
    public void simulateMany (Integer n, Boolean noisy) {
	for (int i = 0; i<n; i++) {
	    init();
	    if (noisy) System.out.println("ID: "+ i);
	    csim.start();
	    rngCancer.resetNextSubstream();
	    rngCancerDeath.resetNextSubstream();
	    rngOtherDeath.resetNextSubstream();
	}
    }

    public static void main (String[] args) {

	PersonExample person = new PersonExample();
	Integer n = 1000000;
	person.simulateOneRun ();
	person.simulateMany (n, false);
	System.out.println(person.eventReport.pt);
	System.out.println(person.eventReport.events);

	// check the number of events
	Integer total_events = 0;
	for (Integer value : person.eventReport.events.values())
	    total_events += value;
	System.out.println("Total events = " + total_events);
	// calculate life expectancy
	Double total_pt = 0.0;
	for (Double value : person.eventReport.pt.values())
	    total_pt += value;
	System.out.println("Life expectancy = " + total_pt/n);

    }

}
