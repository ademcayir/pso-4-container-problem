package tr.edu.istanbul.pso4containerproblem;

import java.util.Vector;

class Suru {
	private Problem  tp_problem;
	private Particle particles[];
	private double global_best[];
	private int global_best_order[];
	private double global_cost;
	private boolean init;
	private boolean stop;
	private long baslangic_zamani;
	private int current_iterasyon_sayisi;
	private double inertia_factor;
	private double start_inertia;
	private double min_inertia;
	private int progress_start,progress_amount;
	public Suru(){
		init = false;
	}
	public void stop(){
		stop = true;
	}
	public void setProgressBounds(int start,int amount){
		progress_start = start;
		progress_amount = amount;
	}
	public boolean isRunning(){
		return !stop;
	}
	public double[] getBestVals(){
		return global_best;
	}
	public int[] getBest(){
		return global_best_order;
	}
	public double getBestCost(){
		return global_cost;
	}
	
	public void init(Problem tp_problem,int particle_num,int x_min,int x_max,int v_min,int v_max,double min_inertia,double inertia_factor,double start_inertia){
		this.tp_problem = tp_problem;
		stop = false;
		particles = new Particle[particle_num];
		global_best = new double[tp_problem.getSeriesCount()];
		global_best_order = new int[tp_problem.getSeriesCount()];
		for (int i = 0; i < particles.length; i++) {
			particles[i] = new Particle();
			particles[i].init(tp_problem,x_min, x_max, v_min, v_max);
			if (i == 0){
				System.arraycopy(particles[i].getBestX(), 0, global_best, 0, global_best.length);
				System.arraycopy(particles[i].getBestOrder(), 0, global_best_order, 0, global_best_order.length);
				global_cost = particles[i].getBestFitness();
			}
		}
		calculate_global_best(0);
		init = true;
	}
	
	public void solve(int maks_itesyon, int maks_millis){
		double w = start_inertia;
		double b = inertia_factor;
		int percent=0;
		baslangic_zamani = System.currentTimeMillis();
		int j=0;
		if (maks_millis <= 0){
			maks_millis = -1;
		}
		for (j = 0;; j++) {
			for (int i = 0; i < particles.length; i++) {
				particles[i].nextMove(global_best, w, 2, 2);
			}
			w = w * b;
			if (w < min_inertia){
				w = min_inertia;	
			}
			calculate_global_best(j);
			
			if (maks_itesyon != -1 ){
				percent = (j*100)/maks_itesyon;
				if (j >= maks_itesyon){
					break;
				}
			}
			long ti2 = System.currentTimeMillis();
			if (maks_millis != -1 ) {
				if ( ti2 - baslangic_zamani > maks_millis){
					break;
				}
				int percent2 = (int)(((ti2-baslangic_zamani)*100)/maks_millis);
				if (percent2 > percent){
					percent = percent2;
				}
			}
			if (stop){
				break;
			}
			UI.instance.canvas.repaint();
			UI.instance.setProgress( (percent*progress_amount)/100 + progress_start	);
			current_iterasyon_sayisi = j;
		}
		stop = true;
	}
	private void calculate_global_best(int step){
		for (int i = 0; i < particles.length; i++) {
			double p_x[] = particles[i].getBestX();
			int p_order[] = particles[i].getBestOrder();
			double pbest = particles[i].getBestFitness();
			
			if (pbest > global_cost ){
				global_cost = pbest;
				
				tp_problem.best_packs = particles[i].getPacks();
				
				UI.instance.update(step, System.currentTimeMillis() - baslangic_zamani, global_cost);
				System.arraycopy(p_x, 0, global_best, 0, p_x.length);
				System.arraycopy(p_order, 0, global_best_order, 0, p_x.length);
			} else {
				
			}
		}
	}
}
