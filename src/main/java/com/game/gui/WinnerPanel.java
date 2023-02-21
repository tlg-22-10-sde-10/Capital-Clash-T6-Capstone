package com.game.gui;

import com.game.players.Computer;
import com.game.players.Player;
import com.game.storage.StockInventory;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.MissingFormatArgumentException;
import java.util.concurrent.TimeUnit;

public class WinnerPanel extends JPanel implements ActionListener {

    IntroPanel introPanel = new IntroPanel();
    Font startBtnFont = new Font("Times New Roman", Font.PLAIN, 30);
    Font sorryYouLoseTextArea2Font = new Font("Serif", Font.PLAIN, 20);
    JButton startBtn;
    JButton exitBtn;
    JButton exitBtn2;
    private Player player;
    private Computer computer;
    StockInventory stockInventory;

    public WinnerPanel() throws IOException, MissingFormatArgumentException {

        GuiGame test = GuiGame.getInstance();

        this.player = test.getPlayer();
        this.computer = test.getComputer();
        this.stockInventory = test.getStockInventory();

        setPreferredSize(new Dimension(Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT));
        setLayout(null);
        setBackground(Color.decode(Global.BG_COLOR));

        IconBuilder icon = new IconBuilder();
        ImageIcon titleIcon = icon.imageIcon("/Winner.png", Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT, Image.SCALE_DEFAULT);

        JLabel imageLabel = new JLabel(titleIcon);
        imageLabel.setBounds(0, 0, Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT);

        double playerStockBalance = player.getBalanceFromHolding(stockInventory);
        String yourB = (String.format("%.2f", playerStockBalance + player.getAccount().getCashBalance()));
        JTextArea yourBalance = new JTextArea(yourB);
//        yourBalance.setFont(new Font("Arial "))
        yourBalance.setBounds(600, 287, 200, 25);
        yourBalance.setBackground(Color.black);
        yourBalance.setForeground(Color.white);
        yourBalance.setFont(sorryYouLoseTextArea2Font);
        yourBalance.setLineWrap(true);
        add(yourBalance);

        double computerStockBalance = computer.getBalanceFromHolding(stockInventory);
        String brotherB = (String.format("%.2f", computerStockBalance + computer.getAccount().getCashBalance()));
        JTextArea brotherBalance = new JTextArea(brotherB);
        brotherBalance.setBounds(765, 317, 200, 25);
        brotherBalance.setBackground(Color.black);
        brotherBalance.setForeground(Color.white);
        brotherBalance.setFont(sorryYouLoseTextArea2Font);
        brotherBalance.setLineWrap(true);
        add(brotherBalance);
        

        exitBtn = new JButton("YES");
        exitBtn.setOpaque(true);
        exitBtn.setBorder(null);
        exitBtn.setBounds(200, 525, 125, 40);
        exitBtn.setBackground(Color.YELLOW);
        exitBtn.setForeground(Color.black);
        exitBtn.setFont(startBtnFont);
        exitBtn.addActionListener(this);
        exitBtn.setActionCommand("yes");

        exitBtn2 = new JButton("NO");
        exitBtn2.setOpaque(true);
        exitBtn2.setBorder(null);
        exitBtn2.setBounds(200, 600, 125, 40);
        exitBtn2.setBackground(Color.YELLOW);
        exitBtn2.setForeground(Color.black);
        exitBtn2.setFont(startBtnFont);
        exitBtn2.addActionListener(this);
        exitBtn2.setActionCommand("no");


        setVisible(true);
        add(imageLabel);
        add(exitBtn);
        add(exitBtn2);

        Computer computer;
//        System.out.println(String.format("%.2f", computer.getAccount().getCashBalance()));
//        Computer player = null;
//        System.out.println(String.format("%.2f", player.getAccount().getCashBalance()));

    }
    int x = 0;
    int y= 100;
    int a = 400;
    int b = 200;

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("yes")) {
            try {
                TimeUnit.SECONDS.sleep(1);
                GuiGame.getInstance().reset();
                Frame.getScreen(introPanel);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } else if (e.getActionCommand().equals("no")) {
            System.exit(0);
        }
    }

}

