package com.minghao.stockprediction.stock_prediction_system.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "technical_indicators")
public class TechnicalIndicator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @Column(nullable = false)
    private LocalDate date;

    // 移动平均线
    @Column(precision = 10, scale = 2)
    private BigDecimal ma5;   // 5日移动平均线

    @Column(precision = 10, scale = 2)
    private BigDecimal ma10;  // 10日移动平均线

    @Column(precision = 10, scale = 2)
    private BigDecimal ma20;  // 20日移动平均线

    // 技术信号
    @Column(name = "`signal`", length = 20)
    private String signal;    // BUY, SELL, HOLD

    @Column(precision = 5, scale = 2)
    private BigDecimal confidence; // 信号置信度 (0-100)

    // RSI指标
    @Column(precision = 5, scale = 2)
    private BigDecimal rsi;       // RSI值 (0-100)

    @Column(name = "`rsi_signal`", length = 20)
    private String rsiSignal;     // RSI信号：OVERBOUGHT, OVERSOLD, NORMAL

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

    public BigDecimal getMa5() {
        return ma5;
    }

    public void setMa5(BigDecimal ma5) {
        this.ma5 = ma5;
    }

    public BigDecimal getMa10() {
        return ma10;
    }

    public void setMa10(BigDecimal ma10) {
        this.ma10 = ma10;
    }

    public BigDecimal getMa20() {
        return ma20;
    }

    public void setMa20(BigDecimal ma20) {
        this.ma20 = ma20;
    }

    public String getSignal() {
        return signal;
    }

    public void setSignal(String signal) {
        this.signal = signal;
    }

    public BigDecimal getConfidence() {
        return confidence;
    }

    public void setConfidence(BigDecimal confidence) {
        this.confidence = confidence;
    }

    public BigDecimal getRsi() {
        return rsi;
    }

    public void setRsi(BigDecimal rsi) {
        this.rsi = rsi;
    }

    public String getRsiSignal() {
        return rsiSignal;
    }

    public void setRsiSignal(String rsiSignal) {
        this.rsiSignal = rsiSignal;
    }
}