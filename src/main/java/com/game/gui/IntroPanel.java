package com.game.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class IntroPanel extends JPanel implements ActionListener {
    Font btnFont = new Font("Arial", Font.BOLD, 15);
    static GamePanel gamePanel;
    static {
        try {
            gamePanel = new GamePanel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public IntroPanel() throws IOException {
        // Setting up the dimensions and background color
        setPreferredSize (new Dimension(Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT));
        setLayout(null);
        setBackground(Color.decode(Global.BG_COLOR));

        // The title background image
        IconBuilder icon = new IconBuilder();
        ImageIcon titleIcon = icon.imageIcon("/ccinstructions.png", Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT, Image.SCALE_DEFAULT);

        // Placing the background image
        JLabel imageLabel = new JLabel(titleIcon);
        imageLabel.setBounds (0, 0, Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT);

        // Continue button
        JButton playBtn = new JButton("Play");
        playBtn.setOpaque(false);
        playBtn.setBackground(Color.decode(Global.MAIN_COLOR));
        playBtn.setForeground(Color.decode(Global.BTN_COLOR));
        playBtn.setBorder(null);
        playBtn.setFont(btnFont);
        playBtn.addActionListener(this);
        playBtn.setActionCommand("continue");
        playBtn.setBounds(430, 600, 150, 50);
        ImageIcon playIcon = icon.imageIcon("/buttonbg.png", 150, 50, Image.SCALE_DEFAULT);
        JLabel playBg = new JLabel(playIcon);
        playBg.setBounds(430, 600, 150, 50);

        add(playBtn);
        add(playBg);
        add(imageLabel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("continue")) {
            try {
                TimeUnit.SECONDS.sleep(1);
//                Frame.getScreen(gamePanel);
                Frame.getScreen(new GameClientGui());
            } catch (InterruptedException | IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}