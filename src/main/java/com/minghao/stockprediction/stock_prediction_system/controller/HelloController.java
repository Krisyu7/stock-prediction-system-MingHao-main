package com.minghao.stockprediction.stock_prediction_system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "Hello Stock Prediction System! \uD83D\uDE80";
    }
    @GetMapping("/")
    public String home(){
        return "Welcome to Stock Prediction System - Ready to predict the market! 📈";
    }
}
