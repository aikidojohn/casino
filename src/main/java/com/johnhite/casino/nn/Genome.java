package com.johnhite.casino.nn;

import java.util.List;

public interface Genome extends Comparable<Genome> {

	public List<Double> getGenes();
	
	public void setGenes(List<Double> genes);

	public double getFitness();

	public void resetFitness();

	@Override
	public default int compareTo(Genome o){
		if (getFitness() > o.getFitness()) {
			return 1;
		}
		if (getFitness() < o.getFitness()) {
			return -1;
		}
		return 0;
	};
}