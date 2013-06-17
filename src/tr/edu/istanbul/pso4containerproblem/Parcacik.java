package tr.edu.istanbul.pso4containerproblem;

import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;


class Particle {
	private double x[];
	private double v[];
	private double best_x[];
	private int best_order[];
	private int current_order[];
	private double best_cost;
	private Vector<Pack> best_packs;
	private Problem tp_problem;
	public void init(Problem tp_problem,int x_min,int x_max,int v_min,int v_max){
		this.tp_problem = tp_problem;
		x = new double[tp_problem.getSeriesCount()];
		v = new double[tp_problem.getSeriesCount()];
		best_x = new double[tp_problem.getSeriesCount()];
		best_order = new int[tp_problem.getSeriesCount()];
		current_order = new int[tp_problem.getSeriesCount()];
		for (int i = 0; i < x.length; i++) {
			x[i] = Algorithms.random.nextDouble()*(x_max - x_min) + x_min;			
			v[i] = Algorithms.random.nextDouble()*(v_min - v_max ) + v_min;			
		}
		
		System.arraycopy(x, 0, best_x, 0, best_x.length);
		Algorithms.apply_SPV("init",best_x, best_order);
		best_packs = tp_problem.applySerie(best_order);
		best_cost = tp_problem.getFitnessRate();
	}
	
	public double[] getBestX(){
		return best_x;
	}
	public int[] getBestOrder(){
		return best_order;
	}
	public double getBestFitness(){
		return best_cost;
	}
	public double[] getCurrentX(){
		return x;
	}
	public double[] getCurrentV(){
		return v;
	}
	public void nextMove(double global_best[], double inertia,double c1, double c2){
		for (int i = 0; i < global_best.length; i++) {
			v[i] = v[i] * inertia + c1 * Math.abs(Algorithms.random.nextDouble()) * (best_x[i] - x[i]) + c2 * Math.abs(Algorithms.random.nextDouble())*(global_best[i] - x[i]);
			x[i] = x[i] + v[i];
		}
		Algorithms.apply_SPV("nextMove",x,current_order);
		Vector<Pack> p = tp_problem.applySerie(current_order);
		double current_cost = tp_problem.getFitnessRate();
		if (current_cost > best_cost ){
			best_cost = current_cost;
			best_packs = p;
			System.arraycopy(x, 0, best_x, 0, x.length);
			System.arraycopy(current_order, 0, best_order, 0, current_order.length);
		}
	}
	
	public Vector<Pack> getPacks(){
		return best_packs;
	}
}
