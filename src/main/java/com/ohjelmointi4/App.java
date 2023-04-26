package com.ohjelmointi4;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import java.awt.Canvas;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;



/**
 * Hello world!
 *
 */
public class App extends JFrame implements ActionListener {

    public static App instance;

    CardLayout crd;

    Container cPane;
    // menu
    Container mainMenuContainer = new Container();
    JButton newGameButton = new JButton("Uusi peli");
    JButton leaderboardButton = new JButton("Pisteet");
    JButton settingsButton = new JButton("Asetukset");
    JButton quitButton = new JButton("Poistu");
    // pisteet
    Container leaderboardContainer = new Container();
    JButton leaderboardBackButton = new JButton("Takaisin");
    // asetukset
    Container settingsContainer = new Container();
    JButton settingsBackButton = new JButton("Takaisin");
    String[] resolutions = {"800x600", "1200x800", "1920x1080"};
    final JComboBox<String> resolutionComboBox = new JComboBox<String>(resolutions);
    JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, -80, 30, -25);
    int volume = 100;
    JButton chooseFileButton = new JButton("Valitse tiedosto...");
    JFileChooser cardFileChooser = new JFileChooser();
    FileFilter imageFileFilter = new FileFilter() {

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            String extension = "";
            int i = f.getName().lastIndexOf('.');

            if (i != -1) {
                extension = f.getName().substring(i + 1);
            }

            if (Arrays.asList(ImageIO.getReaderFileSuffixes()).contains(extension)) {
                return true;
            }

            return false;
        }

        @Override
        public String getDescription() {
            return "Image Files";
        }

    };

    JButton defaultCardsButton = new JButton("Oletuskortit");

    // peli
    Container gameContainer = new Container();
    JButton gameBackButton = new JButton("Takaisin");
    JLabel scoreText = new JLabel("Siirrot: 0");
    JLabel timeText = new JLabel("Aika: 0:00");
    GamePanel gamePanel;
    JButton instructionsButton = new JButton("Ohjeet");
    JButton restartGameButton = new JButton("Aloita alusta");

    Sound buttonSound;

    ImageIcon icon = new ImageIcon(getClass().getResource("/nimetön.png"));

    // constructor of the class
    App() {

        super("Pasianssi");

        instance = this;

        cPane = getContentPane();

        cPane.setPreferredSize(new Dimension(800, 600));
        pack();

        // default constructor used
        // therefore, components will
        // cover the whole area
        crd = new CardLayout();

        cPane.setLayout(crd);

        buttonSound = new Sound("/timpani.wav");

        setIconImage(icon.getImage());
        setResizable(false);

        // peli
        gamePanel = new GamePanel();
        scoreText.setFont(new Font("Serif", Font.PLAIN, 24));
        scoreText.setHorizontalAlignment(SwingConstants.CENTER);
        scoreText.setVerticalAlignment(SwingConstants.CENTER);
        timeText.setFont(new Font("Serif", Font.PLAIN, 24));
        timeText.setHorizontalAlignment(SwingConstants.CENTER);
        timeText.setVerticalAlignment(SwingConstants.CENTER);

        

        // Adding listeners to the buttons
        newGameButton.addActionListener(this);
        leaderboardButton.addActionListener(this);
        settingsButton.addActionListener(this);
        quitButton.addActionListener(this);

        leaderboardBackButton.addActionListener(this);

        settingsBackButton.addActionListener(this);
        chooseFileButton.addActionListener(this);
        resolutionComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String s = resolutionComboBox.getSelectedItem().toString();

                switch (s) {
                    case "800x600":
                        cPane.setPreferredSize(new Dimension(800, 600));
                        pack();
                        break;

                    case "1200x800":
                        cPane.setPreferredSize(new Dimension(1200, 800));
                        pack();
                        break;

                    case "1920x1080":
                        cPane.setPreferredSize(new Dimension(1920, 1080));
                        pack();
                        break;
                }
                updateBounds();
            }
        });
        cardFileChooser.setFileFilter(imageFileFilter);
        defaultCardsButton.addActionListener(this);

        volumeSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int i = volumeSlider.getValue();
                buttonSound.setVolume(i);
            }

        });

        updateBounds();
        updateGameTexts();
        buttonMouseOvers();

        gameBackButton.addActionListener(this);
        instructionsButton.addActionListener(this);
        restartGameButton.addActionListener(this);

        mainMenuContainer.add(newGameButton);
        mainMenuContainer.add(leaderboardButton);
        mainMenuContainer.add(settingsButton);
        mainMenuContainer.add(quitButton);

        leaderboardContainer.add(leaderboardBackButton);

        settingsContainer.add(settingsBackButton);
        settingsContainer.add(resolutionComboBox);
        settingsContainer.add(volumeSlider);
        settingsContainer.add(chooseFileButton);
        settingsContainer.add(defaultCardsButton);

        gameContainer.add(gameBackButton);
        gameContainer.add(scoreText);
        gameContainer.add(timeText);
        gameContainer.add(gamePanel);
        gameContainer.add(instructionsButton);
        gameContainer.add(restartGameButton);

        cPane.add("main menu", mainMenuContainer);
        cPane.add("leaderboards", leaderboardContainer);
        cPane.add("settings", settingsContainer);
        cPane.add("game", gameContainer);

    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == newGameButton) {
            gamePanel.start();
            crd.show(cPane, "game");
            buttonSound.playSound();

        } else if (e.getSource() == leaderboardButton) {
            crd.show(cPane, "leaderboards");
            buttonSound.playSound();

        } else if (e.getSource() == settingsButton) {
            crd.show(cPane, "settings");
            buttonSound.playSound();

        } else if (e.getSource() == quitButton) {
            System.exit(0);

        } else if (e.getSource() == settingsBackButton || e.getSource() == leaderboardBackButton) {
            
            crd.show(cPane, "main menu");
            buttonSound.playSound();

        } else if (e.getSource() == chooseFileButton) {
            buttonSound.playSound();
            int state = cardFileChooser.showOpenDialog(this);

            if (state == JFileChooser.APPROVE_OPTION) {
                try {
                    URL fileURL = cardFileChooser.getSelectedFile().toURI().toURL();
                    gamePanel.loadCardImage(fileURL);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        } else if (e.getSource() == defaultCardsButton) {
            try {
                buttonSound.playSound();
                gamePanel.loadCardImage(getClass().getResource("/kortit.png"));
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } else if (e.getSource() == gameBackButton ) {

            String[] o = {"Ok", "Peruuta"};
            int i = JOptionPane.showOptionDialog(this, "Tämä lopettaa pelin", "Varoitus", JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE, null, o, null);

            if (i == 0) {
            gamePanel.stop();
            crd.show(cPane, "main menu");
            buttonSound.playSound();
            }

        } else if (e.getSource() == restartGameButton) {
            String[] o = {"Kyllä", "Ei"};
            int i = JOptionPane.showOptionDialog(this, "Haluatko varmasti aloittaa uuden pelin?", "Varoitus", JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE, null, o, null);

            if (i == 0) {
            gamePanel.start();
            buttonSound.playSound();
            }
        } else if (e.getSource() == instructionsButton) {
            String[] o = {"Ok"};
            JOptionPane.showOptionDialog(this, "Tähän säännöt", "Säännöt", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, o, null);
        }

    }

    public void updateBounds() {

        Rectangle backButtonRectangle = new Rectangle(25, cPane.getHeight() - 75, (int) (cPane.getWidth() * 0.15), 50);

        // menu
        newGameButton.setBounds((cPane.getWidth() / 2) - (cPane.getWidth() / 4), cPane.getHeight() - (int) (cPane.getHeight() * 0.85), (cPane.getWidth() / 2), cPane.getHeight() / 15);
        leaderboardButton.setBounds((cPane.getWidth() / 2) - (cPane.getWidth() / 4), cPane.getHeight() - (int) (cPane.getHeight() * 0.65), (cPane.getWidth() / 2), cPane.getHeight() / 15);
        settingsButton.setBounds((cPane.getWidth() / 2) - (cPane.getWidth() / 4), cPane.getHeight() - (int) (cPane.getHeight() * 0.45), (cPane.getWidth() / 2), cPane.getHeight() / 15);
        quitButton.setBounds((cPane.getWidth() / 2) - (cPane.getWidth() / 4), cPane.getHeight() - (int) (cPane.getHeight() * 0.25), (cPane.getWidth() / 2), cPane.getHeight() / 15);

        // pisteet
        leaderboardBackButton.setBounds(backButtonRectangle);
        // asetukset
        settingsBackButton.setBounds(backButtonRectangle);
        resolutionComboBox.setBounds((int) (cPane.getWidth() * 0.625), (int) (cPane.getHeight() * 0.17), (int) (cPane.getWidth() * 0.15), (int) (cPane.getHeight() * 0.0625));
        volumeSlider.setBounds((int) (cPane.getWidth() * 0.625), (int) (cPane.getHeight() * 0.35), (int) (cPane.getWidth() * 0.15), (int) (cPane.getHeight() * 0.0625));
        chooseFileButton.setBounds((int) (cPane.getWidth() * 0.625), (int) (cPane.getHeight() * 0.52), (int) (cPane.getWidth() * 0.15), (int) (cPane.getHeight() * 0.0625));
        defaultCardsButton.setBounds((int) (cPane.getWidth() * 0.625), (int) (cPane.getHeight() * 0.70), (int) (cPane.getWidth() * 0.15), (int) (cPane.getHeight() * 0.0625));

        // peli
        gameBackButton.setBounds(backButtonRectangle);
        scoreText.setBounds(4 * cPane.getWidth() / 5 - (int) (cPane.getWidth() * 0.15), cPane.getHeight() - 100, (int) (cPane.getWidth() * 0.3), 100);
        timeText.setBounds(cPane.getWidth() / 2 - (int) (cPane.getWidth() * 0.15), cPane.getHeight() - 100, (int) (cPane.getWidth() * 0.3), 100);
        gamePanel.setBounds(0, 0, (int) (cPane.getWidth() * 0.875), cPane.getHeight() - 100);
        instructionsButton.setBounds( (int) (cPane.getWidth() * 0.88), (int) (cPane.getHeight() * 0.15), (int) (cPane.getWidth() * 0.10), (int) (cPane.getHeight() * 0.05) );
        restartGameButton.setBounds( (int) (cPane.getWidth() * 0.88), (int) (cPane.getHeight() * 0.25), (int) (cPane.getWidth() * 0.10), (int) (cPane.getHeight() * 0.05) );
    }

    public void buttonMouseOvers() {

        // menu
        newGameButton.setToolTipText("Aloittaa uuden pelin");
        leaderboardButton.setToolTipText("Pelin läpäisseiden tilastot");
        settingsButton.setToolTipText("Asetukset valikko");
        quitButton.setToolTipText("Lopettaa ohjelman");

        // pisteet
        leaderboardBackButton.setToolTipText("Takaisin päävalikkoon");

        // asetukset
        settingsBackButton.setToolTipText("Takaisin päävalikkoon");
        resolutionComboBox.setToolTipText("Valitse peli-ikkunan koko");
        defaultCardsButton.setToolTipText("Tästä voit palauttaa pelin alkuperäiset kortit");

        // peli
        gameBackButton.setToolTipText("Takaisin päävalikkoon. Lopettaa pelin");

    }

    public void updateGameTexts() {
        scoreText.setText("Siirrot: " + gamePanel.moves);
        long timeInSeconds = TimeUnit.SECONDS.convert(System.nanoTime() - gamePanel.startTime, TimeUnit.NANOSECONDS);
        timeText.setText(String.format("Aika: %d:%02d", timeInSeconds / 60, timeInSeconds % 60));
    }


    // main method
    public static void main(String argvs[]) {

        App app = new App();

        app.setVisible(true);
        app.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
