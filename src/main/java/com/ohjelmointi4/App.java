package com.ohjelmointi4;

import javax.swing.*;
import java.awt.Canvas;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Hello world!
 *
 */
public class App extends JFrame implements ActionListener {

    CardLayout crd;

    Container cPane;

    // constructor of the class
    App() {

        setSize(800, 600);

        cPane = getContentPane();

        // default constructor used
        // therefore, components will
        // cover the whole area
        crd = new CardLayout();

        cPane.setLayout(crd);

        Container mainMenuContainer = new Container();
        Container leaderboardContainer = new Container();
        Container settingsContainer = new Container();

        ImageIcon icon = new ImageIcon("/nimetön.png");

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

        // adding listeners to it
        a.addActionListener(this);
        b.addActionListener(this);
        c.addActionListener(this);

        mainMenuContainer.add(a);
        mainMenuContainer.add(b);
        mainMenuContainer.add(c);
        mainMenuContainer.add(d);

        cPane.add("main menu", mainMenuContainer);
        cPane.add("leaderboards", leaderboardContainer);
        cPane.add("settings", settingsContainer);


    }

    public void actionPerformed(ActionEvent e) {
        // Upon clicking the button, the next card of the container is shown
        // after the last card, again, the first card of the container is shown upon clicking

        if (e.getActionCommand() == "Uusi peli") {
            crd.show(cPane, "leaderboards");
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
