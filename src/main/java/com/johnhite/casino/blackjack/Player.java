package com.johnhite.casino.blackjack;

import java.util.List;

import com.google.common.collect.Lists;
import com.johnhite.casino.Card;
import com.johnhite.casino.blackjack.strategy.BlackjackStrategy;

public class Player {
	private List<Hand> hands = Lists.newArrayList();
	private int money;
	private BlackjackStrategy strategy;
	
	public Player(int money, BlackjackStrategy strategy) {
		this.money = money;
		this.strategy = strategy;
	}
	
	public List<Hand> getHands() {
		return hands;
	}
	
	public int getMoney() {
		return this.money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	
	public int ante(int min, int max) {
		int bet = strategy.getBet(min, max);
		if (money >= bet) {
			money -= bet;
			hands.clear();
			hands.add(new Hand(bet));
			return bet;
		}
		return 0;
	}
	
	public void pay(int amount) {
		money += amount;
	}
	
	public void doubleBet(Hand h) {
		int bet = h.getBet();
		this.money -= bet;
		h.increaseBet(bet);
		h.setDone(true);
	}
	
	public Hand[] splitHand(Hand h) {
		Hand newHand = new Hand(h.getBet());
		newHand.addCard(h.removeCardAt(0));
		
		this.hands.add(newHand);
		this.money -= h.getBet();
		return new Hand[]{ h, newHand };
	}
	
	public void surrender(Hand h) {
		this.hands.remove(h);
		this.money += h.getBet() / 2;
		h.setDone(true);
	}
	
	public void notifyResult(Hand dealerHand, Hand playerHand, int amountWon) {
		strategy.notifyResult(dealerHand, playerHand, amountWon);
	}
	
	public Action getAction(Card dealerCard, Hand hand) {
		Action action = strategy.play(dealerCard, hand);
		
		return action;
	}
}