package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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