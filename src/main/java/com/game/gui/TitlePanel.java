package com.game.gui;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TitlePanel extends JPanel implements ActionListener {


    private static final int BOLD = 20;
    private JProgressBar progressBar;

    IntroPanel introPanel = new IntroPanel();
    Font btnFont = new Font("Arial", Font.BOLD, 20);

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
        imageLabel.setBounds(0, -50, Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT);


        // Start button
        JButton startBtn = new JButton("Start");
        startBtn.setOpaque(false);
        startBtn.setBackground(Color.decode(Global.MAIN_COLOR));
        startBtn.setForeground(Color.decode(Global.BTN_COLOR));
        startBtn.setBorder(null);
        startBtn.setFont(btnFont);
        startBtn.addActionListener(this);
        startBtn.setActionCommand("start");
        startBtn.setBounds(430, 525, 150, 50);
        startBtn.setEnabled(false);
        ImageIcon startIcon = icon.imageIcon("/buttonbg.png", 150, 50, Image.SCALE_DEFAULT);
        JLabel startBg = new JLabel(startIcon);
        startBg.setBounds(430, 525, 150, 50);

        // progress bar
        progressBar = new JProgressBar();
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setBounds(360, 595, 300, 25);
        progressBar.setForeground(Color.decode("#63a344"));
        //progressBar.setString("Loading api...");



        add(progressBar);
        add(startBtn);
        add(startBg);
        add(imageLabel);


        fillProgressBar(progressBar, 7);


        progressBar.addChangeListener(evt -> {
                JProgressBar progressBar = (JProgressBar) evt.getSource();
                if (progressBar.getValue() == progressBar.getMaximum()) {
                    startBtn.setEnabled(true);
                    //progressBar.setString("Ready!");
                }
        });


    }


    public static void fillProgressBar(JProgressBar progressBar, int durationInSeconds) {
        int steps = 60;
        int delay = durationInSeconds * 1000 / steps;

        SwingWorker<Void, Integer> worker = new SwingWorker<>() {
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
