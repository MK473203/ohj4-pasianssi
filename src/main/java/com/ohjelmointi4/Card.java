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
	
}
