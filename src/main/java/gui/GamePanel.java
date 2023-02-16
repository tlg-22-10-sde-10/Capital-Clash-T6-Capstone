package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class GamePanel extends JPanel implements ActionListener {

    public GamePanel() throws IOException {
        setPreferredSize (new Dimension(Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT));
        setLayout(null);
        setBackground(Color.decode(Global.BG_COLOR));
    }

    @Override
    public void actionPerformed(ActionEvent e) {}
}
