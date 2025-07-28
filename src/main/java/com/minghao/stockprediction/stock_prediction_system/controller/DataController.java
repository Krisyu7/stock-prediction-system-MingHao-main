package com.minghao.stockprediction.stock_prediction_system.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.minghao.stockprediction.stock_prediction_system.entity.Stock;
import com.minghao.stockprediction.stock_prediction_system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/data")
public class DataController {

    @Autowired
    private AlphaVantageService alphaVantageService;

    @Autowired
    private DataImportService dataImportService;

    @Autowired
    private MovingAverageAnalysisService movingAverageAnalysisService;

    @Autowired
    private SmartUpdateService smartUpdateService;

    @Autowired
    private StockService stockService;

    @Autowired
    private RSIAnalysisService rsiAnalysisService;


    // 测试API调用：GET /api/data/test/AAPL
    @GetMapping("/test/{symbol}")
    public JsonNode testApiCall(@PathVariable String symbol) {
        return alphaVantageService.getDailyData(symbol);
    }

    // 获取公司信息：GET /api/data/company/AAPL
    @GetMapping("/company/{symbol}")
    public JsonNode getCompanyInfo(@PathVariable String symbol) {
        return alphaVantageService.getCompanyOverview(symbol);
    }

    // 导入股票历史数据：POST /api/data/import/AAPL
    @PostMapping("/import/{symbol}")
    public String importStockData(@PathVariable String symbol) {
        int count = dataImportService.importStockData(symbol);
        if (count >= 0) {  // 改为 >= 0，因为0也表示成功（无新数据）
            if (count > 0) {
                return "成功导入 " + symbol + " 的 " + count + " 条新记录";
            } else {
                return "数据检查完成：" + symbol + " 已是最新，无需更新";
            }
        } else {
            return "导入失败：" + symbol;
        }
    }


    // 计算均线指标：POST /api/data/analyze/AAPL
    @PostMapping("/analyze-mAvg/{symbol}")
    public String calculateIndicators(@PathVariable String symbol) {
        int count = movingAverageAnalysisService.calculateMovingAverageIndicators(symbol);
        if (count > 0) {
            return "成功计算 " + symbol + " 的 " + count + " 条技术指标";
        } else {
            return "计算失败：" + symbol;
        }
    }

    // 计算RSI指标：POST /api/data/analyze-rsi/AAPL
    @PostMapping("/analyze-rsi/{symbol}")
    public String calculateRSIIndicators(@PathVariable String symbol) {
        int count = rsiAnalysisService.calculateRSIIndicators(symbol);
        if (count > 0) {
            return "成功计算 " + symbol + " 的 " + count + " 条RSI指标";
        } else {
            return "计算失败：" + symbol;
        }
    }

    // 检查市场状态：GET /api/data/market-status
    @GetMapping("/market-status")
    public String checkMarketStatus() {
        boolean isOpen = alphaVantageService.isMarketOpen();
        return "市场状态: " + (isOpen ? "开市" : "休市");
    }

    // 获取详细市场状态：GET /api/data/market-status-detail
    @GetMapping("/market-status-detail")
    public JsonNode getMarketStatusDetail() {
        return alphaVantageService.getMarketStatus();
    }

    // 智能更新所有股票：POST /api/data/smart-update
    @PostMapping("/smart-update")
    public String smartUpdateAll() {
        return smartUpdateService.smartUpdateAllStocks();
    }

    // 添加股票并自动导入数据：POST /api/data/add-stock-with-data
    @PostMapping("/add-stock-with-data")
    public String addStockWithData(@RequestBody Stock stock) {
        return stockService.addStockWithData(stock);
    }
}