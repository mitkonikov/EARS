package org.um.feri.ears.examples;

import org.um.feri.ears.algorithms.moo.nsga2.D_NSGAII;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem1;
import org.um.feri.ears.quality_indicator.IndicatorFactory;
import org.um.feri.ears.quality_indicator.QualityIndicator;
import org.um.feri.ears.quality_indicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.util.Util;

public class QualityIndicatorExample {

	public static void main(String[] args) {

		D_NSGAII nsga2 = new D_NSGAII();
		DoubleProblem problem = new UnconstrainedProblem1();
		MOTask<Double> t = new MOTask<>(new UnconstrainedProblem1(), StopCriterion.EVALUATIONS, 300000, 0, 0);

		QualityIndicator qi = IndicatorFactory.createIndicator(IndicatorName.NATIVE_HV, t.problem.getNumberOfObjectives(), t.problem.getReferenceSetFileName());

		try {
			ParetoSolution<Double> result = nsga2.execute(t);
			double value = qi.evaluate(result);
			System.out.println("QI value: "+value);
		} catch (StopCriterionException e) {
			e.printStackTrace();
		}
	}
}
