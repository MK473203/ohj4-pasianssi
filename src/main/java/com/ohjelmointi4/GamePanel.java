package com.ohjelmointi4;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.imageio.*;

public class GamePanel extends JPanel implements ActionListener {


	BufferedImage gameImage;

	BufferedImage cardImage;

	Timer tickTimer = new Timer(16, this);

	public int deckX = 0;

	public int ticks;

	public int cardWidth = 60;
	public int cardHeight = 100;

	public Color backgroundColor = new Color(0, 150, 0);

	public GamePanel() {
		super();

		try {
			cardImage = ImageIO.read(new File("kortit.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		gameImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		setBackground(backgroundColor);
		tickTimer.start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawDeck(Deck.getAllCardsDeck(), deckX, 0);
		g.drawImage(gameImage, 0, 0, null);
	}


	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		gameImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	private void drawCard(Card card, int x, int y) {
		BufferedImage cardSprite = cardImage.getSubimage(card.getRank() * cardWidth, card.getSuit() * cardHeight, cardWidth, cardHeight);
		Graphics2D gameGraphics = gameImage.createGraphics();
		gameGraphics.drawImage(cardSprite, null, x, y);
		gameGraphics.dispose();

	}

	private void drawDeck(Deck deck, int x, int y) {

		List<Card> cards = deck.getCards();

		for (int i = 0; i < cards.size(); i++) {
			Card card = cards.get(i);
			drawCard(card, x + i * deck.cardOffsetX, y + i * deck.cardOffsetY);
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		deckX = 250 + (int)(250 * Math.sin(ticks / 10.0));

		ticks++;
		tickTimer.restart();
		gameImage = new BufferedImage(getWidth(), getWidth(), BufferedImage.TYPE_INT_ARGB);
		repaint();

	}

}
