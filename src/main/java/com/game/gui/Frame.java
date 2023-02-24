package com.game.gui;

import com.game.stock.StockApi;
import yahoofinance.Stock;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;


public class Frame extends Component implements ActionListener {
    private static JFrame frame;
    private static TitlePanel titlePanel;
    Sound sound = new Sound();
    MusicPanel mp;


    static {
        try {
            titlePanel = new TitlePanel();
        } catch (IOException e) {
            // throw runtime exception
            throw new RuntimeException(e);
            //e.printStackTrace();
        }
    }

    public Frame() {
        Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = new Dimension ( Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT);
        frame = new JFrame("Capital Clash");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(titlePanel);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.pack();

        frame.setBounds ( ss.width / 2 - frameSize.width / 2,
                ss.height / 2 - frameSize.height / 2,
                frameSize.width, frameSize.height );
        frame.setVisible(true);
        StockApi.getInstance();

        URL sound = getClass().getResource("/clash-app-song.wav");
        MusicPanel.playMusic(sound);
    }


    public static void getScreen(JPanel panel){
        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        frame.revalidate();
        frame.repaint();
    }


    @Override
    public void actionPerformed(ActionEvent e) {}
}