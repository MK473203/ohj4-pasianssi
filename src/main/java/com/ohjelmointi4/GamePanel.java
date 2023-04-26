package com.ohjelmointi4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.imageio.*;

public class GamePanel extends JPanel implements ActionListener {


	BufferedImage gameImage;
	BufferedImage cardImage;

	Graphics2D graphics;

	private int cardImageCardWidth;
	private int cardImageCardHeight;
	private AffineTransform cardImageToGameImage;

	private static final int targetFPS = 60;

	Timer tickTimer = new Timer((int) (1000 / targetFPS), this);

	public int ticks;

	public int cardWidth;
	public int cardHeight;
	public int deckPaddingX;
	public int deckPaddingY;
	public int globalOffsetX;

	public int moves = 0;
	public long startTime;

	public Color backgroundColor = new Color(0, 150, 0);

	public Deck[] decks = new Deck[13];

	public MovableDeck selectedDeck = null;

	public GamePanel() {
		super();

		gameImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		setBackground(backgroundColor);
		setIgnoreRepaint(true);

		cardImageToGameImage = new AffineTransform();
		loadCardImage(getClass().getResource("/kortit.png"));


		addMouseListener(new MouseListener() {

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && selectedDeck == null) {
					Deck deck = getDeckAtPos(e.getX(), e.getY());
					if (deck != null) {
						MovableDeck deckToBeSelected = deck.handleMousePress((GamePanel) e.getComponent(), e);
						if (deckToBeSelected != null) {
							selectedDeck = deckToBeSelected;
							selectedDeck.startDeck = deck;
							selectedDeck.selected = true;
							selectedDeck.cardOffsetY = deck.cardOffsetY;
						}
					}
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (selectedDeck != null) {
					Deck deck = getDeckAtPos(e.getX(), e.getY());

					if (deck != null && deck.canCombineWith(selectedDeck)) {
						deck.combineWith(selectedDeck);
						if (selectedDeck.startDeck.cards.size() > 0) {
							selectedDeck.startDeck.cards.get(selectedDeck.startDeck.cards.size() - 1).hidden = false;
						}
						selectedDeck = null;
						moves++;
						App.instance.updateGameTexts();
					} else {
						selectedDeck.selected = false;
					}

					if (!isWin()) {
						long gameDurationSeconds = TimeUnit.SECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
						String dialogString = "<html>Voitit pelin!<br>Aika:<br>Siirrot:</html>";
						String dialogString2 = String.format("<html><br>%d:%02d<br>%d</html>", gameDurationSeconds / 60, gameDurationSeconds % 60, moves);

						Object[] options = {"Tallenna", "Älä tallenna"};

						JPanel panel = new JPanel();
						//panel.setBackground(Color.GRAY);
						panel.setLayout(new GridBagLayout());
						GridBagConstraints c = new GridBagConstraints();
						c.fill = GridBagConstraints.HORIZONTAL;
						c.ipadx = 30;
						c.gridx = 0;
						c.gridy = 0;
						JLabel label = new JLabel(dialogString);
						panel.add(label, c);

						c.gridx = 1;
						c.gridy = 0;
						panel.add(new JLabel(dialogString2), c);

						c.gridx = 0;
						c.gridy = 1;
						panel.add(new JLabel("Nimi:"), c);
						JTextField textField = new JTextField("Pelaaja");
						c.gridx = 1;
						c.gridy = 1;
						panel.add(textField, c);

						int result = JOptionPane.showOptionDialog(App.instance, panel, "Onneksi olkoon!", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);

						if (result == JOptionPane.YES_OPTION) {
							String name = textField.getText();
							System.out.println(name);
						}
					}

				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
		});

		setBounds(0, 0, 100, 100);

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		graphics.setColor(backgroundColor);
		graphics.fillRect(0, 0, gameImage.getWidth(), gameImage.getHeight());

		for (Deck deck : decks) {
			drawDeck(graphics, deck);
		}

		if (selectedDeck != null) {
			drawDeck(graphics, selectedDeck);
		}

		g.drawImage(gameImage, 0, 0, null);
	}


	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		gameImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		cardHeight = getHeight() / 5;
		cardWidth = cardHeight * cardImageCardWidth / cardImageCardHeight;
		deckPaddingX = cardWidth / 3;
		deckPaddingY = cardWidth / 3;
		globalOffsetX = (getWidth() - 7 * (cardWidth + deckPaddingX)) / 2;
		cardImageToGameImage.setToScale(cardWidth / (double) cardImageCardWidth, cardHeight / (double) cardImageCardHeight);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ticks++;
		tickTimer.restart();

		Point mousePos = getMousePosition();

		if (selectedDeck != null) {
			if (mousePos != null && selectedDeck.selected) {
				selectedDeck.deckX = (int) mousePos.getX() - cardWidth / 2;
				selectedDeck.deckY = (int) mousePos.getY() - deckPaddingY / 2;
			} else {
				int destX = selectedDeck.startDeck.deckX;
				int destY = selectedDeck.startDeck.deckY + (selectedDeck.startDeck.cards.size()) * selectedDeck.startDeck.cardOffsetY;

				selectedDeck.deckX = (destX + 2 * selectedDeck.deckX) / 3;
				selectedDeck.deckY = (destY + 2 * selectedDeck.deckY) / 3;

				if (Math.abs(destX - selectedDeck.deckX) < 5 && Math.abs(destY - selectedDeck.deckY) < 5) {
					selectedDeck.startDeck.combineWith(selectedDeck);
					selectedDeck = null;
				}
			}
		}

		App.instance.updateGameTexts();

		repaint();

	}

