package com.johnhite.casino.blackjack.strategy;

import java.security.SecureRandom;
import java.util.List;

import com.google.common.collect.Lists;
import com.johnhite.casino.Card;
import com.johnhite.casino.blackjack.Action;
import com.johnhite.casino.blackjack.Hand;
import com.johnhite.casino.nn.Genome;
import com.johnhite.casino.nn.MathUtil;


public class GeneticLearningStrategy implements BlackjackStrategy, Genome<Double> {
	private NeuralStrategy neuralStrategy;
	private int amountWagered;
	private int amountWon;
	private int amountLost;
	private int numberGames= 0;
	private int gamesWon= 0;
	private List<Double> roundFitness = Lists.newArrayList();
	private int sumX;
	private int sumY;
	private int sumX2;
	private int sumXY;
	
	public GeneticLearningStrategy() {
		this.neuralStrategy = new NeuralStrategy();
	}

	@Override
	public Action play(Card dealer, Hand hand) {
		if (dirty) {
			setGenes(weights);
			weights = null;
		}
		return neuralStrategy.play(dealer, hand);
	}

	@Override
	public int getBet(int min, int max) {
		return min;
	}
	
	@Override
	public void notifyResult(Hand dealerHand, Hand playerHand, int amountWon) {
		this.sumX += numberGames;
		this.sumX2 += (numberGames * numberGames);
		this.sumY += amountWon;
		this.sumXY += (amountWon * numberGames);
		this.numberGames++;
		
		/*this.amountWon += amountWon;
		this.amountWagered += playerHand.getBet();
		if (amountWon >= playerHand.getBet()) {
			gamesWon++;
		} else {
			amountLost += amountWon;
		}*/
		
		/*if (this.numberGames > 0 && this.numberGames % 100 == 0) {
			roundFitness.add((double)this.amountWon / (double)amountWagered);
			//averageSum += (double)this.amountWon / (double)amountWagered;
			//this.numberRounds++;
			totalWon += this.amountWon;
			totalWagered += this.amountWagered;
			this.amountWagered = 0;
			this.amountWon = 0;
		}*/
	}
	
	@Override
	public void resetFitness() {
		this.amountWagered = 0;
		this.amountWon = 0;
		this.numberGames =0;
		this.gamesWon = 0;
		this.amountLost = 0;
		sumX = 0;
		sumY = 0;
		sumX2 = 0;
		sumXY = 0;
		roundFitness.clear();
	}
	
	@Override
	public double getFitness() {
		/*if (roundFitness.size() > 0)
			return ((double)totalWon / (double)totalWagered) / MathUtil.std(roundFitness);
			//return (double)averageSum / (double)numberRounds;
		return Integer.MIN_VALUE;*/
		//double percentWin = (double)gamesWon / (double)numberGames;
		//double luck = 1 - (percentWin - 0.5);
		//double gamesLost = (double)(numberGames - gamesWon);
		//return ((double)amountLost / (double)amountWagered) + ((double)(amountWon) / (double)amountWagered);
		//return ((double)amountWon) / (double)amountWagered;
		//return (double)gamesWon / (double)numberGames;
		
		double alpha = (double)(numberGames*sumXY - (sumX*sumY)) / (double)(numberGames * sumX2 - sumX*sumX);
		double beta = (double)(sumY - alpha * sumX) / (double)numberGames;
		double x1 = 0;
		double y1 = beta;
		double y2 = 0;
		double x2 = (-1.0*beta) / alpha;
		double m = (y1 - y2) / (x1 - x2);
		return m;
	}


	private List<Double> weights;
	private boolean dirty = false;
	@Override
	public List<Double> getGenes() {
		if (weights == null) {
			weights = neuralStrategy.getBrain().getWeights();
		}
		return weights;
	}
	
	@Override
	public void setGenes(List<Double> genes) {
		neuralStrategy.getBrain().setWeights(genes);
		dirty = false;
	}

	@Override
	public Double getGene(int index) {
		return getGenes().get(index);
	}

	@Override
	public void setGene(int index, Double value) {
		weights.set(index, value);
		dirty = true;
	}

	@Override
	public Double mutate(int index) {
		return MathUtil.randomGaussian();
	}
}
