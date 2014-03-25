package com.johnhite.casino.blackjack.strategy;

import java.util.List;

import com.google.common.collect.Lists;
import com.johnhite.casino.Card;
import com.johnhite.casino.Card.Ordinal;
import com.johnhite.casino.Card.Suit;
import com.johnhite.casino.blackjack.Action;
import com.johnhite.casino.blackjack.Hand;

public class StrategyPrinter {

	public static void printStrategy(BlackjackStrategy strategy) {
		List<Card> dealerCards = Lists.newArrayList(new Card[]{new Card(2, Suit.HEART), new Card(3, Suit.HEART), new Card(4, Suit.HEART), new Card(5, Suit.HEART),
				new Card(6, Suit.HEART), new Card(7, Suit.HEART),new Card(8, Suit.HEART),new Card(9, Suit.HEART),new Card(10, Suit.HEART),new Card(1, Suit.HEART)});
		
		System.out.print(" ");
		for (Card dealerCard : dealerCards) {
			System.out.print(String.valueOf(dealerCard.getValue()) + " ");
		}
		
		System.out.println();
		for (int i = 20; i >= 5; i--) {
			Hand hand = getHardHand(i);
			System.out.print(i + " ");
			for (Card dealerCard : dealerCards) {
				Action action = strategy.play(dealerCard, hand);
				System.out.print(action + " ");
			}
			System.out.println();
		}
		
		System.out.println();
		for (int i= 20; i >= 12; i--) {
			Hand hand = getSoftHand(i);
			System.out.print(i + " ");
			for (Card dealerCard : dealerCards) {
				Action action = strategy.play(dealerCard, hand);
				System.out.print(action + " ");
			}
			System.out.println();
		}
		
		System.out.println();
		for (int i= 10; i >= 1; i--) {
			Hand hand = getPairHand(i);
			System.out.print(i + " ");
			for (Card dealerCard : dealerCards) {
				Action action = strategy.play(dealerCard, hand);
				System.out.print(action + " ");
			}
			System.out.println();
		}
		
	}
	
	public static Hand getHardHand(int value) {
		Hand hand = new Hand(0);
		if (value <= 10) {
			hand.addCard(new Card(value, Suit.HEART));
		}
		else if (value >= 11 && value <= 12) {
			hand.addCard(new Card(9, Suit.HEART));
			hand.addCard(new Card(value - 9, Suit.HEART));
		}
		else if (value >= 13 && value <= 20) {
			hand.addCard(new Card(Ordinal.KING, Suit.HEART));
			hand.addCard(new Card(value - 10, Suit.HEART));
		} else {
			hand.addCard(new Card(10, Suit.HEART));
			hand.addCard(new Card(10, Suit.HEART));
			hand.addCard(new Card(value - 20, Suit.HEART));
		}
	    
		return hand;
	}
	
	public static Hand getSoftHand(int value) {
		Hand hand = new Hand(0);
		hand.addCard(new Card(1, Suit.HEART));
		hand.addCard(new Card(value - 11, Suit.HEART));
	    
		return hand;
	}
	
	public static Hand getPairHand(int value) {
		Hand hand = new Hand(0);
		hand.addCard(new Card(value, Suit.HEART));
		hand.addCard(new Card(value, Suit.HEART));
	    
		return hand;
	}
}
