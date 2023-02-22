package com.game.gui;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TitlePanel extends JPanel implements ActionListener {


    private static final int BOLD = 20;
    private JProgressBar progressBar;

    IntroPanel introPanel = new IntroPanel();
    Font btnFont = new Font("Bebas Neue", Font.BOLD, 20);

    public TitlePanel() throws IOException {
        // Setting up the dimensions and background color
        setPreferredSize(new Dimension(Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT));
        setLayout(null);
        setBackground(Color.decode(Global.BG_COLOR));

        // The title background image
        IconBuilder icon = new IconBuilder();
        ImageIcon titleIcon = icon.imageIcon("/CapitalClash.png",500, 500, Image.SCALE_DEFAULT);

        // Placing the background image
        JLabel imageLabel = new JLabel(titleIcon);
        imageLabel.setBounds(0, 0, Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT);

        // Start button
        JButton startBtn = new JButton("START");
        startBtn.setOpaque(true);
        startBtn.setBackground(Color.decode(Global.MAIN_COLOR));
        startBtn.setForeground(Color.WHITE);
        startBtn.setBorder(null);
        startBtn.setFont(btnFont);
        startBtn.addActionListener(this);
        startBtn.setActionCommand("start");
        startBtn.setBounds(448, 525, 125, 40);
        startBtn.setEnabled(false);

        // progress bar
        progressBar = new JProgressBar();
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setBounds(360, 575, 300, 25);
        progressBar.setString("Loading api...");



        add(progressBar);
        add(startBtn);
        add(imageLabel);


        fillProgressBar(progressBar, 7);


        progressBar.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                JProgressBar progressBar = (JProgressBar) evt.getSource();
                if (progressBar.getValue() == progressBar.getMaximum()) {
                    startBtn.setEnabled(true);
                }
            }
        });


    }

    public static void fillProgressBar(JProgressBar progressBar, int durationInSeconds) {
        // Same as in previous example
        int steps = 60;
        int delay = durationInSeconds * 1000 / steps;

        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                int progress = 0;
                for (int i = 0; i < steps; i++) {
                    progress = i * 100 / steps;
                    publish(progress);
                    Thread.sleep(delay);
                }
                publish(100);
                return null;
            }

            @Override
            protected void process(java.util.List<Integer> chunks) {
                int value = chunks.get(chunks.size() - 1);
                progressBar.setValue(value);
            }
        };

        worker.execute();
        System.out.println("complete");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("start")) {
            try {
                TimeUnit.SECONDS.sleep(1);
                Frame.getScreen(introPanel);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

    }}
