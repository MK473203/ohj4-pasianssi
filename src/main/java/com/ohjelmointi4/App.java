package com.ohjelmointi4;

import javax.swing.*;
import java.awt.Canvas;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
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

        //menu
        JButton mainMenu1 = new JButton("Uusi peli");
        mainMenu1.setBounds((getWidth() / 2) - (getWidth() / 4), 100, (getWidth() / 2), 40);

        JButton mainMenu2 = new JButton("Pisteet");
        mainMenu2.setBounds(350, 200, 100, 40);

        JButton mainMenu3 = new JButton("Asetukset");
        mainMenu3.setBounds(350, 300, 100, 40);

        JButton mainMenu4 = new JButton("Poistu");
        mainMenu4.setBounds(350, 400, 100, 40);

        //pisteet
        JButton leaderboards1 = new JButton("Takaisin");
        leaderboards1.setBounds(100, 500, 100, 40);

        //asetukset
        JButton settings1 = new JButton("Takaisin");
        settings1.setBounds(100, 500, 100, 40);

        String[] resolutions = {"800x600", "1200x800"};
        final JComboBox settings2 = new JComboBox<String>(resolutions);
        settings2.setBounds(500, 100, 100, 50);
        
        settings2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String s = settings2.getSelectedItem().toString();

                switch (s) {

                    case "800x600":
                        setSize(800, 600);
                    break;

                    case "1200x800":
                        setSize(1200,800);
                    break;
                }

            }
        });

        //peli
        JButton game1 = new JButton("Takaisin");
        game1.setBounds(100, 500, 100, 40);

        gamePanel = new GamePanel();
        gamePanel.setBounds(0, 0, getWidth() - 100, getHeight() - 100);

        // Adding listeners to the buttons
        mainMenu1.addActionListener(this);
        mainMenu2.addActionListener(this);
        mainMenu3.addActionListener(this);
        mainMenu4.addActionListener(this);

        leaderboards1.addActionListener(this);
        settings1.addActionListener(this);
        game1.addActionListener(this);

        mainMenuContainer.add(mainMenu1);
        mainMenuContainer.add(mainMenu2);
        mainMenuContainer.add(mainMenu3);
        mainMenuContainer.add(mainMenu4);

        leaderboardContainer.add(leaderboards1);

        settingsContainer.add(settings1);
        settingsContainer.add(settings2);

        gameContainer.add(game1);
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
