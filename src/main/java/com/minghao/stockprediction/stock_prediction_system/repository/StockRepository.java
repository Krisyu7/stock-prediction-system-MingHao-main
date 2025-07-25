package com.minghao.stockprediction.stock_prediction_system.repository;

import com.minghao.stockprediction.stock_prediction_system.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock,Long>{

    // JpaRepository提供了基本的CRUD操作：
    // save() - 保存
    // findAll() - 查询所有
    // findById() - 根据ID查询
    // deleteById() - 根据ID删除

    // 我们可以添加自定义查询方法
    Stock findBySymbol(String symbol);  // 根据股票代码查询






}
