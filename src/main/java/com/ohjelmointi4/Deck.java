package com.ohjelmointi4;

import java.util.List;
import java.util.Random;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class Deck {

	protected List<Card> cards;

	public int deckX = 0;
	public int deckY = 0;
	public int cardOffsetX = 0;
	public int cardOffsetY = 0;

	public Deck() {
		cards = new ArrayList<Card>();
	}

	public Deck(Card[] cards) {
		this.cards = new ArrayList<Card>(Arrays.asList(cards));
	}

	public boolean canCombineWith(Deck deck2) {
		return true;
	}

	public MovableDeck handleMousePress(GamePanel gp, MouseEvent e) {
		return null;
	}

	public List<Card> getCards() {
		return cards;
	}

	public void shuffle() {
		for (int i = cards.size() - 1; i > 0; i--) {
			int j = new Random().nextInt(i + 1);
			Card temp = cards.get(i);
			cards.set(i, cards.get(j));
			cards.set(j, temp);
		}
	}

	public void dealTo(Deck deck2, int amount, boolean hideAllButOne) {
		if (this.cards.size() >= amount) {
			for (int i = 0; i < amount; i++) {
				Card card = this.cards.remove(0);
				if (hideAllButOne) {
					card.hidden = i != amount - 1;
				}
				deck2.getCards().add(card);
			}
		}
	}

	public void combineWith(Deck deck2) {
		this.cards.addAll(deck2.getCards());
		
	}

	public MovableDeck splitAt(int index) {

		List<Card> subList = this.cards.subList(index, cards.size());
		MovableDeck result = new MovableDeck((new ArrayList<Card>(subList)).toArray(new Card[subList.size()]));
		subList.clear();

		return result;

	}

}
