package com.minghao.stockprediction.stock_prediction_system.service;


import com.minghao.stockprediction.stock_prediction_system.entity.Stock;
import com.minghao.stockprediction.stock_prediction_system.entity.StockPrice;
import com.minghao.stockprediction.stock_prediction_system.repository.StockPriceRepository;
import com.minghao.stockprediction.stock_prediction_system.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;

@Service
public class StockPriceService {
    @Autowired
    private StockPriceRepository stockPriceRepository;

    @Autowired
    private StockRepository stockRepository;

    public StockPrice addStockPrice(StockPrice stockPrice) {
        return stockPriceRepository.save(stockPrice);
    }

    public List<StockPrice> getPricesBySymbol(String symbol){
        Stock stock = stockRepository.findBySymbol(symbol);

        if(stock!=null){
            return stockPriceRepository.findByStock(stock);
        }
        return List.of();
    }

    public StockPrice getPricesBySymbolAndDate(String symbol, LocalDate date) {
        Stock stock = stockRepository.findBySymbol(symbol);
        if (stock != null) {
            return stockPriceRepository.findByStockAndDate(stock, date);
        }
        return null;
    }

    // 获取某只股票最新N天的价格记录
    public List<StockPrice> getLatestPrices(String symbol, int days) {
        Stock stock = stockRepository.findBySymbol(symbol);
        if (stock != null) {
            Pageable pageable = (Pageable) PageRequest.of(0, days);  // 第0页，取days条记录
            return stockPriceRepository.findByStockOrderByDateDesc(stock, (org.springframework.data.domain.Pageable) pageable);
        }
        return List.of();
    }
}
