package com.johnhite.casino.nn;

import java.util.List;

import com.google.common.collect.Lists;

public class BasicGenome implements Genome {
	private List<Double> genes = Lists.newArrayList();
	private double fitness;
	
	public BasicGenome(List<Double> genes, double fitness) {
		this.genes = genes;
		this.fitness = fitness;
	}
	
	@Override
	public List<Double> getGenes() {
		return this.genes;
	}
	
	@Override
	public void setGenes(List<Double> genes) {
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
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Genome {").append(genes).append(", fitness=")
				.append(fitness).append("}");
		return builder.toString();
	}
}
