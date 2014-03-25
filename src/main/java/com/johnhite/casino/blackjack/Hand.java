package com.johnhite.casino.blackjack;

import java.util.List;

import com.google.common.collect.Lists;
import com.johnhite.casino.Card;
import com.johnhite.casino.Card.Ordinal;

public class Hand implements Comparable<Hand> {
	private List<Card> hand = Lists.newArrayList();
	private int score;
	private boolean isSoft;
	private int bet;
	private boolean isDone;
	
	public Hand(List<Card> cards) {
		this.hand = cards;
		computeScore();
	}
	
	public Hand(int bet) {
		this.bet = bet;
	}
	
	public void addCard(Card c) {
		hand.add(c);
		computeScore();
	}
	
	public void increaseBet(int amount) {
		this.bet += amount;
	}
	
	public int getScore() {
		return score;
	}
	
	public boolean isBust() {
		return getScore() > 21;
	}
	
	public boolean isPair() {
		return (hand.size() == 2) && (hand.get(0).getOrdinal() == hand.get(1).getOrdinal());
	}
	
	public boolean isSoft() {
		return this.isSoft;
	}
	
	public boolean isBlackjack() {
		if (hand.size() == 2) {
			boolean ace = false;
			boolean ten = false;
			for (Card c : hand) {
				if (c.getOrdinal() == Ordinal.ACE) {
					ace = true;
				}
				if (c.getValue() == 10) {
					ten = true;
				}
			}
			return ace & ten;
		}
		return false;
	}
	
	public Card getCardAt(int index) {
		if (index >= 0 && index < hand.size()) {
			return hand.get(index);
		}
		return null;
	}
	
	public Card removeCardAt(int index) {
		return hand.remove(index);
	}
	
	public int getNumberCards() {
		return hand.size();
	}
	
	public int getBet() {
		return bet;
	}
	
	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

	private void computeScore() {
		int score = 0;
		int aces = 0;
		for (Card c : hand) {
			score += c.getValue();
			if (c.getOrdinal() == Ordinal.ACE) {
				aces++;
			}
		}
		//soft if there is an ace and the score minus any aces is less than 10 (at least one ace is counted as 11)
		this.isSoft = (aces > 0 && (score - aces) < 10);
		
		//Expand aces if necessary
		while (score <= 11 && aces > 0) {
			score += 10;
			aces--;
		}
		this.score = score;
	}

	@Override
	public int compareTo(Hand o) {
		if (this.isBlackjack() && o.isBlackjack()) {
			return 0;
		}
		if (this.isBlackjack()) {
			return 1;
		}
		if (o.isBlackjack()) {
			return -1;
		}
		
		if (this.isBust() && !o.isBust()) {
			return -1;
		}
		if (!this.isBust() && o.isBust()) {
			return 1;
		}
		
		if (this.getScore() == o.getScore()) {
			return 0;
		}
		
		if (this.getScore() > o.getScore()) {
			return 1;
		}
		else {
			return -1;
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Hand [hand=").append(hand).append(", score=")
				.append(score).append(", isSoft=").append(isSoft)
				.append(", bet=").append(bet).append(", isDone=")
				.append(isDone).append("]");
		return builder.toString();
	}
}