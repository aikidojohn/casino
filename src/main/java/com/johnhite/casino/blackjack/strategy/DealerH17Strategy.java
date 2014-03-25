package com.johnhite.casino.blackjack.strategy;

import java.util.List;
import java.util.function.BiFunction;

import com.google.common.collect.Lists;
import com.johnhite.casino.blackjack.Action;
import com.johnhite.casino.blackjack.Hand;
import com.johnhite.casino.Card;

/**
 * Dealer strategy where dealer hits on soft 17
 * @author John
 *
 */
public class DealerH17Strategy implements BlackjackStrategy {
	private List<BiFunction<Card, Hand, Action>> rules = Lists.newArrayList();
	
	public DealerH17Strategy() {

		//Hit on soft 17 and below, Stands on hard 18 and above
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (hand.isSoft() && hand.getScore() <= 17) { return Action.HIT; } 
			if (hand.getScore() <= 16) { return Action.HIT; }
			return Action.STAND;
		});
		
	}
	
	@Override
	public Action play(Card dealer, Hand hand) {
		for(BiFunction<Card, Hand, Action> rule : rules) {
			Action action = rule.apply(dealer, hand);
			if (action != null) {
				return action;
			}
		}
		throw new RuntimeException("Could not play with dealer card " + dealer + " and player hand " + hand);
	}

	@Override
	public int getBet(int min, int max) {
		// TODO Auto-generated method stub
		return 0;
	}
}