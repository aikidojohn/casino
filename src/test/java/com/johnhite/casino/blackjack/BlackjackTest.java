package com.johnhite.casino.blackjack;

import static org.mockito.Mockito.*;

import java.util.List;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.johnhite.casino.Card;
import com.johnhite.casino.Card.Suit;
import com.johnhite.casino.Deck;
import com.johnhite.casino.blackjack.strategy.BasicStrategy;

public class BlackjackTest {
	Deck deck;
	Player player;
	Blackjack game;
	
	@Before
	public void setup() {
		deck = Mockito.mock(Deck.class);
		player = new Player(10000, new BasicStrategy());
		List<Player> players = Lists.newArrayList(player);
		game = new Blackjack(deck, players);
	}
	
	@Test
	public void testPlayerBlackJack() {
		when(deck.deal()).thenReturn(new Card(1, Suit.HEART), new Card(2, Suit.HEART), new Card(10, Suit.HEART), new Card(2, Suit.HEART));
		game.playRound();
		
		assertEquals(player.getMoney(), 10007);
	}
	
	@Test
	public void testPlayerAndDealerBlackJack() {
		when(deck.deal()).thenReturn(new Card(1, Suit.HEART), new Card(1, Suit.HEART), new Card(10, Suit.HEART), new Card(10, Suit.HEART));
		game.playRound();
		
		assertEquals(player.getMoney(), 10000);
	}
	
	@Test
	public void testDealerBlackJack() {
		when(deck.deal()).thenReturn(new Card(2, Suit.HEART), new Card(1, Suit.HEART), new Card(2, Suit.HEART), new Card(10, Suit.HEART));
		game.playRound();
		
		assertEquals(player.getMoney(), 9995);
	}
	
	@Test
	public void testDealerBust() {
		when(deck.deal()).thenReturn(new Card(10, Suit.HEART), new Card(8, Suit.HEART), new Card(10, Suit.HEART), new Card(8, Suit.HEART), new Card(8, Suit.HEART));
		game.playRound();
		
		assertEquals(player.getMoney(), 10005);
	}
	
	@Test
	public void testPlayerBust() {
		when(deck.deal()).thenReturn(new Card(5, Suit.HEART), new Card(7, Suit.HEART), new Card(7, Suit.HEART), new Card(10, Suit.HEART), new Card(10, Suit.HEART));
		game.playRound();
		
		assertEquals(player.getMoney(), 9995);
	}
}
