package tr.edu.istanbul.pso4containerproblem;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class UI {
	
	private static final int XMAX = 4;
	private static final int XMIN = -4;
	private static final int VMAX = 4;
	private static final int VMIN = 0;
	private static final double INTERTIA_FACTOR = 0.975;
	private static final double MIN_INTERTIA = 0.4;
	private static final double START_INTERTIA = 0.9;
	final JButton start;
	static UI instance;
	JFrame frame;
	Suru current_suru;
	
	Canvas canvas;
	JComboBox iterasyon;
	JComboBox zaman;
	JComboBox min_paket;
	JComboBox max_paket;
	JProgressBar progress;
	JLabel info;
	JLabel info2;
	class Iterasyon{
		public Iterasyon(String l ,int c) {
			label = l;
			count = c;
		}
		String label;
		int count;
		@Override
		public String toString() {
			return label;
		}
	}
	Problem current_problem;
	UI(){
		frame = new JFrame();
		JPanel control = new JPanel();
		frame.setLayout(new BorderLayout());
		frame.add(control,BorderLayout.WEST);
		control.setLayout(new GridLayout(15, 1));
		canvas = new Canvas() {
			@Override
			public void paint(Graphics g) {
				if (current_problem != null && current_problem.best_packs != null){
					current_problem.drawSolution(g,current_problem.best_packs, getWidth(), getHeight());
				}
			}
			@Override
			public void update(Graphics arg0) {
				paint(arg0);
			}
		};
		frame.add(canvas,BorderLayout.CENTER);
		start = new JButton(L.start);
		control.add(start);
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (start.getText().equals(L.start)){
					start.setText(L.stop);
					solve();
				} else {
					start.setText(L.start);
					stop_solve();
				}
			}
		});
		iterasyon = new JComboBox();
		iterasyon.addItem(new Iterasyon("100 Ýterasyon", 100));
		iterasyon.addItem(new Iterasyon("500 Ýterasyon", 500));
		iterasyon.addItem(new Iterasyon("1000 Ýterasyon", 1000));
		iterasyon.addItem(new Iterasyon("5000 Ýterasyon", 5000));
		iterasyon.addItem(new Iterasyon("10000 Ýterasyon", 10000));
		iterasyon.addItem(new Iterasyon("50000 Ýterasyon", 50000));
		iterasyon.addItem(new Iterasyon("100000 Ýterasyon", 100000));
		iterasyon.setSelectedIndex(2);
		control.add(iterasyon);
		zaman = new JComboBox();
		zaman.addItem(new Iterasyon("30 sn", 30));
		zaman.addItem(new Iterasyon("1 dk", 60));
		zaman.addItem(new Iterasyon("3 dk", 3*60));
		zaman.addItem(new Iterasyon("10 dk", 10*60));
		zaman.setSelectedIndex(0);
		control.add(zaman);
		
		control.add(new JLabel("Minimum paket"));
		min_paket = new JComboBox();
		min_paket.addItem(new Iterasyon("0 kutu", 0));
		min_paket.addItem(new Iterasyon("2 kutu", 2));
		min_paket.addItem(new Iterasyon("4 kutu", 4));
		min_paket.addItem(new Iterasyon("8 kutu", 8));
		min_paket.addItem(new Iterasyon("10 kutu", 19));
		min_paket.setSelectedIndex(0);
		control.add(min_paket);
		control.add(new JLabel("Maksimum paket"));
		max_paket = new JComboBox();
		max_paket.addItem(new Iterasyon("5 kutu", 5));
		max_paket.addItem(new Iterasyon("10 kutu", 10));
		max_paket.addItem(new Iterasyon("20 kutu", 20));
		max_paket.addItem(new Iterasyon("40 kutu", 40));
		max_paket.addItem(new Iterasyon("80 kutu", 80));
		max_paket.setSelectedIndex(4);
		control.add(max_paket);
		
		
		progress = new JProgressBar();
		control.add(progress);
		info = new JLabel();
		control.add(info);
		info2 = new JLabel();
		control.add(info2);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500,400);
		frame.setVisible(true);
	}
	boolean solving;
	void stop_solve(){
		solving = false;
		if (current_suru != null){
			current_suru.stop();
		}
		
	}
	void solve(){
		solving = true;
		progress.setMaximum(100);
		progress.setValue(0);
		new Thread(){
			public void run() {
				Iterasyon i = (Iterasyon)iterasyon.getSelectedItem();
				Iterasyon z = (Iterasyon)zaman.getSelectedItem();
				current_problem = SampleProblems.sample1();
				current_problem.init();
				int count = 1;
				double best_cost=0;
				double best_vals[]=null;
				int box_w=0;
				int box_h=0;
				int min_paket_count = ((Iterasyon)min_paket.getSelectedItem()).count;
				int max_paket_count = ((Iterasyon)max_paket.getSelectedItem()).count;
				int max_alternative = BoxAlternative.maxAlternative(current_problem,min_paket_count,max_paket_count);
				progress.setMaximum(100*max_alternative);
				progress.setValue(0);
				
				while (BoxAlternative.nextAlternative(current_problem,count++ ,min_paket_count,max_paket_count) && solving){
					current_suru = new Suru();
					current_suru.setProgressBounds(count * 100, 100);
					current_suru.init(current_problem, 10,XMIN,XMAX,VMIN,VMAX,MIN_INTERTIA,INTERTIA_FACTOR,START_INTERTIA);
					current_suru.solve(i.count, z.count*1000);
					if (current_suru.getBestCost() > best_cost){
						best_cost = current_suru.getBestCost();
						best_vals = current_suru.getBestVals();
						box_w = current_problem.box_w;
						box_h = current_problem.box_h;
					}
				}
				start.setText(L.start);
				current_problem.box_h = box_h;
				current_problem.box_w = box_w;
				int best_order[] = new int[best_vals.length];
				Algorithms.apply_SPV("", best_vals, best_order);
				current_problem.best_packs = current_problem.applySerie(best_order);
				
				int pw = current_problem.box_w / current_problem.product_w;
				int ph = current_problem.box_h / current_problem.product_h;
				info.setText("en iyi paket:"+current_problem.box_w+"cm x "+current_problem.box_h+" cm ("+pw+"x"+ph+"="+(pw*ph)+")");
				info2.setText("en iyi doluluk:"+(int)(best_cost*100)+"%");
				
				stop_solve();
			};
		}.start();
		
	}
	public static void main(String[] args) {
		instance = new UI();
		
	}
	public void update(int step,long time,double cost){
		int pw = current_problem.box_w / current_problem.product_w;
		int ph = current_problem.box_h / current_problem.product_h;
		info.setText("paket:"+current_problem.box_w+"cm x "+current_problem.box_h+" cm ("+pw+"x"+ph+"="+(pw*ph)+")");
		info2.setText("doluluk:"+(int)(cost*100)+"%");
	}
	public void setProgress(int pro){
		progress.setValue(pro);
	}
}
