package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://benchmarkfcns.xyz/benchmarkfcns/xinsheyangn2fcn.html
http://infinity77.net/global_optimization/test_functions_nd_X.html#go_benchmark.XinSheYang02
 */
public class XinSheYang2 extends Problem {

    public XinSheYang2() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -2.0 * PI));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 2.0 * PI));
        name = "XinSheYang2";
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        double sum1 = 0, sum2 = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            sum1 += abs(x[i]);
            sum2 += sin(pow(x[i], 2));
        }
        fitness = sum1 * exp(-sum2);

        return fitness;
    }
}