package com.minghao.stockprediction.stock_prediction_system.repository;

import com.minghao.stockprediction.stock_prediction_system.entity.Stock;
import com.minghao.stockprediction.stock_prediction_system.entity.TechnicalIndicator;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TechnicalIndicatorRepository extends JpaRepository<TechnicalIndicator, Long> {

    // 查询某只股票的所有技术指标
    List<TechnicalIndicator> findByStock(Stock stock);

    // 查询某只股票在特定日期的技术指标
    TechnicalIndicator findByStockAndDate(Stock stock, LocalDate date);

    // 查询某只股票最新的技术指标（按日期倒序）
    List<TechnicalIndicator> findByStockOrderByDateDesc(Stock stock, Pageable pageable);

    // 查询某只股票特定信号的记录
    List<TechnicalIndicator> findByStockAndSignal(Stock stock, String signal);

    // 查询某只股票在日期范围内的技术指标
    List<TechnicalIndicator> findByStockAndDateBetween(Stock stock, LocalDate startDate, LocalDate endDate);
}