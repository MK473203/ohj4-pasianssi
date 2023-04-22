package com.ohjelmointi4;

import java.awt.event.MouseEvent;

public class SuitDeck extends Deck {

	@Override
	public boolean canCombineWith(Deck deck2) {
		if (deck2.getCards().size() != 1) {
			return false;
		}
		Card lastOfThisDeck = this.getCards().get(this.getCards().size() - 1);
		Card firstOfThatDeck = deck2.getCards().get(0);
		return !lastOfThisDeck.hidden && (lastOfThisDeck.getSuit() == firstOfThatDeck.getSuit()) && (lastOfThisDeck.getRank() == firstOfThatDeck.getRank() - 1);
	}

	@Override
	public MovableDeck handleMousePress(GamePanel gp, MouseEvent e) {
		return splitAt(this.cards.size() - 1);
	}

}
