/*Author: Guojun Zhang,Felipe
 * FileName: Generation.java
 * Course: CSC680 Artificial Intelligence
 * HW: #5
 * Due: 11/26/2012
 * Description: Generation represent number of individual(chromosome)
 * 
 */
public class Generation {
	private int numPopulation;
	private static final double PC = 0.75;
	private static final double PM = 0.001;
	private Chromosome[] chroms;
	private Chromosome bestChrom;
	private double bestFitness;
	private double avgFitness;

	/*
	 * constructor
	 */
	Generation(int numPopulation,double[] weight){
		this.numPopulation = numPopulation;
		chroms = new Chromosome[numPopulation];
		for(int i = 0; i < numPopulation; i++){
			chroms[i] = new Chromosome(weight);
		}
		evaluateFitnesses();
	}
	
	/*
	 * constructor
	 */
	public Chromosome[] getChroms(){
		return chroms;
	}
	
	/*
	 * apply survival of the fittest theory 
	 * using roulette wheel selection 
	 */
	public void select() {
		int n = 0;
		Chromosome[] selectedPopulation = new Chromosome[numPopulation];
		double[] cp = computeCumProb();
		
		for (int i = 0; i < cp.length; i++) {
			double r = Math.random();
			for (int j = 0; j < cp.length; j++) {

				if (r < cp[j]) {
					selectedPopulation[n++] = new Chromosome(chroms[j]);
					break;
				}
			}
		}
		chroms = selectedPopulation;
	}
	/*
	 * two chromosomes do one-point crossover
	 */
	private void crossover(int n, int m) {
		
		int totalGeneBitsNum = chroms[0].getTotalGeneBitsNum();
		int  splitPoint = (int)(Math.random()*(totalGeneBitsNum - 1));
	
		int[] gen1 = chroms[n].getGeneBits();
		int[] gen2 = chroms[m].getGeneBits();

		int tmp;
		for (int i = 0; i < splitPoint; i++) {
			tmp = gen1[i];
			gen1[i] = gen2[i];
			gen2[i] = tmp;
		}

	}
	/*
	 * do crossover involve PC percentage of population. in this case PC is 75%
	 */
	public void doCrossover() {
		int[] maker = new int[numPopulation];
		// count how many times mating
		int count = (int) (PC * numPopulation / 2);
		for (int i = 0; i < count; i++) {
			int r1, r2;
			do{
				r1 = (int) (Math.random() * (numPopulation - 1));
			} while (maker[r1] == 1);
			maker[r1] = 1;
			do{
				r2 = (int) (Math.random() * (numPopulation - 1));
			} while (maker[r2] == 1);

			maker[r2] = 1;
			crossover(r1, r2);
		}
		
	}
	/*
	 * apply mutate involve PM percentage population. in this case PM is 0.001
	 */
	public void mutate() {
		int totalGeneBitsNum = chroms[0].getTotalGeneBitsNum();
		int mutationNum = (int) (PM * numPopulation * totalGeneBitsNum);

		for (int i = 0; i < mutationNum; i++) {
			int r = (int) (Math.random() * (numPopulation * totalGeneBitsNum));
			int row = r / totalGeneBitsNum;
			int col = r - row * totalGeneBitsNum;
			

			if (chroms[row].getGeneBits()[col] == 0) {
				chroms[row].getGeneBits()[col] = 1;
			} else {
				chroms[row].getGeneBits()[col] = 0;
			}
		}
	}
	/*
	 * compute relative cumulative fitness for each chromosome 
	 */
	private double[] computeCumProb(){
		double[] cumProb = new double[numPopulation];
		double totalFit = 0; 	
		for(int i = 0; i < numPopulation; i++){
			
			totalFit += (1/chroms[i].computeTotalFitness());	
		}
			
		for(int i = 0; i < cumProb.length; i++){
			if(i == 0){
				cumProb[i] = (1/chroms[i].computeTotalFitness())/totalFit;
			}else{
				cumProb[i] = (1/chroms[i].computeTotalFitness())/totalFit + cumProb[i-1];
			}
		}
		
		return cumProb;
	}
	
	/*
	 * update the instance variables bestFitness and avgFitness 
	 */
	public void evaluateFitnesses(){
		for(int i = 0; i < numPopulation; i++){
			chroms[i].evaluateFitness();
		}
		double total = chroms[0].computeTotalFitness();
		bestFitness = chroms[0].computeTotalFitness();
		bestChrom = chroms[0];
		for(int i = 1; i < chroms.length; i++){
			double fitness = chroms[i].computeTotalFitness();
			if(fitness < bestFitness){
				
				bestFitness = fitness;
				bestChrom = chroms[i];
				
			}
			total += fitness;
		}
		avgFitness = total / numPopulation;
	}
	
	
	public double getBestFitness(){
		return bestFitness;
	}
	
	public double getAvgFitness(){
		return avgFitness;
	}
	/*
	 * toString
	 * 
	 */
	
	public String toString(){
		return bestChrom.toString() +
				"\nAvg fitness: " + avgFitness+"\n";
	}
	
}
