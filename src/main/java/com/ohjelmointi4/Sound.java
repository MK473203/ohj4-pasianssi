package com.ohjelmointi4;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;


public class Sound {

    
    private String fileName;        // Clip tukee .wav .aiff .au muotoisia äänitiedostoja 
    private int volume;

    public Sound(String fileName) {
        this.fileName = fileName;
        this.volume = -55;
    }

    // metodi avaa äänitiedoston ja Clip toistaa äänen
    public void playSound() {

        if (this.volume > -80) {

            try {
                AudioInputStream  audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource(this.fileName));
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue((volumeForFloatControl() ));
    
                clip.start();
                //clip.stop();
                audioInputStream.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    
    public int getVolume() {
        return this.volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public float volumeForFloatControl() {
       return (float) ( this.volume * 0.2 );
    }

}