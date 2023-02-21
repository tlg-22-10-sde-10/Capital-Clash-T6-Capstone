package com.game.gui;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TitlePanel extends JPanel implements ActionListener {
    private static final int BOLD = 20;
    IntroPanel introPanel = new IntroPanel();
    Font btnFont = new Font("Bebas Neue", Font.BOLD, 20);

    public TitlePanel() throws IOException {
        // Setting up the dimensions and background color
        setPreferredSize(new Dimension(Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT));
        setLayout(null);
        setBackground(Color.decode(Global.BG_COLOR));

        // The title background image
        IconBuilder icon = new IconBuilder();
        ImageIcon titleIcon = icon.imageIcon("/CapitalClash.png",500, 500, Image.SCALE_DEFAULT);

        // Placing the background image
        JLabel imageLabel = new JLabel(titleIcon);
        imageLabel.setBounds(0, 0, Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT);

        // Start button
        JButton startBtn = new JButton("START");
        startBtn.setOpaque(true);
        startBtn.setBackground(Color.decode("#65a147"));
        startBtn.setForeground(Color.WHITE);
        startBtn.setBorder(null);
        startBtn.setFont(btnFont);
        startBtn.addActionListener(this);
        startBtn.setActionCommand("start");
        startBtn.setBounds(448, 525, 125, 40);

        add(startBtn);
        add(imageLabel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("start")) {
            try {
                TimeUnit.SECONDS.sleep(1);
                Frame.getScreen(introPanel);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

    }}
