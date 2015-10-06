package com.johnhite.casino.blackjack.strategy;

import java.util.List;

import com.google.common.collect.Lists;
import com.johnhite.casino.Card;
import com.johnhite.casino.blackjack.Action;
import com.johnhite.casino.blackjack.Hand;
import com.johnhite.casino.nn.NeuralNetwork;

public class NeuralStrategy implements BlackjackStrategy  {
	private NeuralNetwork brain;
	
	public NeuralStrategy() {
		brain = new NeuralNetwork(5, 5, 4, 50);
	}
	
	public NeuralStrategy(List<Double> weights) {
		this();
		brain.setWeights(weights);
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
		
		Action action = fromDoubles(brain.update(inputs));
		if (action == Action.SPLIT && !hand.isPair()) {
			return Action.SURRENDER;
		}
		if (action == Action.DOUBLE && hand.isDone()) {
			return Action.STAND;
		}
		return action;
	}

	private Action fromDoubles(List<Double> output) {
		int index = 0;
		int maxIndex = -1;
		double max = Double.NEGATIVE_INFINITY;
		for (double o : output) {
			if ( o > max) {
				maxIndex = index;
				max = o;
			}
			index++;
		}
		
		if (maxIndex == 0) {
			return Action.STAND;
		}
		if (maxIndex == 1) {
			return Action.SPLIT;
		}
		if (maxIndex == 2) {
			return Action.DOUBLE;
		}
		if (maxIndex == 3) {
			return Action.HIT;
		}
		return Action.SURRENDER;
	}
	
	@Override
	public int getBet(int min, int max) {
		return min;
	}
	
	public NeuralNetwork getBrain() {
		return brain;
	}
}
