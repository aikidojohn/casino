package com.johnhite.casino.nn;

import java.util.List;

import com.google.common.collect.Lists;

public class GeneticAlgorithm {
	private List<? extends Genome> population = Lists.newArrayList();
	private final double mutationRate;
	private final double crossoverRate;
	private Genome bestSoFar;
	private Genome bestLastRound;
	private double averageFitness;
	
	public GeneticAlgorithm() {
		this(0.7, 0.05);
	}
	
	public GeneticAlgorithm(double crossoverRate, double mutationRate) {
		this.crossoverRate = crossoverRate;
		this.mutationRate = mutationRate;
	}
	
	public Genome getBestSoFar() {
		return bestSoFar;
	}
	
	public Genome getBestLastRound() {
		return bestLastRound;
	}

	public double getAverageFitness() {
		return averageFitness;
	}

	public List<? extends Genome> getPopulation() {
		return population;
	}
	
	public void setPopulation(List<? extends Genome> chromosomes) {
		this.population = chromosomes;
	}
	
	public List<? extends Genome> epoch() {
		List<List<Double>> newPopulation = Lists.newArrayList();
		Genome best = null;
		double totalFitness = 0;
		for (Genome c : population) {
			totalFitness += c.getFitness();
			if (best == null || best.getFitness() < c.getFitness()) {
				best = c;
			}
		}
		
		bestLastRound = new BasicGenome(Lists.newArrayList(best.getGenes()), best.getFitness());
		if(bestSoFar == null || bestLastRound.getFitness() > bestSoFar.getFitness() ) {
			bestSoFar = bestLastRound;
		}
		averageFitness = totalFitness / population.size();
		
		while(newPopulation.size() < population.size()) {
			Genome mom = rouletteSelect(totalFitness);
			Genome dad = rouletteSelect(totalFitness);
			
			if (mom == null || dad == null) {
				continue;
			}
			List<Double> a = mom.getGenes();
			List<Double> b = dad.getGenes();
			
			crossover(a, b);
			
			mutate(a);
			mutate(b);
			newPopulation.add(a);
			newPopulation.add(b);
		}
		//update all the genes
		for (int i=0; i < population.size(); i++) {
			population.get(i).setGenes(newPopulation.get(i));
			population.get(i).resetFitness();
		}
		return this.population;
	}
	
	private void crossover(List<Double> a, List<Double> b) {
		if (Math.random() < this.crossoverRate) {
			int cut = (int) Math.random() * a.size();
			for (int i=0; i < cut; i++) {
				a.set(i, b.get(i));
			}
			for (int i = cut; i < a.size(); i++) {
				b.set(i, a.get(i));
			}
		}
	}
	
	private void mutate(List<Double> c) {
		for (int i= 0; i < c.size(); i++) {
			if (Math.random() < this.mutationRate) {
				c.set(i, MathUtil.randomGaussian());
			}
		}
	}
	
	private Genome rouletteSelect(double totalFitness) {
		double target = Math.random() * totalFitness;
		double fitnessSoFar = 0.0;
		for(Genome c : population) {
			fitnessSoFar += c.getFitness();
			if(fitnessSoFar >= target) {
				return c;
			}
		}
		return null;
	}
}
