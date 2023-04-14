package com.ohjelmointi4;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Deck {

	public static Deck allCardsDeck = null;

	private List<Card> cards;

	private int startingAmount = 0;
	public int maxAmount = 52;

	public int deckX = 0;
	public int deckY = 0;
	public int cardOffsetX = 0;
	public int cardOffsetY = 20;

	public Deck(int startingAmount, int maxAmount) {
		cards = new ArrayList<Card>();
		this.startingAmount = startingAmount;
	}

	private Deck(Card[] cards) {
		this.cards = Arrays.asList(cards);
		startingAmount = this.cards.size();
	}

	public boolean canCombine(Deck deck2) {
		Card firstOfThisDeck = this.getCards().get(0);
		Card lastOfThatDeck = deck2.getCards().get(deck2.getCards().size() - 1);
		return !lastOfThatDeck.hidden && (firstOfThisDeck.getSuit() % 2 != lastOfThatDeck.getSuit() % 2) && (firstOfThisDeck.getRank() == lastOfThatDeck.getRank() + 1);
	}

	public List<Card> getCards() {
		return cards;
	}

	public static Deck getAllCardsDeck() {

		// If this is the first time we're getting all cards, automatically fill the deck with one of every
		// card.
		if (allCardsDeck == null) {
			Card[] cards = new Card[52];
			for (int suit = 0; suit < 4; suit++) {
				for (int rank = 0; rank < 13; rank++) {
					cards[suit * 13 + rank] = new Card(suit, rank);
				}
			}
			allCardsDeck = new Deck(cards);
		}

		return allCardsDeck;
	}

}
