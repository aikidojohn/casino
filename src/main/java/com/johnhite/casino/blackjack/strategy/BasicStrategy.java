package com.johnhite.casino.blackjack.strategy;

import java.util.List;
import java.util.function.BiFunction;

import com.google.common.collect.Lists;
import com.johnhite.casino.Card;
import com.johnhite.casino.Card.Ordinal;
import com.johnhite.casino.blackjack.Action;
import com.johnhite.casino.blackjack.Hand;

public class BasicStrategy implements BlackjackStrategy {
	private List<BiFunction<Card, Hand, Action>> rules = Lists.newArrayList();
	
	public BasicStrategy() {

		//Pair rules
		//Always split aces
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (hand.isPair() && hand.getCardAt(0).getOrdinal() == Ordinal.ACE) { return Action.SPLIT; } 
			return null;
		});
		//never split 10's
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (hand.isPair() && hand.getCardAt(0).getValue() == 10) { return Action.STAND; } 
			return null;
		});
		//9's
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (hand.isPair() && hand.getCardAt(0).getValue() == 9) {
				final int dv = dealerCard.getValue();
				if (dv == 1 || dv == 7 || dv == 10 ) { return Action.STAND; }
				return Action.SPLIT;
			} 
			return null;
		});
		// 8's
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (hand.isPair() && hand.getCardAt(0).getValue() == 8) { return Action.SPLIT; } 
			return null;
		});
		//7's
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (hand.isPair() && hand.getCardAt(0).getValue() == 7) {
				final int dv = dealerCard.getValue();
				if (dv >= 2  && dv <= 7 ) { return Action.SPLIT; }
				return Action.HIT;
			} 
			return null;
		});	
		//6's
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (hand.isPair() && hand.getCardAt(0).getValue() == 6) {
				final int dv = dealerCard.getValue();
				if (dv >= 2  && dv <= 6 ) { return Action.SPLIT; }
				return Action.HIT;
			} 
			return null;
		});
		//5's
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (hand.isPair() && hand.getCardAt(0).getValue() == 5) {
				final int dv = dealerCard.getValue();
				if (dv >= 2  && dv <= 9 ) { return Action.DOUBLE; }
				return Action.HIT;
			} 
			return null;
		});
		//4's
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (hand.isPair() && hand.getCardAt(0).getValue() == 4) {
				final int dv = dealerCard.getValue();
				if (dv == 5  || dv == 6 ) { return Action.SPLIT; }
				return Action.HIT;
			} 
			return null;
		});
		//3's and 2's
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (hand.isPair() && (hand.getCardAt(0).getValue() == 3 || hand.getCardAt(0).getValue() == 2)) {
				final int dv = dealerCard.getValue();
				if (dv >= 2  && dv <= 7 ) { return Action.SPLIT; }
				return Action.HIT;
			} 
			return null;
		});
		
		//always stand on 19 or above
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (hand.getScore() >= 19) { return Action.STAND; } 
			return null;
		});
		
		//Soft totals
		//A, 7 - soft 18
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (hand.isSoft() && hand.getScore() == 18) {
				final int dv = dealerCard.getValue();
				if (dv == 2  || dv == 7 || dv == 8 ) { return Action.STAND; }
				else if (dv >= 3 && dv <= 6) {return Action.DOUBLE; }
				return Action.HIT;
			} 
			return null;
		});
		//A, 6 - soft 17
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (hand.isSoft() && hand.getScore() == 17) {
				final int dv = dealerCard.getValue();
				if (dv >= 3 && dv <= 6) {return Action.DOUBLE; }
				return Action.HIT;
			} 
			return null;
		});
		//A, 4, 5 - soft 15, 16
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (hand.isSoft() && (hand.getScore() == 15 ||  hand.getScore() == 16)) {
				final int dv = dealerCard.getValue();
				if (dv >= 4 && dv <= 6) {return Action.DOUBLE; }
				return Action.HIT;
			} 
			return null;
		});
		//A, 2, 3 - soft 13, 14
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (hand.isSoft() && (hand.getScore() == 13 ||  hand.getScore() == 14)) {
				final int dv = dealerCard.getValue();
				if (dv >= 5 && dv <= 6) {return Action.DOUBLE; }
				return Action.HIT;
			} 
			return null;
		});
		//Hard totals
		//always stand on hard 17 or above
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (hand.getScore() >= 17) { return Action.STAND; } 
			return null;
		});
		//hard 16
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (hand.getScore() == 16) {
				final int dv = dealerCard.getValue();
				if (dv >= 2  && dv <= 6) { return Action.STAND; }
				else if (dv >= 7  && dv <= 8) {return Action.HIT; }
				return Action.SURRENDER;
			} 
			return null;
		});
		//hard 15
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (hand.getScore() == 15) {
				final int dv = dealerCard.getValue();
				if (dv >= 2  && dv <= 6) { return Action.STAND; }
				else if (dv == 10) {return Action.SURRENDER; }
				return Action.HIT;
			} 
			return null;
		});	
		//hard 13,14
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (hand.getScore() == 13 || hand.getScore() == 14) {
				final int dv = dealerCard.getValue();
				if (dv >= 2  && dv <= 6) { return Action.STAND; }
				return Action.HIT;
			} 
			return null;
		});
		//hard 12
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (hand.getScore() == 12) {
				final int dv = dealerCard.getValue();
				if (dv >= 4  && dv <= 6) { return Action.STAND; }
				return Action.HIT;
			} 
			return null;
		});
		//hard 11
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (hand.getScore() == 11) {
				final int dv = dealerCard.getValue();
				if (dv >= 2  && dv <= 10) { return Action.DOUBLE; }
				return Action.HIT;
			} 
			return null;
		});
		//hard 10
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (hand.getScore() == 10) {
				final int dv = dealerCard.getValue();
				if (dv >= 2  && dv <= 9) { return Action.DOUBLE; }
				return Action.HIT;
			} 
			return null;
		});
		//hard 9
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (hand.getScore() == 9) {
				final int dv = dealerCard.getValue();
				if (dv >= 3  && dv <= 6) { return Action.DOUBLE; }
				return Action.HIT;
			} 
			return null;
		});
		//hard 5-8
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (hand.getScore() >= 5 && hand.getScore()  <= 8) { return Action.HIT; } 
			return null;
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
		return min;
	}
}