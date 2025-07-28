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
public class MovingAverageAnalysisService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockPriceService stockPriceService;

    @Autowired
    private TechnicalIndicatorRepository technicalIndicatorRepository;

    /**
     * 计算某只股票的技术指标
     * @param symbol 股票代码
     * @return 计算的指标数量
     */
    public int calculateMovingAverageIndicators(String symbol) {
        Stock stock = stockRepository.findBySymbol(symbol);
        if (stock == null) {
            System.err.println("股票不存在: " + symbol);
            return 0;
        }

        // 获取股票的历史价格数据（按日期正序）
        List<StockPrice> prices = stockPriceService.getPricesBySymbol(symbol);
        if (prices.size() < 20) {
            System.err.println("数据不足，需要至少20天数据");
            return 0;
        }

        // 按日期排序（确保是正序）
        prices.sort((a, b) -> a.getDate().compareTo(b.getDate()));

        int calculatedCount = 0;

        // 从第20天开始计算（确保有足够数据计算20日均线）
        for (int i = 19; i < prices.size(); i++) {
            StockPrice currentPrice = prices.get(i);

            // 计算移动平均线
            BigDecimal ma5 = calculateMovingAverage(prices, i, 5);
            BigDecimal ma10 = calculateMovingAverage(prices, i, 10);
            BigDecimal ma20 = calculateMovingAverage(prices, i, 20);

            // 生成交易信号
            String signal = generateSignal(ma5, ma10, ma20);
            BigDecimal confidence = calculateConfidence(ma5, ma10, ma20);

            // 创建技术指标记录
            TechnicalIndicator indicator = new TechnicalIndicator();
            indicator.setStock(stock);
            indicator.setDate(currentPrice.getDate());
            indicator.setMa5(ma5);
            indicator.setMa10(ma10);
            indicator.setMa20(ma20);
            indicator.setSignal(signal);
            indicator.setConfidence(confidence);

            // 保存到数据库
            technicalIndicatorRepository.save(indicator);
            calculatedCount++;
        }

        System.out.println("成功计算 " + symbol + " 的 " + calculatedCount + " 条移动平均线指标");
        return calculatedCount;
    }

    /**
     * 计算移动平均线
     */
    private BigDecimal calculateMovingAverage(List<StockPrice> prices, int endIndex, int period) {
        BigDecimal sum = BigDecimal.ZERO;

        for (int i = endIndex - period + 1; i <= endIndex; i++) {
            sum = sum.add(prices.get(i).getClose());
        }

        return sum.divide(BigDecimal.valueOf(period), 2, RoundingMode.HALF_UP);
    }

    /**
     * 生成交易信号
     */
    private String generateSignal(BigDecimal ma5, BigDecimal ma10, BigDecimal ma20) {
        // 简单的黄金交叉策略
        if (ma5.compareTo(ma10) > 0 && ma10.compareTo(ma20) > 0) {
            return "BUY";   // 短期均线在长期均线之上，买入信号
        } else if (ma5.compareTo(ma10) < 0 && ma10.compareTo(ma20) < 0) {
            return "SELL";  // 短期均线在长期均线之下，卖出信号
        } else {
            return "HOLD";  // 其他情况持有
        }
    }

    /**
     * 计算信号置信度
     */
    private BigDecimal calculateConfidence(BigDecimal ma5, BigDecimal ma10, BigDecimal ma20) {
        // 简单的置信度计算：均线间距离越大，置信度越高
        BigDecimal diff1 = ma5.subtract(ma10).abs();
        BigDecimal diff2 = ma10.subtract(ma20).abs();
        BigDecimal avgDiff = diff1.add(diff2).divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);

        // 转换为百分比（这里是简化计算）
        BigDecimal confidence = avgDiff.multiply(BigDecimal.valueOf(10));

        // 限制在0-100之间
        if (confidence.compareTo(BigDecimal.valueOf(100)) > 0) {
            confidence = BigDecimal.valueOf(100);
        }

        return confidence;
    }
}