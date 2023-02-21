package com.game.ui;
import com.game.players.Computer;
import com.game.storage.StockInventory;
import com.game.players.Player;
import com.game.stock.Stock;

import javax.sound.sampled.*;
import java.io.IOException;

import static javax.swing.JOptionPane.showMessageDialog;

public class TradingRoomMenuOne {

    public static void menuOneBuy(int day) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        System.out.println("Please enter the symbol of the stock that you want to purchase:");
        String stockSymbol = GlobalMethodsAndAttributes.ui.userInput().toUpperCase();
        //handle unrecognized symbol error
        while (GlobalMethodsAndAttributes.inventory.findBySymbol(stockSymbol) == null) {
            System.out.println(GlobalMethodsAndAttributes.ANSI_RED+"                       ***Stock not offered. Please select from the list below***\n"+ GlobalMethodsAndAttributes.ANSI_RESET);
            showTradingRoomStockDashboard(day);
            System.out.println("\nPlease enter the symbol of the stock that you want to purchase:");
            stockSymbol = GlobalMethodsAndAttributes.ui.userInput().toUpperCase();
        }
        System.out.println("How many shares would you like? " +
                "Fractional numbers are not allowed! (Enter an integer ONLY)");
        //handle quantity-is-not-an-integer problem
        String quantityInput = GlobalMethodsAndAttributes.ui.userInput();
        while (!GlobalMethodsAndAttributes.isPositiveInteger(quantityInput)) {
            System.out.println(GlobalMethodsAndAttributes.ANSI_RED+"                       ***Your input is not a positive integer. Please try again***\n"+ GlobalMethodsAndAttributes.ANSI_RESET);
            System.out.println("How many shares would you like? " +
                    "Fractional numbers are not allowed. (Enter an integer ONLY)");
            quantityInput = GlobalMethodsAndAttributes.ui.userInput();
        }
        int numberOfStockPurchaseByPlayer = Integer.parseInt(quantityInput);

        Stock playerStock = GlobalMethodsAndAttributes.inventory.findBySymbol(stockSymbol);
        double valueOfStockPurchasedByPlayer = numberOfStockPurchaseByPlayer * playerStock.getCurrentPrice();

        if (valueOfStockPurchasedByPlayer > GlobalMethodsAndAttributes.player.getAccount().getCashBalance()) {
            System.out.println(GlobalMethodsAndAttributes.ANSI_RED+"                          ***Unauthorized Purchased!Not enough balance!***\n"+ GlobalMethodsAndAttributes.ANSI_RESET);
        } else {
            if (GlobalMethodsAndAttributes.playerStockMap.containsKey(stockSymbol)) {
                GlobalMethodsAndAttributes.playerStockMap.put(playerStock.getSymbol(), GlobalMethodsAndAttributes.playerStockMap.get(stockSymbol) + numberOfStockPurchaseByPlayer);
            } else {
                GlobalMethodsAndAttributes.playerStockMap.put(playerStock.getSymbol(), numberOfStockPurchaseByPlayer);
            }
            GlobalMethodsAndAttributes.playerStocks.add(playerStock.getSymbol());
            GlobalMethodsAndAttributes.player.setStockNames(GlobalMethodsAndAttributes.playerStocks);
            GlobalMethodsAndAttributes.player.setStocks(GlobalMethodsAndAttributes.playerStockMap);
            GlobalMethodsAndAttributes.player.getAccount().deductBalance(numberOfStockPurchaseByPlayer
                    * playerStock.getCurrentPrice());

            System.out.println(GlobalMethodsAndAttributes.ANSI_GREEN+"                          ***Successfully Purchased "+numberOfStockPurchaseByPlayer
                    +" shares of "+ GlobalMethodsAndAttributes.inventory.findBySymbol(stockSymbol).getStockName()+  "***\n"+           GlobalMethodsAndAttributes.ANSI_RESET);

            GlobalMethodsAndAttributes.playAudio("cashier.wav");

        }
        // brother randomly purchase the stock
        int numberOfStockPurchasedByBrother = 1 + (int) (Math.random() * 6);
        Stock brotherStock = GlobalMethodsAndAttributes.inventory.getRandomStock();
        if(GlobalMethodsAndAttributes.brotherStockMap.containsKey(brotherStock.getSymbol())) {
            GlobalMethodsAndAttributes.brotherStockMap.put(brotherStock.getSymbol(), numberOfStockPurchasedByBrother+ GlobalMethodsAndAttributes.brotherStockMap.get(brotherStock.getSymbol()));
        } else {
            GlobalMethodsAndAttributes.brotherStockMap.put(brotherStock.getSymbol(), numberOfStockPurchasedByBrother);
        }

