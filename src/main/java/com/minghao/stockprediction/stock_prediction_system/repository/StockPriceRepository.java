package com.minghao.stockprediction.stock_prediction_system.repository;

import com.minghao.stockprediction.stock_prediction_system.entity.Stock;
import com.minghao.stockprediction.stock_prediction_system.entity.StockPrice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StockPriceRepository extends JpaRepository<StockPrice, Long> {

    // 查询某只股票的所有价格记录
    List<StockPrice> findByStock(Stock stock);

    // 查询某只股票在特定日期的价格
    StockPrice findByStockAndDate(Stock stock, LocalDate date);

    // 查询某只股票在日期范围内的价格记录
    List<StockPrice> findByStockAndDateBetween(Stock stock, LocalDate startDate, LocalDate endDate);

    // 查询某只股票最新的N条价格记录（按日期倒序）- 使用Pageable
    List<StockPrice> findByStockOrderByDateDesc(Stock stock, Pageable pageable);

    @Query("SELECT MAX(sp.date) FROM StockPrice sp WHERE sp.stock = ?1")
    LocalDate findLatestDateByStock(Stock stock);
}