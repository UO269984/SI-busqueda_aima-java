package aima.core.search.local;

public class FitnessData {
	public double bestFitness;
	public double averageFitness;
	
	public FitnessData(double bestFitness, double averageFitness) {
		this.bestFitness = bestFitness;
		this.averageFitness = averageFitness;
	}
	
	@Override
	public String toString() {
		return "Best: " + this.bestFitness + " - Average" + this.averageFitness;
	}
}