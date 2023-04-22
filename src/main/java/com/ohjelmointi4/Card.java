package com.ohjelmointi4;

public class Card {

	public static final int HEART = 0;
	public static final int SPADE = 1;
	public static final int DIAMOND = 2;
	public static final int CLUB = 3;

	public boolean hidden = false;

	private int suit;
	private int rank;

	public Card(int suit, int rank) {
		setSuit(suit);
		setRank(rank);
	}

	public int getSuit() {
		return suit;
	}

	public void setSuit(int suit) {
		this.suit = Math.max(0, Math.min(3, suit));
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = Math.max(0, Math.min(12, rank));
	}

	public static Card[] getAllCards() {

		Card[] cards = new Card[52];
		for (int suit = 0; suit < 4; suit++) {
			for (int rank = 0; rank < 13; rank++) {
				cards[suit * 13 + rank] = new Card(suit, rank);
			}
		}

		return cards;
	}

}
