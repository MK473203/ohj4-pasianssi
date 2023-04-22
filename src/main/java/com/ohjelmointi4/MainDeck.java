package com.ohjelmointi4;

import java.awt.event.MouseEvent;

public class MainDeck extends Deck {

	@Override
	public boolean canCombineWith(Deck deck2) {
		if (cards.size() == 0) {
			return deck2.cards.get(0).getRank() == 12;
		}
		Card lastOfThisDeck = this.cards.get(this.cards.size() - 1);
		Card firstOfThatDeck = deck2.cards.get(0);
		return !lastOfThisDeck.hidden && (lastOfThisDeck.getSuit() % 2 != firstOfThatDeck.getSuit() % 2) && (lastOfThisDeck.getRank() == firstOfThatDeck.getRank() + 1);
	}

	@Override
	public MovableDeck handleMousePress(GamePanel gp, MouseEvent e) {
		return gp.splitDeckAtPos(this, e.getX(), e.getY());
	}

}
