package com.johnhite.casino.nn;

import java.util.List;

public interface Genome<T> extends Comparable<Genome<T>> {

	public List<T> getGenes();
	
	public void setGenes(List<T> genes);

	public double getFitness();

	public void resetFitness();
	
	public T getGene(int index);
	public void setGene(int index, T value);
	public T mutate(int index);

	@Override
	public default int compareTo(Genome<T> o){
		if (getFitness() > o.getFitness()) {
			return 1;
		}
		if (getFitness() < o.getFitness()) {
			return -1;
		}
		return 0;
	};
}