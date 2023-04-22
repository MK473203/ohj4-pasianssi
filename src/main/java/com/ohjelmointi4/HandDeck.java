package com.ohjelmointi4;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class HandDeck extends Deck {

	public Deck dealDeck;

	public HandDeck(Card[] cards) {
		super(cards);
	}

	@Override
	public boolean canCombineWith(Deck deck2) {
		return false;
	}

	@Override
	public MovableDeck handleMousePress(GamePanel gp, MouseEvent e) {
		if (dealDeck != null) {
			this.dealTo(dealDeck, 1, false);
		}
		return null;
	}

}
