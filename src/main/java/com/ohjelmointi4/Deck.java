package com.ohjelmointi4;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

public class Deck {

	private List<Card> cards;

	public int deckX = 0;
	public int deckY = 0;
	public int cardOffsetX = 0;
	public int cardOffsetY = 0;

	public Deck() {
		cards = new ArrayList<Card>();
	}

	private Deck(Card[] cards) {
		this.cards = new ArrayList<Card>(Arrays.asList(cards));
	}

	public boolean canCombine(Deck deck2) {
		Card firstOfThisDeck = this.getCards().get(0);
		Card lastOfThatDeck = deck2.getCards().get(deck2.getCards().size() - 1);
		return !lastOfThatDeck.hidden && (firstOfThisDeck.getSuit() % 2 != lastOfThatDeck.getSuit() % 2) && (firstOfThisDeck.getRank() == lastOfThatDeck.getRank() + 1);
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
		for (int i = 0; i < amount; i++) {
			Card card = this.cards.remove(0);
			if (hideAllButOne) {
				card.hidden = i != amount - 1;
			}
			deck2.getCards().add(card);
		}
	}

	public static Deck getAllCardsDeck() {
		
		Card[] cards = new Card[52];
		for (int suit = 0; suit < 4; suit++) {
			for (int rank = 0; rank < 13; rank++) {
				cards[suit * 13 + rank] = new Card(suit, rank);
			}
		}

		return new Deck(cards);
	}

}
