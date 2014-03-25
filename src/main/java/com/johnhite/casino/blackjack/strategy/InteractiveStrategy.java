package com.johnhite.casino.blackjack.strategy;

import java.util.Scanner;

import com.johnhite.casino.Card;
import com.johnhite.casino.blackjack.Action;
import com.johnhite.casino.blackjack.DeckListener;
import com.johnhite.casino.blackjack.Hand;

public class InteractiveStrategy implements BlackjackStrategy, DeckListener {
	private Scanner scanner = new Scanner(System.in);
	
	@Override
	public Action play(Card dealer, Hand hand) {
		System.out.println();
		System.out.println("Dealer: " + dealer.getOrdinal() + " Player: " + hand.toString());
		System.out.println("Action: ");
		String actionCode = scanner.next();
		switch(actionCode) {
		case "H":
			return Action.HIT;
		case "S":
			return Action.STAND;
		case "D":
			return Action.DOUBLE;
		case "P":
			return Action.SPLIT;
		case "X":
			return Action.SURRENDER;
		}
		return Action.STAND;
	}

	@Override
	public int getBet(int min, int max) {
		System.out.println();
		System.out.print("Bet: ");
		int bet = scanner.nextInt();
		System.out.println();
		return bet;
	}
	
	@Override
	public  void notifyResult(Hand dealerHand, Hand playerHand, int amountWon) {
		System.out.println();
		System.out.println("Dealer: " + dealerHand);
		System.out.println("Player: " + playerHand);
		System.out.println("Win/Lose " + amountWon);
	}

	@Override
	public void cardDealt(Card c) {
	}

	@Override
	public void shuffle() {
	}
	

}
