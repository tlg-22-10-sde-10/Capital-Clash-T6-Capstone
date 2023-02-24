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
        yesBtn.setBounds(400, 675, 150, 50);
        yesBtn.setOpaque(false);
        yesBtn.setBackground(Color.decode(Global.MAIN_COLOR));
        yesBtn.setForeground(Color.decode(Global.BTN_COLOR));
        yesBtn.setBorder(null);
        yesBtn.setFont(btnFont);
        ImageIcon yesIcon = icon.imageIcon("/buttonbg.png", 150, 50, Image.SCALE_DEFAULT);
        JLabel yesBg = new JLabel(yesIcon);
        yesBg.setBounds(400, 675, 150, 50);


        noBtn = new JButton("NO");
        noBtn.setActionCommand("no");
        noBtn.addActionListener(this);
        noBtn.setBounds(575, 675, 150, 50);
        noBtn.setOpaque(false);
        noBtn.setBackground(Color.decode(Global.MAIN_COLOR));
        noBtn.setForeground(Color.decode(Global.BTN_COLOR));
        noBtn.setBorder(null);
        noBtn.setFont(btnFont);
        ImageIcon noIcon = icon.imageIcon("/buttonbg.png", 150, 50, Image.SCALE_DEFAULT);
        JLabel noBg = new JLabel(noIcon);
        noBg.setBounds(575, 675, 150, 50);

        add(yesBtn);
        add(noBtn);
        add(yesBg);
        add(noBg);
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
