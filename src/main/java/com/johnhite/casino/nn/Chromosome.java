package com.johnhite.casino.nn;

import java.util.List;

import com.google.common.collect.Lists;

public class Chromosome implements Comparable<Chromosome>, Cloneable {
	private List<Double> genes = Lists.newArrayList();
	private double fitness;
	
	public Chromosome(List<Double> genes, double fitness) {
		this.genes = genes;
		this.fitness = fitness;
	}
	
	public List<Double> getGenes() {
		return this.genes;
	}
	
	public double getFitness() {
		return fitness;
	}
	
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	@Override
	public int compareTo(Chromosome o) {
		if (this.fitness > o.fitness) {
			return 1;
		}
		if (this.fitness < o.fitness) {
			return -1;
		}
		return 0;
	}
	
	@Override
	public Chromosome clone() {
		return new Chromosome(Lists.newArrayList(genes), 0);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Chromosome {").append(genes).append(", fitness=")
				.append(fitness).append("}");
		return builder.toString();
	}
}
