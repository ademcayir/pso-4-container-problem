package tr.edu.istanbul.pso4containerproblem;

public class SampleProblems {
	
	public static Problem sample1(){
		Problem p = new Problem();
		p.product_w = 23;
		p.product_h = 10;
		p.product_a = 31;
		p.product_weight = 140 + 37;
		p.box_tolerance = 1;
		p.palet_w = 110;
		p.palet_h = 110;
		return p;
	}
}
