package com.ohjelmointi4;

import java.awt.event.MouseEvent;

public class SuitDeck extends Deck {

	@Override
	public boolean canCombineWith(Deck deck2) {
		if (deck2.cards.size() != 1) {
			return false;
		}
		if (this.cards.size() != 0) {
			Card lastOfThisDeck = this.cards.get(this.cards.size() - 1);
			Card firstOfThatDeck = deck2.cards.get(0);
			return !lastOfThisDeck.hidden && (lastOfThisDeck.getSuit() == firstOfThatDeck.getSuit()) && (lastOfThisDeck.getRank() == firstOfThatDeck.getRank() - 1);
		} else {
			return deck2.cards.get(0).getRank() == 0;
		}
	}

	@Override
	public MovableDeck handleMousePress(GamePanel gp, MouseEvent e) {
		return splitAt(this.cards.size() - 1);
	}

}
