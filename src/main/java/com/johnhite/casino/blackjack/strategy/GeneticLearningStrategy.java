package com.johnhite.casino.blackjack.strategy;

import java.util.List;

import com.google.common.collect.Lists;
import com.johnhite.casino.Card;
import com.johnhite.casino.blackjack.Action;
import com.johnhite.casino.blackjack.DeckListener;
import com.johnhite.casino.blackjack.Hand;
import com.johnhite.casino.nn.NeuralNetwork;


public class GeneticLearningStrategy implements BlackjackStrategy, DeckListener {
	private NeuralNetwork brain;
	private int amountWagered;
	private int amountWon;
	private int numberGames= 0;
	private int numberRounds= 0;
	private int averageSum=0;
	
	public GeneticLearningStrategy(NeuralNetwork brain) {
		this.brain = brain;
	}
	
	@Override
	public void cardDealt(Card c) {
		
	}

	@Override
	public void shuffle() {
	}

	@Override
	public Action play(Card dealer, Hand hand) {
		List<Double> inputs = Lists.newArrayList();
		int dv = dealer.getValue();
		if (dv == 1) {
			dv += 10;
		}
		
		inputs.add((double)dv);
		inputs.add((double)hand.getScore());
		inputs.add((double)hand.getNumberCards());
		inputs.add(hand.isPair() ? 1.0 : 0.0);
		inputs.add(hand.isSoft() ? 1.0 : 0.0);
		
		double result = brain.update(inputs).get(0);
		Action action = fromDouble(result);
		if (action == Action.SPLIT && !hand.isPair()) {
			return Action.STAND;
		}
		if (action == Action.DOUBLE && hand.isDone()) {
			return Action.STAND;
		}
		return action;
	}
	
	private Action fromDouble(double val) {
		if (val <= 0.2) {
			return Action.HIT;
		}
		if (val <= 0.4) {
			return Action.DOUBLE;
		}
		if (val <= 0.6) {
			return Action.SPLIT;
		}
		if (val <= 0.8) {
			return Action.SURRENDER;
		}
		return Action.STAND;
	}

	@Override
	public int getBet(int min, int max) {
		return min;
	}
	
	@Override
	public void notifyResult(Hand dealerHand, Hand playerHand, int amountWon) {
		this.amountWon += amountWon;
		this.amountWagered += playerHand.getBet();
		/*this.numberGames++;
		if (this.numberGames > 0 && this.numberGames % 100 == 0) {
			averageSum += (double)this.amountWon / (double)amountWagered;
			this.numberRounds++;
		}*/
		brain.setFitness(getFitness());
	}
	
	public void resetStats() {
		this.amountWagered = 0;
		this.amountWon = 0;
		this.numberGames =0;
		this.numberRounds =0;
		this.averageSum = 0;
		brain.setFitness(0);
	}
	
	public double getFitness() {
		/*if (numberRounds > 0)
			return averageSum / numberRounds;
		return Integer.MIN_VALUE;
		*/
		return (double)amountWon / (double)amountWagered;
	}
}