	public void start() {

		if (graphics != null) {
			graphics.dispose();
		}
		graphics = gameImage.createGraphics();

		decks[12] = new SuitDeck();
		decks[12].deckX = globalOffsetX + cardWidth + deckPaddingX;
		decks[12].deckY = deckPaddingY;

		decks[0] = new HandDeck(Card.getAllCards(), decks[12]);
		decks[0].deckX = globalOffsetX;
		decks[0].deckY = deckPaddingY;
		decks[0].shuffle();

		for (int i = 1; i < 5; i++) {
			decks[i] = new SuitDeck();
			decks[i].deckX = globalOffsetX + (2 + i) * (cardWidth + deckPaddingX);
			decks[i].deckY = deckPaddingY;
		}

		for (int i = 5; i < 12; i++) {
			decks[i] = new MainDeck();
			decks[i].cardOffsetY = cardWidth / 3;
			decks[i].deckX = globalOffsetX + (i - 5) * (cardWidth + deckPaddingX);
			decks[i].deckY = cardHeight + 2 * deckPaddingY;
			decks[0].dealTo(decks[i], i - 4, true);
		}

		moves = 0;

		tickTimer.start();
		startTime = System.nanoTime();
	}

	public void stop() {
		tickTimer.stop();
		if (graphics != null) {
			graphics.dispose();
		}
	}

	public boolean isWin() {
		for (int i = 1; i < 5; i++) {
			if (decks[i].cards.size() != 13) {
				return false;
			}
		}
		return true;
	}

	public void loadCardImage(URL file) {
		try {
			cardImage = ImageIO.read(file);
		} catch (Exception e) {
			System.err.println(String.format("Could not read file %s. Loading default cards...", file.toString()));
			try {
				cardImage = ImageIO.read(getClass().getResource("/kortit.png"));
			} catch (Exception e2) {
				e2.printStackTrace();
				System.exit(0);
			}
		}
		cardImageCardWidth = cardImage.getWidth() / 13;
		cardImageCardHeight = cardImage.getHeight() / 5;
		cardImageToGameImage.setToScale(cardWidth / (double) cardImageCardWidth, cardHeight / (double) cardImageCardHeight);
	}

	private void drawCard(Graphics2D graphics, Card card, int x, int y) {
		BufferedImage cardSprite;
		if (card.hidden) {
			cardSprite = cardImage.getSubimage(0 * cardImageCardWidth, 4 * cardImageCardHeight, cardImageCardWidth, cardImageCardHeight);
		} else {
			cardSprite = cardImage.getSubimage(card.getRank() * cardImageCardWidth, card.getSuit() * cardImageCardHeight, cardImageCardWidth, cardImageCardHeight);
		}

		graphics.drawImage(cardSprite, new AffineTransformOp(cardImageToGameImage, AffineTransformOp.TYPE_BICUBIC), x, y);
	}

	private void drawDeck(Graphics2D graphics, Deck deck) {

		List<Card> cards = deck.cards;

		graphics.setColor(new Color(0, 0, 0, 50));
		graphics.fillRect(deck.deckX, deck.deckY, cardWidth, cardHeight);

		for (int i = 0; i < cards.size(); i++) {
			Card card = cards.get(i);
			drawCard(graphics, card, deck.deckX + i * deck.cardOffsetX, deck.deckY + i * deck.cardOffsetY);
		}

	}

	public Deck getDeckAtPos(int x, int y) {

		for (Deck deck : decks) {

			if (deck.deckX < x && x < deck.deckX + cardWidth && deck.deckY < y && y < deck.deckY + cardHeight + (deck.cards.size() - 1) * deck.cardOffsetY) {
				return deck;
			}

		}

		return null;

	}

	public MovableDeck splitDeckAtPos(Deck deck, int x, int y) {

		int tempY = y - deck.deckY;

		tempY = Math.min(tempY, (deck.cards.size() - 1) * deck.cardOffsetY);

		int index = tempY / deck.cardOffsetY;

		if (deck.cards.get(index).hidden) {
			return null;
		}

		return deck.splitAt(index);

	}

}
