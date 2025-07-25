package com.minghao.stockprediction.stock_prediction_system.service;

import com.minghao.stockprediction.stock_prediction_system.entity.Stock;
import com.minghao.stockprediction.stock_prediction_system.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {
    @Autowired
    private StockRepository stockRepository;


    // add stock
    public Stock addStock(Stock stock){
        return stockRepository.save(stock);

    }
    // Get all stocks
    public List<Stock> getAllStocks(){
        return stockRepository.findAll();
    }

    // search by stock symbol
    public Stock getStockBySymbol(String symbol) {
        return stockRepository.findBySymbol(symbol);
    }


}
