package com.minghao.stockprediction.stock_prediction_system.repository;


import com.minghao.stockprediction.stock_prediction_system.entity.Stock;
import com.minghao.stockprediction.stock_prediction_system.entity.StockPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface StockPriceRepository extends JpaRepository<StockPrice, Long> {

    //list all price record by stock
    List<StockPrice> findByStock(Stock stock);

    //list the price on specific day on specific stock
    StockPrice findByStockAndDate(Stock stock , LocalDate date);

    //list the price between two days on specific stock
    List<StockPrice> findByStockAndDateBetween(Stock stock, LocalDate startDate, LocalDate endDate);

    //list the latest record on specific stock
    List<StockPrice> findByStockOrderByDateDesc(Stock stock, Pageable pageable);


}
