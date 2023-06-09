package com.ohjelmointi4;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeUnit;


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
    Container RemovebuttonContainer = new Container();
    JButton leaderboardBackButton = new JButton();
    JLabel leaderboardsBackLabel = new JLabel("Takaisin");
    List<LeaderboardItem> leaderboardItems = new ArrayList<LeaderboardItem>();
    JLabel leaderboardGamerLabel = new JLabel("Pelaaja");
    JLabel leaderboardGamer = new JLabel();
    JLabel leaderboardTimeLabel = new JLabel("Aika");
    JLabel leaderboardTime = new JLabel();
    JLabel leaderboardMovesLabel = new JLabel("Siirrot");
    JLabel leaderboardMoves = new JLabel();
    JLabel leaderboardDateLabel = new JLabel("Pvm.");
    JLabel leaderboardDate = new JLabel();
    JButton[] removeButtons = null;

    // asetukset
    Container settingsContainer = new Container();
    JButton settingsBackButton = new JButton();
    JLabel settingsBackLabel = new JLabel("Takaisin");
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
    JLabel chooseFileLabel = new JLabel("Korttien ulkoasu");
    JLabel minusLabel = new JLabel("-");
    JLabel plusLabel = new JLabel("+");


    JButton defaultCardsButton = new JButton("Oletuskortit");

    // peli
    Container gameContainer = new Container();
    JButton gameBackButton = new JButton();
    JLabel gameBackLabel = new JLabel("Takaisin");
    JLabel scoreText = new JLabel("Siirrot: 0");
    JLabel timeText = new JLabel("Aika: 0:00");
    GamePanel gamePanel;
    JButton gameInstructionsButton = new JButton();
    JButton restartGameButton = new JButton();

    Sound buttonSound;

    
    ImageIcon backButtonIcon = new ImageIcon(getClass().getResource("/nuoli.png"));
    ImageIcon restartGameButtonIcon = new ImageIcon(getClass().getResource("/uusiPeli.png"));
    ImageIcon gameInstructionIcon = new ImageIcon(getClass().getResource("/ohjeet.png"));
    ImageIcon removeButtonIcon = new ImageIcon(getClass().getResource("/vinoristiP.png"));

    JLabel restartGameLabel = new JLabel("Uusi peli");
    JLabel gameInstructionLabel = new JLabel("Ohjeet");


    App() {

        super("Pasianssi");

        instance = this;

        cPane = getContentPane();

        cPane.setPreferredSize(new Dimension(800, 600));
        pack();

        crd = new CardLayout();

        cPane.setLayout(crd);

        buttonSound = new Sound("/click2.wav");

        setResizable(false);

        // peli
        gamePanel = new GamePanel();
        scoreText.setHorizontalAlignment(SwingConstants.CENTER);
        scoreText.setVerticalAlignment(SwingConstants.CENTER);
        timeText.setHorizontalAlignment(SwingConstants.CENTER);
        timeText.setVerticalAlignment(SwingConstants.CENTER);
        restartGameLabel.setVerticalAlignment(SwingConstants.TOP);
        restartGameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gameInstructionLabel.setVerticalAlignment(SwingConstants.TOP);
        gameInstructionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // pisteet
        loadLeaderboard();
        leaderboardGamer.setVerticalAlignment(SwingConstants.TOP);
        leaderboardTime.setVerticalAlignment(SwingConstants.TOP);
        leaderboardMoves.setVerticalAlignment(SwingConstants.TOP);
        leaderboardDate.setVerticalAlignment(SwingConstants.TOP);

        gameBackLabel.setVerticalAlignment(SwingConstants.CENTER);
        settingsBackLabel.setVerticalAlignment(SwingConstants.CENTER);
        leaderboardsBackLabel.setVerticalAlignment(SwingConstants.CENTER);

        // päävalikon painikkeiden kuuntelijat
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
        Dictionary<Integer, JComponent> labelTable = new Hashtable<Integer, JComponent>();
        labelTable.put(volumeSlider.getMinimum(), minusLabel);
        labelTable.put(volumeSlider.getMaximum(), plusLabel);
        volumeSlider.setLabelTable(labelTable);
        volumeSlider.setPaintLabels(true);

        updateBounds();
        updateGameTexts();
        buttonMouseOvers();

        // pelinäkymän painikkeiden kuuntelijat
        gameBackButton.addActionListener(this);
        restartGameButton.addActionListener(this);
        gameInstructionsButton.addActionListener(this);

        mainMenuContainer.add(newGameButton);
        mainMenuContainer.add(leaderboardButton);
        mainMenuContainer.add(settingsButton);
        mainMenuContainer.add(quitButton);

        leaderboardContainer.add(leaderboardBackButton);
        leaderboardContainer.add(leaderboardsBackLabel);
        leaderboardContainer.add(leaderboardGamerLabel);
        leaderboardContainer.add(leaderboardTimeLabel);
        leaderboardContainer.add(leaderboardMovesLabel);
        leaderboardContainer.add(leaderboardDateLabel);

        leaderboardContainer.add(leaderboardGamer);
        leaderboardContainer.add(leaderboardTime);
        leaderboardContainer.add(leaderboardMoves);
        leaderboardContainer.add(leaderboardDate);

        leaderboardContainer.add(RemovebuttonContainer);

        settingsContainer.add(settingsBackButton);
        settingsContainer.add(resolutionComboBox);
        settingsContainer.add(volumeSlider);
        settingsContainer.add(chooseFileButton);
        settingsContainer.add(defaultCardsButton);
        settingsContainer.add(resolutionLabel);
        settingsContainer.add(volumeLabel);
        settingsContainer.add(chooseFileLabel);
        settingsContainer.add(settingsBackLabel);

        gameContainer.add(gameBackButton);
        gameContainer.add(scoreText);
        gameContainer.add(timeText);
        gameContainer.add(gamePanel);
        gameContainer.add(gameInstructionsButton);
        gameContainer.add(restartGameButton);
        gameContainer.add(gameBackLabel);
        gameContainer.add(gameInstructionLabel);
        gameContainer.add(restartGameLabel);

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
            loadLeaderboard();
            sortLeaderboardByTime();

            LeaderboardLabelTexts();
            createRemoveButtons();

            buttonSound.playSound();


        } else if (e.getSource() == settingsButton) {
            crd.show(cPane, "settings");
            buttonSound.playSound();

        } else if (e.getSource() == quitButton) {
            System.exit(0);

        } else if (e.getSource() == settingsBackButton) {

            crd.show(cPane, "main menu");
            buttonSound.playSound();

        } else if (e.getSource() == leaderboardBackButton) {
            deleteRemoveButtons();
            crd.show(cPane, "main menu");
            buttonSound.playSound();
        }

        else if (e.getSource() == chooseFileButton) {
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
        } else if (e.getSource() == gameBackButton) {

            String[] o = {"Ok", "Peruuta"};
            int i = JOptionPane.showOptionDialog(this, "Haluatko palata\ntakaisin päävalikkoon?\nEt voi jatkaa peliä.", "Varoitus", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, o, null);

            if (i == 0) {
                gamePanel.stop();
                crd.show(cPane, "main menu");
                buttonSound.playSound();
            }

        } else if (e.getSource() == restartGameButton) {
            String[] o = {"Kyllä", "Ei"};
            int i = JOptionPane.showOptionDialog(this, "Haluatko varmasti\naloittaa uuden pelin?", "Varoitus", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, o, null);

            if (i == 0) {
                gamePanel.start();
                buttonSound.playSound();
            }
        } else if (e.getSource() == gameInstructionsButton) {

            String rules = "Klondike pasianssin tarkoituksena on täyttää tyhjät peruspakat pelin oikeassa yläkulmassa.\n" +
                    "Yhteen peruspakkaan voi laittaa vain yhden maan kortteja.\n" +
                    "Tyhjien pakkojen täyttö aloitetaan pienimmästä kortista eli ässästä.\nSeuraavaksi tyhjään pakkaan laitetaan seuraava kortti arvojärjestyksessä.\n" +
                    "Pelin voittaa, kun kaikki tyhjät peruspakat ovat täytetty ässästä kuninkaaseen asti.\n" +
                    "Vasemmassa yläreunassa on käsipakka, mistä voi ottaa kortteja.\n" +
                    "Pöydällä on seitsemän pinoa. Pinojen päällimmäisen kortin alle voi\nlisätä eri värisen maan kortin, jonka arvo on yhtä pienempi.\n" +
                    "Tyhjään pinoon voi asettaa Kuningas-arvoisen kortin.\n";

            String[] o = {"Ok"};
            JOptionPane.showOptionDialog(this, rules, "Säännöt", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, o, null);
        }

    }

    public void updateBounds() {

        int width = cPane.getWidth();
        int height = cPane.getHeight();

        Rectangle backButtonRectangle = new Rectangle(25, height - 75, 150, 50);

        Font font = new Font("Calibri", Font.BOLD, width / 50);

        // menu
        newGameButton.setBounds((width / 2) - (width / 4), height - (int) (height * 0.85), (width / 2), height / 15);
        leaderboardButton.setBounds((width / 2) - (width / 4), height - (int) (height * 0.65), (width / 2), height / 15);
        settingsButton.setBounds((width / 2) - (width / 4), height - (int) (height * 0.45), (width / 2), height / 15);
        quitButton.setBounds((width / 2) - (width / 4), height - (int) (height * 0.25), (width / 2), height / 15);

        newGameButton.setFont(font);
        leaderboardButton.setFont(font);
        settingsButton.setFont(font);
        quitButton.setFont(font);


        // pisteet
        leaderboardBackButton.setBounds(backButtonRectangle);
        leaderboardGamerLabel.setBounds((int) (width * 0.1), (int) (height * 0.1), (int) (width * 0.125), (int) (getHeight() * 0.1));
        leaderboardTimeLabel.setBounds((int) (width * 0.3), (int) (height * 0.1), (int) (width * 0.125), (int) (getHeight() * 0.1));
        leaderboardMovesLabel.setBounds((int) (width * 0.5), (int) (height * 0.1), (int) (width * 0.125), (int) (getHeight() * 0.1));
        leaderboardDateLabel.setBounds((int) (width * 0.7), (int) (height * 0.1), (int) (width * 0.2), (int) (getHeight() * 0.1));

        leaderboardGamerLabel.setFont(font);
        leaderboardTimeLabel.setFont(font);
        leaderboardMovesLabel.setFont(font);
        leaderboardDateLabel.setFont(font);

        leaderboardGamer.setBounds((int) (width * 0.1), (int) (height * 0.26), (int) (width * 0.125), (int) (getHeight() * 0.5));
        leaderboardTime.setBounds((int) (width * 0.3), (int) (height * 0.26), (int) (width * 0.125), (int) (getHeight() * 0.5));
        leaderboardMoves.setBounds((int) (width * 0.5), (int) (height * 0.26), (int) (width * 0.125), (int) (getHeight() * 0.5));
        leaderboardDate.setBounds((int) (width * 0.65), (int) (height * 0.26), (int) (width * 0.2), (int) (getHeight() * 0.5));

        leaderboardGamer.setFont(font);
        leaderboardTime.setFont(font);
        leaderboardMoves.setFont(font);
        leaderboardDate.setFont(font);

        // asetukset
        settingsBackButton.setBounds(backButtonRectangle);
        resolutionComboBox.setBounds((int) (width * 0.625), (int) (height * 0.17), (int) (width * 0.2), (int) (height * 0.0625));
        resolutionLabel.setBounds((int) (width * 0.275), (int) (height * 0.17), (int) (width * 0.4), (int) (height * 0.0625));
        volumeSlider.setBounds((int) (width * 0.625), (int) (height * 0.35), (int) (width * 0.2), (int) (height * 0.0625));
        volumeLabel.setBounds((int) (width * 0.275), (int) (height * 0.35), (int) (width * 0.4), (int) (height * 0.0625));
        chooseFileButton.setBounds((int) (width * 0.625), (int) (height * 0.52), (int) (width * 0.2), (int) (height * 0.0625));
        chooseFileLabel.setBounds((int) (width * 0.275), (int) (height * 0.52), (int) (width * 0.4), (int) (height * 0.0625));
        defaultCardsButton.setBounds((int) (width * 0.625), (int) (height * 0.70), (int) (width * 0.2), (int) (height * 0.0625));

        settingsBackButton.setFont(font);
        resolutionComboBox.setFont(font);
        resolutionLabel.setFont(font);
        volumeSlider.setFont(font);
        volumeLabel.setFont(font);
        chooseFileButton.setFont(font);
        chooseFileLabel.setFont(font);
        defaultCardsButton.setFont(font);




        // peli
        gameBackButton.setBounds(backButtonRectangle);
        scoreText.setBounds(4 * width / 5 - (int) (width * 0.15), height - 100, (int) (width * 0.3), 100);
        timeText.setBounds(width / 2 - (int) (width * 0.15), height - 100, (int) (width * 0.3), 100);
        gamePanel.setBounds(0, 0, (int) (width * 0.9), height - 100);
        gameInstructionsButton.setBounds((int) (width * 0.91), gamePanel.deckPaddingY, (int) (width * 0.08), (int) (width * 0.08));
        gameInstructionLabel.setBounds((int) (width * 0.91), (int) (gameInstructionsButton.getLocation().y + gameInstructionsButton.getSize().getHeight() + 5), (int) (width * 0.08), (int) (width * 0.08));

        restartGameButton.setBounds((int) (width * 0.91), 2 * gamePanel.deckPaddingY + gamePanel.cardHeight, (int) (width * 0.08), (int) (width * 0.08));
        restartGameLabel.setBounds((int) (width * 0.91), (int) (restartGameButton.getLocation().y + restartGameButton.getSize().getHeight() + 5), (int) (width * 0.08), (int) (width * 0.08));

        gameBackButton.setFont(font);
        scoreText.setFont(font);
        timeText.setFont(font);
        gamePanel.setFont(font);
        gameInstructionsButton.setFont(font);
        gameInstructionLabel.setFont(font);
        restartGameButton.setFont(font);
        restartGameLabel.setFont(font);

        // takaisin nappien ikoni
        Image image = backButtonIcon.getImage();
        ImageIcon newImg = new ImageIcon(image.getScaledInstance((int) (gameBackButton.getWidth() * 0.95), (int) (gameBackButton.getHeight() * 0.9), java.awt.Image.SCALE_SMOOTH));
        gameBackButton.setIcon(newImg);
        leaderboardBackButton.setIcon(newImg);
        settingsBackButton.setIcon(newImg);

        gameBackButton.setFont(font);
        leaderboardBackButton.setFont(font);
        settingsBackButton.setFont(font);

        // takaisin nappien teksti
        settingsBackLabel.setBounds(gameBackButton.getX() + backButtonRectangle.width + 20, backButtonRectangle.y - (int) (backButtonRectangle.height * 0.5), 200, 100);
        gameBackLabel.setBounds(gameBackButton.getX() + backButtonRectangle.width + 20, backButtonRectangle.y - (int) (backButtonRectangle.height * 0.5), 200, 100);
        leaderboardsBackLabel.setBounds(gameBackButton.getX() + backButtonRectangle.width + 20, backButtonRectangle.y - (int) (backButtonRectangle.height * 0.5), 200, 100);

        settingsBackLabel.setFont(font);
        gameBackLabel.setFont(font);
        leaderboardsBackLabel.setFont(font);

        // pelinäkymän nappien ikonit
        image = restartGameButtonIcon.getImage();
        newImg = new ImageIcon(image.getScaledInstance((int) (restartGameButton.getWidth() * 0.95), (int) (restartGameButton.getHeight() * 0.9), java.awt.Image.SCALE_SMOOTH));
        restartGameButton.setIcon(newImg);


        image = gameInstructionIcon.getImage();
        newImg = new ImageIcon(image.getScaledInstance((int) (gameInstructionsButton.getWidth() * 0.95), (int) (gameInstructionsButton.getHeight() * 0.9), java.awt.Image.SCALE_SMOOTH));
        gameInstructionsButton.setIcon(newImg);


    }

    public void buttonMouseOvers() {

        // menu
        newGameButton.setToolTipText("Aloittaa uuden pelin");
        leaderboardButton.setToolTipText("Pelin läpäisseiden nimet ovat täällä");
        settingsButton.setToolTipText("Voit säätää asetuksia tästä");
        quitButton.setToolTipText("Sulkee ohjelman");

        // pisteet
        leaderboardBackButton.setToolTipText("Palaa päävalikkoon");

        // asetukset
        settingsBackButton.setToolTipText("Palaa päävalikkoon");
        resolutionComboBox.setToolTipText("Voit valita peli-ikkunan koon tästä");
        volumeSlider.setToolTipText("Liukusäätimestä voit säätää äänenvoimakkuutta");
        chooseFileButton.setToolTipText("Voit valita tietokoneeltasi tiedoston, josta korttien kuvat haetaan");
        defaultCardsButton.setToolTipText("Tästä voit palauttaa pelin alkuperäiset kortit");
        

        // peli
        gameBackButton.setToolTipText("Palaa päävalikkoon. Lopettaa pelin");
        gameInstructionsButton.setToolTipText("Pelin ohjeet");
        restartGameButton.setToolTipText("Aloittaa uuden pelin. Vanha peli menetetään");


    }

    public void updateGameTexts() {
        scoreText.setText("Siirrot: " + gamePanel.moves);
        long timeInSeconds = TimeUnit.SECONDS.convert(System.nanoTime() - gamePanel.startTime, TimeUnit.NANOSECONDS);
        timeText.setText(String.format("Aika: %d:%02d", timeInSeconds / 60, timeInSeconds % 60));
    }

    public void loadLeaderboard() {
        try {
            FileInputStream fis = new FileInputStream("pisteet.data");
            ObjectInputStream ois = new ObjectInputStream(fis);
            leaderboardItems = (ArrayList<LeaderboardItem>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveLeaderboard() {
        try {
            FileOutputStream fos = new FileOutputStream("pisteet.data");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(leaderboardItems);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sortLeaderboardByTime() {
        Collections.sort(leaderboardItems, new Comparator<LeaderboardItem>() {
            @Override
            public int compare(LeaderboardItem o1, LeaderboardItem o2) {
                return Integer.compare(o1.getGameSeconds(), o2.getGameSeconds());
            }
        });
    }

    public void LeaderboardLabelTexts() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        StringBuilder sb;

        sb = new StringBuilder("<html>");

        int i;

        for (i = 0; i < Math.min(5, leaderboardItems.size()); i++) {
            sb.append(leaderboardItems.get(i).name);
            sb.append("<br/>");
            sb.append("<br/>");
        }
        sb.append("</html>");
        leaderboardGamer.setText(sb.toString());

        sb = new StringBuilder("<html>");

        for (i = 0; i < Math.min(5, leaderboardItems.size()); i++) {
            sb.append(leaderboardItems.get(i).gameSeconds);
            sb.append("<br/>");
            sb.append("<br/>");
        }
        sb.append("</html>");
        leaderboardTime.setText(sb.toString());

        sb = new StringBuilder("<html>");

        for (i = 0; i < Math.min(5, leaderboardItems.size()); i++) {
            sb.append(leaderboardItems.get(i).moves);
            sb.append("<br/>");
            sb.append("<br/>");
        }
        sb.append("</html>");
        leaderboardMoves.setText(sb.toString());

        sb = new StringBuilder("<html>");

        for (i = 0; i < Math.min(5, leaderboardItems.size()); i++) {
            String dateText = leaderboardItems.get(i).dateTime.format(formatter);
            sb.append(dateText);
            sb.append("<br/>");
            sb.append("<br/>");
        }
        sb.append("</html>");
        leaderboardDate.setText(sb.toString());

    }

    public void createRemoveButtons() {

        Image image = removeButtonIcon.getImage();
        int i = 0;
        int width = getWidth();
        int height = getHeight();
        removeButtons = new JButton[Math.min(5, leaderboardItems.size())];

        for (i = 0; i < Math.min(5, leaderboardItems.size()); i++) {
            removeButtons[i] = new JButton(removeButtonIcon);
            removeButtons[i].setBounds((int) (width * 0.90), (int) (height * 0.25) + i * (int) (leaderboardGamer.getFont().getSize() * 2.5f), (int) (width * 0.02), (int) (width * 0.02));
            ImageIcon newImg = new ImageIcon(image.getScaledInstance((int) (removeButtons[i].getWidth() * 0.95), (int) (removeButtons[i].getHeight() * 0.9), java.awt.Image.SCALE_SMOOTH));
            removeButtons[i].setIcon(newImg);
            removeButtons[i].setToolTipText("Poista pelaajan tulos");
            removeButtons[i].addActionListener(new ActionListenerWithInteger(i));
            leaderboardContainer.add(removeButtons[i]);
        }
    }

    public void deleteRemoveButtons() {
        for (int i = removeButtons.length - 1; i >= 0; i--) {
            leaderboardContainer.remove(removeButtons[i]);
        }
    }

    //Poista nappien kuuntelija
    public class ActionListenerWithInteger implements ActionListener {
        private int number;

        public ActionListenerWithInteger(int number) {
            this.number = number;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String[] o = {"Kyllä", "Ei"};
            int j = JOptionPane.showOptionDialog(cPane, "Haluatko varmasti\npoistaa tuloksen?", "Varoitus", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, o, null);

            if (j == 0) {
                leaderboardItems.remove(this.number);
                LeaderboardLabelTexts();
                deleteRemoveButtons();
                createRemoveButtons();
                saveLeaderboard();
                repaint();

            }
        }
    }

    // main method
    public static void main(String argvs[]) {

        App app = new App();

        app.setVisible(true);
        app.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
