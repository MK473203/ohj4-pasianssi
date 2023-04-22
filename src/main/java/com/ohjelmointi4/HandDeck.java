package com.ohjelmointi4;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class HandDeck extends Deck {

	public Deck dealDeck;

	public HandDeck(Card[] cards, Deck _dealDeck) {
		super(cards);
		dealDeck = _dealDeck;
	}

	@Override
	public boolean canCombineWith(Deck deck2) {
		return false;
	}

	@Override
	public MovableDeck handleMousePress(GamePanel gp, MouseEvent e) {
		if (dealDeck != null) {
			if (cards.size() == 0) {
				this.cards = new ArrayList<Card>(dealDeck.cards);
				dealDeck.cards.clear();
				for (Card card : this.cards) {
					card.hidden = true;
				}
				this.shuffle();
			} else {
				this.dealTo(dealDeck, 1, false);
			}
		}
		return null;
	}

}
