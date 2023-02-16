package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class LoserPanel extends JPanel implements ActionListener {

    IntroPanel introPanel = new IntroPanel();
    Container container;
    JPanel sorryYouLosePanel, exitBtnPanel;
    JLabel titleLabel;
    Font titleFont = new Font("Times New Roman", Font.PLAIN, 100);
    Font startBtnFont = new Font("Times New Roman", Font.PLAIN, 30);
    Font sorryYouLoseTextArea2Font = new Font("Serif", Font.PLAIN, 14);
    JButton startBtn;
    JButton exitBtn;
    JButton exitBtn2;
    JTextArea sorryYouLoseTextArea;
    JTextArea sorryYouLoseTextArea2;
    JTextArea sorryYouLoseTextArea3;
    JTextArea sorryYouLoseTextArea4;
    JTextArea sorryYouLoseTextArea5;

    public LoserPanel() throws IOException {

        sorryYouLosePanel = new JPanel();

        setPreferredSize (new Dimension(Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT));
        setLayout(null);
        setBackground(Color.decode(Global.BG_COLOR));

        IconBuilder icon = new IconBuilder();
        ImageIcon titleIcon = icon.imageIcon("/loser.png", Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT, Image.SCALE_DEFAULT);

        JLabel imageLabel = new JLabel(titleIcon);
        imageLabel.setBounds(0, 0, Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT);

//        sorryYouLosePanel = new JPanel();
////        sorryYouLosePanel.setBounds(250, 0, 800, 400);
////        sorryYouLosePanel.setBackground(Color.black);
//
//        sorryYouLoseTextArea = new JTextArea("Sorry You Lose!");
//        sorryYouLoseTextArea.setBounds(250, 100, 675, 100);
//        sorryYouLoseTextArea.setBackground(Color.black);
//        sorryYouLoseTextArea.setForeground(Color.yellow);
//        sorryYouLoseTextArea.setFont(titleFont);
//        sorryYouLoseTextArea.setLineWrap(true);
//        sorryYouLosePanel.add(sorryYouLoseTextArea);
//
//        sorryYouLoseTextArea2 = new JTextArea("\n \n Your total balance is $");
//        sorryYouLoseTextArea2.setBounds(250, 100, 675, 100);
//        sorryYouLoseTextArea2.setBackground(Color.black);
//        sorryYouLoseTextArea2.setForeground(Color.white);
//        sorryYouLoseTextArea2.setFont(sorryYouLoseTextArea2Font);
//        sorryYouLoseTextArea2.setLineWrap(true);
//        sorryYouLosePanel.add(sorryYouLoseTextArea2);
//
//        sorryYouLoseTextArea3 = new JTextArea("\n Your brother's total balance is $ ");
//        sorryYouLoseTextArea3.setBounds(250, 100, 675, 100);
//        sorryYouLoseTextArea3.setBackground(Color.black);
//        sorryYouLoseTextArea3.setForeground(Color.white);
//        sorryYouLoseTextArea3.setFont(sorryYouLoseTextArea2Font);
//        sorryYouLoseTextArea3.setLineWrap(true);
//        sorryYouLosePanel.add(sorryYouLoseTextArea3);
//
//        sorryYouLoseTextArea4 = new JTextArea("\n \n Would you like to EXIT the game?");
//        sorryYouLoseTextArea4.setBounds(250, 100, 580, 100);
//        sorryYouLoseTextArea4.setBackground(Color.black);
//        sorryYouLoseTextArea4.setForeground(Color.white);
//        sorryYouLoseTextArea4.setFont(sorryYouLoseTextArea2Font);
//        sorryYouLoseTextArea4.setLineWrap(true);
//        sorryYouLosePanel.add(sorryYouLoseTextArea4);
//
//
//        exitBtnPanel = new JPanel();
//        exitBtnPanel.setBounds(250, 800, 500, 200);
//        exitBtnPanel.setBackground(Color.white);
//        sorryYouLosePanel.add(exitBtnPanel);

        exitBtn = new JButton("YES");
        exitBtn.setOpaque(true);
        exitBtn.setBorder(null);
        exitBtn.setBounds(200, 525, 125, 40);
        exitBtn.setBackground(Color.YELLOW);
        exitBtn.setForeground(Color.black);
        exitBtn.setFont(startBtnFont);
        exitBtn.addActionListener(this);
        exitBtn.setActionCommand("yes");

        exitBtn2= new JButton("NO");
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

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("yes")) {
            try {
                TimeUnit.SECONDS.sleep(1);
                Frame.getScreen(introPanel);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } else if (e.getActionCommand().equals("no")) {
            System.exit(0);
        }
    }

}

