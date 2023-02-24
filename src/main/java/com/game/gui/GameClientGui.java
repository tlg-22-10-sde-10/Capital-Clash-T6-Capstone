package com.game.gui;

import com.game.marketreturn.MarketReturnGenerator;
import com.game.players.Computer;
import com.game.stock.StockApi;
import com.game.storage.StockInventory;
import com.game.ui.GlobalMethodsAndAttributes;
import com.game.ui.TradingRoomMenuTwo;
import com.game.players.Player;
import com.game.random.RandomNumberForNews;
import com.game.stock.Stock;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.Stack;

import static com.game.ui.TradingRoomMenuOne.buyStock;
import static javax.swing.JOptionPane.showMessageDialog;

public class GameClientGui extends JPanel implements ActionListener, ChangeListener {

    public static final int STOCK_MIN = 0;
    public static final int STOCK_MAX = 50;

    private JTable table;
    private JButton buyBtn;
    private JButton sellBtn;
    private JLabel selectedStockLabel;
    private JSlider buySlider;
    private int stockQuantity;
    private JLabel quantity;
    private JDialog quantityDialog;
    private JButton confirmStockBtn;
    private JButton cancelStockBtn;
    private JButton insiderBtn;
    private JDialog insiderDialog;
    private double playerInsiderMultiplier = 1.0;
    private double computerInsiderMultiplier = 1.0;
    private double netPlayerBalance;
    private double netComputerBalance;
    private JButton confirmInsiderBtn;
    private JButton cancelInsiderBtn;


    private JLabel currentDay;
    private JButton currentDayButton;

    private int currentTradingDayInt;
    private String currentSelectedStockTicker;


    private JLabel timeLabel;

    private JLabel playerAccount;
    private JTextPane playerStockList;
    private JLabel computerAccount;
    private JTextPane computerStockList;
    private JButton endGame;
    private JButton settings;

    private DefaultTableModel stockTableModel;

    DefaultTableCellRenderer cellRenderer;
    // temp
    private Player player;
    private Computer computer;
    private StockInventory stockInventory;
    private List<Double> previousStockInventory;
    private List<Double> currentStockInventory;
    private Font digital7 = null;

    Font gameFont = new Font("Arial", Font.BOLD, 35);
    Font insiderFont = new Font("Arial", Font.BOLD, 15);
    Font btnFont = new Font("Arial", Font.BOLD, 14);

    //    private static final int DIALOG = 5;
    int x = -7000;
    int y = 50;

    public void paint(Graphics g) {

        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.green);
        g2d.setFont(new Font(Font.DIALOG, Font.BOLD, 25));


        g2d.drawString("Apple | AAPL | 143.72 | 1.72 | -0.0031 | 0.0 | Technology " +
                " Boeing | BA | 213.03 | 0.95 | 0.0043 | 0.0 | Industrials" +
                " Costco | COST | 511.14 | 1.16 | -0.0013 | 0.0 | ConsumerDefensive " +
                " Delta Airlines | DAL | 38.97 | 1.17 | 0.004 | 0.0 | Industrials " +
                " JP Morgan Chase | JPM | 139.96 | 0.88  0.0008 | 0.0 | FinancialServices " +
                " Meta Platforms | META | 148.97 | -0.18 | 0.0036 | 0.0 | CommunicationServices " +
                " Nike | NKE | 127.33 | -0.32 | 0.0063 | 0.0 | ConsumerCyclical " +
                " Pfizer | PFE | 44.16 | 0.6 | -0.0012 | 0.0 | Healthcare " +
                " Tesla | TSLA | 173.22 | 1.77 | -0.0056 | 0.0 | ConsumerDiscretionary " +
                " United Health | UNH | 499.19 | 0.43 | -0.0021 | 0.0 | Healthcare ", x, y);

