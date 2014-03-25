package com.johnhite.casino.blackjack.strategy;

import com.johnhite.casino.Card;
import com.johnhite.casino.Card.Ordinal;
import com.johnhite.casino.blackjack.Action;
import com.johnhite.casino.blackjack.DeckListener;
import com.johnhite.casino.blackjack.Hand;

public class AceFiveStrategy implements BlackjackStrategy, DeckListener {
	private final BlackjackStrategy basic = new BasicStrategy();
	private int count = 0;
	private int maxBet;
	private int lastBet;
	
	public AceFiveStrategy(int maxBet) {
		this.maxBet = maxBet;
	}
	
	@Override
	public void cardDealt(Card c) {
		if (c.getOrdinal() == Ordinal.FIVE) {
			count++;
		}
		else if (c.getOrdinal() == Ordinal.ACE) {
			count--;
		}
	}

	@Override
	public void shuffle() {
		count = 0;
		lastBet = 0;
	}

	@Override
	public Action play(Card dealer, Hand hand) {
		return basic.play(dealer, hand);
	}

	@Override
	public int getBet(int min, int max) {
		if (count >= 2) {
			if (lastBet * 2 <= maxBet) {
				lastBet *= 2;
			}
		} else  {
			lastBet = min;
		}
		return lastBet;
	}
}
