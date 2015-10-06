package com.johnhite.casino.blackjack.strategy;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import com.johnhite.casino.Card;
import com.johnhite.casino.Card.Ordinal;
import com.johnhite.casino.blackjack.Action;
import com.johnhite.casino.blackjack.Hand;
import com.johnhite.casino.nn.Genome;


public class EvolutionStrategy implements BlackjackStrategy, Genome<Integer> {
	private int amountWon;
	private int gamesPlayed;
	private SecureRandom rand = new SecureRandom();
	private List<Integer> strategy = new ArrayList<>(340);
	
	public EvolutionStrategy() {
		for (int offset =0; offset < 340; offset += 34) {
			//0-9 contains the pair strategy
			for (int i=0; i < 10; i++) {
				strategy.add(rand.nextInt(5));
			}
			//10-17 contains strategy for soft totals
			//18-33 contains strategy for hard totals
			for (int i=10; i < 34; i++) {
				strategy.add(rand.nextInt(4));
			}
		}
	}
	
	public EvolutionStrategy(List<Integer> weights) {
		this.strategy = weights;
	}

	@Override
	public Action play(Card dealer, Hand hand) {
		int dealerIndex = dealer.getValue() - 1;
		if (hand.canSplit()) {
			//Pairs 0-9
			if (hand.getCardAt(0).getOrdinal() == Ordinal.ACE) {
				return getAction(dealerIndex, 0);
			} else {
				return getAction(dealerIndex, hand.getScore() / 2 - 1);
			}
		} else if (hand.isSoft()) {
			//soft totals 10-17
			return getAction(dealerIndex, hand.getScore() - 3); // min soft score is 13 and min soft score index is 10 so subtract 3 from score to get index
		} else if (hand.getScore() == 21) {
			return Action.STAND;
		}
		else {
			//hard totals 18-33
			//min hard score is 5 and min hard index is 18, so add 13 to the score to get index
			return getAction(dealerIndex, hand.getScore() + 13);
		}
	}
	
	private Action getAction(int dealerIndex, int playerIndex) {
		return actionFromNumber(strategy.get(dealerIndex * 34 + playerIndex));
	}

	private Action actionFromNumber(int number) {
		switch (number) {
			case 0:
				return Action.SURRENDER;
			case 1:
				return Action.STAND;
			case 2:
				return Action.HIT;
			case 3:
				return Action.DOUBLE;
			case 4:
				return Action.SPLIT;
			default:
				return Action.SURRENDER;
		}
	}
	
	@Override
	public int getBet(int min, int max) {
		return min;
	}
	
	@Override
	public void notifyResult(Hand dealerHand, Hand playerHand, int amountWon) {
		this.amountWon += amountWon - playerHand.getBet();
		this.gamesPlayed++;
	}
	
	@Override
	public void resetFitness() {
		this.amountWon = 0;
		this.gamesPlayed = 0;
	}
	
	@Override
	public double getFitness() {
		//return ((double)amountWon)/((double)gamesPlayed);
		return (100000.0 + (double)amountWon) / 100000.0;
	}

	@Override
	public List<Integer> getGenes() {
		// TODO Auto-generated method stub
		return strategy;
	}
	
	@Override
	public void setGenes(List<Integer> genes) {
		this.strategy = genes;
	}

	@Override
	public Integer mutate(int index) {
		int strategyIndex = index % 34;
		if (strategyIndex < 10) {
			return rand.nextInt(5);
		}
		return rand.nextInt(4);
	}
	
	@Override
	public Integer getGene(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGene(int index, Integer value) {
	}
}
