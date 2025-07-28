package com.minghao.stockprediction.stock_prediction_system.service;

import com.minghao.stockprediction.stock_prediction_system.entity.Stock;
import com.minghao.stockprediction.stock_prediction_system.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service  // 告诉Spring这是业务逻辑层
public class StockService {

    @Autowired  // 自动注入Repository
    private StockRepository stockRepository;

    @Autowired
    private DataImportService dataImportService;

    // 添加股票（自动导入历史数据）
    public String addStockWithData(Stock stock) {
        // 1. 检查股票是否已存在
        if (stockRepository.findBySymbol(stock.getSymbol()) != null) {
            return "股票已存在: " + stock.getSymbol();
        }

        // 2. 保存股票基本信息
        Stock savedStock = stockRepository.save(stock);

        // 3. 自动导入历史数据
        int dataCount = dataImportService.importStockData(stock.getSymbol());

        if (dataCount > 0) {
            return "成功添加股票 " + stock.getSymbol() + " 并导入 " + dataCount + " 条历史数据";
        } else {
            return "成功添加股票 " + stock.getSymbol() + " 但历史数据导入失败";
        }
    }

    // 原有的简单添加方法（保留兼容性）
    public Stock addStock(Stock stock) {
        return stockRepository.save(stock);
    }

    // 获取所有股票
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    // 根据股票代码查询
    public Stock getStockBySymbol(String symbol) {
        return stockRepository.findBySymbol(symbol);
    }
}