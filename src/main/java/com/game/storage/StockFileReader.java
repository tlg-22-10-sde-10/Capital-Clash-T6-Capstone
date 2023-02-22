package com.game.storage;

import com.game.stock.Stock;
import com.game.stock.StockApi;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.game.ui.GlobalMethodsAndAttributes.df;

public class StockFileReader {


    // orignal
//    public List<Stock> processStockInventory() throws FileNotFoundException {
//        //noinspection ConstantConditions
//        try(Scanner scanner = new Scanner(getClass().getClassLoader().getResourceAsStream("StockFileSystem.CSV"))){
//            List<Stock> stocksList = new ArrayList<>();
//            while (scanner.hasNextLine()) {
//                String line = scanner.nextLine();
//                String[] fields = line.split("\\|");
//                Stock stock = new Stock(fields[0], fields[1],Double.parseDouble(fields[2]),
//                        Double.parseDouble(fields[3]), Double.parseDouble(fields[4]),Double.parseDouble(fields[5]),StockType.valueOf(fields[6]));
//                stocksList.add(stock);
//            }
//            return stocksList;
//        }
//    }


    // modified for api
    public List<Stock> processStockInventory() throws FileNotFoundException {
        //noinspection ConstantConditions
        try(Scanner scanner = new Scanner(getClass().getClassLoader().getResourceAsStream("StockFileSystem.CSV"))){
            List<Stock> stocksList = new ArrayList<>();
            while (scanner.hasNextLine()) {

                String line = scanner.nextLine();
                String[] fields = line.split("\\|");

                double stockPriceFormatted;
                DecimalFormat df = new DecimalFormat("#.##");
                String formattedNum = df.format(StockApi.getStockPrices().get(fields[1]).get(0));
                stockPriceFormatted = Double.parseDouble(formattedNum);

                Stock stock = new Stock(fields[0], fields[1],stockPriceFormatted,
                        Double.parseDouble(fields[3]), Double.parseDouble(fields[4]),Double.parseDouble(fields[5]),StockType.valueOf(fields[6]));
                stocksList.add(stock);
            }
            return stocksList;
        }
    }
}
