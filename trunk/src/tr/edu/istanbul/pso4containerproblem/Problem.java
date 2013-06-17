package tr.edu.istanbul.pso4containerproblem;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JFrame;

public class Problem {
	String label;
	int product_w;
	int product_h;
	int product_a;
	int product_weight;
	
	int box_tolerance;
	int box_w;
	int box_h;
	int box_a;
	int weight;
	int palet_w;
	int palet_h;
	Vector<Pack> best_packs;
	private double current_fitness_rate;
	
	public int getSeriesCount(){
		int min = box_w;
		if (box_h < min){
			min = box_h;
		}
		int seri_count = (palet_w/min + 1) * (palet_h/min + 1);
		return seri_count;
	}
	
	public double getFitnessRate(){
		return current_fitness_rate;
	}
	
	
	
	public Vector<Pack> applySerie(int series[]) {
		final Vector<Pack> packages = new Vector<Pack>();
		int current_x=0
				,current_y=0;
		Pack left_beginning = null;
		double total_fit = 0;
		for (int i = 0; i < series.length; i++) {
			Pack p = new Pack();
			if (series[i] == 1) {
				p.w = box_w;
				p.h = box_h;
			} else {
				p.w = box_h;
				p.h = box_w;
			}
			
			p.x = current_x;
			p.y = current_y;
			if (p.x + p.w > palet_w) {
				if (left_beginning == null){
					if (packages.size() == 0){
						break;
					} else {
						left_beginning = packages.get(0);
					}
				}
				// sona geldin. ilk basa don
				p.x = left_beginning.x;
				p.y = left_beginning.y + left_beginning.h;
				left_beginning = p;
			}
			
			Pack above = findJustAbove(packages, p.x, p.y);
			if (above != null){
				p.y = above.y + above.h;
			} else {
				p.y = 0;
			}
			
			
			for (int j = 0; j < packages.size(); j++) {
				if (intersect(p, packages.get(j))) {
					p.y = packages.get(j).y + packages.get(j).h;
				}
			}
			
			
			if (p.y + p.h > palet_h){
				continue;
			}
			current_x = p.x + p.w;
			current_y = p.y;
			total_fit += p.w * p.h;
			packages.add(p);
		}
		current_fitness_rate = total_fit / (palet_h*palet_w);
		return packages;
	}
	private static Pack findJustAbove(Vector<Pack> packages,int x,int y){
		Pack top = null;
		for (int i = 0; i < packages.size(); i++) {
			Pack p = packages.get(i);
			if (x >= p.x && x < p.x + p.w){
				if (top == null){
					top = p;
				} else if (top.y + top.h < p.y){
					top = p;
				}
			}
		}
		return top;
	}
	
	private static boolean intersect(Pack p1,Pack p2){
		Polygon _p1 = new Polygon();
		_p1.addPoint(p1.x, p1.y);
		_p1.addPoint(p1.x+p1.w, p1.y);
		_p1.addPoint(p1.x+p1.w, p1.y+p1.h);
		_p1.addPoint(p1.x, p1.y+p1.h);
		
		Polygon _p2 = new Polygon();
		_p2.addPoint(p2.x, p2.y);
		_p2.addPoint(p2.x+p2.w, p2.y);
		_p2.addPoint(p2.x+p2.w, p2.y+p2.h);
		_p2.addPoint(p2.x, p2.y+p2.h);
		
		return _p1.intersects(_p2.getBounds2D());
	}
	
	private Image image;
	private Graphics graphics;
	public void init(){
		BufferedImage buf = new BufferedImage(palet_w+2, palet_h+2, BufferedImage.TYPE_3BYTE_BGR);
		image = buf;
		graphics = image.getGraphics();
	}
	

	public void drawSolution(Graphics g,Vector<Pack> packs,int w,int h){
		graphics.clearRect(0,0,image.getWidth(null),image.getHeight(null));
		
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, w, h);
		Color colors[] = {
			Color.BLACK,
			Color.BLUE,
			Color.RED,
			Color.GREEN,
			Color.CYAN,
			Color.ORANGE,
			Color.PINK,
			Color.MAGENTA,
			Color.YELLOW,
		};
		graphics.setColor(Color.BLACK);
		
		for (int i = 0; packs != null && i < packs.size(); i++) {
			Pack p = packs.get(i);
			graphics.setColor(colors[i%colors.length]);
			graphics.fillRect(p.x, p.y, p.w, p.h);
			graphics.setColor(Color.GRAY);
			graphics.fillRect(p.x+2, p.y+2, p.w-4, p.h-4);
		}
		
		
		Graphics2D gg = (Graphics2D)g;
		gg.drawImage(image, 0, 0, w, h, null);
		
		
		
	}
}
