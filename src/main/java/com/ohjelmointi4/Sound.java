package com.ohjelmointi4;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class Sound {

    
    private String fileName;        // Clip tukee .wav .aiff .au muotoisia äänitiedostoja 

    private int volume;

    public Sound(String fileName) {
        this.fileName = fileName;
    }

    // metodi avaa äänitiedoston ja Clip toistaa äänen
    public void playSound() {
        try {
            AudioInputStream  audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource(this.fileName));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            //clip.stop();
            audioInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

}