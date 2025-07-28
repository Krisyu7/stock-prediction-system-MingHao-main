package com.minghao.stockprediction.stock_prediction_system.controller;

import com.minghao.stockprediction.stock_prediction_system.entity.Stock;
import com.minghao.stockprediction.stock_prediction_system.entity.TechnicalIndicator;
import com.minghao.stockprediction.stock_prediction_system.repository.StockRepository;
import com.minghao.stockprediction.stock_prediction_system.repository.TechnicalIndicatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analysis")
public class TechnicalAnalysisController {

    @Autowired
    private TechnicalIndicatorRepository technicalIndicatorRepository;

    @Autowired
    private StockRepository stockRepository;

    // 获取某股票最新的技术指标：GET /api/analysis/AAPL/latest?days=10
    @GetMapping("/{symbol}/latest")
    public List<TechnicalIndicator> getLatestIndicators(@PathVariable String symbol,
                                                        @RequestParam(defaultValue = "10") int days) {
        Stock stock = stockRepository.findBySymbol(symbol);
        if (stock != null) {
            Pageable pageable = (Pageable) PageRequest.of(0, days);
            return technicalIndicatorRepository.findByStockOrderByDateDesc(stock, pageable);
        }
        return List.of();
    }

    // 获取某股票的买入信号：GET /api/analysis/AAPL/signals/BUY
    @GetMapping("/{symbol}/signals/{signal}")
    public List<TechnicalIndicator> getSignals(@PathVariable String symbol,
                                               @PathVariable String signal) {
        Stock stock = stockRepository.findBySymbol(symbol);
        if (stock != null) {
            return technicalIndicatorRepository.findByStockAndSignal(stock, signal);
        }
        return List.of();
    }

    // 获取某股票所有技术指标：GET /api/analysis/AAPL
    @GetMapping("/{symbol}")
    public List<TechnicalIndicator> getAllIndicators(@PathVariable String symbol) {
        Stock stock = stockRepository.findBySymbol(symbol);
        if (stock != null) {
            return technicalIndicatorRepository.findByStock(stock);
        }
        return List.of();
    }
}