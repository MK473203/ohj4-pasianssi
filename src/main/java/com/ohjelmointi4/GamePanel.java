package com.ohjelmointi4;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.imageio.*;

public class GamePanel extends JPanel implements ActionListener {


	BufferedImage gameImage;
	BufferedImage cardImage;

	private static final int targetFPS = 60;

	Timer tickTimer = new Timer((int) (1000 / targetFPS), this);

	public int ticks;

	public int cardWidth = 60;
	public int cardHeight = 100;
	public int deckPaddingX = 20;
	public int deckPaddingY = 20;

	public Color backgroundColor = new Color(0, 150, 0);

	public Deck handDeck;

	public Deck[] suitDecks = new Deck[4];
	public Deck[] mainDecks = new Deck[7];

	public GamePanel() {
		super();

		gameImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		// setBackground(backgroundColor);
		setIgnoreRepaint(true);

		try {
			cardImage = ImageIO.read(getClass().getResource("/kortit.png"));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

	}

	public void start() {
		handDeck = Deck.getAllCardsDeck();
		handDeck.deckX = deckPaddingX;
		handDeck.deckY = deckPaddingY;
		handDeck.shuffle();

		for (int i = 0; i < suitDecks.length; i++) {
			suitDecks[i] = new Deck();
			suitDecks[i].deckX = deckPaddingX + (3 + i) * (cardWidth + deckPaddingX);
			suitDecks[i].deckY = deckPaddingY;
		}

		for (int i = 0; i < mainDecks.length; i++) {
			mainDecks[i] = new Deck();
			mainDecks[i].cardOffsetY = 20;
			mainDecks[i].deckX = deckPaddingX + i * (cardWidth + deckPaddingX);
			mainDecks[i].deckY = cardHeight + 2 * deckPaddingY;
			handDeck.dealTo(mainDecks[i], i + 1, true);
		}

		tickTimer.start();
	}

	public void stop() {
		tickTimer.stop();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D graphics = gameImage.createGraphics();
		graphics.setColor(backgroundColor);
		graphics.fillRect(0, 0, gameImage.getWidth(), gameImage.getHeight());

		drawDeck(graphics, handDeck);

		for (Deck deck : suitDecks) {
			drawDeck(graphics, deck);
		}

		for (Deck deck : mainDecks) {
			drawDeck(graphics, deck);
		}

		graphics.dispose();
		g.drawImage(gameImage, 0, 0, null);
	}


	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		gameImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	private void drawCard(Graphics2D graphics, Card card, int x, int y) {
		BufferedImage cardSprite;
		if (card.hidden) {
			cardSprite = cardImage.getSubimage(0 * cardWidth, 4 * cardHeight, cardWidth, cardHeight);
		} else {
			cardSprite = cardImage.getSubimage(card.getRank() * cardWidth, card.getSuit() * cardHeight, cardWidth, cardHeight);
		}

		graphics.drawImage(cardSprite, null, x, y);
	}

	private void drawDeck(Graphics2D graphics, Deck deck) {

		List<Card> cards = deck.getCards();

		graphics.setColor(new Color(0, 0, 0, 50));
		graphics.fillRect(deck.deckX, deck.deckY, cardWidth, cardHeight);

		for (int i = 0; i < cards.size(); i++) {
			Card card = cards.get(i);
			drawCard(graphics, card, deck.deckX + i * deck.cardOffsetX, deck.deckY + i * deck.cardOffsetY);
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ticks++;
		tickTimer.restart();

		handDeck.deckY = 250 + (int) (250 * Math.sin(ticks / 10.0));
		handDeck.deckX = 250 + (int) (250 * Math.cos(ticks / 3.0));

		repaint();

	}

}
