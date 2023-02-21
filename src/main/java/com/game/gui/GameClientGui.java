package com.game.gui;

import com.game.marketreturn.MarketReturnGenerator;
import com.game.players.Computer;
import com.game.storage.StockInventory;
import com.game.ui.GlobalMethodsAndAttributes;
import com.game.ui.TradingRoomMenuTwo;
import com.game.players.Player;
import com.game.random.RandomNumberForNews;
import com.game.stock.Stock;
import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    private int currentTradingDayInt = 0;
    private String currentSelectedStockTicker;

    private JLabel playerAccount;
    private JLabel computerAccount;
    private JButton endGame;

    private DefaultTableModel stockTableModel;

    DefaultTableCellRenderer cellRenderer;
    // temp
    private Player player;
    private Computer computer;
    private StockInventory stockInventory;
    private List<Double> previousStockInventory;
    private List<Double> currentStockInventory;

    Font gameFont = new Font("Bebas Neue", Font.BOLD, 40);
    Font insiderFont = new Font("Bebas Neue", Font.BOLD, 20);
    Font btnFont = new Font("Bebas Neue", Font.BOLD, 20);

//    private static final int DIALOG = 5;
    int x = -7000;
    int y = 100;
    int a = -7000;
    int b = 200;


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
    }

    public GameClientGui() {
        setPreferredSize (new Dimension(Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT));
        setLayout(null);
        setBackground(Color.decode(Global.BG_COLOR));

        // temp
        getPlayers();

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
        buyBtn.setOpaque(true);
        buyBtn.setBackground(Color.decode(Global.MAIN_COLOR));
        buyBtn.setForeground(Color.WHITE);
        buyBtn.setBorder(null);
        buyBtn.setFont(btnFont);


        // sell button
        sellBtn = new JButton("Sell Stock");
        sellBtn.setActionCommand("sell");
        sellBtn.addActionListener(this);
        sellBtn.setBounds(500, 500, 150, 50);
        sellBtn.setEnabled(false);
        sellBtn.setOpaque(true);
        sellBtn.setBackground(Color.decode(Global.MAIN_COLOR));
        sellBtn.setForeground(Color.WHITE);
        sellBtn.setBorder(null);
        sellBtn.setFont(btnFont);

        // insider trading button
        insiderBtn = new JButton("Insider Trade");
        insiderBtn.setActionCommand("insider");
        insiderBtn.addActionListener(this);
        insiderBtn.setBounds(325, 565, 150, 50);
        insiderBtn.setOpaque(true);
        insiderBtn.setBackground(Color.decode(Global.MAIN_COLOR));
        insiderBtn.setForeground(Color.WHITE);
        insiderBtn.setBorder(null);
        insiderBtn.setFont(btnFont);


        // end game disable until day 4
        endGame = new JButton("End Game");
        endGame.setActionCommand("end");
        endGame.addActionListener(this);
        endGame.setBounds(815, 500, 150, 50);
        endGame.setEnabled(false);
        endGame.setOpaque(true);
        endGame.setBackground(Color.decode(Global.MAIN_COLOR));
        endGame.setForeground(Color.WHITE);
        endGame.setBorder(null);
        endGame.setFont(btnFont);

        // current stock selected
        selectedStockLabel = new JLabel("Selected Stock:");
        selectedStockLabel.setFont(gameFont);
        scrollPane.setColumnHeaderView(selectedStockLabel);
        selectedStockLabel.setBounds(450, 25, 400, 200);


        // current day label
        currentDay = new JLabel("Day #" + currentTradingDayInt);
        currentDay.setFont(gameFont);
        currentDay.setBounds(305, 25, 400, 200);

        // current day button
        currentDayButton = new JButton("End Trading Day");
        currentDayButton.setActionCommand("increaseDay");
        currentDayButton.addActionListener(this);
        currentDayButton.setBounds(60, 500, 150, 50);
        currentDayButton.setOpaque(true);
        currentDayButton.setBackground(Color.decode(Global.MAIN_COLOR));
        currentDayButton.setForeground(Color.WHITE);
        currentDayButton.setBorder(null);
        currentDayButton.setFont(btnFont);


        // player account label
        playerAccount = new JLabel("", SwingConstants.CENTER);
        playerAccount.setBounds(50, 150, 180, 300);
        playerAccount.setVisible(true);
        //playerAccount.setVerticalAlignment(SwingConstants.CENTER);

        // needed for background color
        playerAccount.setOpaque(true);
        playerAccount.setBackground(Color.decode("#c9caca"));

        // computer account label
        computerAccount = new JLabel("", SwingConstants.CENTER);
        computerAccount.setBounds(800, 150, 180, 300);
        computerAccount.setVisible(true);

        // needed for background color
        computerAccount.setOpaque(true);
        computerAccount.setBackground(Color.decode("#c9caca"));

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

        // insider trading
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
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        warning.setText("\nIf you successfully pull off insider trading there is a guaranteed 1% gain.\nHowever there is a 25% chance for the computer to gain 2%\nand a 25% chance to get caught and lose it all..");
        warning.setEditable(false);
        warning.setBackground(Color.decode(Global.BG_COLOR));
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
        add(playerAccount);
        add(computerAccount);
        add(currentDay);
        add(endGame);
        add(currentDayButton);
        add(insiderBtn);

    }

    private JComponent createAlternating(DefaultTableModel model) {
        table = new JTable(model) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);

                //c.setBackground(Color.decode("#c9caca"));

                if (!isRowSelected(row) && previousStockInventory != null) {
                    boolean changeColor = comparePreviousStocks(row);
                    c.setForeground(changeColor ? Color.GREEN : Color.RED);
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
            String accStocks = accounts.get(i).getStocks() == null ? "Empty" : accounts.get(i).getStocks().toString();
            String accCashBalance = String.format("%.2f", accounts.get(i).getAccount().getCashBalance());
            String accStockBalanceFormat = String.format("%.2f", accounts.get(i).getBalanceFromHolding(stockInventory));
            String accNetBalance = i == 0 ? String.format("%.2f", netPlayerBalance) : String.format("%.2f", netComputerBalance);

            String accToString = "<html>" + accounts.get(i).getName()
                    + "<br/><hr><br/>" + "\n Cash Balance: " + accCashBalance
                    + "<br/><hr><br/>" + "\n Stocks: " + accStocks
                    + "<br/><hr><br/>" + "\n Stock Balance: " + accStockBalanceFormat
                    + "<br/><hr><br/>" + "\n Net Balance: " + accNetBalance + "</html>";


            System.out.println(accToString);
            accLabels.get(i).setText(accToString);
            accLabels.get(i).setFont(btnFont);
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

    private void winOrLose() throws IOException {

        LoserPanel loserPanel = new LoserPanel(netPlayerBalance, netComputerBalance);
        WinnerPanel winnerPanel = new WinnerPanel(netPlayerBalance, netComputerBalance);
        TiePanel tiePanel = new TiePanel(netPlayerBalance, netComputerBalance);

        if (netPlayerBalance < netComputerBalance) {
             Frame.getScreen(loserPanel);
        } else if (netPlayerBalance > netComputerBalance) {
             Frame.getScreen(winnerPanel);
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

            if (currentTradingDayInt < 4) {
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
            }

            for (int i = 0; i < stockInventory.getAllStocks().size(); i++) {
                Double price = stockInventory.getAllStocks().get(i).getCurrentPrice();
                currentStockInventory.add(price);
            }

            currentDay.setText("Day #" + currentTradingDayInt);
            setTableStockLabels();
        } else if (command.equals("end")) {
            try {
                  winOrLose();
            } catch (IOException e) {
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
                }  catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
                    e.printStackTrace();
                }
            }
        } else if (command.equals("insiderCancel")) {
            insiderDialog.setVisible(false);
        }

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
            stockQuantity = (int)source.getValue();
            System.out.println(stockQuantity);
            quantity.setText(String.valueOf(stockQuantity));
        }
    }
}


