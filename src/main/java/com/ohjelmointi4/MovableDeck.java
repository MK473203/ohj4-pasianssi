package com.ohjelmointi4;

public class MovableDeck extends Deck {

	public Deck startDeck;

	public boolean selected = true;

	public MovableDeck(Card[] cards) {
		super(cards);
	}

	@Override
	public boolean canCombineWith(Deck deck2) {
		return false;
	}

}
