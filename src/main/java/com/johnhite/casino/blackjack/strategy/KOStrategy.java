package com.johnhite.casino.blackjack.strategy;

import com.johnhite.casino.Card;
import com.johnhite.casino.Card.Ordinal;
import com.johnhite.casino.blackjack.Action;
import com.johnhite.casino.blackjack.DeckListener;
import com.johnhite.casino.blackjack.Hand;

public class KOStrategy implements BlackjackStrategy, DeckListener {
	private final BlackjackStrategy basic = new BasicStrategy();
	private int count = 0;
	private int numberDecks;
	
	public KOStrategy(int decks) {
		this.numberDecks = decks;
		this.count = getInitialCount();
	}
	
	@Override
	public void cardDealt(Card c) {
		if (c.getValue() >= 2 && c.getValue() <= 7) {
			count++;
		}
		else if (c.getValue() == 10 || c.getOrdinal() == Ordinal.ACE) {
			count--;
		}
	}

	@Override
	public void shuffle() {
		this.count = getInitialCount();
	}

	@Override
	public Action play(Card dealer, Hand hand) {
		return basic.play(dealer, hand);
	}

	@Override
	public int getBet(int min, int max) {
		if (count <= -4) {
			return min;
		}
		if (count == -3) {
			return min *2;
		}
		if (count == -2) {
			return min * 4;
		}
		if (count <= 0) {
			return min * 5;
		}
		if (count <= 3) {
			return min * 10;
		}
		if (count <= 5) {
			return min * 15;
		}
		if (count <= 6) {
			return min * 20;
		}
		return min;
	}
	
	public int getInitialCount() {
		if (numberDecks == 1) {
			return -1;
		}
		if (numberDecks == 2) {
			return -5;
		}
		if (numberDecks == 6) {
			return -20;
		}
		if (numberDecks == 8) {
			return -27;
		}
		
		return (int)(-3.4 * numberDecks);
	}
}
