package com.game.ui;
import com.game.storage.StockInventory;
import com.game.players.Computer;
import com.game.players.Player;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.ArrayList;

public class TradingRoomMenuTwo {
    public static void menuTwoSell() throws IOException, UnsupportedAudioFileException, LineUnavailableException {

        if (GlobalMethodsAndAttributes.playerStockMap.isEmpty()) {
            System.out.println(GlobalMethodsAndAttributes.ANSI_RED+"                          ***No Current Holdings. Transactions cannot be completed***\n"+ GlobalMethodsAndAttributes.ANSI_RESET);
        } else {
            ArrayList<String> playersStocksLists = new ArrayList<String>(GlobalMethodsAndAttributes.playerStockMap.keySet());

            GlobalMethodsAndAttributes.showHoldings(playersStocksLists);
            boolean isSellMenuRunning = true;
            System.out.println("Please enter the stock symbol that you want to sell.");
            String stockSymbol = GlobalMethodsAndAttributes.ui.userInput().toUpperCase();
            //handle unrecognized symbol error
            while (!GlobalMethodsAndAttributes.playerStockMap.containsKey(stockSymbol)) {
                System.out.println(GlobalMethodsAndAttributes.ANSI_RED+"                          ***This stock is not in your holdings***"+ GlobalMethodsAndAttributes.ANSI_RESET);
                System.out.println(GlobalMethodsAndAttributes.ANSI_RED+"                          ***Please try again and select from your holdings.***\n"+ GlobalMethodsAndAttributes.ANSI_RESET);
                GlobalMethodsAndAttributes.showHoldings(playersStocksLists);
                System.out.println("Please enter the stock symbol that you want to sell.");
                stockSymbol = GlobalMethodsAndAttributes.ui.userInput().toUpperCase();
            }

            String quantityInput = "";
            // edge cases player cannot enter more than what they have
            while (isSellMenuRunning) {
                System.out.println("Please enter the quantity:");

                quantityInput = GlobalMethodsAndAttributes.ui.userInput();
                while (!GlobalMethodsAndAttributes.isPositiveInteger(quantityInput)) {


                    System.out.println(GlobalMethodsAndAttributes.ANSI_RED+"                          ***Your input is not an integer. Please try again.***\n"+ GlobalMethodsAndAttributes.ANSI_RESET);

                    System.out.println("How many shares would you like? " +
                            "Fractional numbers are not allowed! (Enter an integer ONLY)");
                    quantityInput = GlobalMethodsAndAttributes.ui.userInput();
                }
                int quantity = Integer.parseInt(quantityInput);

                if (GlobalMethodsAndAttributes.playerStockMap.get(stockSymbol) >= quantity) {
                    GlobalMethodsAndAttributes.player.getAccount().calculateBalance(quantity *
                            GlobalMethodsAndAttributes.inventory.findBySymbol(stockSymbol).getCurrentPrice());
                    // update map once the sell is completed
                    GlobalMethodsAndAttributes.playerStockMap.put(stockSymbol, GlobalMethodsAndAttributes.playerStockMap.get(stockSymbol) - quantity);
                    if (GlobalMethodsAndAttributes.playerStockMap.get(stockSymbol) == 0) {
                        GlobalMethodsAndAttributes.playerStockMap.remove(stockSymbol);
                    }
                    // plays sound after sell transactions
                    GlobalMethodsAndAttributes.playAudio("sell.wav");
                    isSellMenuRunning = false;
                } else {
                    System.out.println(GlobalMethodsAndAttributes.ANSI_RED+"                          ***Please try again and enter the valid stock quantity.***\n"+ GlobalMethodsAndAttributes.ANSI_RESET);

                }

            }
            System.out.println(GlobalMethodsAndAttributes.ANSI_GREEN+"                          ***Successfully Sold "+quantityInput
                    +" shares of "+ GlobalMethodsAndAttributes.inventory.findBySymbol(stockSymbol).getStockName()+  "***"+           GlobalMethodsAndAttributes.ANSI_RESET);

        }




    }




    public static void sellStock(Player player, Computer computer, String stockSymbol, StockInventory inventory) throws IOException, UnsupportedAudioFileException, LineUnavailableException {


        GlobalMethodsAndAttributes.playerStockMap = player.getStocks();
        GlobalMethodsAndAttributes.brotherStockMap = computer.getStocks();
        GlobalMethodsAndAttributes.playerStocks = player.getStockNames();


        if (GlobalMethodsAndAttributes.playerStockMap.isEmpty()) {
            System.out.println(GlobalMethodsAndAttributes.ANSI_RED+"                          ***No Current Holdings. Transactions cannot be completed***\n"+ GlobalMethodsAndAttributes.ANSI_RESET);
        } else {
            ArrayList<String> playersStocksLists = new ArrayList<String>(GlobalMethodsAndAttributes.playerStockMap.keySet());
            GlobalMethodsAndAttributes.showHoldings(playersStocksLists);
            while (!GlobalMethodsAndAttributes.playerStockMap.containsKey(stockSymbol)) {
                System.out.println(GlobalMethodsAndAttributes.ANSI_RED+"                          ***This stock is not in your holdings***"+ GlobalMethodsAndAttributes.ANSI_RESET);
                System.out.println(GlobalMethodsAndAttributes.ANSI_RED+"                          ***Please try again and select from your holdings.***\n"+ GlobalMethodsAndAttributes.ANSI_RESET);
                return;
            }

            String quantityInput = "1";
            // edge cases player cannot enter more than what they have

            boolean menuOpen = true;
            while (menuOpen) {

                int quantity = Integer.parseInt(quantityInput);

                if (GlobalMethodsAndAttributes.playerStockMap.get(stockSymbol) >= quantity) {
                    player.getAccount().calculateBalance(quantity *
                            inventory.findBySymbol(stockSymbol).getCurrentPrice());
                    // update map once the sell is completed
                    GlobalMethodsAndAttributes.playerStockMap.put(stockSymbol, GlobalMethodsAndAttributes.playerStockMap.get(stockSymbol) - quantity);
                    if (GlobalMethodsAndAttributes.playerStockMap.get(stockSymbol) == 0) {
                        GlobalMethodsAndAttributes.playerStockMap.remove(stockSymbol);
                    }
                    // plays sound after sell transactions
                    GlobalMethodsAndAttributes.playAudio("sell.wav");
                    menuOpen = false;
                } else {
                    System.out.println(GlobalMethodsAndAttributes.ANSI_RED+"                          ***Please try again and enter the valid stock quantity.***\n"+ GlobalMethodsAndAttributes.ANSI_RESET);

                }

            }
            System.out.println(GlobalMethodsAndAttributes.ANSI_GREEN+"                          ***Successfully Sold "+quantityInput
                    +" shares of "+ inventory.findBySymbol(stockSymbol).getStockName()+  "***"+           GlobalMethodsAndAttributes.ANSI_RESET);

        }

    }

}
