package com.minghao.stockprediction.stock_prediction_system.service;

import com.minghao.stockprediction.stock_prediction_system.entity.Stock;
import com.minghao.stockprediction.stock_prediction_system.entity.StockPrice;
import com.minghao.stockprediction.stock_prediction_system.entity.TechnicalIndicator;
import com.minghao.stockprediction.stock_prediction_system.repository.StockRepository;
import com.minghao.stockprediction.stock_prediction_system.repository.TechnicalIndicatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class RSIAnalysisService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockPriceService stockPriceService;

    @Autowired
    private TechnicalIndicatorRepository technicalIndicatorRepository;

    /**
     * 计算某只股票的RSI指标并更新数据库
     * @param symbol 股票代码
     * @return 更新的指标数量
     */
    public int calculateRSIIndicators(String symbol) {
        Stock stock = stockRepository.findBySymbol(symbol);
        if (stock == null) {
            System.err.println("股票不存在: " + symbol);
            return 0;
        }

        // 获取股票的历史价格数据（按日期正序）
        List<StockPrice> prices = stockPriceService.getPricesBySymbol(symbol);
        if (prices.size() < 15) {
            System.err.println("数据不足，需要至少15天数据计算RSI");
            return 0;
        }

        // 按日期排序（确保是正序）
        prices.sort((a, b) -> a.getDate().compareTo(b.getDate()));

        int updatedCount = 0;

        // 从第15天开始计算RSI（需要至少14天的历史数据）
        for (int i = 14; i < prices.size(); i++) {
            StockPrice currentPrice = prices.get(i);

            // 计算RSI
            BigDecimal rsi = calculateRSI(prices, i, 14);
            String rsiSignal = generateRSISignal(rsi);

            // 查找对应日期的技术指标记录
            TechnicalIndicator indicator = technicalIndicatorRepository.findByStockAndDate(stock, currentPrice.getDate());

            if (indicator != null) {
                // 更新现有记录
                indicator.setRsi(rsi);
                indicator.setRsiSignal(rsiSignal);
                technicalIndicatorRepository.save(indicator);
                updatedCount++;
            } else {
                // 创建新记录（只包含RSI数据）
                TechnicalIndicator newIndicator = new TechnicalIndicator();
                newIndicator.setStock(stock);
                newIndicator.setDate(currentPrice.getDate());
                newIndicator.setRsi(rsi);
                newIndicator.setRsiSignal(rsiSignal);
                technicalIndicatorRepository.save(newIndicator);
                updatedCount++;
            }
        }

        System.out.println("成功计算 " + symbol + " 的 " + updatedCount + " 条RSI指标");
        return updatedCount;
    }

    /**
     * 计算RSI指标
     * @param prices 价格数据
     * @param endIndex 结束索引
     * @param period RSI周期（通常是14）
     * @return RSI值
     */
    private BigDecimal calculateRSI(List<StockPrice> prices, int endIndex, int period) {
        if (endIndex < period) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalGain = BigDecimal.ZERO;
        BigDecimal totalLoss = BigDecimal.ZERO;

        // 计算最近period天的涨跌
        for (int i = endIndex - period + 1; i <= endIndex; i++) {
            BigDecimal currentClose = prices.get(i).getClose();
            BigDecimal previousClose = prices.get(i - 1).getClose();
            BigDecimal change = currentClose.subtract(previousClose);

            if (change.compareTo(BigDecimal.ZERO) > 0) {
                totalGain = totalGain.add(change);
            } else if (change.compareTo(BigDecimal.ZERO) < 0) {
                totalLoss = totalLoss.add(change.abs());
            }
        }

        // 计算平均涨幅和平均跌幅
        BigDecimal avgGain = totalGain.divide(BigDecimal.valueOf(period), 4, RoundingMode.HALF_UP);
        BigDecimal avgLoss = totalLoss.divide(BigDecimal.valueOf(period), 4, RoundingMode.HALF_UP);

        // 避免除零错误
        if (avgLoss.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.valueOf(100);
        }

        // 计算RS和RSI
        BigDecimal rs = avgGain.divide(avgLoss, 4, RoundingMode.HALF_UP);
        BigDecimal rsi = BigDecimal.valueOf(100).subtract(
                BigDecimal.valueOf(100).divide(BigDecimal.ONE.add(rs), 2, RoundingMode.HALF_UP)
        );

        return rsi;
    }

    /**
     * 生成RSI信号
     * @param rsi RSI值
     * @return 信号类型
     */
    private String generateRSISignal(BigDecimal rsi) {
        if (rsi == null) {
            return "NORMAL";
        }

        if (rsi.compareTo(BigDecimal.valueOf(70)) > 0) {
            return "OVERBOUGHT";  // 超买
        } else if (rsi.compareTo(BigDecimal.valueOf(30)) < 0) {
            return "OVERSOLD";    // 超卖
        } else {
            return "NORMAL";      // 正常
        }
    }
}