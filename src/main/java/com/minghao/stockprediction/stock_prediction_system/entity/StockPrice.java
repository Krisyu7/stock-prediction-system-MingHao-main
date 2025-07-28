package com.minghao.stockprediction.stock_prediction_system.entity;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "stock_prices",
        uniqueConstraints = @UniqueConstraint(columnNames = {"stock_id", "date"}))
public class StockPrice {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id" , nullable = false)
    private Stock stock;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false , precision = 10 ,scale = 2)
    private BigDecimal open;  // open price

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal high;  // highest price

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal low;   // lowest price

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal close; // close price

    @Column(nullable = false)
    private Long volume;      //volume

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }
}
