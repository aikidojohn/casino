package com.johnhite.casino;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

public class Deck implements Iterable<Card> {
	private final List<Card> cards = Lists.newArrayList();
	private final int numberDecks;
	private int index = 0;
	private int shuffleMarker = 0;
	
	public Deck(int numberDecks) {
		this.numberDecks = numberDecks;
		List<Card> deck = Lists.newArrayList();
		for (int i = 0; i < 52; i++) {
			deck.add(new Card(i));
		}
		for (int i = 0; i < numberDecks; i++) {
			cards.addAll(deck);
		}
	}
	
	public void shuffle() {
		Shuffle s = (Deck d) -> { 
			final SecureRandom rand = new SecureRandom();
			for (int i= d.cards.size()-1; i > 0; i--) {
				int a = (int)(rand.nextDouble()*i);
				Card tmp = d.cards.get(a);
				d.cards.set(a, d.cards.get(i));
				d.cards.set(i, tmp);
			}
		};
		shuffle(s);
	}
	
	public void shuffle(Shuffle s) {
		s.shuffle(this);
		//deal index reset to 0;
		this.index = 0;
		//put the shuffle marker in the bottom quarter
		this.shuffleMarker = cards.size() - (int)(Math.random() * (cards.size() / 4));
	}
	
	public Optional<Card> dealOptional() {
		if (index == shuffleMarker || index >= cards.size()) {
			return Optional.absent();
		}
		
		Card c = cards.get(index++);
		return Optional.of(c);
	}
	
	public Card deal() {
		if (index == shuffleMarker || index >= cards.size()) {
			shuffle();
		}
		
		Card c = cards.get(index++);
		return c;
	}
	
	public void setShuffleMarker(int index) {
		this.shuffleMarker = index;
	}
	
	public boolean atShuffleMarker() {
		return index == shuffleMarker;
	}
	
	public int getNumberDecks() {
		return this.numberDecks;
	}
	
	@Override
	public Iterator<Card> iterator() {
		return new Iterator<Card>() {

			@Override
			public boolean hasNext() {
				return !atShuffleMarker();
			}

			@Override
			public Card next() {
				Optional<Card> card = dealOptional();
				if (card.isPresent()) {
					return card.get();
				}
				throw new NoSuchElementException();
			}
			
		};
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Deck [cards=").append(cards).append(",\n index=")
				.append(index).append(", shuffleMarker=").append(shuffleMarker)
				.append("]");
		return builder.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Deck d = new Deck(3);
		d.shuffle();
		System.out.println(d);
		int dealt = 0;
		for (Card c : d) {
			System.out.println(c);
			dealt++;
		}
		System.out.println(dealt);
	}

}
