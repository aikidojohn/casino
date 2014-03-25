package com.johnhite.casino.nn;

import java.util.List;

import com.google.common.collect.Lists;

public class GeneticAlgorithm {
	private List<Chromosome> population = Lists.newArrayList();
	private final double mutationRate = 0.05;
	private final double crossoverRate = 0.5;
	private Chromosome bestSoFar;
	private Chromosome bestLastRound;
	private double averageFitness;
	
	public Chromosome getBestSoFar() {
		return bestSoFar;
	}
	
	public Chromosome getBestLastRound() {
		return bestLastRound;
	}

	public double getAverageFitness() {
		return averageFitness;
	}

	public List<Chromosome> getPopulation() {
		return population;
	}
	
	public void setPopulation(List<Chromosome> chromosomes) {
		this.population = chromosomes;
	}
	
	public List<Chromosome> epoch() {
		List<Chromosome> newPopulation = Lists.newArrayList();
		Chromosome best = null;
		double totalFitness = 0;
		for (Chromosome c : population) {
			totalFitness += c.getFitness();
			if (best == null || best.getFitness() < c.getFitness()) {
				best = c;
			}
		}
		
		if(bestSoFar == null || best.getFitness() > bestSoFar.getFitness() ) {
			bestSoFar = best;
		}
		bestLastRound = best;
		averageFitness = totalFitness / population.size();
		
		while(newPopulation.size() < population.size()) {
			Chromosome mom = rouletteSelect(totalFitness);
			Chromosome dad = rouletteSelect(totalFitness);
			
			if (mom == null || dad == null) {
				continue;
			}
			Chromosome a = mom.clone();
			Chromosome b = dad.clone();
			
			crossover(a, b);
			
			mutate(a);
			mutate(b);
			newPopulation.add(a);
			newPopulation.add(b);
		}
		
		this.population = newPopulation;
		return newPopulation;
	}
	
	private void crossover(Chromosome a, Chromosome b) {
		if (Math.random() < this.crossoverRate) {
			int cut = (int) Math.random() * a.getGenes().size();
			for (int i=0; i < cut; i++) {
				a.getGenes().set(i, b.getGenes().get(i));
			}
			for (int i = cut; i < a.getGenes().size(); i++) {
				b.getGenes().set(i, a.getGenes().get(i));
			}
		}
	}
	
	private void mutate(Chromosome c) {
		for (int i= 0; i < c.getGenes().size(); i++) {
			if (Math.random() < this.mutationRate) {
				c.getGenes().set(i, MathUtil.randomGaussian());
			}
		}
	}
	
	private Chromosome rouletteSelect(double totalFitness) {
		double target = Math.random() * totalFitness;
		double fitnessSoFar = 0.0;
		for(Chromosome c : population) {
			fitnessSoFar += c.getFitness();
			if(fitnessSoFar >= target) {
				return c;
			}
		}
		return null;
	}
}
