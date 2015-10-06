package com.johnhite.casino.blackjack;

import java.util.List;

import com.johnhite.casino.Card;

public class FreeHand extends Hand {
	private int freeBet;
	
	public FreeHand(Hand other) {
		super(other.hand);
		this.increaseBet(other.getBet());
	}
	public FreeHand(List<Card> cards) {
		super(cards);
	}
	
	public FreeHand(int bet) {
		super(bet);
	}

	public int getFreeBet() {
		return freeBet;
	}

	public void setFreeBet(int freeBet) {
		this.freeBet = freeBet;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FreeHand [hand=").append(hand).append(", score=")
				.append(score).append(", isSoft=").append(isSoft)
				.append(", bet=").append(bet).append(", freeBet=").append(freeBet).append(", isDone=")
				.append(isDone).append("]");
		return builder.toString();
	}
}