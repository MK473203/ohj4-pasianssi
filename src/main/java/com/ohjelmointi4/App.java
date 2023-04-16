package com.ohjelmointi4;

import javax.swing.*;
import java.awt.Canvas;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;



/**
 * Hello world!
 *
 */
public class App extends JFrame implements ActionListener {

    CardLayout crd;

    Container cPane;

    Container mainMenuContainer = new Container();
    Container leaderboardContainer = new Container();
    Container settingsContainer = new Container();
    Container gameContainer = new Container();
    GamePanel gamePanel;

    Sound buttonSound;

    ImageIcon icon = new ImageIcon(getClass().getResource("/nimetön.png"));

    // constructor of the class
    App() {

        super("Pasianssi");

        setSize(800, 600);

        cPane = getContentPane();

        // default constructor used
        // therefore, components will
        // cover the whole area
        crd = new CardLayout();

        cPane.setLayout(crd);

        buttonSound = new Sound("/timpani.wav");

        setIconImage(icon.getImage());
        setResizable(false);

        JButton a = new JButton("Uusi peli");
        a.setBounds((getWidth() / 2) - (getWidth() / 4), 100, (getWidth() / 2), 40);

        JButton b = new JButton("Pisteet");
        b.setBounds(350, 200, 100, 40);

        JButton c = new JButton("Asetukset");
        c.setBounds(350, 300, 100, 40);

        JButton d = new JButton("Poistu");
        d.setBounds(350, 400, 100, 40);

        JButton e = new JButton("Takaisin");
        e.setBounds(100, 500, 100, 40);

        JButton f = new JButton("Takaisin");
        f.setBounds(100, 500, 100, 40);

        JButton g = new JButton("Takaisin");
        g.setBounds(100, 500, 100, 40);

        gamePanel = new GamePanel();
        gamePanel.setBounds(0, 0, getWidth() - 100, getHeight() - 100);

        // Adding listeners to the buttons
        a.addActionListener(this);
        b.addActionListener(this);
        c.addActionListener(this);
        d.addActionListener(this);
        e.addActionListener(this);
        f.addActionListener(this);
        g.addActionListener(this);

        mainMenuContainer.add(a);
        mainMenuContainer.add(b);
        mainMenuContainer.add(c);
        mainMenuContainer.add(d);

        leaderboardContainer.add(e);
        settingsContainer.add(f);
        gameContainer.add(g);
        gameContainer.add(gamePanel);


        cPane.add("main menu", mainMenuContainer);
        cPane.add("leaderboards", leaderboardContainer);
        cPane.add("settings", settingsContainer);
        cPane.add("game", gameContainer);

    }

    public void actionPerformed(ActionEvent e) {
        // Upon clicking the button, the next card of the container is shown
        // after the last card, again, the first card of the container is shown upon clicking

        if (e.getActionCommand() == "Uusi peli") {
            gamePanel.start();
            crd.show(cPane, "game");
            
            buttonSound.playSound();
        }

        if (e.getActionCommand() == "Pisteet") {
            crd.show(cPane, "leaderboards");
            buttonSound.playSound();
        }

        if (e.getActionCommand() == "Asetukset") {
            crd.show(cPane, "settings");
            buttonSound.playSound();
        }

        if (e.getActionCommand() == "Poistu") {
            System.exit(0);
        }

        if (e.getActionCommand() == "Takaisin") {
            crd.show(cPane, "main menu");
            gamePanel.stop();
            buttonSound.playSound();
        }

    }

    

    // main method
    public static void main(String argvs[]) {
        // creating an object of the class CardLayoutExample1
        App crdl = new App();

        // size is 300 * 300
        crdl.setVisible(true);
        crdl.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
