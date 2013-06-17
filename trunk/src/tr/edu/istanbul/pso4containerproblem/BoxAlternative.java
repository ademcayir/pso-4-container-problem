package tr.edu.istanbul.pso4containerproblem;

public class BoxAlternative {
	
	public static int maxAlternative(Problem p,int min,int max){
		int w = p.palet_w/p.product_w;
		int h = p.palet_h/p.product_h;
		int count = 0;
		for (int i = 0; i < w*h; i++) {
			int total = (i%w + 1) * (i/w + 1); 	
			if (total >= min && total <= max){
				count++;
			}
		}
		w = p.palet_w/p.product_h;
		h = p.palet_h/p.product_w;
		for (int i = 0; i < w*h; i++) {
			int total = (i%w + 1) * (i/w + 1); 	
			if (total >= min && total <= max){
				count++;
			}
		}
		
		return count;
	}
	public static boolean nextAlternative(Problem p,int ite,int min,int max){
		int w = p.palet_w/p.product_w;
		int h = p.palet_h/p.product_h;
		int count = 0;
		for (int i = 0; i < w*h; i++) {
			int total = (i%w + 1) * (i/w + 1); 	
			if (total >= min && total <= max){
				if (count == ite){
					p.box_w = (i%w + 1)*p.product_w + p.box_tolerance*2;
					p.box_h = (i/w + 1)*p.product_h+ p.box_tolerance*2;
					System.out.println("alternative:"+i+">"+(i%w)+"x"+(i/2));
					return true;
				}
				count++;
			}
		}
		w = p.palet_w/p.product_h;
		h = p.palet_h/p.product_w;
		for (int i = 0; i < w*h; i++) {
			int total = (i%w + 1) * (i/w + 1); 	
			if (total >= min && total <= max){
				if (count == ite){
					p.box_w = (i%w + 1)*p.product_h + p.box_tolerance*2;
					p.box_h = (i/w + 1)*p.product_w+ p.box_tolerance*2;
					System.out.println("alternative:"+i+">"+(i%w)+"x"+(i/2));
					return true;
				}
				count++;
			}
		}
		
		return false;
	}
}
