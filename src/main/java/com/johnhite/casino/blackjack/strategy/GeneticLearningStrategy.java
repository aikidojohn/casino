package com.johnhite.casino.blackjack.strategy;

import java.util.List;

import com.google.common.collect.Lists;
import com.johnhite.casino.Card;
import com.johnhite.casino.blackjack.Action;
import com.johnhite.casino.blackjack.DeckListener;
import com.johnhite.casino.blackjack.Hand;
import com.johnhite.casino.nn.Genome;
import com.johnhite.casino.nn.MathUtil;
import com.johnhite.casino.nn.NeuralNetwork;


public class GeneticLearningStrategy implements BlackjackStrategy, Genome {
	private NeuralNetwork brain;
	private NeuralStrategy neuralStrategy;
	private int amountWagered;
	private int amountWon;
	private int numberGames= 0;
	private int gamesWon= 0;
	private List<Double> roundFitness = Lists.newArrayList();
	
	public GeneticLearningStrategy(NeuralNetwork brain) {
		this.brain = brain;
		this.neuralStrategy = new NeuralStrategy(brain);
	}

	@Override
	public Action play(Card dealer, Hand hand) {
		return neuralStrategy.play(dealer, hand);
	}

	@Override
	public int getBet(int min, int max) {
		return min;
	}
	
	@Override
	public void notifyResult(Hand dealerHand, Hand playerHand, int amountWon) {
		this.amountWon += amountWon;
		this.amountWagered += playerHand.getBet();
		this.numberGames++;
		if (amountWon > 0) {
			gamesWon++;
		}
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
		this.gamesWon =0;
		roundFitness.clear();
	}
	
	@Override
	public double getFitness() {
		/*if (roundFitness.size() > 0)
			return ((double)totalWon / (double)totalWagered) / MathUtil.std(roundFitness);
			//return (double)averageSum / (double)numberRounds;
		return Integer.MIN_VALUE;*/
		//return (double)amountWon / (double)amountWagered;
		return (double)gamesWon / (double)numberGames;
	}


	@Override
	public List<Double> getGenes() {
		return brain.getWeights();
	}
	
	@Override
	public void setGenes(List<Double> genes) {
		this.brain.setWeights(genes);
	}
}
