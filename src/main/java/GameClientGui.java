import marketreturn.MarketReturnGenerator;
import players.Computer;
import players.Player;
import random.RandomNumberForNews;
import storage.StockInventory;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Company");
        tableModel.addColumn("Ticker");
        tableModel.addColumn("Price");
        tableModel.addColumn("Sector");

        // place holder add api access later
        tableModel.addRow(new Object[]{"Apple", "AAPL", 143.7, "Technology"});
        tableModel.addRow(new Object[]{"Boeing", "BA", 213.03, "Industrials"});
        tableModel.addRow(new Object[]{"Costco", "COST", 511.14, "ConsumerDefensive"});
        tableModel.addRow(new Object[]{"Delta Airlines", "DAL", 38.97, "Industrials"});
        tableModel.addRow(new Object[]{"JP Morgan Chase", "JPM", 139.96, "FinancialServices"});
        tableModel.addRow(new Object[]{"Meta Platforms", "META", 148.97, "CommunicationServices"});
        tableModel.addRow(new Object[]{"Nike", "NKE", 127.33, "ConsumerCyclical"});
        tableModel.addRow(new Object[]{"Pfizer", "PFE", 44.16, "Healthcare"});
        tableModel.addRow(new Object[]{"Tesla", "TSLA", 173.22, "ConsumerDiscretionary"});
        tableModel.addRow(new Object[]{"United Health", "UNH", 499.1, "Healthcare"});

        table = new JTable(tableModel);

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
        String playerCashBalance = String.format("%.2f", computer.getAccount().getCashBalance());
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

    @Override
    public void actionPerformed(ActionEvent event) {

        String command = event.getActionCommand();
        System.out.println(currentSelectedStockTicker);

        if (command.equals("buy")) {
            System.out.println("Buying Stock!");

            try {
                buyStock(1, player, computer, currentSelectedStockTicker, stockInventory);
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
                updateAccountLabels();

            } else {
                endGame.setEnabled(true);
                currentDayButton.setEnabled(false);
            }
            currentDay.setText("Day #" + currentTradingDayInt);

        }

    }
}


