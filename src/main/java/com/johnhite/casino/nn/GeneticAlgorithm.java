package com.johnhite.casino.nn;

import java.security.SecureRandom;
import java.util.List;

import com.google.common.collect.Lists;

public class GeneticAlgorithm {
	private List<? extends Genome> population = Lists.newArrayList();
	private final double mutationRate;
	private final double crossoverRate;
	private Genome bestSoFar;
	private Genome bestLastRound;
	private double averageFitness;
	private List<Genome> allTheBest = Lists.newArrayList();
	private SecureRandom random = new SecureRandom();
	
	public GeneticAlgorithm() {
		this(0.7, 0.05);
	}
	
	public GeneticAlgorithm(double crossoverRate, double mutationRate) {
		this.crossoverRate = crossoverRate;
		this.mutationRate = mutationRate;
	}
	
	public List<Genome> getAllTheBest() {
		return allTheBest;
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
	
	public <T> List<? extends Genome<T>> epoch() {
		List<List<T>> newPopulation = Lists.newArrayList();
		Genome<T> best = null;
		double totalFitness = 0;
		for (Genome<T> c : population) {
			totalFitness += c.getFitness();
			if (best == null || best.getFitness() < c.getFitness()) {
				best = c;
			}
		}
		
		bestLastRound = new BasicGenome<T>(Lists.newArrayList(best.getGenes()), best.getFitness());
		allTheBest.add(bestLastRound);
		if(bestSoFar == null || bestLastRound.getFitness() > bestSoFar.getFitness() ) {
			bestSoFar = bestLastRound;
		}
		averageFitness = totalFitness / population.size();
		
		while(newPopulation.size() < population.size()) {
			Genome<T> mom = rouletteSelect(totalFitness);
			Genome<T> dad = rouletteSelect(totalFitness);
			
			if (mom == null || dad == null) {
				continue;
			}
			List<T> a = mom.getGenes();
			List<T> b = dad.getGenes();
			
			crossover(a, b);
			
			mutate(a, mom);
			mutate(b, mom);
			newPopulation.add(a);
			newPopulation.add(b);
		}
		//update all the genes
		for (int i=0; i < population.size(); i++) {
			population.get(i).setGenes(newPopulation.get(i));
			population.get(i).resetFitness();
		}
		return (List<? extends Genome<T>>) this.population;
	}
	
	private <T> void crossover(List<T> a, List<T> b) {
		if (random.nextDouble() < this.crossoverRate) {
			int cut = (int) random.nextDouble() * a.size();
			for (int i=0; i < cut; i++) {
				a.set(i, b.get(i));
			}
			for (int i = cut; i < a.size(); i++) {
				b.set(i, a.get(i));
			}
		}
	}
	
	private <T> void mutate(List<T> c, Genome<T> example) {
		for (int i= 0; i < c.size(); i++) {
			if (random.nextDouble() < this.mutationRate) {
				c.set(i, example.mutate(i));
			}
		}
	}
	
	private <T> Genome<T> rouletteSelect(double totalFitness) {
		double target = Math.random() * totalFitness;
		double fitnessSoFar = 0.0;
		for(Genome<T> c : population) {
			fitnessSoFar += c.getFitness();
			if(fitnessSoFar >= target) {
				return c;
			}
		}
		return null;
	}
	
	private <T> Genome<T> tournamentSelect() {
		int sampleSize = (int)(Math.random() * population.size());
		Genome<T> best = null;
		for (int i=0; i < sampleSize; i++) {
			Genome<T> s = population.get(i);
			if (best == null || s.getFitness() > best.getFitness()) {
				best = s;
			}
		}
		return best;
	}
}
