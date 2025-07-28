package com.minghao.stockprediction.stock_prediction_system.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AlphaVantageService {

    // 从配置文件读取API Key
    @Value("${alphavantage.api.key}")

    /* 检查市场是否开放
     * @return true表示开市，false表示休市
     */
    public boolean isMarketOpen() {
        try {
            String url = String.format("%s?function=MARKET_STATUS&apikey=%s",
                    BASE_URL, apiKey);

            String response = restTemplate.getForObject(url, String.class);
            JsonNode data = objectMapper.readTree(response);

            // 解析市场状态
            JsonNode markets = data.get("markets");
            if (markets != null && markets.isArray()) {
                for (JsonNode market : markets) {
                    String marketType = market.get("market_type").asText();
                    if ("Equity".equals(marketType)) {  // 股票市场
                        String status = market.get("current_status").asText();
                        return "open".equalsIgnoreCase(status);
                    }
                }
            }

            return false;  // 默认返回休市

        } catch (Exception e) {
            System.err.println("检查市场状态失败: " + e.getMessage());
            return false;  // 出错时默认不更新
        }
    }

    /**
     * 获取市场状态详细信息（用于调试）
     */
    public JsonNode getMarketStatus() {
        try {
            String url = String.format("%s?function=MARKET_STATUS&apikey=%s",
                    BASE_URL, apiKey);

            String response = restTemplate.getForObject(url, String.class);
            return objectMapper.readTree(response);

        } catch (Exception e) {
            System.err.println("获取市场状态失败: " + e.getMessage());
            return null;
        }
    }
    private String apiKey;



    private final String BASE_URL = "https://www.alphavantage.co/query";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();








    /**
     * 获取股票的历史日线数据
     * @param symbol 股票代码，如 AAPL
     * @return JSON格式的历史数据
     */
    public JsonNode getDailyData(String symbol) {
        try {
            // 构建API URL
            String url = String.format("%s?function=TIME_SERIES_DAILY&symbol=%s&apikey=%s",
                    BASE_URL, symbol, apiKey);

            // 发送HTTP请求
            String response = restTemplate.getForObject(url, String.class);

            // 解析JSON响应
            return objectMapper.readTree(response);

        } catch (Exception e) {
            System.err.println("获取股票数据失败: " + e.getMessage());
            return null;
        }
    }

    /**
     * 获取股票基本信息
     * @param symbol 股票代码
     * @return 股票基本信息
     */
    public JsonNode getCompanyOverview(String symbol) {
        try {
            String url = String.format("%s?function=OVERVIEW&symbol=%s&apikey=%s",
                    BASE_URL, symbol, apiKey);

            String response = restTemplate.getForObject(url, String.class);
            return objectMapper.readTree(response);

        } catch (Exception e) {
            System.err.println("获取公司信息失败: " + e.getMessage());
            return null;
        }
    }
}