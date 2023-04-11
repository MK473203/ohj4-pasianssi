package com.ohjelmointi4;

import javax.swing.*;  
import java.awt.Rectangle;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

         JFrame f=new JFrame("pasianssipeli :)");//creating instance of JFrame  


        ImageIcon icon = new ImageIcon("/nimetön.png");

        f.setIconImage(icon.getImage());
        f.setResizable(false);
                
        JButton a=new JButton("uusi peli");//creating instance of JButton  
        a.setBounds(100,100,100, 40);//x axis, y axis, width, height  
                
        JButton b=new JButton("pisteet");
        b.setBounds(100,200,100, 40);

        JButton c=new JButton("asetukset");
        c.setBounds(100,300,100, 40);

        JButton d=new JButton("ohjeet");
        d.setBounds(100,400,100, 40);

        Rectangle r = new Rectangle(600, 100, 100, 100);

        JSlider e = new JSlider(JSlider.HORIZONTAL,0, 100, 10);
        e.setBounds(r);

        f.add(a);
        f.add(b);//adding button in JFrame  
        f.add(c);
        f.add(d);
        f.add(e);
                
        f.setSize(800,600);//400 width and 500 height  
        f.setLayout(null);//using no layout managers  
        f.setVisible(true);//making the frame visible  

        System.out.println( "Hello World!" );
    }
}
