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

public class TiePanel extends JPanel implements ActionListener {
    IntroPanel introPanel = new IntroPanel();
    Font startBtnFont = new Font("Arial", Font.PLAIN, 25);
    Font sorryYouLoseTextArea2Font = new Font("Serif", Font.PLAIN, 25);
    JButton startBtn;
    JButton exitBtn;
    JButton exitBtn2;
    private Player player;
    private Computer computer;
    StockInventory stockInventory;

    public TiePanel (double netPlayerBalance, double netComputerBalance) throws IOException, MissingFormatArgumentException {

        GuiGame test = GuiGame.getInstance();

        this.player = test.getPlayer();
        this.computer = test.getComputer();
        this.stockInventory = test.getStockInventory();

        setPreferredSize(new Dimension(Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT));
        setLayout(null);
        setBackground(Color.decode(Global.BG_COLOR));

        IconBuilder icon = new IconBuilder();
        ImageIcon titleIcon = icon.imageIcon("/tie.png", Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT, Image.SCALE_DEFAULT);

        JLabel imageLabel = new JLabel(titleIcon);
        imageLabel.setBounds(0, 0, Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT);

        double playerStockBalance = player.getBalanceFromHolding(stockInventory);
        String yourB = String.format("%.2f", netPlayerBalance);
        JTextArea yourBalance = new JTextArea(yourB);
//        yourBalance.setFont(new Font("Arial "))
        yourBalance.setBounds(600, 287, 200, 25);
        yourBalance.setBackground(Color.black);
        yourBalance.setForeground(Color.white);
        yourBalance.setFont(sorryYouLoseTextArea2Font);
        yourBalance.setLineWrap(true);
        add(yourBalance);

        double computerStockBalance = computer.getBalanceFromHolding(stockInventory);
        String brotherB = String.format("%.2f", netComputerBalance);
        JTextArea brotherBalance = new JTextArea(brotherB);
        brotherBalance.setBounds(765, 317, 200, 25);
        brotherBalance.setBackground(Color.black);
        brotherBalance.setForeground(Color.white);
        brotherBalance.setFont(sorryYouLoseTextArea2Font);
        brotherBalance.setLineWrap(true);
        add(brotherBalance);


        exitBtn = new JButton("YES");
        exitBtn.setOpaque(false);
        exitBtn.setBorder(null);
        exitBtn.setBounds(200, 525, 150, 50);
        exitBtn.setBackground(Color.decode(Global.MAIN_COLOR));
        exitBtn.setForeground(Color.decode(Global.BTN_COLOR));
        exitBtn.setFont(startBtnFont);
        exitBtn.addActionListener(this);
        exitBtn.setActionCommand("yes");
        ImageIcon yesIcon = icon.imageIcon("/buttonbg.png", 150, 50, Image.SCALE_DEFAULT);
        JLabel yesBg = new JLabel(yesIcon);
        yesBg.setBounds(200, 525, 150, 50);

        exitBtn2 = new JButton("NO");
        exitBtn2.setOpaque(false);
        exitBtn2.setBorder(null);
        exitBtn2.setBounds(200, 600, 150, 50);
        exitBtn2.setBackground(Color.decode(Global.MAIN_COLOR));
        exitBtn2.setForeground(Color.decode(Global.BTN_COLOR));
        exitBtn2.setFont(startBtnFont);
        exitBtn2.addActionListener(this);
        exitBtn2.setActionCommand("no");
        ImageIcon noIcon = icon.imageIcon("/buttonbg.png", 150, 50, Image.SCALE_DEFAULT);
        JLabel noBg = new JLabel(noIcon);
        noBg.setBounds(200, 600, 150, 50);


        setVisible(true);
        add(exitBtn);
        add(exitBtn2);
        add(yesBg);
        add(noBg);
        add(imageLabel);


        Computer computer;
//        System.out.println(String.format("%.2f", computer.getAccount().getCashBalance()));
//        Computer player = null;
//        System.out.println(String.format("%.2f", player.getAccount().getCashBalance()));

    }

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
