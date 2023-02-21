package com.game.stock;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class StockApi {

    static Map<String, List<Double>> stockPrices = new HashMap<>();
    static Map<String, Stock> stocks = new HashMap<>();
    private static StockApi single_instance = null;


    public StockApi() {
        try {
            getStock();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void getStock() throws IOException {
        long startTime = System.nanoTime();


        String[] symbols = new String[]{"GME", "AAPL", "BA", "COST", "DAL", "JPM", "META", "NKE", "PFE", "TSLA", "UNH"};

        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        from.add(Calendar.YEAR, -1); // from 1 year ago


        Arrays.stream(symbols).parallel().forEach(stockSymbol -> {
            try {
                Stock temp = YahooFinance.get(stockSymbol, from, to, Interval.WEEKLY);
                stocks.put(stockSymbol, temp);
                Stock currentStock = stocks.get(stockSymbol);

                List<HistoricalQuote> currentStockHistory = currentStock.getHistory(from, to, Interval.DAILY);
                for (int i = 0; i < currentStockHistory.size(); i++) {
                    BigDecimal tempPrice = currentStockHistory.get(i).getOpen();
                    double d = tempPrice.doubleValue();
                    if (stockPrices.containsKey(stockSymbol)) {
                        List<Double> existingList = stockPrices.get(stockSymbol);
                        existingList.add(d);
                        stockPrices.put(stockSymbol, existingList);

                    } else {
                        ArrayList<Double> tempList = new ArrayList<>();
                        tempList.add(d);
                        stockPrices.put(stockSymbol, tempList);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        long endTime = System.nanoTime();

        // Calculate the elapsed time and print it
        double elapsedTime = (endTime - startTime) / 1000000000.0;
        System.out.println("Elapsed time: " + elapsedTime + " seconds");
    }


    public static Map<String, List<Double>> getStockPrices() {
        return stockPrices;
    }

    public static Double getStockPricesDay(String stockSymbol, int day) {
        List<Double> doubles = stockPrices.get(stockSymbol);
        return doubles.get(day);
    }

    public static Map<String, Stock> getStocks() {
        return stocks;
    }

    public static StockApi getInstance()
    {
        if (single_instance == null)
            single_instance = new StockApi();

        return single_instance;
    }

}