        GlobalMethodsAndAttributes.brother.setStocks(GlobalMethodsAndAttributes.brotherStockMap);
        GlobalMethodsAndAttributes.brother.getAccount().deductBalance(numberOfStockPurchasedByBrother * brotherStock.getCurrentPrice());
    }




    public static void buyStock(int day, Player player, Computer computer, String stockSymbol, StockInventory inventory, int quantityInput) throws IOException, UnsupportedAudioFileException, LineUnavailableException {

        GlobalMethodsAndAttributes.playerStockMap = player.getStocks();
        GlobalMethodsAndAttributes.brotherStockMap = computer.getStocks();
        GlobalMethodsAndAttributes.playerStocks = player.getStockNames();


        while (inventory.findBySymbol(stockSymbol) == null) {
            System.out.println(GlobalMethodsAndAttributes.ANSI_RED+"                       ***Stock not offered. Please select from the list below***\n"+ GlobalMethodsAndAttributes.ANSI_RESET);
            showTradingRoomStockDashboard(day);
            System.out.println("\nPlease enter the symbol of the stock that you want to purchase:");
        }

        int numberOfStockPurchaseByPlayer = quantityInput;

        Stock playerStock = inventory.findBySymbol(stockSymbol);
        double valueOfStockPurchasedByPlayer = numberOfStockPurchaseByPlayer * playerStock.getCurrentPrice();

        if (valueOfStockPurchasedByPlayer > player.getAccount().getCashBalance()) {
            showMessageDialog(null, "***Unauthorized Purchased! Not enough balance!***");
            System.out.println(GlobalMethodsAndAttributes.ANSI_RED+"***Unauthorized Purchased!Not enough balance!***\n"+ GlobalMethodsAndAttributes.ANSI_RESET);
        } else {
            if (GlobalMethodsAndAttributes.playerStockMap.containsKey(stockSymbol)) {
                GlobalMethodsAndAttributes.playerStockMap.put(playerStock.getSymbol(), GlobalMethodsAndAttributes.playerStockMap.get(stockSymbol) + numberOfStockPurchaseByPlayer);
            } else {
                GlobalMethodsAndAttributes.playerStockMap.put(playerStock.getSymbol(), numberOfStockPurchaseByPlayer);
            }
            GlobalMethodsAndAttributes.playerStocks.add(playerStock.getSymbol());
            player.setStockNames(GlobalMethodsAndAttributes.playerStocks);
            player.setStocks(GlobalMethodsAndAttributes.playerStockMap);
            player.getAccount().deductBalance(numberOfStockPurchaseByPlayer
                    * playerStock.getCurrentPrice());

            System.out.println(GlobalMethodsAndAttributes.ANSI_GREEN+"***Successfully Purchased "+numberOfStockPurchaseByPlayer
                    +" shares of "+ inventory.findBySymbol(stockSymbol).getStockName()+  "***\n"+           GlobalMethodsAndAttributes.ANSI_RESET);

            GlobalMethodsAndAttributes.playAudio("cashier.wav");
            showMessageDialog(null, "Successfully purchased " + numberOfStockPurchaseByPlayer + " share(s) of " + inventory.findBySymbol(stockSymbol).getStockName());

        }
        // brother randomly purchase the stock
        int numberOfStockPurchasedByComputer = 1 + (int) (Math.random() * 6);
        Stock computerStock = inventory.getRandomStock();

        double valueOfStockPurchasedByComputer = numberOfStockPurchasedByComputer * computerStock.getCurrentPrice();

        if (valueOfStockPurchasedByComputer > computer.getAccount().getCashBalance()) {
            System.out.println(GlobalMethodsAndAttributes.ANSI_RED+"***Unauthorized Purchased!Not enough balance!***\n"+ GlobalMethodsAndAttributes.ANSI_RESET);
        }else {

            if (GlobalMethodsAndAttributes.brotherStockMap.containsKey(computerStock.getSymbol())) {
                GlobalMethodsAndAttributes.brotherStockMap.put(computerStock.getSymbol(), numberOfStockPurchasedByComputer + GlobalMethodsAndAttributes.brotherStockMap.get(computerStock.getSymbol()));
            } else {
                GlobalMethodsAndAttributes.brotherStockMap.put(computerStock.getSymbol(), numberOfStockPurchasedByComputer);
            }

            computer.setStocks(GlobalMethodsAndAttributes.brotherStockMap);
            computer.getAccount().deductBalance(numberOfStockPurchasedByComputer * computerStock.getCurrentPrice());
        }
    }




    private static void showTradingRoomStockDashboard(int day) {
        GlobalMethodsAndAttributes.ui.titleBarForInventory(day);
        for (Stock stock : GlobalMethodsAndAttributes.inventory.getAllStocks()) {
            System.out.println(stock.toString());
        }
    }
}
