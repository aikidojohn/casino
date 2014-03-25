package com.johnhite.casino.blackjack;

import com.johnhite.casino.Card;

public interface DeckListener {

	public void cardDealt(Card c);
	public void shuffle();
}
