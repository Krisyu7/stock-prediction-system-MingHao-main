package com.minghao.stockprediction.stock_prediction_system.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.minghao.stockprediction.stock_prediction_system.entity.Stock;
import com.minghao.stockprediction.stock_prediction_system.entity.StockPrice;
import com.minghao.stockprediction.stock_prediction_system.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class DataImportService {

    @Autowired
    private AlphaVantageService alphaVantageService;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockPriceService stockPriceService;

    /**
     * 导入某只股票的历史数据
     * @param symbol 股票代码
     * @return 导入的记录数
     */
    public int importStockData(String symbol) {
        try {
            // 1. 检查股票是否存在
            Stock stock = stockRepository.findBySymbol(symbol);
            if (stock == null) {
                System.err.println("股票不存在: " + symbol);
                return 0;
            }

            // 2. 获取API数据
            JsonNode data = alphaVantageService.getDailyData(symbol);
            if (data == null) {
                System.err.println("获取数据失败: " + symbol);
                return 0;
            }

            // 3. 解析JSON数据
            JsonNode timeSeries = data.get("Time Series (Daily)");
            if (timeSeries == null) {
                System.err.println("数据格式错误: " + symbol);
                return 0;
            }

            // 4. 转换并保存数据
            List<StockPrice> stockPrices = new ArrayList<>();
            Iterator<String> dates = timeSeries.fieldNames();

            while (dates.hasNext()) {
                String dateStr = dates.next();
                JsonNode dayData = timeSeries.get(dateStr);

                StockPrice stockPrice = new StockPrice();
                stockPrice.setStock(stock);
                stockPrice.setDate(LocalDate.parse(dateStr));
                stockPrice.setOpen(new BigDecimal(dayData.get("1. open").asText()));
                stockPrice.setHigh(new BigDecimal(dayData.get("2. high").asText()));
                stockPrice.setLow(new BigDecimal(dayData.get("3. low").asText()));
                stockPrice.setClose(new BigDecimal(dayData.get("4. close").asText()));
                stockPrice.setVolume(dayData.get("5. volume").asLong());

                stockPrices.add(stockPrice);
            }

            // 5. 批量保存到数据库
            for (StockPrice price : stockPrices) {
                stockPriceService.addStockPrice(price);
            }

            System.out.println("成功导入 " + symbol + " 的 " + stockPrices.size() + " 条记录");
            return stockPrices.size();

        } catch (Exception e) {
            System.err.println("导入数据失败: " + e.getMessage());
            return 0;
        }
    }
}