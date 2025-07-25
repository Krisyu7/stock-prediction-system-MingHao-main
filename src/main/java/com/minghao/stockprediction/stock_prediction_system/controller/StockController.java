package com.minghao.stockprediction.stock_prediction_system.controller;

import com.minghao.stockprediction.stock_prediction_system.entity.Stock;
import com.minghao.stockprediction.stock_prediction_system.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/stocks") //To the interface
public class StockController {


    @Autowired
    private StockService stockService;


    // add stock: POST /api/stocks
    @PostMapping
    public Stock addStock(@RequestBody Stock stock){
        return stockService.addStock(stock);

    }


    @GetMapping
    public List<Stock> getALlStocks(){
        return stockService.getAllStocks();
    }

    @GetMapping("/{symbol}")
    public Stock getStockBySymbol(@PathVariable String symbol){
        return stockService.getStockBySymbol(symbol);
    }

}
