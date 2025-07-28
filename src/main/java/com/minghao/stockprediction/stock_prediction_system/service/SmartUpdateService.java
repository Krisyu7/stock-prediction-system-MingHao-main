package com.minghao.stockprediction.stock_prediction_system.service;

import com.minghao.stockprediction.stock_prediction_system.entity.Stock;
import com.minghao.stockprediction.stock_prediction_system.repository.StockRepository;
import com.minghao.stockprediction.stock_prediction_system.repository.StockPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SmartUpdateService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockPriceRepository stockPriceRepository;

    @Autowired
    private DataImportService dataImportService;

    @Autowired
    private AlphaVantageService alphaVantageService;

    /**
     * 智能更新所有股票到最新
     * 这是日常维护的主要方法
     */
    public String smartUpdateAllStocks() {
        StringBuilder result = new StringBuilder();
        result.append("=== 智能更新开始 ===\n");

        // 检查市场状态
        try {
            boolean isMarketOpen = alphaVantageService.isMarketOpen();
            result.append("市场状态: ").append(isMarketOpen ? "开市" : "休市").append("\n");
        } catch (Exception e) {
            result.append("注意: 无法检查市场状态\n");
        }

        // 获取所有股票并更新
        List<Stock> allStocks = stockRepository.findAll();
        int totalUpdated = 0;
        int totalNewRecords = 0;

        for (Stock stock : allStocks) {
            String updateResult = smartUpdateSingleStock(stock);
            result.append(updateResult).append("\n");

            // 统计更新数量
            if (updateResult.contains("导入") && !updateResult.contains("0 条")) {
                totalUpdated++;
                // 提取新记录数量（简化统计）
                if (updateResult.contains("条新记录")) {
                    // 可以进一步解析具体数量
                }
            }
        }

        result.append("=== 更新完成 ===\n");
        result.append("共处理 ").append(allStocks.size()).append(" 只股票，");
        result.append("其中 ").append(totalUpdated).append(" 只有新数据");

        return result.toString();
    }

    /**
     * 智能更新单只股票
     */
    private String smartUpdateSingleStock(Stock stock) {
        try {
            // 查询数据库中最新的日期
            LocalDate latestDate = stockPriceRepository.findLatestDateByStock(stock);
            LocalDate today = LocalDate.now();

            if (latestDate == null) {
                // 理论上不应该发生，因为添加股票时会自动导入数据
                int count = dataImportService.importStockData(stock.getSymbol());
                return stock.getSymbol() + ": 异常情况，重新导入 " + count + " 条记录";
            }

            if (!latestDate.isBefore(today)) {
                // 数据已经是最新的
                return stock.getSymbol() + ": 已是最新（" + latestDate + "）";
            }

            // 计算缺失的天数
            long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(latestDate, today);

            // 执行增量更新
            int count = dataImportService.importStockData(stock.getSymbol());

            if (count > 0) {
                return stock.getSymbol() + ": 更新成功，导入 " + count + " 条新记录（缺失 " + daysBetween + " 天）";
            } else {
                return stock.getSymbol() + ": 已是最新，无需更新";
            }

        } catch (Exception e) {
            return stock.getSymbol() + ": 更新失败 - " + e.getMessage();
        }
    }
}