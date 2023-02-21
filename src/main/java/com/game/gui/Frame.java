package com.game.gui;

import com.game.stock.StockApi;
import yahoofinance.Stock;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Frame implements ActionListener {
    private static JFrame frame;
    private static TitlePanel titlePanel;


    static {
        try {
            titlePanel = new TitlePanel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Frame() {
        frame = new JFrame("Capital Clash");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(titlePanel);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        StockApi.getInstance();
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