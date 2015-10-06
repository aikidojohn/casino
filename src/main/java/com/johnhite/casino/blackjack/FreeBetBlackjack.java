package com.johnhite.casino.blackjack;

import java.util.List;

import com.google.common.collect.Lists;
import com.johnhite.casino.Card;
import com.johnhite.casino.Deck;
import com.johnhite.casino.blackjack.strategy.DealerH17Strategy;
import com.johnhite.casino.blackjack.strategy.FreeBetInteractiveStrategy;

public class FreeBetBlackjack {
	private final Deck deck;
	private List<Player> players = Lists.newArrayList();
	private int minBet = 5;
	private int maxBet = 100;
	private List<DeckListener> listeners = Lists.newArrayList();
	
	public FreeBetBlackjack(Deck deck, List<Player> players) {
		this.deck = deck;
		this.players = players;
		deck.shuffle();
	}
	
	private Card deal() {
		if (deck.atShuffleMarker()) {
			for (DeckListener l : listeners) {
				l.shuffle();
			}
		}
		
		Card c = deck.deal();
		for (DeckListener l : listeners) {
			l.cardDealt(c);
		}
		return c;
	}
	
	public void addDeckListener(DeckListener listener) {
		listeners.add(listener);
	}
	
	public void playRound() {
		//each player must ante
		List<Player> roundPlayers = Lists.newArrayList();
		for (Player p : players) {
			if (p.ante(minBet, maxBet) > 0) {
				roundPlayers.add(p);
			}
		}
		
		//deal 1 card to all players
		for (Player p : roundPlayers) {
			for (Hand h : p.getHands()) {
				h.addCard(deal());
			}
		}
		//deal a card to the dealer
		List<Card> dealerHand = Lists.newArrayList();
		Hand dh = new Hand(dealerHand);
		dh.addCard(deal());
		
		//deal another card to the players
		for (Player p : roundPlayers) {
			for (Hand h : p.getHands()) {
				h.addCard(deal());
			}
		}
		
		//deal last card to the dealer
		dh.addCard(deal());
		
		if (!dh.isBlackjack()) {
			//TODO handle insurance
			//handle each player hand in turn
			for (Player p : roundPlayers) {
				List<Hand> handsToPlay = Lists.newArrayList(p.getHands());
				while (!handsToPlay.isEmpty()) {
					Hand h = handsToPlay.remove(0);
					Action playerAction = null;
					do  {
						playerAction = p.getAction(dealerHand.get(0), h);
						switch(playerAction) {
						case HIT: 
							h.addCard(deal());
							break;
						case DOUBLE:
							p.doubleBet(h);
							h.addCard(deal());
							break;
						case FREE_DOUBLE:
							p.freeDoubleBet(h).addCard(deal());
							break;
						case SPLIT:
							Hand[] hands = p.splitHand(h);
							for (Hand newHand : hands) {
								newHand.addCard(deal());
							}
							//add new Hand
							handsToPlay.add(hands[1]);
							break;
						case FREE_SPLIT:
							Hand[] freeHands = p.freeSplitHand(h);
							for (Hand newHand : freeHands) {
								newHand.addCard(deal());
							}
							//add new Hand
							handsToPlay.add(freeHands[1]);
							break;
						case SURRENDER:
							p.surrender(h);
							p.notifyResult(dh, h, -1 * (h.getBet()/2));
							break;
						default:
							break;
						}
					} while (playerAction != Action.STAND && playerAction != Action.DOUBLE && playerAction != Action.FREE_DOUBLE && playerAction != Action.SURRENDER && !h.isBust());
				}
			}
			
			//play dealer hand
			DealerH17Strategy dealerStrategy = new DealerH17Strategy();
			Action dealerAction = dealerStrategy.play((Card)null, dh);
			while (!dh.isBust() && dealerAction != Action.STAND) {
				dh.addCard(deal());
				dealerAction = dealerStrategy.play((Card)null, dh);
			}
		} else {
			//Offer insurance;
		}
		
		//compare dealer hand to player hand and pay out winners
		for (Player p : roundPlayers) {
			for (Hand h : p.getHands()) {
				if (h.isBust()) {
					p.notifyResult(dh, h, h.getBet() * -1);
				} else if (dh.getScore() == 22 || dh.compareTo(h) == 0) {
					int payout = pushPayout(h);
					p.pay(payout);
					p.notifyResult(dh, h, payout);
				} else if (h.isBlackjack()) {
					int payout = blackjackPayout(h);
					p.pay(payout);
					p.notifyResult(dh, h, payout);
				} else if (dh.compareTo(h) < 0) {
					int payout = winPayout(h);
					p.pay(payout);
					p.notifyResult(dh, h, payout);
				} else {
					int loss =  -1 * h.getBet();
					p.notifyResult(dh, h, loss);
				}
			}
		}
	}
	
	private int pushPayout(Hand h) {
		return h.getBet();
	}
	
	private int winPayout(Hand h) {
		if (h instanceof FreeHand) {
			return h.getBet() * 2 + ((FreeHand)h).getFreeBet();
		}
		return h.getBet() * 2;
	}
	
	private int blackjackPayout(Hand h) {
		if (h instanceof FreeHand) {
			return ((FreeHand)h).getFreeBet();
		}
		return (int)(h.getBet() + h.getBet() * 1.5);
	}
	
	
	
	public static void interactivePlay() {
		Deck deck = new Deck(8);
		deck.shuffle();
		
		FreeBetInteractiveStrategy strategy = new FreeBetInteractiveStrategy();
		List<Player> players = Lists.newArrayList();
		players.add(new Player(10000, strategy));
		
		FreeBetBlackjack game = new FreeBetBlackjack(deck, players);
		game.addDeckListener(strategy);
		for (int i=0; i< 50; i++) {
			System.out.println(players.get(0).getMoney());
			game.playRound();
		}
		
		for (Player p : players) {
			System.out.println(p.getMoney());
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		interactivePlay();
		
		//printStrategy( new BasicStrategy() );
	}
	

}
