package com.game.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class IntroPanel extends JPanel implements ActionListener {
    Font btnFont = new Font("Bebas Neue", Font.BOLD, 20);
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
        JButton startBtn = new JButton("PLAY");
        startBtn.setOpaque(true);
        startBtn.setBackground(Color.decode(Global.MAIN_COLOR));
        startBtn.setForeground(Color.WHITE);
        startBtn.setBorder(null);
        startBtn.setFont(btnFont);
        startBtn.addActionListener(this);
        startBtn.setActionCommand("continue");
        startBtn.setBounds(448, 600, 125, 40);

        add(startBtn);
        add(imageLabel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("continue")) {
            try {
                TimeUnit.SECONDS.sleep(1);
//                Frame.getScreen(gamePanel);
                Frame.getScreen(new GameClientGui());
                        } catch (Exception error) {
                System.out.println("An error occurred: " + error);
            }
        }
    }
}