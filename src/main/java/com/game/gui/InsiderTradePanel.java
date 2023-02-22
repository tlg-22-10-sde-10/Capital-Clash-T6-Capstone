package com.game.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class InsiderTradePanel extends JPanel implements ActionListener {
    private JButton noBtn;
    private JButton yesBtn;
    Font btnFont = new Font("Arial", Font.BOLD, 20);
    public InsiderTradePanel() throws IOException {
        // Setting up the dimensions and background color
        setPreferredSize (new Dimension(Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT));
        setLayout(null);
        setBackground(Color.decode(Global.BG_COLOR));

        // The title background image
        IconBuilder icon = new IconBuilder();
        ImageIcon titleIcon = icon.imageIcon("/caught.png", Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT, Image.SCALE_DEFAULT);

        // Placing the background image
        JLabel imageLabel = new JLabel(titleIcon);
        imageLabel.setBounds (0, 0, Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT);

        yesBtn = new JButton("YES");
        yesBtn.setActionCommand("yes");
        yesBtn.addActionListener(this);
        yesBtn.setBounds(400, 675, 125, 50);
        yesBtn.setOpaque(true);
        yesBtn.setBackground(Color.decode(Global.MAIN_COLOR));
        yesBtn.setForeground(Color.decode(Global.BTN_COLOR));
        yesBtn.setBorder(null);
        yesBtn.setFont(btnFont);

        noBtn = new JButton("NO");
        noBtn.setActionCommand("no");
        noBtn.addActionListener(this);
        noBtn.setBounds(550, 675, 125, 50);
        noBtn.setOpaque(true);
        noBtn.setBackground(Color.decode(Global.MAIN_COLOR));
        noBtn.setForeground(Color.decode(Global.BTN_COLOR));
        noBtn.setBorder(null);
        noBtn.setFont(btnFont);

        add(yesBtn);
        add(noBtn);
        add(imageLabel);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("yes")) {
            try {
                TimeUnit.SECONDS.sleep(1);
                GuiGame.getInstance().reset();
                Frame.getScreen(new IntroPanel());
            } catch (InterruptedException | IOException ex) {
                ex.printStackTrace();
            }
        } else if (e.getActionCommand().equals("no")) {
            System.exit(0);
        }

    }
}
