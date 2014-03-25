package com.johnhite.casino.blackjack.strategy;

import java.util.List;
import java.util.function.BiFunction;

import com.google.common.collect.Lists;
import com.johnhite.casino.Card;
import com.johnhite.casino.Card.Ordinal;
import com.johnhite.casino.blackjack.Action;
import com.johnhite.casino.blackjack.DeckListener;
import com.johnhite.casino.blackjack.Hand;

public class HighLowStrategy implements BlackjackStrategy, DeckListener {
	private final BlackjackStrategy basic = new BasicStrategy();
	private List<BiFunction<Card, Hand, Action>> rules = Lists.newArrayList();
	private int count = 0;
	private int cardsSeen = 0;
	private int maxBet;
	private int numberDecks;
	
	public HighLowStrategy(int maxBet, int numberDecks) {
		this.maxBet = maxBet;
		this.numberDecks = numberDecks;
		
		//modify basic strategy based on true count
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (dealerCard.getValue() == 10 && hand.getScore() == 16) {
				return (getTrueCount() >= 0) ? Action.STAND : Action.HIT;
			}
			return null;
		});
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (dealerCard.getValue() == 10 && hand.getScore() == 15) {
				return (getTrueCount() >= 4) ? Action.STAND : Action.HIT;
			}
			return null;
		});
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (dealerCard.getValue() == 5 && hand.isPair() && hand.getScore() == 20) {
				return (getTrueCount() >= 5) ? Action.SPLIT : Action.STAND;
			}
			return null;
		});
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (dealerCard.getValue() == 6 && hand.isPair() && hand.getScore() == 20) {
				return (getTrueCount() >= 4) ? Action.SPLIT : Action.STAND;
			}
			return null;
		});
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (dealerCard.getValue() == 10 && hand.getScore() == 10) {
				return (getTrueCount() >= 4) ? Action.DOUBLE : Action.HIT;
			}
			return null;
		});
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (dealerCard.getValue() == 3 && hand.getScore() == 12) {
				return (getTrueCount() >= 2) ? Action.STAND : Action.HIT;
			}
			return null;
		});
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (dealerCard.getValue() == 2 && hand.getScore() == 12) {
				return (getTrueCount() >= 3) ? Action.STAND : Action.HIT;
			}
			return null;
		});
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (dealerCard.getOrdinal() == Ordinal.ACE && hand.getScore() == 11) {
				return (getTrueCount() >= 1) ? Action.DOUBLE : Action.HIT;
			}
			return null;
		});
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (dealerCard.getValue() == 2 && hand.getScore() == 9) {
				return (getTrueCount() >= 1) ? Action.DOUBLE : Action.HIT;
			}
			return null;
		});
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (dealerCard.getOrdinal() == Ordinal.ACE && hand.getScore() == 10) {
				return (getTrueCount() >= 4) ? Action.DOUBLE : Action.HIT;
			}
			return null;
		});
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (dealerCard.getValue() == 7 && hand.getScore() == 9) {
				return (getTrueCount() >= 3) ? Action.DOUBLE : Action.HIT;
			}
			return null;
		});
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (dealerCard.getValue() == 9 && hand.getScore() == 16) {
				return (getTrueCount() >= 5) ? Action.STAND : Action.HIT;
			}
			return null;
		});
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (dealerCard.getValue() == 2 && hand.getScore() == 13) {
				return (getTrueCount() >= -1) ? Action.STAND : Action.HIT;
			}
			return null;
		});
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (dealerCard.getValue() == 4 && hand.getScore() == 12) {
				return (getTrueCount() >= 0) ? Action.STAND : Action.HIT;
			}
			return null;
		});
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (dealerCard.getValue() == 5 && hand.getScore() == 12) {
				return (getTrueCount() >= -2) ? Action.STAND : Action.HIT;
			}
			return null;
		});
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (dealerCard.getValue() == 6 && hand.getScore() == 12) {
				return (getTrueCount() >= -1) ? Action.STAND : Action.HIT;
			}
			return null;
		});
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (dealerCard.getValue() == 3 && hand.getScore() == 13) {
				return (getTrueCount() >= -2) ? Action.STAND : Action.HIT;
			}
			return null;
		});
		
		//surrenders
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (dealerCard.getValue() == 10 && hand.getScore() == 14 ) {
				return (getTrueCount() >= 3) ? Action.SURRENDER : null;
			}
			return null;
		});
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (dealerCard.getValue() == 10 && hand.getScore() == 15 ) {
				return (getTrueCount() >= 0) ? Action.SURRENDER : null;
			}
			return null;
		});
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (dealerCard.getValue() == 9 && hand.getScore() == 15 ) {
				return (getTrueCount() >= 2) ? Action.SURRENDER : null;
			}
			return null;
		});
		rules.add( (Card dealerCard, Hand hand) -> { 
			if (dealerCard.getOrdinal() == Ordinal.ACE && hand.getScore() == 15 ) {
				return (getTrueCount() >= 1) ? Action.SURRENDER : null;
			}
			return null;
		});
	}
	
	@Override
	public void cardDealt(Card c) {
		if (c.getValue() >= 2 && c.getValue() <= 6) {
			count++;
		}
		else if (c.getOrdinal() == Ordinal.ACE || c.getValue() == 10) {
			count--;
		}
		cardsSeen++;
	}

	@Override
	public void shuffle() {
		count = 0;
		cardsSeen = 0;
	}

	@Override
	public Action play(Card dealer, Hand hand) {
		for(BiFunction<Card, Hand, Action> rule : rules) {
			Action action = rule.apply(dealer, hand);
			if (action != null) {
				return action;
			}
		}
		return basic.play(dealer, hand);
	}

	@Override
	public int getBet(int min, int max) {
		double multiplier = getTrueCount();
		multiplier *= multiplier;
		int bet = min;
		if (multiplier > 1) {
			bet *= multiplier;
		}
		return bet;
	}
	
	public double getTrueCount() {
		return count / (numberDecks - cardsSeen/52);
	}
}
