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

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import static com.game.ui.TradingRoomMenuOne.buyStock;

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
    private JDialog dialog;
    private JButton confirmBtn;
    private JButton cancelBtn;


    private JLabel currentDay;
    private JButton currentDayButton;

    private int currentTradingDayInt = 0;
    private String currentSelectedStockTicker;



    private JLabel timeLabel;

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
    private Font digital7 = null;













































    Font btnFont = new Font("Bebas Neue", Font.BOLD, 40);

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

    public GameClientGui() {
        setPreferredSize (new Dimension(Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT));
        setLayout(null);
        setBackground(Color.decode(Global.BG_COLOR));

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
                selectedStockLabel.setFont(btnFont);
                currentSelectedStockTicker = (String) selectedValue;
                buyBtn.setEnabled(true);
            }
        });

        // play again button
        buyBtn = new JButton("Buy Stock");
        buyBtn.setActionCommand("buy");
        buyBtn.addActionListener(this);
        buyBtn.setBounds(350, 450, 150, 50);
        buyBtn.setEnabled(false);


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
        selectedStockLabel.setFont(btnFont);
        scrollPane.setColumnHeaderView(selectedStockLabel);
        selectedStockLabel.setBounds(450, 25, 400, 200);


        // current day label
        currentDay = new JLabel("Day #" + currentTradingDayInt);
        currentDay.setFont(btnFont);
        currentDay.setBounds(305, 25, 400, 200);


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
        currentDayButton.setBounds(100, 450, 150, 50);
//        currentDayButton.setEnabled(false);


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
        dialog = new JDialog(null, "", Dialog.ModalityType.DOCUMENT_MODAL);
        dialog.setBounds(ss.width / 2 - frameSize.width / 4, ss.height / 2 - frameSize.height / 4, 500, 200);
        Container dialogContainer = dialog.getContentPane();
        dialogContainer.setLayout(new BorderLayout());
        dialogContainer.add(buySlider, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        confirmBtn = new JButton("Confirm");
        confirmBtn.addActionListener(this);

        cancelBtn = new JButton("Cancel");
        cancelBtn.setActionCommand("cancel");
        cancelBtn.addActionListener(this);
        buttonPanel.add(confirmBtn);
        buttonPanel.add(cancelBtn);

        JPanel quantityPanel = new JPanel();
        quantityPanel.setLayout(new FlowLayout());
        JLabel quantityLabel = new JLabel("Quantity: ");
        stockQuantity = 1;
        quantity = new JLabel(String.valueOf(stockQuantity));
        quantityPanel.add(quantityLabel);
        quantityPanel.add(quantity);



        dialogContainer.add(quantityPanel, BorderLayout.NORTH);
        dialogContainer.add(buttonPanel, BorderLayout.SOUTH);

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
            confirmBtn.setActionCommand("confirmBuy");
            dialog.setVisible(true);

        } else if (command.equals("confirmBuy")) {

            System.out.println("Buying Stock!");
            try {
                buyStock(currentTradingDayInt, player, computer, currentSelectedStockTicker, stockInventory, stockQuantity);
                updateAccountLabels();
            } catch (Exception error) {
                System.out.println("An error occurred: " + error);
            }

            System.out.println(player.getStockNames());

        } else if (command.equals("cancel")) {
            dialog.setVisible(false);

        } else if (command.equals("sell")) {

            confirmBtn.setActionCommand("confirmSell");
            dialog.setVisible(true);
            System.out.println("Selling Stock!");

        } else if (command.equals("confirmSell")) {

            try {
                TradingRoomMenuTwo.sellStock(player, computer, currentSelectedStockTicker, stockInventory, stockQuantity);
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

                GlobalMethodsAndAttributes.updateDashboard(currentTradingDayInt+1, 0, 0.0, stockInventory);

            }

            for (int i = 0; i < stockInventory.getAllStocks().size(); i++) {
                Double price = stockInventory.getAllStocks().get(i).getCurrentPrice();
                currentStockInventory.add(price);
            }

            currentDay.setText("Day #" + currentTradingDayInt);
            setTableStockLabels();
//            startClock();
        } else if (command.equals("end")) {
            try {
                  winOrLose();
            } catch (IOException e) {
                e.printStackTrace();
            }
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


