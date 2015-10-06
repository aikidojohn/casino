package com.johnhite.casino.nn;

import java.util.List;

import com.google.common.collect.Lists;

public class BasicGenome<T> implements Genome<T> {
	private List<T> genes = Lists.newArrayList();
	private double fitness;
	
	public BasicGenome(List<T> genes, double fitness) {
		this.genes = genes;
		this.fitness = fitness;
	}
	
	@Override
	public List<T> getGenes() {
		return this.genes;
	}
	
	@Override
	public void setGenes(List<T> genes) {
		this.genes = genes;
	}
	
	@Override
	public double getFitness() {
		return fitness;
	}
	
	@Override
	public void resetFitness() {
		this.fitness = 0;
	}

	@Override
	public T getGene(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGene(int index, T value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public T mutate(int index) {
		return null;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Genome {").append(genes).append(", fitness=")
				.append(fitness).append("}");
		return builder.toString();
	}
}
