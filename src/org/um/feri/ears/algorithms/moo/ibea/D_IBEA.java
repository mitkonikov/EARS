package org.um.feri.ears.algorithms.moo.ibea;

import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PolynomialMutation;
import org.um.feri.ears.operators.SBXCrossover;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;

public class D_IBEA extends IBEA<Double, DoubleProblem, MOTask<Double>> {
	
	public D_IBEA() {
		this(new SBXCrossover(0.9, 20.0), new PolynomialMutation(1.0 / 10, 20.0), 100, 100);
	}
	
	public D_IBEA(int populationSize) {
		this(new SBXCrossover(0.9, 20.0), new PolynomialMutation(1.0 / 10, 20.0), populationSize, populationSize);
	}
	
	public D_IBEA(int populationSize, int archiveSize) {
		this(new SBXCrossover(0.9, 20.0), new PolynomialMutation(1.0 / 10, 20.0), populationSize, archiveSize);
	}

	public D_IBEA(CrossoverOperator<Double, MOTask<Double>, NumberSolution<Double>> crossover, MutationOperator<Double, MOTask<Double>, NumberSolution<Double>> mutation, int populationSize, int archiveSize) {
		super(crossover, mutation, populationSize, archiveSize);
	}

	@Override
	public void start() throws StopCriterionException {
		super.start();
		mut.setProbability(1.0 / numVar);
	}
	
}
