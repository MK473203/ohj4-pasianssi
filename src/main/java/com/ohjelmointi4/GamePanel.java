package com.ohjelmointi4;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class GamePanel extends JPanel {

	BufferedImage gameImage;

	public GamePanel() {
		super();
		gameImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		setBackground(Color.RED);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		gameImage.setRGB(1, 1, 0xFF000000);
		g.drawImage(gameImage, 0, 0, null);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		gameImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

}
