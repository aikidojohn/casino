package com.johnhite.casino.blackjack;

import java.util.List;

import com.google.common.collect.Lists;
import com.johnhite.casino.Card;
import com.johnhite.casino.Deck;
import com.johnhite.casino.blackjack.strategy.BasicStrategy;
import com.johnhite.casino.blackjack.strategy.DealerH17Strategy;
import com.johnhite.casino.blackjack.strategy.GeneticLearningStrategy;
import com.johnhite.casino.blackjack.strategy.InteractiveStrategy;
import com.johnhite.casino.blackjack.strategy.KOStrategy;
import com.johnhite.casino.blackjack.strategy.NeuralStrategy;
import com.johnhite.casino.blackjack.strategy.StrategyPrinter;
import com.johnhite.casino.nn.GeneticAlgorithm;
import com.johnhite.casino.nn.NeuralNetwork;

public class Blackjack {
	private final Deck deck;
	private List<Player> players = Lists.newArrayList();
	private int minBet = 5;
	private int maxBet = 100;
	private List<DeckListener> listeners = Lists.newArrayList();
	
	public Blackjack(Deck deck, List<Player> players) {
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
						case SPLIT:
							Hand[] hands = p.splitHand(h);
							for (Hand newHand : hands) {
								newHand.addCard(deal());
							}
							//add new Hand
							handsToPlay.add(hands[1]);
							break;
						case SURRENDER:
							p.surrender(h);
							p.notifyResult(dh, h, -1 * (h.getBet()/2));
							break;
						default:
							break;
						}
					} while (playerAction != Action.STAND && playerAction != Action.DOUBLE && playerAction != Action.SURRENDER && !h.isBust());
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
				} else if (dh.compareTo(h) == 0) {
					p.pay(h.getBet());
					p.notifyResult(dh, h, h.getBet());
				} else if (h.isBlackjack()) {
					p.pay(h.getBet() + (int)(h.getBet() * 1.5));
					p.notifyResult(dh, h, (int)(h.getBet() * 1.5));
				} else if (dh.compareTo(h) < 0) {
					p.pay(h.getBet() * 2);
					p.notifyResult(dh, h, h.getBet());
				} else {
					p.notifyResult(dh, h, -1 * h.getBet());
				}
			}
		}
	}
	
	public static List<Double> genetic() {
		List<NeuralNetwork> brains = Lists.newArrayList();
		List<Blackjack> games = Lists.newArrayList();
		List<GeneticLearningStrategy> strategies = Lists.newArrayList();
		List<Player> allPlayers =Lists.newArrayList();
		for (int i=0; i < 100; i++) {
			Deck deck = new Deck(8);
			deck.shuffle();
			
			List<Player> players = Lists.newArrayList();
			NeuralNetwork b1 = new NeuralNetwork(5,1,1,20);
			NeuralNetwork b2 = new NeuralNetwork(5,1,1,20);
			brains.add(b1);
			brains.add(b2);
			GeneticLearningStrategy p1 = new GeneticLearningStrategy(b1);
			GeneticLearningStrategy p2 = new GeneticLearningStrategy(b2);
			strategies.add(p1);
			strategies.add(p2);
			players.add(new Player(Integer.MAX_VALUE/2, p1));
			players.add(new Player(Integer.MAX_VALUE/2, p2));
			allPlayers.addAll(players);
			
			Blackjack game = new Blackjack(deck, players);
			games.add(game);
		}

		GeneticAlgorithm ga = new GeneticAlgorithm();
		ga.setPopulation(strategies);
		
		//play 1000 hands 
		for (int i=0; i< 50000; i++) {
			if (i > 0 && i % 1000 == 0) {
				ga.epoch();
				System.out.println("Starting generation " + i + ". Best So Far: " + ga.getBestSoFar().getFitness() + " Best This Round: " + ga.getBestLastRound().getFitness() + " Average Fitness: " + ga.getAverageFitness());
				
				for (Player player : allPlayers) {
					player.setMoney(Integer.MAX_VALUE/2);
				}
			}
			for (Blackjack game : games) {
				game.playRound();
			}
		}
		
		//print out best chromosome
		System.out.println("Best Found: " + ga.getBestSoFar());
		System.out.println("Fitness: " + ga.getBestSoFar().getFitness());
		System.out.println("Best Last Round: " + ga.getBestLastRound());
		System.out.println();
		return ga.getBestSoFar().getGenes();
	}
	
	public static void strategyTest() {
		Deck deck = new Deck(8);
		deck.shuffle();
		
		List<Player> players = Lists.newArrayList();
		players.add(new Player(10000, new BasicStrategy()));
		players.add(new Player(10000, new BasicStrategy()));
		//AceFiveStrategy p1 = new AceFiveStrategy(32*5);
		//AceFiveStrategy p2 = new AceFiveStrategy(32*5);
		//HighLowStrategy p1 = new HighLowStrategy(32*5, 8);
		//HighLowStrategy p2 = new HighLowStrategy(32*5, 8);
		KOStrategy p1 = new KOStrategy(8);
		KOStrategy p2 = new KOStrategy(8);
		players.add(new Player(10000, p1));
		players.add(new Player(10000, p2));
		
		Blackjack game = new Blackjack(deck, players);
		game.addDeckListener(p1);
		game.addDeckListener(p2);
		for (int i=0; i< 1000; i++) {
			game.playRound();
		}
		
		for (Player p : players) {
			System.out.println(p.getMoney());
		}
	}
	
	public static void testNeuralStrategy(List<Double> weights) {
		Deck deck = new Deck(8);
		deck.shuffle();
		
		List<Player> players = Lists.newArrayList();
		players.add(new Player(100000, new BasicStrategy()));
		players.add(new Player(100000, new BasicStrategy()));
		NeuralStrategy p1 = new NeuralStrategy(weights);
		NeuralStrategy p2 = new NeuralStrategy(weights);
		players.add(new Player(100000, p1));
		players.add(new Player(100000, p2));
		
		Blackjack game = new Blackjack(deck, players);
		for (int i=0; i< 5000; i++) {
			game.playRound();
		}
		
		for (Player p : players) {
			System.out.println(p.getMoney());
		}
	}
	
	public static void interactivePlay() {
		Deck deck = new Deck(8);
		deck.shuffle();
		
		InteractiveStrategy strategy = new InteractiveStrategy();
		List<Player> players = Lists.newArrayList();
		players.add(new Player(10000, strategy));
		
		Blackjack game = new Blackjack(deck, players);
		game.addDeckListener(strategy);
		for (int i=0; i< 50; i++) {
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
		List<Double> weights = genetic();
		System.out.println("Testing\n");
		testNeuralStrategy(weights);
		System.out.println("\nStrategy\n");
		StrategyPrinter.printStrategy( new NeuralStrategy(weights) );
		
		//interactivePlay();
		
		//printStrategy( new BasicStrategy() );
	}
	

}