        try {
            Thread.sleep(100);
            x += 20;

            if (x > getWidth()) {
                x = -7000;
            }
            repaint();

        } catch (InterruptedException e) {
            JOptionPane.showMessageDialog(this, e);

        }
    }

    public void getPlayers() {
        GuiGame test = GuiGame.getInstance();

        this.player = test.getPlayer();
        this.computer = test.getComputer();
        this.stockInventory = test.getStockInventory();
        this.currentTradingDayInt = test.getDay();

        try {
            InputStream is = getClass().getResourceAsStream("/digital-7.ttf");
            digital7 = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(digital7);
            System.out.println("created font");
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

    }

    public GameClientGui() throws IOException {
        setPreferredSize (new Dimension(Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT));
        setLayout(null);
        setBackground(Color.decode(Global.BG_COLOR));

        // temp
        getPlayers();

        //background image
        IconBuilder icon = new IconBuilder();
        ImageIcon bg = icon.imageIcon("/stockbg.jpg", Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT, Image.SCALE_DEFAULT);

        // Placing background image
        JLabel bgImage = new JLabel(bg);
        bgImage.setBounds(0, 0, Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT);

        stockTableModel = new DefaultTableModel();
        cellRenderer = new DefaultTableCellRenderer();

        stockTableModel.addColumn("Company");
        stockTableModel.addColumn("Ticker");
        stockTableModel.addColumn("Price");
        stockTableModel.addColumn("Sector");

        // placeholder add api access later
        // need to update prices per day

        setTableStockLabels();

        // table = new JTable(stockTableModel);
        // JScrollPane scrollPane = new JScrollPane(table);
        JScrollPane scrollPane = (JScrollPane) createAlternating(stockTableModel);

        scrollPane.setBounds(240, 150, 550, 300);

        //Table Event Listener to show what stock is currently listed
        table.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                int selectedColumn = 1;
                Object selectedValue = table.getValueAt(selectedRow, selectedColumn);
                System.out.println("Selected: " + selectedValue);
                selectedStockLabel.setText("Selected Stock: " + selectedValue);
                selectedStockLabel.setFont(gameFont);
                selectedStockLabel.setForeground(Color.WHITE);
                currentSelectedStockTicker = (String) selectedValue;
                buyBtn.setEnabled(true);
                sellBtn.setEnabled(true);
            }
        });

        // buy button
        buyBtn = new JButton("Buy Stock");
        buyBtn.setActionCommand("buy");
        buyBtn.addActionListener(this);
        buyBtn.setBounds(325, 500, 150, 50);
        buyBtn.setEnabled(false);
        buyBtn.setOpaque(false);
        buyBtn.setBackground(Color.decode(Global.MAIN_COLOR));
        buyBtn.setForeground(Color.decode(Global.BTN_COLOR));
        buyBtn.setBorder(null);
        buyBtn.setFont(btnFont);
        ImageIcon buyIcon = icon.imageIcon("/buttonbg.png", 150, 50, Image.SCALE_DEFAULT);
        JLabel buyBg = new JLabel(buyIcon);
        buyBg.setBounds(325, 500, 150, 50);


        // sell button
        sellBtn = new JButton("Sell Stock");
        sellBtn.setActionCommand("sell");
        sellBtn.addActionListener(this);
        sellBtn.setBounds(500, 500, 150, 50);
        sellBtn.setEnabled(false);
        sellBtn.setOpaque(false);
        sellBtn.setBackground(Color.decode(Global.MAIN_COLOR));
        sellBtn.setForeground(Color.decode(Global.BTN_COLOR));
        sellBtn.setBorder(null);
        sellBtn.setFont(btnFont);
        ImageIcon sellIcon = icon.imageIcon("/buttonbg.png", 150, 50, Image.SCALE_DEFAULT);
        JLabel sellBg = new JLabel(sellIcon);
        sellBg.setBounds(500, 500, 150, 50);

        // insider trading button
        insiderBtn = new JButton("Insider Trade");
        insiderBtn.setActionCommand("insider");
        insiderBtn.addActionListener(this);
        insiderBtn.setBounds(60, 565, 150, 50);
        insiderBtn.setOpaque(false);
        insiderBtn.setBackground(Color.decode(Global.MAIN_COLOR));
        insiderBtn.setForeground(Color.decode(Global.BTN_COLOR));
        insiderBtn.setBorder(null);
        insiderBtn.setFont(btnFont);
        ImageIcon insiderIcon = icon.imageIcon("/buttonbg.png", 150, 50, Image.SCALE_DEFAULT);
        JLabel insiderBg = new JLabel(insiderIcon);
        insiderBg.setBounds(60, 565, 150, 50);


        // end game disable until day 12
        endGame = new JButton("End Game");
        endGame.setActionCommand("end");
        endGame.addActionListener(this);
        endGame.setBounds(815, 500, 150, 50);
        endGame.setEnabled(false);
        endGame.setOpaque(false);
        endGame.setBackground(Color.decode(Global.MAIN_COLOR));
        endGame.setForeground(Color.decode(Global.BTN_COLOR));
        endGame.setBorder(null);
        endGame.setFont(btnFont);
        ImageIcon endGameIcon = icon.imageIcon("/buttonbg.png", 150, 50, Image.SCALE_DEFAULT);
        JLabel endGameBg = new JLabel(endGameIcon);
        endGameBg.setBounds(815, 500, 150, 50);


        // settings
        settings = new JButton("settings");
        settings.setActionCommand("settings");
        settings.addActionListener(this);
        settings.setBounds(815, 565, 150, 50);
        settings.setEnabled(true);
        settings.setOpaque(false);
        settings.setBackground(Color.decode(Global.MAIN_COLOR));
        settings.setForeground(Color.decode(Global.BTN_COLOR));
        settings.setBorder(null);
        settings.setFont(btnFont);
        ImageIcon settingIcon = icon.imageIcon("/buttonbg.png", 150, 50, Image.SCALE_DEFAULT);
        JLabel settingBg = new JLabel(settingIcon);
        settingBg.setBounds(815, 565, 150, 50);

        // current stock selected
        selectedStockLabel = new JLabel("Selected Stock:");
        selectedStockLabel.setFont(gameFont);
        scrollPane.setColumnHeaderView(selectedStockLabel);
        selectedStockLabel.setBounds(425, 25, 400, 200);
        selectedStockLabel.setForeground(Color.white);


        // current day label
        currentDay = new JLabel("Day #" + currentTradingDayInt);
        currentDay.setFont(gameFont);
        currentDay.setBounds(285, 25, 400, 200);
        currentDay.setForeground(Color.WHITE);


        // clock label
        timeLabel = new JLabel();
        timeLabel.setFont(digital7.deriveFont(Font.PLAIN, 48));

        timeLabel.setOpaque(true);
        timeLabel.setBackground(Color.BLACK);

        timeLabel.setBounds(400, 5, 170, 45);
        timeLabel.setForeground(Color.GREEN);
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeLabel.setVerticalAlignment(SwingConstants.CENTER);


        // current day button
        currentDayButton = new JButton("End Trading Day");
        currentDayButton.setActionCommand("increaseDay");
        currentDayButton.addActionListener(this);
        currentDayButton.setBounds(60, 500, 150, 50);
        currentDayButton.setOpaque(false);
        currentDayButton.setBackground(Color.decode(Global.MAIN_COLOR));
        currentDayButton.setForeground(Color.decode(Global.BTN_COLOR));
        currentDayButton.setBorder(null);
        currentDayButton.setFont(btnFont);
        ImageIcon endIcon = icon.imageIcon("/buttonbg.png", 150, 50, Image.SCALE_DEFAULT);
        JLabel endDayBg = new JLabel(endIcon);
        endDayBg.setBounds(60, 500, 150, 50);


        // player account label
        playerAccount = new JLabel("", SwingConstants.CENTER);
        playerAccount.setBounds(50, 147, 170, 300);
        playerAccount.setVisible(true);
        playerAccount.setOpaque(false);
        ImageIcon player = icon.imageIcon("/sidemenu.png", 210, 320, Image.SCALE_DEFAULT);
        JLabel playerMenu = new JLabel(player);
        playerMenu.setBounds(30, 138, 210, 320);

        playerStockList = new JTextPane();
        StyledDocument playerDoc = playerStockList.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        playerDoc.setParagraphAttributes(0, playerDoc.getLength(), center, false);
        playerStockList.setBounds(50, 200, 170, 300);
        playerStockList.setOpaque(false);
        playerStockList.setFont(new Font("Arial", Font.BOLD, 12));


        // computer account label
        computerAccount = new JLabel("", SwingConstants.CENTER);
        computerAccount.setBounds(810, 147, 170, 300);
        computerAccount.setVisible(true);
        computerAccount.setOpaque(false);
        ImageIcon computer = icon.imageIcon("/sidemenu.png", 210, 320, Image.SCALE_DEFAULT);
        JLabel computerMenu = new JLabel(computer);
        computerMenu.setBounds(790, 138, 210, 320);

        computerStockList = new JTextPane();
        StyledDocument compDoc = computerStockList.getStyledDocument();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        compDoc.setParagraphAttributes(0, playerDoc.getLength(), center, false);
        computerStockList.setBounds(810, 200, 170, 300);
        computerStockList.setOpaque(false);
        computerStockList.setFont(new Font("Arial", Font.BOLD, 12));

        //buy stock slider
        buySlider = new JSlider(JSlider.HORIZONTAL, STOCK_MIN, STOCK_MAX, 1);
        buySlider.addChangeListener(this);
        buySlider.setSize(300, 100);
        buySlider.setMajorTickSpacing(10);
        buySlider.setMinorTickSpacing(1);
        buySlider.setPaintTicks(true);
        buySlider.setPaintLabels(true);

        //buy stock quantity popup
        Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = new Dimension ( Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT);
        quantityDialog = new JDialog(null, "", Dialog.ModalityType.DOCUMENT_MODAL);
        quantityDialog.setBounds(ss.width / 2 - frameSize.width / 4, ss.height / 2 - frameSize.height / 4, 500, 200);
        Container quantityContainer = quantityDialog.getContentPane();
        quantityContainer.setLayout(new BorderLayout());
        quantityContainer.add(buySlider, BorderLayout.CENTER);

        JPanel stockBtnPanel = new JPanel();
        stockBtnPanel.setLayout(new FlowLayout());
        confirmStockBtn = new JButton("Confirm");
        confirmStockBtn.addActionListener(this);

        cancelStockBtn = new JButton("Cancel");
        cancelStockBtn.addActionListener(this);
        stockBtnPanel.add(confirmStockBtn);
        stockBtnPanel.add(cancelStockBtn);

        JPanel quantityPanel = new JPanel();
        quantityPanel.setLayout(new FlowLayout());
        JLabel quantityLabel = new JLabel("Quantity: ");
        stockQuantity = 1;
        quantity = new JLabel(String.valueOf(stockQuantity));
        quantityPanel.add(quantityLabel);
        quantityPanel.add(quantity);

        quantityContainer.add(quantityPanel, BorderLayout.NORTH);
        quantityContainer.add(stockBtnPanel, BorderLayout.SOUTH);

        // insider trading popup
        JPanel insiderBtnPanel = new JPanel();
        insiderBtnPanel.setLayout(new FlowLayout());
        confirmInsiderBtn = new JButton("Confirm");
        confirmInsiderBtn.addActionListener(this);
        cancelInsiderBtn = new JButton("Cancel");
        cancelInsiderBtn.addActionListener(this);
        insiderBtnPanel.add(confirmInsiderBtn);
        insiderBtnPanel.add(cancelInsiderBtn);
        JTextPane warning = new JTextPane();
        StyledDocument doc = warning.getStyledDocument();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        warning.setText("\nIf you successfully pull off insider trading there is a guaranteed 1% gain.\nHowever there is a 25% chance for the computer to gain 2%\nand a 25% chance to get caught and lose it all..");
        warning.setEditable(false);
        warning.setBackground(Color.decode(Global.BG_COLOR));
        warning.setForeground(Color.white);
        warning.setFont(insiderFont);
        insiderDialog = new JDialog(null, "Insider Trading", Dialog.ModalityType.DOCUMENT_MODAL);
        insiderDialog.setBounds(ss.width / 2 - frameSize.width / 4, ss.height / 2 - frameSize.height / 6, 525, 175);
        Container insiderContainer = insiderDialog.getContentPane();
        insiderContainer.setLayout(new BorderLayout());
        insiderContainer.add(warning, BorderLayout.CENTER);
        insiderContainer.add(insiderBtnPanel, BorderLayout.SOUTH);


        // updates both player and computer
        updateAccountLabels();

        add(scrollPane);
        add(buyBtn);
        add(sellBtn);
        add(selectedStockLabel);
        add(playerStockList);
        add(computerStockList);
        add(playerAccount);
        add(computerAccount);
        add(currentDay);
        add(endGame);
        add(currentDayButton);
        add(insiderBtn);
        add(settings);
        add(playerMenu);
        add(computerMenu);
        add(buyBg);
        add(sellBg);
        add(insiderBg);
        add(endDayBg);
        add(endGameBg);
        add(settingBg);
        add(bgImage);
    }

    private JComponent createAlternating(DefaultTableModel model) {
        table = new JTable(model) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);

                //c.setBackground(Color.decode("#c9caca"));

                if (!isRowSelected(row) && previousStockInventory != null) {
                    boolean changeColor = comparePreviousStocks(row);
                    c.setForeground(changeColor ? Color.decode("#65a147") : Color.RED);
                }else{
                    c.setForeground(Color.BLACK);
                }

                return c;
            }
        };

        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        //table.changeSelection(0, 0, true, false);
        table.setRowHeight(25);
        return new JScrollPane(table);

    }


    private void startClock() {
        Thread clockThread = new Thread() {
            public void run() {
                LocalTime time = LocalTime.of(9, 30);
                boolean startClock = true;

                while (startClock) {
                    time = time.plusMinutes(1);

                    timeLabel.setText(DateTimeFormatter.ofPattern("hh:mm a").format(time));

                    if (time.getHour() == 16) {
//                        System.out.println("It is 4 PM now.");
                        currentDayButton.setEnabled(true);

                        startClock = false;
                    } else {
//                        System.out.println("It is not 4 PM yet.");
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        clockThread.start();
    }


    private boolean comparePreviousStocks(int row) {

        Double prevPrice = previousStockInventory.get(row);
        Double currPrice = currentStockInventory.get(row);

//        System.out.println("prev ->" + prevPrice + " | " + currPrice + "<- curr");

        return currPrice > prevPrice;
    }


    private void updateAccountLabels() {
        List<Computer> accounts = new ArrayList<>(Arrays.asList(player, computer));
        List<JLabel> accLabels = new ArrayList<>(Arrays.asList(playerAccount, computerAccount));
        netPlayerBalance = (accounts.get(0).getBalanceFromHolding(stockInventory) + accounts.get(0).getAccount().getCashBalance()) * playerInsiderMultiplier;
        netComputerBalance = (accounts.get(1).getBalanceFromHolding(stockInventory) + accounts.get(1).getAccount().getCashBalance()) * computerInsiderMultiplier;

        for (int i = 0; i < accounts.size(); i++) {
            //double accStockBalance = accounts.get(i).getBalanceFromHolding(stockInventory);
            //String accStocks = accounts.get(i).getStocks().isEmpty() ? "None" : accounts.get(i).getStocks().toString();
            String accCashBalance = String.format("%.2f", accounts.get(i).getAccount().getCashBalance());
            String accStockBalanceFormat = String.format("%.2f", accounts.get(i).getBalanceFromHolding(stockInventory));
            String accNetBalance = i == 0 ? String.format("%.2f", netPlayerBalance) : String.format("%.2f", netComputerBalance);
            printStockList(accounts.get(i));
            String accToString = "<html>" + accounts.get(i).getName()
                    + "<br/><hr> Stocks: <br/> <br/><br/> "
                    + "<br/><br/><br/><br/><br/><br/><br/><br/>" + "\n  Cash Balance: " + accCashBalance
                    + "<hr>" + "\n Stock Balance: " + accStockBalanceFormat
                    + "<hr>" + "\n Net Balance: " + accNetBalance + "<br/><hr></html>";


            System.out.println(accToString);
            accLabels.get(i).setText(accToString);
            accLabels.get(i).setFont(btnFont);
            accLabels.get(i).setForeground(Color.white);
        }
    }

    private void printStockList(Computer account) {
        String stockList = "";
        for (Map.Entry<String, Integer> entry : account.getStocks().entrySet()) {
            stockList += entry.getKey() + ": " + entry.getValue() + " share(s)\n";
        }
        if (account == player) {
            playerStockList.setText(stockList);
            playerStockList.setForeground(Color.decode(Global.BTN_COLOR));
        } else {
            computerStockList.setText(stockList);
            computerStockList.setForeground(Color.decode(Global.BTN_COLOR));
        }

    }

    private void setTableStockLabels() {

        int numRows = stockTableModel.getRowCount();

        for (int i = 0; i < stockInventory.getAllStocks().size(); i++) {

            Stock currentStock = stockInventory.getAllStocks().get(i);
            String stockName = currentStock.getStockName();
            String stockTicker = currentStock.getSymbol();
            Double stockPrice = currentStock.getCurrentPrice();
            String stockType = String.valueOf(currentStock.getSector());
            List<String> stockHolder =
                    new ArrayList<>(Arrays.asList(stockName, stockTicker, String.valueOf(stockPrice), stockType));

            if (numRows < 2) {
                stockTableModel.addRow(new Object[]{stockName, stockTicker, stockPrice, stockType});
            } else {
                for (int j = 0; j < stockHolder.size(); j++) {
                    stockTableModel.setValueAt(stockHolder.get(j), i, j);
                }
            }
        }
    }

    private void winOrLose() throws IOException, UnsupportedAudioFileException, LineUnavailableException {

        LoserPanel loserPanel = new LoserPanel(netPlayerBalance, netComputerBalance);
        WinnerPanel winnerPanel = new WinnerPanel(netPlayerBalance, netComputerBalance);
        TiePanel tiePanel = new TiePanel(netPlayerBalance, netComputerBalance);

        if (netPlayerBalance < netComputerBalance) {
             Frame.getScreen(loserPanel);
            GlobalMethodsAndAttributes.playAudio("sadTrombone.wav");
        } else if (netPlayerBalance > netComputerBalance) {
            Frame.getScreen(winnerPanel);
            GlobalMethodsAndAttributes.playAudio("win.wav");
        } else {
            Frame.getScreen(tiePanel);
        }

    }


    @Override
    public void actionPerformed(ActionEvent event) {

        String command = event.getActionCommand();
        System.out.println(currentSelectedStockTicker);
        if (command.equals("buy")) {
            confirmStockBtn.setActionCommand("confirmBuy");
            cancelStockBtn.setActionCommand("cancel");
            quantityDialog.setVisible(true);

        } else if (command.equals("confirmBuy")) {

            System.out.println("Buying Stock!");
            try {
                buyStock(currentTradingDayInt, player, computer, currentSelectedStockTicker, stockInventory, stockQuantity);
                quantityDialog.setVisible(false);
                updateAccountLabels();
            } catch (Exception error) {
                System.out.println("An error occurred: " + error);
            }

            System.out.println(player.getStockNames());

        } else if (command.equals("cancel")) {
            quantityDialog.setVisible(false);

        } else if (command.equals("sell")) {

            confirmStockBtn.setActionCommand("confirmSell");
            quantityDialog.setVisible(true);
            System.out.println("Selling Stock!");

        } else if (command.equals("confirmSell")) {

            try {
                TradingRoomMenuTwo.sellStock(player, computer, currentSelectedStockTicker, stockInventory, stockQuantity);
                quantityDialog.setVisible(false);
                updateAccountLabels();
            } catch (Exception error) {
                System.out.println("An error occurred: " + error);
            }

            System.out.println(player.getStockNames());

        } else if (command.equals("increaseDay")) {

            previousStockInventory = new ArrayList<>();
            currentStockInventory = new ArrayList<>();

            for (int i = 0; i < stockInventory.getAllStocks().size(); i++) {
                Double price = stockInventory.getAllStocks().get(i).getCurrentPrice();
                previousStockInventory.add(price);
            }

            if (currentTradingDayInt < 12) {
                currentTradingDayInt += 1;

                System.out.println("Updating day...");

                int newsIndexOfTheDay = RandomNumberForNews.getRandomNumber();
                String todayNews = GlobalMethodsAndAttributes.news.getNewsContent(newsIndexOfTheDay);
                MarketReturnGenerator generator = new MarketReturnGenerator();
                double mktReturnOfTheDay = generator.nextMarketReturn(newsIndexOfTheDay);
                GlobalMethodsAndAttributes.updateDashboard(currentTradingDayInt, newsIndexOfTheDay, mktReturnOfTheDay, stockInventory);

                updateAccountLabels();

            } else {
                endGame.setEnabled(true);
                currentDayButton.setEnabled(false);

                GlobalMethodsAndAttributes.updateDashboard(currentTradingDayInt + 1, 0, 0.0, stockInventory);

            }

            for (int i = 0; i < stockInventory.getAllStocks().size(); i++) {
                Double price = stockInventory.getAllStocks().get(i).getCurrentPrice();
                currentStockInventory.add(price);
            }

            currentDay.setText("Day #" + currentTradingDayInt);
            GuiGame.getInstance().setDay(currentTradingDayInt);
            setTableStockLabels();
//            startClock();
        } else if (command.equals("end")) {
            try {
                winOrLose();
            } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
                e.printStackTrace();
            }
        } else if (command.equals("insider")) {
            confirmInsiderBtn.setActionCommand("insiderConfirm");
            cancelInsiderBtn.setActionCommand("insiderCancel");
            insiderDialog.setVisible(true);
        } else if (command.equals("insiderConfirm")) {
            int riskChance = RandomNumberForNews.getInsiderChance();
            if (riskChance == 1) {
                try {
                    insiderDialog.setVisible(false);
                    Frame.getScreen(new InsiderTradePanel());
                    GlobalMethodsAndAttributes.playAudio("sadTrombone.wav");
                } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
                    e.printStackTrace();
                }
            } else if (riskChance == 2) {
                try {
                    computerInsiderMultiplier += 0.02;
                    GlobalMethodsAndAttributes.playAudio("wrong-answer.wav");
                    updateAccountLabels();
                    showMessageDialog(null, "Oops, you're competition gained instead!");
                    insiderDialog.setVisible(false);
                } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
                    e.printStackTrace();
                }
            } else {
                playerInsiderMultiplier += .01;
                try {
                    showMessageDialog(null, "Wow, you pulled it off!");
                    GlobalMethodsAndAttributes.playAudio("cashier.wav");
                    updateAccountLabels();
                    insiderDialog.setVisible(false);
                } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
                    e.printStackTrace();
                }
            }
        } else if (command.equals("insiderCancel")) {
            insiderDialog.setVisible(false);
        } else if (command.equals("settings")) {
            MusicPanel mp = null;
            try {
                mp = new MusicPanel();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Frame.getScreen(mp);
        }
    }
    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            stockQuantity = (int) source.getValue();
            System.out.println(stockQuantity);
            quantity.setText(String.valueOf(stockQuantity));
        }
    }
}


