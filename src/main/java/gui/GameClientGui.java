package gui;

import marketreturn.MarketReturnGenerator;
import players.Computer;
import players.Player;
import random.RandomNumberForNews;
import stock.Stock;
import storage.StockInventory;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ui.GlobalMethodsAndAttributes.*;
import static ui.TradingRoomMenuOne.buyStock;
import static ui.TradingRoomMenuTwo.sellStock;

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

    private  DefaultTableModel stockTableModel;


    // temp
    private Player player;
    private Computer computer;
    private StockInventory stockInventory;


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

        stockTableModel.addColumn("Company");
        stockTableModel.addColumn("Ticker");
        stockTableModel.addColumn("Price");
        stockTableModel.addColumn("Sector");

        // place holder add api access later
        // need to update prices per day

        initialStockLabels();
        table = new JTable(stockTableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(300, 200, 400, 200);


        //Table Event Listner to show what stock is currently listed
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

    private void updateAccountLabels() {


        double playerStockBalance = player.getBalanceFromHolding(stockInventory);
        String playerStocks = player.getStocks() == null ? "Empty" : player.getStocks().toString();
        String playerCashBalance = String.format("%.2f", player.getAccount().getCashBalance());
        String playerNetBalance = String.format("%.2f", (playerStockBalance + player.getAccount().getCashBalance()));

        String playerToString = "<html>" + player.getName()
                + "<br/>" + "\n Cash Balance: " + playerCashBalance
                + "<br/>" + "\n Stocks: " + playerStocks
                + "<br/>" + "\n Stock Balance: " + player.getBalanceFromHolding(stockInventory)
                + "<br/>" + "\n Net Balance: " + playerNetBalance + "</html>";


        System.out.println(playerToString);
        playerAccount.setText(playerToString);


        // Computer account

        double computerStockBalance = computer.getBalanceFromHolding(stockInventory);
        String computerStocks = computer.getStocks() == null ? "Empty" : computer.getStocks().toString();
        String computerCashBalance = String.format("%.2f", computer.getAccount().getCashBalance());
        String computerNetBalance = String.format("%.2f", (computerStockBalance + computer.getAccount().getCashBalance()));

        String computerToString = "<html>" + computer.getName()
                + "<br/>" + "\n Cash Balance: " + computerCashBalance
                + "<br/>" + "\n Stocks: " + computerStocks
                + "<br/>" + "\n Stock Balance: " + computer.getBalanceFromHolding(stockInventory)
                + "<br/>" + "\n Net Balance: " + computerNetBalance + "</html>";

        computerAccount.setText(computerToString);


    }



    private void initialStockLabels() {

        for (int i = 0; i < stockInventory.getAllStocks().size() ; i++) {

            Stock currentStock = stockInventory.getAllStocks().get(i);
            String stockName = currentStock.getStockName();
            String stockTicker = currentStock.getSymbol();
            Double stockPrice = currentStock.getCurrentPrice();
            String stockType = String.valueOf(currentStock.getSector());

//            System.out.println(stockName);
//            System.out.println(stockTicker);
//            System.out.println(stockPrice);
//            System.out.println(stockType);

            stockTableModel.addRow(new Object[]{stockName, stockTicker, stockPrice, stockType});
        }

    }


    private void updateStockLabel() {

        for (int i = 0; i < stockInventory.getAllStocks().size() ; i++) {

            System.out.println(i);
            Stock currentStock = stockInventory.getAllStocks().get(i);
            List<String> stockHolder = new ArrayList<>();
            String stockName = currentStock.getStockName();
            String stockTicker = currentStock.getSymbol();
            Double stockPrice = currentStock.getCurrentPrice();
            String stockType = String.valueOf(currentStock.getSector());

            stockHolder.add(stockName);
            stockHolder.add(stockTicker);
            stockHolder.add(String.valueOf(stockPrice));
            stockHolder.add(stockType);
//            System.out.println(stockName);
//            System.out.println(stockTicker);
//            System.out.println(stockPrice);
//            System.out.println(stockType);

//            stockTableModel.addRow(new Object[]{stockName, stockTicker, stockPrice, stockType});

            for (int j = 0; j < stockHolder.size(); j++) {
                stockTableModel.setValueAt(stockHolder.get(j), i, j);
            }
        }
        stockTableModel.fireTableDataChanged();
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
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (UnsupportedAudioFileException e) {
                throw new RuntimeException(e);
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }

            System.out.println(player.getStockNames());

        } else if (command.equals("sell")) {
            System.out.println("Selling Stock!");


            try {
                sellStock(player, computer, currentSelectedStockTicker, stockInventory);
                updateAccountLabels();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (UnsupportedAudioFileException e) {
                throw new RuntimeException(e);
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }

            System.out.println(player.getStockNames());

        } else if (command.equals("increaseDay")) {

            if (currentTradingDayInt < 4) {
                currentTradingDayInt += 1;

                System.out.println("update!");

                int newsIndexOfTheDay = RandomNumberForNews.getRandomNumber();
                String todayNews = news.getNewsContent(newsIndexOfTheDay);
                MarketReturnGenerator generator = new MarketReturnGenerator();
                double mktReturnOfTheDay = generator.nextMarketReturn(newsIndexOfTheDay);
                updateDashboard(currentTradingDayInt, newsIndexOfTheDay, mktReturnOfTheDay, stockInventory);

                updateStockLabel();
                updateAccountLabels();

            } else {
                endGame.setEnabled(true);
                currentDayButton.setEnabled(false);
            }
            currentDay.setText("Day #" + currentTradingDayInt);

        }

    }
}


