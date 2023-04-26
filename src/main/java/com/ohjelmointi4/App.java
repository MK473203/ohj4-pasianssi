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
import java.awt.Image;
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
    JButton leaderboardBackButton = new JButton();
    // asetukset
    Container settingsContainer = new Container();
    JButton settingsBackButton = new JButton();
    String[] resolutions = {"800x600", "1200x800", "1920x1080"};
    final JComboBox<String> resolutionComboBox = new JComboBox<String>(resolutions);
    JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, -80, 30, -25);
    
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

    JLabel resolutionLabel = new JLabel("Ikkunan koko");
    JLabel volumeLabel = new JLabel("Äänen voimakkuus");
    JLabel cardsLabel = new JLabel("Korttien ulkoasu");

    JLabel settingsBackLabel = new JLabel("Takaisin");
    JLabel leaderboardsBackLabel = new JLabel("Takaisin");

    JButton defaultCardsButton = new JButton("Oletuskortit");

    // peli
    Container gameContainer = new Container();
    JButton gameBackButton = new JButton();
    JLabel gameBackLabel = new JLabel("Takaisin");
    JLabel scoreText = new JLabel("Siirrot: 0");
    JLabel timeText = new JLabel("Aika: 0:00");
    GamePanel gamePanel;
    JButton gameInstructionsButton = new JButton("?");
    JButton gameRestartGameButton = new JButton();

    Sound buttonSound;

    ImageIcon icon = new ImageIcon(getClass().getResource("/nimetön.png"));
    ImageIcon backButtonIcon = new ImageIcon(getClass().getResource("/nuoli.png"));
    ImageIcon restartGameButtonIcon = new ImageIcon(getClass().getResource("/uusiPeli.png"));
    ImageIcon gameInstructionIcon = new ImageIcon( getClass().getResource("/ohjeet.png") );

    JLabel restartGamLabel = new JLabel("Uusi peli");
    JLabel gameInstructionLabel = new JLabel("Ohjeet");

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
        gameRestartGameButton.addActionListener(this);
        gameInstructionsButton.addActionListener(this);

        mainMenuContainer.add(newGameButton);
        mainMenuContainer.add(leaderboardButton);
        mainMenuContainer.add(settingsButton);
        mainMenuContainer.add(quitButton);

        leaderboardContainer.add(leaderboardBackButton);
        leaderboardContainer.add(leaderboardsBackLabel);

        settingsContainer.add(settingsBackButton);
        settingsContainer.add(resolutionComboBox);
        settingsContainer.add(volumeSlider);
        settingsContainer.add(chooseFileButton);
        settingsContainer.add(defaultCardsButton);
        settingsContainer.add(resolutionLabel);
        settingsContainer.add(volumeLabel);
        settingsContainer.add(cardsLabel);
        settingsContainer.add(settingsBackLabel);

        gameContainer.add(gameBackButton);
        gameContainer.add(scoreText);
        gameContainer.add(timeText);
        gameContainer.add(gamePanel);
        gameContainer.add(gameInstructionsButton);
        gameContainer.add(gameRestartGameButton);
        gameContainer.add(gameBackLabel);
        gameContainer.add(gameInstructionLabel);
        gameContainer.add(restartGamLabel);

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

        } else if (e.getSource() == gameRestartGameButton) {
            String[] o = {"Kyllä", "Ei"};
            int i = JOptionPane.showOptionDialog(this, "Haluatko varmasti aloittaa uuden pelin?", "Varoitus", JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE, null, o, null);

            if (i == 0) {
            gamePanel.start();
            buttonSound.playSound();
            }
        } else if (e.getSource() == gameInstructionsButton) {

            String rules = "Klondike pasianssin tarkoituksena on täyttää tyhjät peruspakat pelin oikeassa yläkulmassa.\n" + 
            "Yhteen peruspakkaan voi laittaa vain yhden maan kortteja.\n" +           
            "Tyhjien pakkojen täyttö aloitetaan pienimmästä kortista eli ässästä.\n Seuraavaksi tyhjään pakkaan laitetaan seuraava kortti arvojärjestyksessä.\n" +
            "Pelin voittaa, kun kaikki tyhjät peruspakat ovat täytetty ässästä kuninkaaseen asti.\n" +
            "Vasemmassa yläreunassa on käsipakka, mistä voi ottaa kortteja.\n" +
            "Pöydällä on seitsemän pinoa. Pinojen päällimmäisen kortin alle voi \n lisätä eri värisen maan kortin, jonka arvo on yhtä pienempi.\n" +
            "Tyhjään pinoon voi asettaa Kuningas-arvoisen kortin.\n";

            String[] o = {"Ok"};
            JOptionPane.showOptionDialog(this, rules, "Säännöt", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, o, null);
        }

    }

    public void updateBounds() {

        int width = cPane.getWidth();
        int height = cPane.getHeight();

        Rectangle backButtonRectangle = new Rectangle(25, height - 75, (int) (width * 0.15), 50);

        // menu
        newGameButton.setBounds((width / 2) - (width / 4), height - (int) (height * 0.85), (width / 2), height / 15);
        leaderboardButton.setBounds((width / 2) - (width / 4), height - (int) (height * 0.65), (width / 2), height / 15);
        settingsButton.setBounds((width / 2) - (width / 4), height - (int) (height * 0.45), (width / 2), height / 15);
        quitButton.setBounds((width / 2) - (width / 4), height - (int) (height * 0.25), (width / 2), height / 15);

        // pisteet
        leaderboardBackButton.setBounds(backButtonRectangle);

        // asetukset
        settingsBackButton.setBounds(backButtonRectangle);
        resolutionComboBox.setBounds((int) (width * 0.625), (int) (height * 0.17), (int) (width * 0.15), (int) (height * 0.0625));
        volumeSlider.setBounds((int) (width * 0.625), (int) (height * 0.35), (int) (width * 0.15), (int) (height * 0.0625));
        chooseFileButton.setBounds((int) (width * 0.625), (int) (height * 0.52), (int) (width * 0.15), (int) (height * 0.0625));
        defaultCardsButton.setBounds((int) (width * 0.625), (int) (height * 0.70), (int) (width * 0.15), (int) (height * 0.0625));

        resolutionLabel.setBounds((int) (width * 0.325), (int) (height * 0.17), (int) (width * 0.15), (int) (height * 0.0625));
        volumeLabel.setBounds((int) (width * 0.325), (int) (height * 0.35), (int) (width * 0.15), (int) (height * 0.0625));
        cardsLabel.setBounds((int) (width * 0.325), (int) (height * 0.52), (int) (width * 0.15), (int) (height * 0.0625));

        // peli
        gameBackButton.setBounds(backButtonRectangle);
        scoreText.setBounds(4 * width / 5 - (int) (width * 0.15), height - 100, (int) (width * 0.3), 100);
        timeText.setBounds(width / 2 - (int) (width * 0.15), height - 100, (int) (width * 0.3), 100);
        gamePanel.setBounds(0, 0, (int) (width * 0.875), height - 100);
        gameInstructionsButton.setBounds( (int) (width * 0.88), (int) (height * 0.15), (int) (width * 0.10), (int) (width * 0.10) );
        gameRestartGameButton.setBounds( (int) (width * 0.88), (int) (height * 0.35), (int) (width * 0.10), (int) (width * 0.10) );

        //takaisin nappien ikoni
        Image image = backButtonIcon.getImage();
        ImageIcon newImg = new ImageIcon( image.getScaledInstance( (int) (gameBackButton.getWidth()* 0.95 ) , (int) ( gameBackButton.getHeight()*0.9),  java.awt.Image.SCALE_SMOOTH) );
        gameBackButton.setIcon (newImg) ;
        leaderboardBackButton.setIcon(newImg);
        settingsBackButton.setIcon(newImg);

        //takaisin nappien teksti
        settingsBackLabel.setBounds(gameBackButton.getX() + backButtonRectangle.width + 30, backButtonRectangle.y - (int) (backButtonRectangle.height * 0.5 ) , 100, 100);
        gameBackLabel.setBounds(gameBackButton.getX() + backButtonRectangle.width + 30, backButtonRectangle.y - (int) (backButtonRectangle.height * 0.5 ) , 100, 100);
        leaderboardsBackLabel.setBounds(gameBackButton.getX() + backButtonRectangle.width + 30, backButtonRectangle.y - (int) (backButtonRectangle.height * 0.5 ) , 100, 100);

       //pelinäkymän nappien ikonit
       image = restartGameButtonIcon.getImage();
       newImg = new ImageIcon( image.getScaledInstance( (int) (gameRestartGameButton.getWidth()* 0.95 ) , (int) ( gameRestartGameButton.getHeight()*0.9),  java.awt.Image.SCALE_SMOOTH) );
       gameRestartGameButton.setIcon(newImg);

       image = gameInstructionIcon.getImage();
       newImg = new ImageIcon( image.getScaledInstance( (int) (gameInstructionsButton.getWidth()* 0.95 ) , (int) ( gameInstructionsButton.getHeight()*0.9),  java.awt.Image.SCALE_SMOOTH) );
       gameInstructionsButton.setIcon(newImg);

        //peli ohjeet ja uusi peli jlabel
        gameInstructionLabel.setBounds( (int) (width * 0.88 + gameInstructionsButton.getWidth()*0.25 ), (int) (height * 0.25), (int) (width * 0.10), (int) (width * 0.10)  );
        restartGamLabel.setBounds( (int) (width * 0.88 + gameRestartGameButton.getWidth()*0.25 ), (int) (height * 0.45), (int) (width * 0.10), (int) (width * 0.10) );


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
        gameInstructionsButton.setToolTipText("Pelin ohjeet");
        gameRestartGameButton.setToolTipText("Aloittaa uuden pelin. Vanha peli menetetään");

        
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
