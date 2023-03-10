package com.game.gui;

import com.game.account.Account;
import com.game.players.Computer;
import com.game.players.Player;
import com.game.storage.StockInventory;

import java.io.FileNotFoundException;

public class GuiGame {
    private Player player;
    private Computer computer;

    private StockInventory stockInventory;
    private int day;

    private static GuiGame single_instance = null;


    public GuiGame() {

        this.player =  new Player("Player", new Account("checking"));
        this.computer = new Player("Computer", new Account("checking"));
        try {
            this.stockInventory = new StockInventory();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Created initial accounts!");


    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Computer getComputer() {
        return computer;
    }

    public void setComputer(Computer computer) {
        this.computer = computer;
    }

    public StockInventory getStockInventory() {
        return stockInventory;
    }

    public void setStockInventory(StockInventory stockInventory) {
        this.stockInventory = stockInventory;
    }

    public int getDay() { return day; }

    public void setDay(int day) { this.day = day; }

    public void reset(){
        single_instance = null;
    }

    public static GuiGame getInstance()
    {
        if (single_instance == null)
            single_instance = new GuiGame();

        return single_instance;
    }
}
