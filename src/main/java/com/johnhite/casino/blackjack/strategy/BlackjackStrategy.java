package com.johnhite.casino.blackjack.strategy;

import com.johnhite.casino.Card;
import com.johnhite.casino.blackjack.Action;
import com.johnhite.casino.blackjack.Hand;

public interface BlackjackStrategy {
	public Action play(Card dealer, Hand hand);
	public int getBet(int min, int max);
	default void notifyResult(Hand dealerHand, Hand playerHand, int amountWon){}
}