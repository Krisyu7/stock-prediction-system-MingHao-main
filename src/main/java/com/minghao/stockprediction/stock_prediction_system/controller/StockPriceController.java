package com.minghao.stockprediction.stock_prediction_system.controller;


import com.minghao.stockprediction.stock_prediction_system.entity.StockPrice;
import com.minghao.stockprediction.stock_prediction_system.service.StockPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/stock-prices")
public class StockPriceController {
    @Autowired
    private StockPriceService stockPriceService;

    // 添加价格数据：POST /api/stock-prices
    @PostMapping
    public StockPrice addStockPrice(@RequestBody StockPrice stockPrice){
        return stockPriceService.addStockPrice(stockPrice);

    }

    // 获取某股票所有价格：GET /api/stock-prices/AAPL
    @GetMapping("/{symbol}")
    public List<StockPrice> getPricesBySymbol(@PathVariable String symbol) {
        return stockPriceService.getPricesBySymbol(symbol);
    }

    // 获取某股票特定日期价格：GET /api/stock-prices/AAPL/2024-01-15
    @GetMapping("/{symbol}/{date}")
    public StockPrice getPriceByDate(@PathVariable String symbol,
                                     @PathVariable String date){
        LocalDate localDate = LocalDate.parse(date);
        return stockPriceService.getPricesBySymbolAndDate(symbol,localDate);
    }
    // 获取某股票最新N天价格：GET /api/stock-prices/AAPL/latest?days=5
    @GetMapping("/{symbol}/latest")
    public List<StockPrice> getLatestPrices(@PathVariable String symbol,
                                            @RequestParam(defaultValue = "5") int days) {
        return stockPriceService.getLatestPrices(symbol, days);
    }


}
