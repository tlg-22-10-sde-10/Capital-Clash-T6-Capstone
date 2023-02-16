package com.game.storage;

import com.game.stock.Stock;

import java.util.List;

public interface IStockInventory {

    List<Stock> getAllStocks();
    Stock findBySymbol(String symbol);
    Stock getRandomStock();
}
