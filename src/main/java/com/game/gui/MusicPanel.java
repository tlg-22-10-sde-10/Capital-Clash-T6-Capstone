package com.game.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class MusicPanel extends JPanel implements ActionListener{

    GameClientGui gcu = new GameClientGui();
    static Sound sound = new Sound();
    JButton closeBtn;

    public MusicPanel() {

        setPreferredSize(new Dimension(Global.SCREEN_WID, Global.SCREEN_HT));
        JPanel inner = new JPanel();
        inner.setPreferredSize(new Dimension(Global.SCREEN_WID, Global.SCREEN_HT));

        JButton volumeUpBtn = new JButton("+");
        volumeUpBtn.addActionListener(e -> sound.volumeUp());
        inner.add(volumeUpBtn);

        JButton volumeDownBtn = new JButton("-");
        volumeDownBtn.addActionListener(e -> sound.volumeDown());
        inner.add(volumeDownBtn);

        JButton muteBtn = new JButton("mute");
        muteBtn.addActionListener(e -> sound.muteVolume());
        inner.add(muteBtn);

        closeBtn = new JButton("CLOSE");
        closeBtn.setActionCommand("close");
        closeBtn.addActionListener(this);
        closeBtn.setBounds(550, 675, 125, 50);
        closeBtn.setOpaque(true);

        inner.add(closeBtn);
        add(inner);
        setVisible(true);


    }

    public static void playMusic(URL url) {
        sound.setFile(url);
        sound.play(url);
        sound.loop(url);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("close")) {
            try {
                TimeUnit.SECONDS.sleep(1);
//                GuiGame.getInstance().reset();
                Frame.getScreen(gcu);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}

