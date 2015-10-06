package com.johnhite.casino;


public class Card {
	public static enum Suit {
		CLUB(2, 'C'),
		DIAMOND(1, 'D'),
		HEART(0, 'H'),
		SPADE(3, 'S');
		private final char symbol;
		private final int id;
		private Suit(int id, char symbol) {
			this.id = id;
			this.symbol = symbol;
		}
		
		public static Suit getById(int id) {
			for (Suit s: Suit.values()) {
				if (id == s.id) {
					return s;
				}
			}
			throw new RuntimeException("invalid suit id");
		}
		
		public String getSymbol() {
			return "_" + symbol;
		}
	}
	public static enum Ordinal {
		ACE(0),
		TWO(1),
		THREE(2),
		FOUR(3),
		FIVE(4),
		SIX(5),
		SEVEN(6),
		EIGHT(7),
		NINE(8),
		TEN(9),
		JACK(10),
		QUEEN(11),
		KING(12);
		
		private final int rank;
		private Ordinal(int rank) {
			this.rank = rank;
		}
		
		public int getRank() {
			return rank + 1;
		}
		
		public static Ordinal getById(int id) {
			for (Ordinal s: Ordinal.values()) {
				if (id == s.rank) {
					return s;
				}
			}
			throw new RuntimeException("invalid ordinal id");
		}
		
		public static Ordinal getByValue(int value) {
			return getById(value -1);
		}
	}
	
	private final int cardId;
	private final Suit suit;
	private final Ordinal ordinal;
	
	public Card(int card) {
		this.cardId = card;
		this.suit = Suit.getById(Card.getSuit(card));
		this.ordinal = Ordinal.getById(Card.getOrdinal(card));
	}
	
	public Card(Ordinal ordinal, Suit suit) {
		this.cardId = ordinal.getRank() + suit.id * 13;
		this.suit = suit;
		this.ordinal = ordinal;
	}
	
	public Card(int ordinal, Suit suit) {
		this.ordinal = Ordinal.getByValue(ordinal);
		this.cardId = this.ordinal.getRank() + suit.id * 13;
		this.suit = suit;
	}

	public int getCardId() {
		return cardId;
	}

	public Suit getSuit() {
		return suit;
	}

	public Ordinal getOrdinal() {
		return ordinal;
	}
	
	public int getValue() {
		if (ordinal.getRank() > 10) {
			return 10;
		}
		return ordinal.getRank();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cardId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (cardId != other.cardId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (ordinal == Ordinal.ACE) {
			builder.append("A");
		}
		else if (ordinal == Ordinal.JACK) {
			builder.append("J");
		}
		else if (ordinal == Ordinal.QUEEN) {
			builder.append("Q");
		}
		else if (ordinal == Ordinal.KING) {
			builder.append("K");
		} else {
			builder.append(ordinal.getRank());
		}
		builder.append(suit.getSymbol());
		return builder.toString();
	}


	public static int getSuit(int card) {
		return card / 13;
	}
	
	public static int getOrdinal(int card) {
		return card % 13;
	}
	
	public static void main(String[] args) {
		for (int i=0; i < 52; i++) {
			System.out.println(new Card(i));
		}
	}
}
