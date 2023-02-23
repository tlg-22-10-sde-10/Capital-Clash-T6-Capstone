package com.game.gui;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

public class Sound {

    Clip clip;
    float previousVol = 0;
    float currentVol = 0;
    FloatControl floatCtrl;
    boolean mute = false;

    public void setFile(URL url) {

        try {
            AudioInputStream sound = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(sound);
            floatCtrl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch (Exception e) {

        }
    }

    public void play(URL url) {

        clip.setFramePosition(0);
        clip.start();
    }

    public void loop(URL url) {

        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop(URL url) {

        clip.stop();

    }

    public void volumeUp() {
        currentVol += 1.0f;
        if(currentVol > 6.0f) {
            currentVol = 6.0f;
        }
        floatCtrl.setValue(currentVol);
    }

    public void volumeDown() {
        currentVol -= 1.0f;
        if(currentVol < -80.0f) {
            currentVol = -80.0f;
        }
        floatCtrl.setValue(currentVol);
    }

    public void muteVolume() {
        if(mute == false) {
            previousVol = currentVol;
            currentVol = -80.0f;
            floatCtrl.setValue(currentVol);
            mute = true;
        }
        else if(mute == true) {
            currentVol = previousVol;
            floatCtrl.setValue(currentVol);
            mute = false;
        }
    }
}
