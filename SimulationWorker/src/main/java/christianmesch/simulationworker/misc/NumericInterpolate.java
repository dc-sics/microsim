import java.util.Arrays;
import java.util.ArrayList;

class NumericInterpolate {
    double[] x, y, slope;
    int n;
    
	/**
       @brief Return the index for the lower bound of a sorted x for a given key. 
       Based on C++'s lower_bound behaviour. Returns -1 if the key is less than x[0].
     **/
    int lower_bound_index(double[] x, double key) {
		int out = Arrays.binarySearch(x, key);
		if (out < -1) out = -out - 2;
		return out;
    }
    

	// simple default constructor
    public NumericInterpolate() {
		x = new double[]{0};
		y = new double[]{0};
		slope = new double[]{0};
		n = 0;
    }
	
    NumericInterpolate(double[] xin, double[] yin) {
		// calculate the slope between points
		x = xin.clone();
		y = yin.clone();
		n = x.length;
		
		slope = new double[n];
		for (int i = 0; i < n - 1; i++) {
			slope[i] = (y[i+1]-y[i]) / (x[i+1]-x[i]);
		}
    }
	
    // NOT efficient: ArrayList<double> would be better (but then I would need to change the getters)
    public void push_back(double xi, double yi) {
		x = Arrays.copyOf(x, x.length+1);
		x[x.length] = xi;
		y = Arrays.copyOf(y, y.length+1);
		y[y.length] = yi;
    }
	
    public double approx(double xfind) {
		int i;
		if (xfind<=x[0]) return y[0];
		else if (xfind>=x[n-1]) return y[n-1]+slope[n-2]*(xfind-x[n-1]); // linear
		
		else {
			i = lower_bound_index(x, xfind);
			return y[i]+slope[i]*(xfind-x[i]);
		}
    }
	
    public double invert(double yfind) { // assumes that the function is increasing
		if (yfind <= y[0]) {
			return x[0];
		}else if (yfind>=y[n-1]) {
			return x[n-1]+(yfind-y[n-1])/slope[n-2];
		}else {
			int i = lower_bound_index(y, yfind);
			return x[i]+(yfind-y[i])/slope[i];
		}
    }
	
    public static void main(String[] args) {
		double[] x = new double[]{0,1,10};
		double[] y = new double[]{0,1,2};
		
		NumericInterpolate interp = new NumericInterpolate(x,y);
		
		System.out.println(interp.invert(1.5));
		System.out.println(interp.approx(5.5));
    }
}
