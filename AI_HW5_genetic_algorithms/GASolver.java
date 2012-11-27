/*Author: Guojun Zhang,Felipe
 * FileName: GASolver.java
 * Course: CSC680 Artificial Intelligence
 * HW: #5
 * Due: 11/26/2012
 * Description: the main program
 * 
 */

import jahuwaldt.plot.*;
import java.awt.*;
import javax.swing.*;
public class GASolver {

	private static final int NUMGENERATIONS = 100;
	private static final int POPULATION = 50;
	private Generation generation;
	private double[] bestFitnesses;
	private double[] avgFitnesses;
	
	/*  constructor  */
	public GASolver(){		
		bestFitnesses = new double[NUMGENERATIONS];
		avgFitnesses = new double[NUMGENERATIONS];
	}

	
	/*
	 * run the genetic algorithm
	 */
	public void doGA(double[] weight){
		generation = new Generation(POPULATION, weight);
		int i = 0;
		do{
			System.out.println("Generation " + i + "'s Best:");
			System.out.println("----------------------------------");
			System.out.println(generation);
			bestFitnesses[i] = generation.getBestFitness();
			avgFitnesses[i] = generation.getAvgFitness();			
			generation.select();		
			generation.doCrossover();
			generation.mutate();
			generation.evaluateFitnesses();
			i++;
		} while(i < NUMGENERATIONS);
		
	}
	
	public void show(String type,String function,double[] fitness){
		//***   Create a Simple 2D XY plot window.

		double[] xArr = new double[NUMGENERATIONS];
		double[] yArr = new double[NUMGENERATIONS];

		for (int i=0; i<NUMGENERATIONS; i++){
		      xArr[i] = i+1;
		      yArr[i] = fitness[i];   // your GASolver should fill up this array
		}
		
		Plot2D aPlot = null;
		
		if(type.equalsIgnoreCase("avg")){
			if(function.equalsIgnoreCase("cross")){
				aPlot = new SimplePlotXY(xArr, yArr, "Cross Section Area", "Generation No.", "Avg. Population Fitness", null, null, null);
			}else if(function.equalsIgnoreCase("static")){
				aPlot = new SimplePlotXY(xArr, yArr, "Static Deflection", "Generation No.", "Avg. Population Fitness", null, null, null);
			}else{
				aPlot = new SimplePlotXY(xArr, yArr, "Mix", "Generation No.", "Avg. Population Fitness", null, null, null);
			}
			
		}else if(type.equalsIgnoreCase("best")){
			if(function.equalsIgnoreCase("cross")){
				aPlot = new SimplePlotXY(xArr, yArr, "Cross Section Area", "Generation No.", "Best. Population Fitness", null, null, null);
			}else if(function.equalsIgnoreCase("static")){
				aPlot = new SimplePlotXY(xArr, yArr, "Static Deflection", "Generation No.", "Best. Population Fitness", null, null, null);
			}else{
				aPlot = new SimplePlotXY(xArr, yArr, "Mix", "Generation No.", "Best. Population Fitness", null, null, null);
			}
		}
		
		// Make the horizontal axis a log axis.
		PlotAxis xAxis = aPlot.getHorizontalAxis();
		xAxis.setScale(new LinearAxisScale());
		PlotPanel panel = new PlotPanel(aPlot);
		panel.setBackground( Color.white );
		PlotWindow window = new PlotWindow("SimplePlotXY Plot Window", panel);
		window.setSize(500, 300);
		window.setLocation(250,250);   // location on screen
		window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		window.show();
	}
	
	public static void main(String[] args) {
		GASolver ga = new GASolver();
		
		if(args.length !=3){
			System.out.println("Missing the weight parameters!");
			System.out.println("Usage: java GASolver 1 0 0 or \n java GASolver 0 1 0 or\n java GASolver 0.3 0.3 0.4");
		}else{
			double[] weight = {Double.parseDouble(args[0]),Double.parseDouble(args[1]),Double.parseDouble(args[2])};
			double total = 0;
			for(int i = 0; i < weight.length; i++){
				total += weight[i];
			}
			if( total != 1.0){
				System.out.println("Your input is not valid. Make sure weight sum is 1");
			}else{
				
				ga.doGA(weight);
				if(weight[0] == 1){
					ga.show("avg","cross",ga.avgFitnesses);
					ga.show("best","cross",ga.bestFitnesses);
				}else if(weight[1] == 1){
					ga.show("avg","static",ga.avgFitnesses);
					ga.show("best","static",ga.bestFitnesses);
				}else{
					ga.show("avg","mix",ga.avgFitnesses);
					ga.show("best","mix",ga.bestFitnesses);
				}
				
				
			}
		}
		
		
	}

}
