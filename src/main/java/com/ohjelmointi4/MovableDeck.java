package com.ohjelmointi4;

public class MovableDeck extends Deck {

	public Deck startDeck;

	public MovableDeck(Card[] cards) {
		super(cards);
	}

	@Override
	public boolean canCombineWith(Deck deck2) {
		return false;
	}

}
