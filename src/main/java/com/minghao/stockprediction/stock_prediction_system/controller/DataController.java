package com.minghao.stockprediction.stock_prediction_system.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.minghao.stockprediction.stock_prediction_system.service.AlphaVantageService;
import com.minghao.stockprediction.stock_prediction_system.service.DataImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/data")
public class DataController {

    @Autowired
    private AlphaVantageService alphaVantageService;

    @Autowired
    private DataImportService dataImportService;

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
        if (count > 0) {
            return "成功导入 " + symbol + " 的 " + count + " 条历史记录";
        } else {
            return "导入失败：" + symbol;
        }
    }
}