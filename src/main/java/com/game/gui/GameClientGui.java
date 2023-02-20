package com.game.gui;

import com.game.marketreturn.MarketReturnGenerator;
import com.game.players.Computer;
import com.game.storage.StockInventory;
import com.game.ui.GlobalMethodsAndAttributes;
import com.game.ui.TradingRoomMenuTwo;
import com.game.players.Player;
import com.game.random.RandomNumberForNews;
import com.game.stock.Stock;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.game.ui.TradingRoomMenuOne.buyStock;

public class GameClientGui extends JPanel implements ActionListener {

    private JTable table;
    private JButton buyBtn;
    private JButton sellBtn;
    private JLabel selectedStockLabel;

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

    public void getPlayers() {
        GuiGame test = GuiGame.getInstance();

        this.player = test.getPlayer();
        this.computer = test.getComputer();
        this.stockInventory = test.getStockInventory();
    }

    public GameClientGui() {
        // temp
        getPlayers();

        setPreferredSize(new Dimension(1000, 1000));

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

        scrollPane.setBounds(300, 200, 400, 200);

        //Table Event Listener to show what stock is currently listed
        table.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                int selectedColumn = 1;
                Object selectedValue = table.getValueAt(selectedRow, selectedColumn);
                System.out.println("Selected: " + selectedValue);
                selectedStockLabel.setText("Selected Stock: " + selectedValue);
                currentSelectedStockTicker = (String) selectedValue;
            }
        });

        // play again button
        buyBtn = new JButton("Buy Stock");
        buyBtn.setActionCommand("buy");
        buyBtn.addActionListener(this);
        buyBtn.setBounds(350, 450, 150, 50);


        // exit button
        sellBtn = new JButton("Sell Stock");
        sellBtn.setActionCommand("sell");
        sellBtn.addActionListener(this);
        sellBtn.setBounds(475, 450, 150, 50);


        // end game disable until day 4
        endGame = new JButton("End Game");
        endGame.setActionCommand("end");
        endGame.addActionListener(this);
        endGame.setBounds(800, 450, 150, 50);
        endGame.setEnabled(false);

        // current stock selected
        selectedStockLabel = new JLabel("Selected Stock:");
        scrollPane.setColumnHeaderView(selectedStockLabel);
        selectedStockLabel.setBounds(450, 25, 400, 200);


        // current day label
        currentDay = new JLabel("Day #" + currentTradingDayInt);
        currentDay.setBounds(375, 25, 400, 200);

        // current day button
        currentDayButton = new JButton("End Trading Day");
        currentDayButton.setActionCommand("increaseDay");
        currentDayButton.addActionListener(this);
        currentDayButton.setBounds(100, 450, 150, 50);


        // player account label
        playerAccount = new JLabel("");
        playerAccount.setBounds(100, 200, 150, 200);
        playerAccount.setVisible(true);

        // needed for background color
        playerAccount.setOpaque(true);
        playerAccount.setBackground(Color.gray);

        // computer account label
        computerAccount = new JLabel("");
        computerAccount.setBounds(800, 200, 150, 200);
        computerAccount.setVisible(true);

        // needed for background color
        computerAccount.setOpaque(true);
        computerAccount.setBackground(Color.gray);


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


        // setBackground(Color.black);
        setLayout(null);
        setVisible(true);

    }


    private JComponent createAlternating(DefaultTableModel model) {
        table = new JTable(model) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);

                c.setBackground(Color.DARK_GRAY);

                if (!isRowSelected(row) && previousStockInventory != null) {
                    boolean changeColor = comparePreviousStocks(row);
                    c.setForeground(changeColor ? Color.GREEN : Color.RED);
                }else{
                    c.setForeground(Color.CYAN);
                }

                return c;
            }
        };

        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.changeSelection(0, 0, false, false);
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

        for (int i = 0; i < accounts.size(); i++) {
            double accStockBalance = accounts.get(i).getBalanceFromHolding(stockInventory);
            String accStocks = accounts.get(i).getStocks() == null ? "Empty" : accounts.get(i).getStocks().toString();
            String accCashBalance = String.format("%.2f", accounts.get(i).getAccount().getCashBalance());
            String accStockBalanceFormat = String.format("%.2f", accounts.get(i).getBalanceFromHolding(stockInventory));
            String accNetBalance = String.format("%.2f", (accStockBalance + accounts.get(i).getAccount().getCashBalance()));

            String accToString = "<html>" + accounts.get(i).getName()
                    + "<br/>" + "\n Cash Balance: " + accCashBalance
                    + "<br/>" + "\n Stocks: " + accStocks
                    + "<br/>" + "\n Stock Balance: " + accStockBalanceFormat
                    + "<br/>" + "\n Net Balance: " + accNetBalance + "</html>";


            System.out.println(accToString);
            accLabels.get(i).setText(accToString);
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

        LoserPanel loserPanel = new LoserPanel();
        WinnerPanel winnerPanel = new WinnerPanel();
        TiePanel tiePanel = new TiePanel();
        double playerStockBalance = player.getBalanceFromHolding(stockInventory);
        double computerStockBalance = computer.getBalanceFromHolding(stockInventory);
        if (playerStockBalance + player.getAccount().getCashBalance() < computerStockBalance + computer.getAccount().getCashBalance()) {
             Frame.getScreen(loserPanel);
        } else if (playerStockBalance + player.getAccount().getCashBalance() > computerStockBalance + computer.getAccount().getCashBalance()) {
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
            System.out.println("Buying Stock!");
            try {
                buyStock(currentTradingDayInt, player, computer, currentSelectedStockTicker, stockInventory);
                updateAccountLabels();
            } catch (Exception error) {
                System.out.println("An error occurred: " + error);
            }

            System.out.println(player.getStockNames());

        } else if (command.equals("sell")) {
            System.out.println("Selling Stock!");


            try {
                TradingRoomMenuTwo.sellStock(player, computer, currentSelectedStockTicker, stockInventory);
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
        }
    }
}


