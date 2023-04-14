package com.ohjelmointi4;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JPanel;

import javax.imageio.*;

public class GamePanel extends JPanel {

	
	File f = null;
	BufferedImage gameImage = null;
	

	public GamePanel() {
		super();
		
		f = new File("kortit.png");

		try {
			gameImage = ImageIO.read(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//gameImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		setBackground(new Color(0,150,0) );
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
		//gameImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
}
